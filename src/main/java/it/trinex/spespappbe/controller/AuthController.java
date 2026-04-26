package it.trinex.spespappbe.controller;

import it.trinex.spespappbe.dto.request.auth.CompleteLoginRequest;
import it.trinex.spespappbe.dto.request.auth.InitLoginRequest;
import it.trinex.spespappbe.dto.response.auth.CompleteLoginResponse;
import it.trinex.spespappbe.model.AuthChallenge;
import it.trinex.spespappbe.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
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

    @PostMapping("/login/test")
    public ResponseEntity<CompleteLoginResponse> testLogin() {
        CompleteLoginResponse response = CompleteLoginResponse.builder()
                .authToken("eyJhbGciOiJIUzI1NiJ9.eyJ1aWQiOjEsInN1YiI6Im1hbnUiLCJpYXQiOjE3NzcxNTM1NjIsImV4cCI6MTc3OTc0NTU2Mn0.msAu2reLLQTFA5oxHaw-Z-A-RvT-mff44_4sshHGD90")
                .build();
        return ResponseEntity.ok(response);
    }

}
