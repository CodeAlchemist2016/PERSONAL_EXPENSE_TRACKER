package com.medvedev.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_join_date", columnList = "joinDate")
})
@ToString(exclude = "password")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "User_Id")
    private Integer userId;

    @Column(name = "Name", nullable = false, length = 100)
    @NotBlank(message = "Name is required")
    private String name;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Account> accounts;

    @Column(name = "Email", nullable = false, length = 150, unique = true)
    @Email(message = "Invalid email address")
    private String email;

    @JsonIgnore
    @Column(name = "Password", nullable = false)
    private String password;

    @Column(name = "joinDate", nullable = false)
    private LocalDateTime joinDate = LocalDateTime.now();

    @PrePersist
    protected void onCreate() {
        if (this.joinDate == null) {
            this.joinDate = LocalDateTime.now();
        }
    }

    public void setId(Integer id) {
        this.userId = id;
    }
}
