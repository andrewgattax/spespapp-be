package it.trinex.spespappbe.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdatePublicKeyRequest {
    @NotBlank(message = "La chiave pubblica è richiesta")
    private String publicKeyBase64;
    @NotBlank(message = "Il device ID è richiesto")
    private String deviceId;
}
