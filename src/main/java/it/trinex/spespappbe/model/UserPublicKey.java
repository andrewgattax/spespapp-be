package it.trinex.spespappbe.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "user_public_keys",
        uniqueConstraints = { @UniqueConstraint(columnNames = {"user_id", "device_id"} )
})
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
    @JoinColumn(name = "user_id")
    private SpespappUser user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String publicKeyBase64;

    @Column(nullable = false)
    private String deviceId;
}
