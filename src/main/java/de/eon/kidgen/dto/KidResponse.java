package de.eon.kidgen.dto;

/**
 * JSON returned after a KID has been generated.
 */
public class KidResponse {

    private final String kid;

    public KidResponse(String kid) {
        this.kid = kid;
    }

    public String getKid() {
        return kid;
    }
}
