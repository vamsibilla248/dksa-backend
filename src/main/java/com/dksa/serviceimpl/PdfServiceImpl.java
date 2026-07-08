package com.dksa.serviceimpl;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dksa.entity.Booking;
import com.dksa.entity.Payment;
import com.dksa.entity.PaymentStatus;
import com.dksa.entity.User;
import com.dksa.exception.ResourceNotFoundException;
import com.dksa.repository.BookingRepository;
import com.dksa.repository.PaymentRepository;
import com.dksa.repository.UserRepository;
import com.dksa.service.PdfService;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;



@Service
public class PdfServiceImpl implements PdfService {

	private final BookingRepository bookingRepository;
	private final PaymentRepository paymentRepository;
	
	private final UserRepository userRepository;

	public PdfServiceImpl(
	        BookingRepository bookingRepository,
	        PaymentRepository paymentRepository,
	        UserRepository userRepository) {

	    this.bookingRepository = bookingRepository;

	    this.paymentRepository = paymentRepository;

	    this.userRepository = userRepository;
	}
	
	
	
	private PdfPCell createCell(
	        String text,
	        Font font,
	        BaseColor backgroundColor) {

	    PdfPCell cell =
	            new PdfPCell(
	                    new Phrase(
	                            text,
	                            font));

	    cell.setPadding(12);

	    cell.setBorderColor(
	            new BaseColor(
	                    226,
	                    232,
	                    240));

	    cell.setBackgroundColor(
	            backgroundColor);

	    return cell;
	}

	@Override
	public byte[] generateReceiptPdf(Long bookingId) {
		
		System.out.println("---------generateReceiptPdf------------");

	    try {
	    	
			BaseColor dksaGreen = new BaseColor(15, 90, 85);

			Font labelFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, new BaseColor(100, 116, 139));

			Font valueFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, dksaGreen);
			
			
			
			
			Booking booking = bookingRepository.findById(bookingId)
					.orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

			Payment payment = paymentRepository.findByBookingId(bookingId)
					.orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			Document document = new Document(PageSize.A4, 40, 40, 40, 40);

			PdfWriter.getInstance(document, baos);

			document.open();

	        BaseColor dksaOrange =
	                new BaseColor(255, 107, 26);

	        BaseColor lightGreen =
	                new BaseColor(231, 244, 239);

	        Font titleFont =
	                new Font(
	                        Font.FontFamily.HELVETICA,
	                        28,
	                        Font.BOLD,
	                        dksaGreen);

	        Font academyFont =
	                new Font(
	                        Font.FontFamily.HELVETICA,
	                        16,
	                        Font.BOLD,
	                        new BaseColor(70, 70, 70));

	        Font headingFont =
	                new Font(
	                        Font.FontFamily.HELVETICA,
	                        14,
	                        Font.BOLD,
	                        dksaGreen);

	        Font normalFont =
	                new Font(
	                        Font.FontFamily.HELVETICA,
	                        11,
	                        Font.NORMAL,
	                        new BaseColor(90,90,90));

	      

	        Font whiteFont =
	                new Font(
	                        Font.FontFamily.HELVETICA,
	                        12,
	                        Font.BOLD,
	                        BaseColor.WHITE);

	        Font amountFont =
	                new Font(
	                        Font.FontFamily.HELVETICA,
	                        26,
	                        Font.BOLD,
	                        BaseColor.WHITE);

	        Paragraph title =
	                new Paragraph(
	                        "DKSA RECEIPT",
	                        titleFont);

	        title.setAlignment(
	                Element.ALIGN_CENTER);

	        title.setSpacingAfter(5);

	        document.add(title);

	        Paragraph academy =
	                new Paragraph(
	                        "Don Bosco Kohli Sports Academy",
	                        academyFont);

	        academy.setAlignment(
	                Element.ALIGN_CENTER);

	        academy.setSpacingAfter(20);

	        document.add(academy);

	        document.add(
	                new Paragraph(" "));

	        PdfPTable statusTable =
	                new PdfPTable(1);

	        statusTable.setWidthPercentage(25);

	        PdfPCell statusCell =
	                new PdfPCell(
	                        new Phrase(
	                                payment.getPaymentStatus()
	                                       .name(),
	                                whiteFont));

