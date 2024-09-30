package com.example.subfit.repository.service;

import com.example.subfit.entity.service.DeliveryService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryServiceRepository extends JpaRepository<DeliveryService, Long> {
    List<DeliveryService> findAll();
}
