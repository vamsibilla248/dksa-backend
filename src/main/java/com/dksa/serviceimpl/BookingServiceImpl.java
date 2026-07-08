package com.dksa.serviceimpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dksa.dto.AdminBookingResponse;
import com.dksa.dto.BookingRequest;
import com.dksa.dto.BookingResponse;
import com.dksa.dto.MonthlyReportResponse;
import com.dksa.dto.ReceiptResponse;
import com.dksa.dto.ReportResponse;
import com.dksa.entity.Booking;
import com.dksa.entity.BookingSlot;
import com.dksa.entity.Payment;
import com.dksa.entity.PaymentStatus;
import com.dksa.entity.Slot;
import com.dksa.entity.SlotStatus;
import com.dksa.entity.User;
import com.dksa.exception.ResourceNotFoundException;
import com.dksa.repository.BookingRepository;
import com.dksa.repository.BookingSlotRepository;
import com.dksa.repository.PaymentRepository;
import com.dksa.repository.SlotRepository;
import com.dksa.repository.UserRepository;
import com.dksa.service.AppSettingService;
import com.dksa.service.BookingService;
import com.dksa.service.EmailService;
import com.dksa.service.PdfService;
import com.dksa.service.WhatsAppService;
import com.dksa.util.SlotValidationUtil;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

	private final BookingRepository bookingRepository;

	private final BookingSlotRepository bookingSlotRepository;

	private final SlotRepository slotRepository;

	private final UserRepository userRepository;
	
	private final PaymentRepository paymentRepository;
	
	private final AppSettingService appSettingService;
	
	private final EmailService emailService;
	
	private final PdfService pdfService;
	
	private final WhatsAppService  whatsAppService;

	public BookingServiceImpl(
	        BookingRepository bookingRepository,
	        BookingSlotRepository bookingSlotRepository,
	        SlotRepository slotRepository,
	        UserRepository userRepository,
	        PaymentRepository paymentRepository,
	        AppSettingService appSettingService,
	        EmailService emailService,
	        PdfService pdfService,
	        WhatsAppService  whatsAppService) {

	    this.bookingRepository = bookingRepository;
	    this.bookingSlotRepository = bookingSlotRepository;
	    this.slotRepository = slotRepository;
	    this.userRepository = userRepository;
	    this.paymentRepository = paymentRepository;
	    this.appSettingService = appSettingService;
	    this.emailService = emailService;
	    this.pdfService = pdfService;
	    this.whatsAppService = whatsAppService;
	}

	@Override
	public BookingResponse createBooking(BookingRequest request) {

		if (request.getSlotIds() == null || request.getSlotIds().size() < 2) {

			throw new RuntimeException("Minimum 2 slots required");
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String email = authentication.getName();

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		List<Slot> slots = slotRepository.findAllById(request.getSlotIds());

		if (slots.size() != request.getSlotIds().size()) {

			throw new RuntimeException("Invalid slot selected");
		}

		Long turfId = slots.get(0).getTurf().getId();

		boolean sameTurf = slots.stream().allMatch(slot -> slot.getTurf().getId().equals(turfId));
		
		if (!SlotValidationUtil.areConsecutiveSlots(slots)) {

			throw new RuntimeException("Please select consecutive slots only");
		}

		if (!sameTurf) {

			throw new RuntimeException("All slots must belong to same turf");
		}

		boolean allOpen = slots.stream().allMatch(slot -> slot.getStatus() == SlotStatus.OPEN);

		if (!allOpen) {

			throw new RuntimeException("One or more slots already booked");
		}

		double totalAmount = slots.stream().mapToDouble(Slot::getPrice).sum();

		Booking booking = Booking.builder().user(user).turf(slots.get(0).getTurf()).totalAmount(totalAmount)
				.paymentStatus(PaymentStatus.PENDING).build();

		booking = bookingRepository.save(booking);

		for (Slot slot : slots) {

			BookingSlot bookingSlot = BookingSlot.builder().booking(booking).slot(slot).build();

			bookingSlotRepository.save(bookingSlot);

			slot.setStatus(SlotStatus.BOOKED);

			slotRepository.save(slot);
		}
		
		try {
			
			// email
			emailService.sendBookingConfirmation(
			        user.getEmail(),
			        user.getName(),
			        booking.getId());

			// pdf
			byte[] pdf =
			        pdfService.generateReceiptPdf(
			                booking.getId());

			// pdf email
			emailService.sendReceiptEmail(
			        user.getEmail(),
			        user.getName(),
			        pdf);
			
			// whatsapp service
			whatsAppService
	        .sendBookingMessage(

	                user.getMobile(),

	                booking.getId());
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		

		return BookingResponse.builder().bookingId(booking.getId()).turfName(booking.getTurf().getTurfName())
				.totalAmount(booking.getTotalAmount()).paymentStatus(booking.getPaymentStatus())
				.bookingTime(booking.getBookingTime()).slotIds(request.getSlotIds()).build();
	}
	
	@Override
	public List<BookingResponse> getMyBookings() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String email = authentication.getName();

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		List<Booking> bookings = bookingRepository.findByUserId(user.getId());

		return bookings.stream().map(this::mapToResponse).collect(Collectors.toList());
	}
	
	
	private BookingResponse mapToResponse(Booking booking) {

	    DateTimeFormatter formatter =
	            DateTimeFormatter.ofPattern("hh:mm a");

	    List<Long> slotIds =
	            booking.getBookingSlots()
	                    .stream()
	                    .map(bs -> bs.getSlot().getId())
	                    .toList();

	    List<String> slotTimes =
	            booking.getBookingSlots()
	                    .stream()
	                    .map(bs -> {

	                        LocalTime start =
	                                bs.getSlot().getSlotTime();

	                        LocalTime end =
	                                start.plusMinutes(30);

	                        return start.format(formatter)
	                                + " - "
	                                + end.format(formatter);
	                    })
	                    .toList();

	    return BookingResponse.builder()
	            .bookingId(booking.getId())
	            .turfName(booking.getTurf().getTurfName())
	            .totalAmount(booking.getTotalAmount())
	            .paymentStatus(booking.getPaymentStatus())
	            .bookingTime(booking.getBookingTime())
	            .slotIds(slotIds)
	            .slotCount(slotTimes.size())
	            .slotTimes(slotTimes)
	            .build();
	}
	
	
	@Override
	public ReceiptResponse getReceipt(Long bookingId) {

	    Booking booking = bookingRepository.findById(bookingId)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Booking not found"));

	    Payment payment = paymentRepository.findByBookingId(bookingId)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Payment not found"));

	    DateTimeFormatter formatter =
	            DateTimeFormatter.ofPattern("hh:mm a");

	    List<String> slots =
	            booking.getBookingSlots()
	                   .stream()
	                   .map(bs -> {

	                       LocalTime startTime =
	                               bs.getSlot()
	                                 .getSlotTime();

	                       LocalTime endTime =
	                               startTime.plusMinutes(30);

	                       return startTime.format(formatter)
	                               + " - "
	                               + endTime.format(formatter);
	                   })
	                   .toList();

	    return ReceiptResponse.builder()
	            .bookingId(booking.getId())
	            .customerName(
	                    booking.getUser().getName())
	            .customerEmail(
	                    booking.getUser().getEmail())
	            .turfName(
	                    booking.getTurf().getTurfName())
	            .orderId(
	                    payment.getRazorpayOrderId())
	            .paymentId(
	                    payment.getRazorpayPaymentId())
	            .amount(
	                    payment.getAmount())
	            .paymentStatus(
	                    payment.getPaymentStatus())
	            .bookingTime(
	                    booking.getBookingTime())
	            .slots(slots)
	            .build();
	}

	@Override
	public BookingResponse createDirectBooking(BookingRequest request) {

		boolean paymentRequired = appSettingService.getPaymentStatus();

		if (paymentRequired) {

			throw new RuntimeException("Payment is mandatory");
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String email = authentication.getName();

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		List<Slot> slots = slotRepository.findAllById(request.getSlotIds());

		if (slots.size() < 2) {

			throw new RuntimeException("Minimum 2 slots required");
		}

		if (!SlotValidationUtil.areConsecutiveSlots(slots)) {

			throw new RuntimeException("Please select consecutive slots only");
		}

		Long turfId = slots.get(0).getTurf().getId();

		boolean sameTurf = slots.stream().allMatch(slot -> slot.getTurf().getId().equals(turfId));

		if (!sameTurf) {

			throw new RuntimeException("All slots must belong to same turf");
		}

		boolean allOpen = slots.stream().allMatch(slot -> slot.getStatus() == SlotStatus.OPEN);

		if (!allOpen) {

			throw new RuntimeException("Slots already booked");
		}

		double totalAmount = slots.stream().mapToDouble(Slot::getPrice).sum();

		Booking booking = Booking.builder().user(user).turf(slots.get(0).getTurf()).totalAmount(totalAmount)
				.paymentStatus(PaymentStatus.PAID).build();

		booking = bookingRepository.save(booking);

		for (Slot slot : slots) {

			BookingSlot bookingSlot = BookingSlot.builder().booking(booking).slot(slot).build();

			bookingSlotRepository.save(bookingSlot);

			slot.setStatus(SlotStatus.BOOKED);

			slotRepository.save(slot);
		}

		return BookingResponse.builder().bookingId(booking.getId()).turfName(booking.getTurf().getTurfName())
				.totalAmount(booking.getTotalAmount()).paymentStatus(booking.getPaymentStatus())
				.bookingTime(booking.getBookingTime()).slotIds(request.getSlotIds()).build();
	}
	
	@Override
	public ReportResponse getReports() {

	    LocalDate today = LocalDate.now();

	    LocalDateTime start = today.atStartOfDay();
	    LocalDateTime end = today.plusDays(1).atStartOfDay();

	    return ReportResponse.builder()

	            .totalBookings(
	                    bookingRepository.getTotalBookings())

	            .totalRevenue(
	                    bookingRepository.getTotalRevenue())

	            .todayBookings(
	                    bookingRepository.getTodayBookings(start, end))

	            .todayRevenue(
	                    bookingRepository.getTodayRevenue(start, end))

	            .build();
	}
	
	
	@Override
	public List<AdminBookingResponse>
	getAllBookings() {

	    return bookingRepository
	            .findAll()
	            .stream()
	            .map(this::mapToAdminResponse)
	            .toList();
	}

	private AdminBookingResponse
	mapToAdminResponse(
	        Booking booking) {

	    return AdminBookingResponse
	            .builder()
	            .bookingId(
	                    booking.getId())
	            .customerName(
	                    booking.getUser()
	                           .getName())
	            .customerEmail(
	                    booking.getUser()
	                           .getEmail())
	            .turfName(
	                    booking.getTurf()
	                           .getTurfName())
	            .totalAmount(
	                    booking.getTotalAmount())
	            .paymentStatus(
	                    booking.getPaymentStatus())
	            .bookingTime(
	                    booking.getBookingTime())
	            .build();
	}
	
	@Override
	@Transactional
	public void cancelBooking(
	        Long bookingId) {

	    Booking booking =
	            bookingRepository
	                    .findById(
	                            bookingId)
	                    .orElseThrow(() ->
	                            new ResourceNotFoundException(
	                                    "Booking not found"));

	    List<BookingSlot>
	            bookingSlots =
	            booking.getBookingSlots();

	    for (BookingSlot bookingSlot :
	            bookingSlots) {

	        Slot slot =
	                bookingSlot.getSlot();

	        slot.setStatus(
	                SlotStatus.OPEN);

	        slotRepository.save(
	                slot);
	    }

	    bookingRepository.delete(
	            booking);
	}
	
	@Override
	public List<MonthlyReportResponse> getMonthlyReports() {

	    List<Booking> bookings = bookingRepository.findAllByOrderByBookingTimeAsc();
	    List<Payment> payments = paymentRepository.findAllByOrderByCreatedAtAsc();

	    Map<Integer, Long> bookingMap = bookings.stream()
	            .collect(Collectors.groupingBy(
	                    b -> b.getBookingTime().getMonthValue(),
	                    TreeMap::new,
	                    Collectors.counting()));

	    Map<Integer, Double> revenueMap = payments.stream()
	            .filter(p -> "SUCCESS".equals(p.getPaymentStatus()))
	            .collect(Collectors.groupingBy(
	                    p -> p.getCreatedAt().getMonthValue(),
	                    TreeMap::new,
	                    Collectors.summingDouble(p -> p.getAmount().doubleValue())));

	    List<MonthlyReportResponse> response = new ArrayList<>();

	    for (int month = 1; month <= 12; month++) {

	        MonthlyReportResponse dto = new MonthlyReportResponse();

	        dto.setMonth(Month.of(month).name());

	        dto.setBookings(bookingMap.getOrDefault(month, 0L));

	        dto.setRevenue(revenueMap.getOrDefault(month, 0.0));

	        response.add(dto);
	    }

	    return response;
	}

}