	        statusCell.setHorizontalAlignment(
	                Element.ALIGN_CENTER);

	        statusCell.setPadding(8);

	        statusCell.setBorder(0);

	        statusCell.setBackgroundColor(
	                payment.getPaymentStatus() ==
	                        PaymentStatus.PAID
	                        ? new BaseColor(34,197,94)
	                        : dksaOrange);

	        statusTable.addCell(
	                statusCell);

	        document.add(
	                statusTable);

	        document.add(
	                new Paragraph(" "));

	        PdfPTable details =
	                new PdfPTable(2);

	        details.setWidthPercentage(100);

	        details.setSpacingAfter(20);

	        details.setWidths(
	                new float[]{1.5f, 2.5f});

	        BaseColor rowBg =
	                new BaseColor(
	                        248,
	                        250,
	                        252);

	        details.addCell(
	                createCell(
	                        "Customer",
	                        labelFont,
	                        rowBg));

	        details.addCell(
	                createCell(
	                        booking.getUser().getName(),
	                        valueFont,
	                        BaseColor.WHITE));

	        details.addCell(
	                createCell(
	                        "Email",
	                        labelFont,
	                        rowBg));

	        details.addCell(
	                createCell(
	                        booking.getUser().getEmail(),
	                        valueFont,
	                        BaseColor.WHITE));

	        details.addCell(
	                createCell(
	                        "Booking ID",
	                        labelFont,
	                        rowBg));

	        details.addCell(
	                createCell(
	                        "#" + booking.getId(),
	                        valueFont,
	                        BaseColor.WHITE));

	        details.addCell(
	                createCell(
	                        "Turf",
	                        labelFont,
	                        rowBg));

	        details.addCell(
	                createCell(
	                        booking.getTurf().getTurfName(),
	                        valueFont,
	                        BaseColor.WHITE));

	        details.addCell(
	                createCell(
	                        "Date",
	                        labelFont,
	                        rowBg));

	        details.addCell(
	                createCell(
	                        booking.getBookingTime()
	                               .format(
	                                       DateTimeFormatter.ofPattern(
	                                               "dd MMM yyyy")),
	                        valueFont,
	                        BaseColor.WHITE));

	        document.add(details);

	        Paragraph slotHeading =
	                new Paragraph(
	                        "SLOT TIMINGS",
	                        headingFont);

	        slotHeading.setSpacingAfter(10);

	        document.add(
	                slotHeading);

	        PdfPTable slotTable =
	                new PdfPTable(2);

	        slotTable.setWidthPercentage(100);

	        DateTimeFormatter formatter =
	                DateTimeFormatter.ofPattern(
	                        "hh:mm a");

	        booking.getBookingSlots()
	                .forEach(bs -> {

	                    String start =
	                            bs.getSlot()
	                              .getSlotTime()
	                              .format(formatter);

	                    String end =
	                            bs.getSlot()
	                              .getSlotTime()
	                              .plusMinutes(30)
	                              .format(formatter);

	                    PdfPCell slotCell =
	                            new PdfPCell(
	                                    new Phrase(
	                                            start
	                                            + " - "
	                                            + end,
	                                            headingFont));

	                    slotCell.setBackgroundColor(
	                            lightGreen);

	                    slotCell.setBorderColor(
	                            new BaseColor(
	                                    205,
	                                    230,
	                                    223));

	                    slotCell.setPadding(10);

	                    slotCell.setHorizontalAlignment(
	                            Element.ALIGN_CENTER);

	                    slotTable.addCell(
	                            slotCell);
	                });

	        document.add(
	                slotTable);

	        document.add(
	                new Paragraph(" "));

	        Paragraph paymentHeading =
	                new Paragraph(
	                        "PAYMENT DETAILS",
	                        headingFont);

	        document.add(
	                paymentHeading);

	        document.add(
	                new Paragraph(
	                        "Order ID : "
	                                + payment.getRazorpayOrderId()));

	        document.add(
	                new Paragraph(
	                        "Payment ID : "
	                                + payment.getRazorpayPaymentId()));

	        document.add(
	                new Paragraph(" "));

	        PdfPTable amountTable =
	                new PdfPTable(1);

