package de.eon.kidgen.service;

import de.eon.kidgen.model.KidRequest;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Creates KIDs using one in-memory counter for every uppercase ASCII letter.
 */
public class KidGenerator {

    private static final Logger LOGGER = Logger.getLogger(KidGenerator.class.getName());

    private final Map<Character, Integer> counters;

    public KidGenerator() {
        counters = new HashMap<>();

        // Each letter starts at zero so that its first generated KID ends in 0001.
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            counters.put(letter, 0);
        }
    }

    public String generateKid(KidRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request must not be null");
        }

        char letter = getKidLetter(request.getGivenName(), request.getSn());
        int nextCounter = counters.get(letter) + 1;
        counters.put(letter, nextCounter);

        String kid = letter + formatCounter(nextCounter);
        logGeneratedKid(kid, request);
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

    public String formatCounter(int counter) {
        return String.format("%04d", counter);
    }

    private void logGeneratedKid(String kid, KidRequest request) {
        String message = "Generated KID: " + kid
                + ", givenName: " + request.getGivenName()
                + ", sn: " + request.getSn()
                + ", eonBUshort: " + request.getEonBUshort()
                + ", eonUserType: " + request.getEonUserType()
                + ", eonUserPurpose: " + request.getEonUserPurpose()
                + ", description: " + request.getDescription()
                + ", generation time: " + LocalDateTime.now();
        LOGGER.info(message);
    }
}
