package it.trinex.spespappbe.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
public class CompleteLoginResponse {
    private String authToken;
}
