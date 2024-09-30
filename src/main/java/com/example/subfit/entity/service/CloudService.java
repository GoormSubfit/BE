package com.example.subfit.entity.service;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cloud_service")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CloudService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "price_per_storage", length = 100)
    private String pricePerStorage;

    @Column(name = "mobile_benefit", length = 100)
    private String mobileBenefit;

    @Column(name = "card_benefit", length = 100)
    private String cardBenefit;

    @Column(name = "primary_use", length = 100)
    private String primaryUse;

    @Column(name = "max_storage", length = 50)
    private String maxStorage;

    @Column(name = "auto_backup")
    private boolean autoBackup;

    @Column(name = "supported_device", length = 50)
    private String supportedDevice;

    @Column(name = "file_sharing")
    private boolean fileSharing;

    @Column(name = "collaboration")
    private boolean collaboration;

    @Column(name = "os_support", length = 100)
    private String osSupport;

    @Column(name = "special_benefit", length = 100)
    private String specialBenefit;
}
