package it.trinex.spespappbe.dto.request;

import lombok.Getter;

@Getter
public class CompleteLoginRequest {
    private String challengeId;
    private String signatureBase64;
}
