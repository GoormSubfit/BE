package com.example.subfit.dto.subscribe;

import lombok.Data;

@Data
public class SubscribeResponseDto {
    private Long id;
    private String name;
    private int price;
    private String cycle;
    private String logoUrl;
    private String createdAt;
    private String updatedAt;
    private String subscribeDate;
}