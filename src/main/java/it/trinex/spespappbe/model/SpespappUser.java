package it.trinex.spespappbe.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class SpespappUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> publicKeys;
}
