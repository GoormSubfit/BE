package com.example.subfit.repository.service;

import com.example.subfit.entity.service.CloudService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CloudServiceRepository extends JpaRepository<CloudService, Long> {
    List<CloudService> findAll();
}
