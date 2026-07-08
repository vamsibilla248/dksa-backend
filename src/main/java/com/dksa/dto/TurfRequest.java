package com.dksa.dto;

import lombok.Data;

@Data
public class TurfRequest {

    private String turfName;

    private String turfType;

    private String location;
    
    private String imageUrl;

    private Boolean active;
    
}