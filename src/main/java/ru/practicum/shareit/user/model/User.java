package ru.practicum.shareit.user.model;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "users", schema = "public")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name")
    private String name;

    @Column(name = "email", unique = true)
    private String email;
}