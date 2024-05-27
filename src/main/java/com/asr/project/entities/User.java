package com.asr.project.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="users")
public class User {

    @Id
    private String userId;

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

}
