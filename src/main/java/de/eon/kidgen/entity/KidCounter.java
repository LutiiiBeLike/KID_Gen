package de.eon.kidgen.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Stores the next number for one KID prefix letter.
 */
@Entity
@Table(name = "kid_counter")
public class KidCounter {

    @Id
    @Column(name = "letter")
    private String letter;

    @Column(name = "counter")
    private long counter;

    protected KidCounter() {
        // JPA needs a no-argument constructor to create objects from database rows.
    }

    public String getLetter() {
        return letter;
    }

    public long getCounter() {
        return counter;
    }

    public long increaseCounter() {
        counter++;
        return counter;
    }
}
