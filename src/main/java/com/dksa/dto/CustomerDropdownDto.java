package com.dksa.dto;

public class CustomerDropdownDto {

    private Long id;
    private String customerName;

    public CustomerDropdownDto() {
    }

    public CustomerDropdownDto(Long id, String customerName) {
        this.id = id;
        this.customerName = customerName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
