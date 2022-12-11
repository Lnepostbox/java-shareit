package ru.practicum.shareit.user.model;
import lombok.*;
import javax.persistence.*;

<<<<<<< HEAD:server/src/main/java/ru/practicum/shareit/user/model/User.java
=======
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
>>>>>>> main:src/main/java/ru/practicum/shareit/user/model/User.java
@Entity
@Table(name = "users", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String name;

    @Column(name = "email", unique = true, length = 512, nullable = false)
    private String email;
}