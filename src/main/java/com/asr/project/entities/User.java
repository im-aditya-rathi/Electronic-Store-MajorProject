package com.asr.project.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name="users")
public class User {

    @Id
    private String userId;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String email;

    @Column(length = 10)
    private String password;

    @Column(length = 10)
    private String gender;

    @Column(length = 500)
    private String about;

    private String userImage;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Order> orders = new ArrayList<>();

}
