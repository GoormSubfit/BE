package com.example.subfit.entity.service;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "music_service")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MusicService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "price", length = 50)
    private String price;

    @Column(name = "mobile_benefit", length = 100)
    private String mobileBenefit;

    @Column(name = "card_benefit", length = 100)
    private String cardBenefit;

    @Column(name = "total_song", length = 50)
    private String totalSong;

    @Column(name = "account_sharing")
    private boolean accountSharing;

    @Column(name = "recommendation_algorithm")
    private boolean recommendationAlgorithm;

    @Column(name = "audio_quality", length = 50)
    private String audioQuality;

    @Column(name = "audiobook_included")
    private boolean audiobookIncluded;

    @Column(name = "offline_storage")
    private boolean offlineStorage;

    @Column(name = "special_benefit", length = 100)
    private String specialBenefit;
}
