package com.example.subfit.repository.service;

import com.example.subfit.entity.service.EbookService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EbookServiceRepository extends JpaRepository<EbookService, Long> {
    List<EbookService> findAll();
}
