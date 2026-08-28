package de.eon.kidgen.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * Stores one generated KID and the request data needed for the audit history.
 */
@Entity
@Table(name = "generated_kid")
public class GeneratedKid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kid", nullable = false, unique = true)
    private String kid;

    @Column(name = "sn", nullable = false)
    private String sn;

    @Column(name = "given_name", nullable = false)
    private String givenName;

    @Column(name = "eon_bu_short", nullable = false)
    private String eonBUshort;

    @Column(name = "eon_user_type", nullable = false)
    private String eonUserType;

    @Column(name = "eon_user_purpose", nullable = false)
    private String eonUserPurpose;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected GeneratedKid() {
        // JPA needs a no-argument constructor to create objects from database rows.
    }

    public GeneratedKid(String kid, String sn, String givenName, String eonBUshort,
                        String eonUserType, String eonUserPurpose, String description,
                        Instant createdAt) {
        this.kid = kid;
        this.sn = sn;
        this.givenName = givenName;
        this.eonBUshort = eonBUshort;
        this.eonUserType = eonUserType;
        this.eonUserPurpose = eonUserPurpose;
        this.description = description;
        this.createdAt = createdAt;
    }
}
