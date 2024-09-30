package com.example.subfit.entity.service;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "delivery_service")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryService {
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

    @Column(name = "payment_benefit", length = 100)
    private String paymentBenefit;

    @Column(name = "family_account_sharing", length = 50)
    private String familyAccountSharing;

    @Column(name = "subscription_delivery_available")
    private boolean subscriptionDeliveryAvailable;

    @Column(name = "fresh_delivery")
    private boolean freshDelivery;

    @Column(name = "eco_friendly_packaging")
    private boolean ecoFriendlyPackaging;

    @Column(name = "special_benefit", length = 100)
    private String specialBenefit;
}
