package com.example.server.model;

import com.example.server.model.enums.Gender;
import com.example.server.model.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "bkav_user")
public class BkavUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "username", updatable = false, nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "role", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "gender", nullable = false, length = 6)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToMany(mappedBy = "bkavUser")
    private List<Device> devices;


}
