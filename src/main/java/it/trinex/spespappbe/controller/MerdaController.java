package it.trinex.spespappbe.controller;

import it.trinex.spespappbe.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/merda")
@RequiredArgsConstructor
public class MerdaController {

    private final RedisService redisService;

    @PostMapping("/{merda}")
    public ResponseEntity<Void> postMerda(@PathVariable String merda) {
        redisService.put("merda", merda, 20);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<String> getMerda() {
        return ResponseEntity.ok(redisService.get("merda"));
    }
}
