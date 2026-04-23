package it.trinex.spespappbe.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

/**
 * Servizio di utilità per la generazione e validazione dei token JWT.
 */
@Service
public class JwtService {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.expiration-ms:3600000}")
    private long expirationMs;

    /**
     * Estrae lo username (subject) dal token.
     */
    public String estraiUsername(String token) {
        return estraiClaim(token, Claims::getSubject);
    }

    /**
     * Estrae il ruolo (claim "ruolo") dal token.
     */
    public String estraiRuolo(String token) {
        return estraiClaim(token, claims -> (String) claims.get("ruolo"));
    }

    /**
     * Estrae l'ID utente (claim "uid") dal token.
     */
    public Long estraiUserId(String token) {
        return estraiClaim(token, claims -> {
            Object val = claims.get("uid");
            if (val instanceof Number n) {
                return n.longValue();
            }
            if (val instanceof String s) {
                try { return Long.parseLong(s); } catch (NumberFormatException e) { return null; }
            }
            return null;
        });
    }

    /**
     * Estrae un claim addizionale dal token specificando il nome del claim.
     *
     * @param <T>       il tipo del valore del claim
     * @param token     il token JWT
     * @param claimName il nome del claim da estrarre
     * @return il valore del claim, o null se non presente
     */
    public <T> T estraiClaimExtra(String token, String claimName) {
        return estraiClaim(token, claims -> {
            Object val = claims.get(claimName);
            if (val == null) {
                return null;
            }
            try {
                @SuppressWarnings("unchecked")
                T result = (T) val;
                return result;
            } catch (ClassCastException e) {
                return null;
            }
        });
    }

    /**
     * Genera un token JWT per l'utente specificato con eventuali claim addizionali.
     */
    public String generaToken(Map<String, Object> extraClaims, UserDetails user) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(expirationMs)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Verifica se il token è valido per l'utente fornito.
     */
    public boolean isTokenValido(String token, UserDetails userDetails) {
        final String username = estraiUsername(token);
        return username.equals(userDetails.getUsername());
    }

    private <T> T estraiClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = estraiTuttiClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims estraiTuttiClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build().parseSignedClaims(token).getPayload();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Restituisce l'istante di scadenza calcolato rispetto alle proprietà di configurazione.
     */
    public Instant calcolaScadenza() {
        return Instant.now().plusMillis(expirationMs);
    }
}
