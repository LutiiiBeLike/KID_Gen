package de.eon.kidgen.service;

import de.eon.kidgen.entity.GeneratedKid;
import de.eon.kidgen.entity.KidCounter;
import de.eon.kidgen.model.KidRequest;
import de.eon.kidgen.repository.GeneratedKidRepository;
import de.eon.kidgen.repository.KidCounterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.time.Instant;
import java.util.logging.Logger;

/**
 * Creates KIDs and stores both their counter and audit record in PostgreSQL.
 */
@Service
public class KidService {

    private static final Logger LOGGER = Logger.getLogger(KidService.class.getName());

    private final KidCounterRepository kidCounterRepository;
    private final GeneratedKidRepository generatedKidRepository;

    public KidService(KidCounterRepository kidCounterRepository,
                      GeneratedKidRepository generatedKidRepository) {
        this.kidCounterRepository = kidCounterRepository;
        this.generatedKidRepository = generatedKidRepository;
    }

    /**
     * A transaction makes the counter update and audit record one all-or-nothing operation.
     */
    @Transactional
    public String generateKid(KidRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request must not be null");
        }

        char letter = getKidLetter(request.getGivenName(), request.getSn());
        KidCounter kidCounter = kidCounterRepository.findByLetterForUpdate(String.valueOf(letter));

        if (kidCounter == null) {
            throw new IllegalStateException("No counter exists for letter " + letter);
        }

        long nextCounter = kidCounter.increaseCounter();
        String kid = letter + formatCounter(nextCounter);
        Instant generationTime = Instant.now();

        kidCounterRepository.save(kidCounter);
        generatedKidRepository.save(new GeneratedKid(
                kid,
                request.getSn(),
                request.getGivenName(),
                request.getEonBUshort(),
                request.getEonUserType(),
                request.getEonUserPurpose(),
                request.getDescription(),
                generationTime
        ));

        logGeneratedKid(kid, request, generationTime);
        return kid;
    }

    public char getKidLetter(String givenName, String sn) {
        Character letterFromGivenName = convertToAsciiLetter(givenName);
        if (letterFromGivenName != null) {
            return letterFromGivenName;
        }

        Character letterFromSn = convertToAsciiLetter(sn);
        if (letterFromSn != null) {
            return letterFromSn;
        }

        return 'X';
    }

    public Character convertToAsciiLetter(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        // Work with one Unicode character before removing its accent marks.
        int firstCodePoint = value.codePointAt(0);
        String firstCharacter = new String(Character.toChars(firstCodePoint));
        String normalized = Normalizer.normalize(firstCharacter, Normalizer.Form.NFD);
        String withoutAccentMarks = normalized.replaceAll("\\p{M}", "");

        if (withoutAccentMarks.length() != 1) {
            return null;
        }

        char candidate = withoutAccentMarks.charAt(0);
        if (candidate >= 'A' && candidate <= 'Z') {
            return candidate;
        }
        if (candidate >= 'a' && candidate <= 'z') {
            return Character.toUpperCase(candidate);
        }

        return null;
    }

    public String formatCounter(long counter) {
        return String.format("%04d", counter);
    }

    private void logGeneratedKid(String kid, KidRequest request, Instant generationTime) {
        String message = "Generated KID: " + kid
                + ", givenName: " + request.getGivenName()
                + ", sn: " + request.getSn()
                + ", eonBUshort: " + request.getEonBUshort()
                + ", eonUserType: " + request.getEonUserType()
                + ", eonUserPurpose: " + request.getEonUserPurpose()
                + ", description: " + request.getDescription()
                + ", generation time: " + generationTime;
        LOGGER.info(message);
    }
}
