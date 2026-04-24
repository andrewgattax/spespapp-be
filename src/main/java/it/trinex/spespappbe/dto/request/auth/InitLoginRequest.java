package it.trinex.spespappbe.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class InitLoginRequest {
    @NotBlank(message = "Lo username è richiesto")
    private String username;
}
