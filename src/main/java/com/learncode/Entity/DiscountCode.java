package com.learncode.Entity;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "discount_codes")
public class DiscountCode {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "code", length = 30, unique = true, nullable = false)
    private String code;

    @Column(name = "value", length = 11, nullable = false)
    private int value;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @Column(name = "created_at")
    private Date createdAt;

    @PrePersist
    public void onCreate() {
        createdAt = new Date();
    }
}
