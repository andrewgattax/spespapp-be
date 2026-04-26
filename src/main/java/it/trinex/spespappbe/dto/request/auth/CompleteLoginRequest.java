package it.trinex.spespappbe.dto.request.auth;

import lombok.Getter;

@Getter
public class CompleteLoginRequest {
    private String challengeId;
    private String signatureBase64;
    private String deviceId;
}
