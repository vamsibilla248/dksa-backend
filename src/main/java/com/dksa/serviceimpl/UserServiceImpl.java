package com.dksa.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dksa.dto.CustomerDto;
import com.dksa.repository.UserRepository;
import com.dksa.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<CustomerDto> getAllCustomers() {	

        return userRepository.findByRole("CUSTOMER")
                .stream()
                .map(user -> new CustomerDto(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getMobile()
                ))
                .toList();
    }
}