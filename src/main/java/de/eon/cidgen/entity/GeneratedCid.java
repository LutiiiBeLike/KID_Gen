package de.eon.cidgen.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * Stores one generated CID and the contract data needed for the audit history.
 */
@Entity
@Table(name = "generated_cid")
public class GeneratedCid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cid", nullable = false, unique = true, length = 6)
    private String cid;

    @Column(name = "hr_system", nullable = false)
    private String hrSystem;

    @Column(name = "eon_accounting_area_id", nullable = false)
    private String eonAccountingAreaID;

    @Column(name = "employee_number", nullable = false)
    private String employeeNumber;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected GeneratedCid() {
        // JPA needs a no-argument constructor to create objects from database rows.
    }

    public GeneratedCid(String cid, String hrSystem, String eonAccountingAreaID,
                        String employeeNumber, Instant createdAt) {
        this.cid = cid;
        this.hrSystem = hrSystem;
        this.eonAccountingAreaID = eonAccountingAreaID;
        this.employeeNumber = employeeNumber;
        this.createdAt = createdAt;
    }

    public String getCid() { return cid; }
    public String getHrSystem() { return hrSystem; }
    public String getEonAccountingAreaID() { return eonAccountingAreaID; }
    public String getEmployeeNumber() { return employeeNumber; }
    public Instant getCreatedAt() { return createdAt; }
}
