package de.eon.kidgen.model;

/**
 * Contains the information supplied when a KID is generated.
 */
public class KidRequest {

    private final String sn;
    private final String givenName;
    private final String eonBUshort;
    private final String eonUserType;
    private final String eonUserPurpose;
    private final String description;

    public KidRequest(String sn, String givenName, String eonBUshort,
                      String eonUserType, String eonUserPurpose, String description) {
        this.sn = requireText(sn, "sn");
        this.givenName = requireText(givenName, "givenName");
        this.eonBUshort = requireText(eonBUshort, "eonBUshort");
        this.eonUserType = requireText(eonUserType, "eonUserType");
        this.eonUserPurpose = requireText(eonUserPurpose, "eonUserPurpose");
        this.description = description;
    }

    private String requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be empty");
        }

        return value;
    }

    public String getSn() {
        return sn;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getEonBUshort() {
        return eonBUshort;
    }

    public String getEonUserType() {
        return eonUserType;
    }

    public String getEonUserPurpose() {
        return eonUserPurpose;
    }

    public String getDescription() {
        return description;
    }
}
