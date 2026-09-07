package de.eon.cidgen.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Stores the next decimal number for one CID range letter.
 */
@Entity
@Table(name = "cid_counter")
public class CidCounter {

    @Id
    @Column(name = "range_letter")
    private String rangeLetter;

    @Column(name = "counter")
    private long counter;

    protected CidCounter() {
        // JPA needs a no-argument constructor to create objects from database rows.
    }

    public CidCounter(String rangeLetter, long counter) {
        this.rangeLetter = rangeLetter;
        this.counter = counter;
    }

    public String getRangeLetter() {
        return rangeLetter;
    }

    public long getCounter() {
        return counter;
    }

    public void increaseCounter() {
        counter++;
    }
}
