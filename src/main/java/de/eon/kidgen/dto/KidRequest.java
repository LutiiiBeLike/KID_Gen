package de.eon.kidgen.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * JSON input accepted by the KID API.
 */
public class KidRequest {

    @NotBlank(message = "sn must not be empty")
    private String sn;

    @NotBlank(message = "givenName must not be empty")
    private String givenName;

    @NotBlank(message = "eonBUshort must not be empty")
    private String eonBUshort;

    @NotBlank(message = "eonUserType must not be empty")
    private String eonUserType;

    @NotBlank(message = "eonUserPurpose must not be empty")
    private String eonUserPurpose;

    private String description;

    public KidRequest() {
        // Jackson uses this constructor before filling the fields from JSON.
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getEonBUshort() {
        return eonBUshort;
    }

    public void setEonBUshort(String eonBUshort) {
        this.eonBUshort = eonBUshort;
    }

    public String getEonUserType() {
        return eonUserType;
    }

    public void setEonUserType(String eonUserType) {
        this.eonUserType = eonUserType;
    }

    public String getEonUserPurpose() {
        return eonUserPurpose;
    }

    public void setEonUserPurpose(String eonUserPurpose) {
        this.eonUserPurpose = eonUserPurpose;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
