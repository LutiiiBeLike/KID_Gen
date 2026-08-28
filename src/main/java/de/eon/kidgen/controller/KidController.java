package de.eon.kidgen.controller;

import de.eon.kidgen.dto.KidRequest;
import de.eon.kidgen.dto.KidResponse;
import de.eon.kidgen.service.KidService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HTTP entry point for generating KIDs.
 */
@RestController
@RequestMapping("/api/kids")
public class KidController {

    private final KidService kidService;

    public KidController(KidService kidService) {
        this.kidService = kidService;
    }

    @PostMapping
    public ResponseEntity<KidResponse> generateKid(@Valid @RequestBody KidRequest request) {
        String kid = kidService.generateKid(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new KidResponse(kid));
    }
}
