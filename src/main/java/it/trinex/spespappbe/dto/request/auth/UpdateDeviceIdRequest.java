package it.trinex.spespappbe.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateDeviceIdRequest {
    @NotBlank(message = "Il device ID precedente è richiesto")
    private String previousDeviceId;
    @NotBlank(message = "Il nuovo device ID è richiesto")
    private String newDeviceId;
}
