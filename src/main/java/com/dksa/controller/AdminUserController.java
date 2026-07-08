package com.dksa.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dksa.entity.User;
import com.dksa.repository.UserRepository;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {
	
	private final UserRepository userRepository;
	
	
	public AdminUserController(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}


	/*@GetMapping
	public ResponseEntity<List<User>>
	getAllUsers() {

	    return ResponseEntity.ok(
	            userRepository
	                    .findAllByOrderByIdDesc());
	}*/
	
	
	@PutMapping("/{id}/toggle")
	public ResponseEntity<String>
	toggleUser(
	        @PathVariable Long id,
	        @RequestParam Boolean active) {

	    User user =
	            userRepository
	                    .findById(id)
	                    .orElseThrow();

	    user.setActive(active);

	    userRepository.save(user);

	    return ResponseEntity.ok(
	            "User Updated");
	}
}
