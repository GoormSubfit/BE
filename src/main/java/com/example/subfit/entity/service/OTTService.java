package com.example.subfit.entity.service;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ott_service")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OTTService {
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

    @Column(name = "total_content", length = 50)
    private String totalContent;

    @Column(name = "simultaneous_devices", length = 50)
    private String simultaneousDevices;

    @Column(name = "ads_include", length = 50)
    private String adsInclude;

    @Column(name = "offline_storage")
    private boolean offlineStorage;

    @Column(name = "key_benefit", length = 100)
    private String keyBenefit;

    @Column(name = "special_benefit", length = 100)
    private String specialBenefit;
}
