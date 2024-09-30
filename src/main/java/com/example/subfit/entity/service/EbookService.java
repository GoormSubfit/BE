package com.example.subfit.entity.service;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ebook_service")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EbookService {
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

    @Column(name = "total_books", length = 100)
    private String totalBooks;

    @Column(name = "most_genres", length = 50)
    private String mostGenres;

    @Column(name = "supported_devices", length = 100)
    private String supportedDevices;

    @Column(name = "foreign_translation")
    private boolean foreignTranslation;

    @Column(name = "offline_storage")
    private boolean offlineStorage;

    @Column(name = "special_benefit", length = 100)
    private String specialBenefit;
}
