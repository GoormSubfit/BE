package com.example.subfit.dto.subscribe;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SubscribeRequestDto {
    private String name;
    private int price;
    private String cycle;
    private String subscribeDate;
    private MultipartFile logoImage;
}