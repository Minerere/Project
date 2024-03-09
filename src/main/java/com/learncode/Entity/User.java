package com.learncode.Entity;

import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

	@Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;
    
    @Column(name = "password", length = 255, nullable = false)
    private String Password;

    @Column(name = "username", length = 50, nullable = true)
    private String username;

    @Column(name = "tel", length = 30, nullable = true)
    private String tel;

    @Column(name = "delivery_address", nullable = true)
    private String deliveryAddress;
    
    @Column(name = "role", length = 20, nullable = true)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "image", length = 500, nullable = true)
    private String image;
    
    @OneToOne(mappedBy = "user")
    private Cart cart;
    
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Order> orders;

    @OneToMany(mappedBy = "user")
    private List<ProductComment> productComments;
}
