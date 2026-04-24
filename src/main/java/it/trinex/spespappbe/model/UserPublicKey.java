package it.trinex.spespappbe.model;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "user_public_keys")
@Getter
public class UserPublicKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private SpespappUser user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String publicKeyBase64;

    public UserPublicKey() {}

    public UserPublicKey(String publicKeyBase64) {
        this.publicKeyBase64 = publicKeyBase64;
    }
}
