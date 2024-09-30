package com.example.subfit.repository.service;

import com.example.subfit.entity.service.MusicService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MusicServiceRepository extends JpaRepository<MusicService, Long> {
    List<MusicService> findAll();
}
