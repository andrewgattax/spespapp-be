package it.trinex.spespappbe.controller;

import it.trinex.spespappbe.dto.request.CompleteLoginRequest;
import it.trinex.spespappbe.dto.request.InitLoginRequest;
import it.trinex.spespappbe.dto.response.CompleteLoginResponse;
import it.trinex.spespappbe.model.AuthChallenge;
import it.trinex.spespappbe.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;

    @PostMapping("/login/init")
    public ResponseEntity<AuthChallenge> initLogin(@Valid @RequestBody InitLoginRequest request) {
        AuthChallenge response = authService.initLogin(request.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login/complete")
    public ResponseEntity<CompleteLoginResponse> initLogin(@Valid @RequestBody CompleteLoginRequest request) {
        CompleteLoginResponse response = authService.completeLogin(request);
        return ResponseEntity.ok(response);
    }

}
