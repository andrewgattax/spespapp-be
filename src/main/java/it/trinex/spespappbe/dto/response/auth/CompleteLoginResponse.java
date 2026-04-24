package it.trinex.spespappbe.dto.response.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CompleteLoginResponse {
    private String authToken;
}