	        amountTable.setWidthPercentage(100);

	        PdfPCell amountCell =
	                new PdfPCell();

	        amountCell.setBackgroundColor(
	                dksaOrange);

	        amountCell.setBorder(0);

	        amountCell.setPadding(20);

	        Paragraph amountTitle =
	                new Paragraph(
	                        "TOTAL PAID",
	                        whiteFont);

	        amountTitle.setAlignment(
	                Element.ALIGN_CENTER);

	        Paragraph amount =
	                new Paragraph(
	                        "₹" +
	                        payment.getAmount(),
	                        amountFont);

	        amount.setAlignment(
	                Element.ALIGN_CENTER);

	        amountCell.addElement(
	                amountTitle);

	        amountCell.addElement(
	                amount);

	        amountTable.addCell(
	                amountCell);

	        document.add(
	                amountTable);

	        document.add(
	                new Paragraph(" "));

	        Paragraph footer =
	                new Paragraph(
	                        "Thank you for choosing DKSA",
	                        headingFont);

	        footer.setAlignment(
	                Element.ALIGN_CENTER);

	        document.add(
	                footer);

	        document.close();
	        
	        System.out.println("---------enddddd------------");

	        return baos.toByteArray();

	    } catch (Exception ex) {

	        throw new RuntimeException(
	                ex.getMessage());
	    }
	    
	    
	    
	}
	
	@Override
	public byte[] generateBookingHistoryPdf() {

	    try {

	        Authentication authentication =
	                SecurityContextHolder
	                        .getContext()
	                        .getAuthentication();

	        String email = authentication.getName();

	        User user =
	                userRepository
	                        .findByEmail(email)
	                        .orElseThrow(() ->
	                                new ResourceNotFoundException(
	                                        "User not found"));

	        List<Booking> bookings =
	                bookingRepository
	                        .findByUserId(user.getId());

	        ByteArrayOutputStream baos =
	                new ByteArrayOutputStream();

	        Document document =
	                new Document(PageSize.A4.rotate());

	        PdfWriter.getInstance(document, baos);

	        document.open();

	        BaseColor dksaGreen =
	                new BaseColor(15, 90, 85);

	        BaseColor dksaOrange =
	                new BaseColor(255, 107, 26);

	        Font titleFont =
	                new Font(
	                        Font.FontFamily.HELVETICA,
	                        24,
	                        Font.BOLD,
	                        dksaGreen);

	        Font sectionFont =
	                new Font(
	                        Font.FontFamily.HELVETICA,
	                        12,
	                        Font.BOLD);

	        Font normalFont =
	                new Font(
	                        Font.FontFamily.HELVETICA,
	                        11);

	        Font whiteFont =
	                new Font(
	                        Font.FontFamily.HELVETICA,
	                        11,
	                        Font.BOLD,
	                        BaseColor.WHITE);

	        Paragraph title =
	                new Paragraph(
	                        "DKSA BOOKING HISTORY",
	                        titleFont);

	        title.setAlignment(Element.ALIGN_CENTER);

	        document.add(title);

	        Paragraph academy =
	                new Paragraph(
	                        "Don Bosco Kohli Sports Academy",
	                        normalFont);

	        academy.setAlignment(Element.ALIGN_CENTER);

	        document.add(academy);

	        document.add(new Paragraph(" "));

	        document.add(
	                new Paragraph(
	                        "Customer : " + user.getName(),
	                        sectionFont));

	        document.add(
	                new Paragraph(
	                        "Email : " + user.getEmail(),
	                        normalFont));

	        document.add(
	                new Paragraph(
	                        "Generated On : " + LocalDate.now(),
	                        normalFont));

	        document.add(new Paragraph(" "));

	        long paidCount =
	                bookings.stream()
	                        .filter(b ->
	                                b.getPaymentStatus()
	                                        == PaymentStatus.PAID)
	                        .count();

	        long pendingCount =
	                bookings.stream()
	                        .filter(b ->
	                                b.getPaymentStatus()
	                                        == PaymentStatus.PENDING)
	                        .count();

	        double revenue =
	                bookings.stream()
	                        .filter(b ->
	                                b.getPaymentStatus()
	                                        == PaymentStatus.PAID)
	                        .mapToDouble(
	                                Booking::getTotalAmount)
	                        .sum();

	        document.add(
	                new Paragraph(
	                        "Total Bookings : "
	                                + bookings.size(),
	                        sectionFont));

	        document.add(
	                new Paragraph(
	                        "Paid Bookings : "
	                                + paidCount,
	                        normalFont));

	        document.add(
	                new Paragraph(
	                        "Pending Bookings : "
	                                + pendingCount,
	                        normalFont));

	        document.add(
	                new Paragraph(
	                        "Total Revenue : ₹"
	                                + revenue,
	                        normalFont));

	        document.add(new Paragraph(" "));

	        PdfPTable table =
	                new PdfPTable(6);

	        table.setWidthPercentage(100);

	        table.setWidths(
	                new float[]{
	                        1f,
	                        3f,
	                        2f,
	                        5f,
	                        2f,
	                        2f
	                });

	        String[] headers = {
	                "ID",
	                "Turf",
	                "Date",
	                "Slot Timings",
	                "Amount",
	                "Status"
	        };

	        for (String header : headers) {

	            PdfPCell cell =
	                    new PdfPCell(
	                            new Phrase(
	                                    header,
	                                    whiteFont));

	            cell.setBackgroundColor(
	                    dksaGreen);

	            cell.setHorizontalAlignment(
	                    Element.ALIGN_CENTER);

	            cell.setPadding(8);

	            table.addCell(cell);
	        }

	        DateTimeFormatter dateFormatter =
	                DateTimeFormatter.ofPattern(
	                        "dd MMM yyyy");

	        DateTimeFormatter timeFormatter =
	                DateTimeFormatter.ofPattern(
	                        "hh:mm a");

	        for (Booking booking : bookings) {

	            table.addCell(
	                    String.valueOf(
	                            booking.getId()));

	            table.addCell(
	                    booking.getTurf()
	                            .getTurfName());

	            table.addCell(
	                    booking.getBookingTime()
	                            .format(dateFormatter));

	            String slotTimes =
	                    booking.getBookingSlots()
	                            .stream()
	                            .map(bs -> {

	                                var start =
	                                        bs.getSlot()
	                                                .getSlotTime();

	                                var end =
	                                        start.plusMinutes(30);

	                                return start.format(
	                                                timeFormatter)
	                                        + " - "
	                                        + end.format(
	                                                timeFormatter);

	                            })
	                            .reduce(
	                                    "",
	                                    (a, b) ->
	                                            a.isEmpty()
	                                                    ? b
	                                                    : a + "\n" + b);

	            table.addCell(slotTimes);

	            table.addCell(
	                    String.format(
	                            "₹%.0f",
	                            booking.getTotalAmount()));

	            PdfPCell statusCell =
	                    new PdfPCell(
	                            new Phrase(
	                                    booking.getPaymentStatus()
	                                            .name(),
	                                    normalFont));

	            statusCell.setHorizontalAlignment(
	                    Element.ALIGN_CENTER);

	            if (booking.getPaymentStatus()
	                    == PaymentStatus.PAID) {

	                statusCell.setBackgroundColor(
	                        new BaseColor(
	                                220,
	                                252,
	                                231));

	            } else {

	                statusCell.setBackgroundColor(
	                        new BaseColor(
	                                255,
	                                237,
	                                213));
	            }

	            table.addCell(statusCell);
	        }

	        document.add(table);

	        document.add(new Paragraph(" "));

	        Paragraph footer =
	                new Paragraph(
	                        "PLAY • TRAIN • WIN",
	                        new Font(
	                                Font.FontFamily.HELVETICA,
	                                14,
	                                Font.BOLD,
	                                dksaOrange));

	        footer.setAlignment(
	                Element.ALIGN_CENTER);

	        document.add(footer);

	        Paragraph thanks =
	                new Paragraph(
	                        "Thank you for choosing DKSA.",
	                        sectionFont);

	        thanks.setAlignment(
	                Element.ALIGN_CENTER);

	        document.add(thanks);

	        document.close();

	        return baos.toByteArray();

	    } catch (Exception ex) {

	        throw new RuntimeException(
	                ex.getMessage());
	    }
	}
}