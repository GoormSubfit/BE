package com.example.subfit.repository.service;

import com.example.subfit.entity.service.OTTService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OTTServiceRepository extends JpaRepository<OTTService, Long> {
    List<OTTService> findAll();
}
