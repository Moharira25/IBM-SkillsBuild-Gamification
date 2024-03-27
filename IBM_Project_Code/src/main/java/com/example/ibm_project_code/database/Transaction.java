package com.example.ibm_project_code.database;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
public class Transaction {

    public enum OrderType {
        BUY, SELL
    }

    // An enum to define the status of the transaction
    public enum TransactionStatus {
        PENDING, COMPLETED, CANCELLED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // This field is used to track whether the transaction is a buy or sell order
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderType orderType;

    // Nullable because a buy order may not immediately have a seller, and vice versa for sell orders
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private User seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    // BigDecimal is preferred for currency-related fields
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    // You might also want to track quantity in the transaction
    @Column(nullable = false)
    private int quantity;

    // A field to keep track of the status of the transaction
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Column(nullable = false)
    private Timestamp transactionDate = Timestamp.from(Instant.now());
}

