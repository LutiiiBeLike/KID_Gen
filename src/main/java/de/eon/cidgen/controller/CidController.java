package de.eon.cidgen.controller;

import de.eon.cidgen.dto.CidRequest;
import de.eon.cidgen.dto.CidResponse;
import de.eon.cidgen.service.CidService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HTTP entry point for generating CIDs.
 */
@RestController
@RequestMapping("/api/cids")
public class CidController {

    private final CidService cidService;

    public CidController(CidService cidService) {
        this.cidService = cidService;
    }

    @PostMapping
    public ResponseEntity<CidResponse> generateCid(@Valid @RequestBody CidRequest request) {
        String cid = cidService.generateCid(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CidResponse(cid));
    }
}
