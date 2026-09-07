package de.eon.cidgen.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * JSON input accepted by the CID API.
 */
public class CidRequest {

    @NotBlank(message = "hrSystem must not be empty")
    private String hrSystem;

    @NotBlank(message = "eonAccountingAreaID must not be empty")
    private String eonAccountingAreaID;

    @NotBlank(message = "employeeNumber must not be empty")
    private String employeeNumber;

    public CidRequest() {
        // Jackson uses this constructor before filling the fields from JSON.
    }

    public String getHrSystem() {
        return hrSystem;
    }

    public void setHrSystem(String hrSystem) {
        this.hrSystem = hrSystem;
    }

    public String getEonAccountingAreaID() {
        return eonAccountingAreaID;
    }

    public void setEonAccountingAreaID(String eonAccountingAreaID) {
        this.eonAccountingAreaID = eonAccountingAreaID;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }
}
