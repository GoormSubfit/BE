package com.example.subfit.repository.subscribe;

import com.example.subfit.entity.subscribe.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {
    List<Subscribe> findByUserId(Long userId);
}