package org.gfg.WalletService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gfg.model.UserIdentifier;
import org.gfg.model.WalletStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity(name = "wallet")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(unique = true)
    private int userId;

    @Column(unique = true)
    private String phoneNo;

    @Enumerated(EnumType.STRING)
    private UserIdentifier userIdentifier;

    @Column(unique = true)
    private String userIdentifierValue;

    private double balance;

    @Enumerated(EnumType.STRING)
    WalletStatus walletStatus;

    @CreationTimestamp
    Date createdOn;

    @UpdateTimestamp
    Date updatedOn;



}
