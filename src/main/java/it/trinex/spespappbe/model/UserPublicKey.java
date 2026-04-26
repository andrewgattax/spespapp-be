package it.trinex.spespappbe.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_public_keys")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPublicKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private SpespappUser user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String publicKeyBase64;

    @Column(nullable = false, unique = true)
    private String deviceId;
}
