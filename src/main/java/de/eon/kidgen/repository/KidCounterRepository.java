package de.eon.kidgen.repository;

import de.eon.kidgen.entity.KidCounter;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Reads KID counters from PostgreSQL.
 */
@Repository
public interface KidCounterRepository extends JpaRepository<KidCounter, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT kidCounter FROM KidCounter kidCounter WHERE kidCounter.letter = :letter")
    KidCounter findByLetterForUpdate(@Param("letter") String letter);
}
