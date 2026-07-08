package com.dksa.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileResponse {

    private String name;

    private String email;

    private String mobileNumber;

    private String profileImage;
}