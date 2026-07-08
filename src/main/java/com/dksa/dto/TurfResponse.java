package com.dksa.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TurfResponse {

    private Long id;

    private String turfName;

    private String turfType;

    private String location;
    
    private String imageUrl;

	private List<String> imageUrls;

    private Boolean active;
}