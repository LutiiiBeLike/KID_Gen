package de.eon.cidgen.repository;

import de.eon.cidgen.entity.CidCounter;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Reads CID counters from PostgreSQL.
 */
@Repository
public interface CidCounterRepository extends JpaRepository<CidCounter, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT cidCounter FROM CidCounter cidCounter WHERE cidCounter.rangeLetter = :rangeLetter")
    CidCounter findByRangeLetterForUpdate(@Param("rangeLetter") String rangeLetter);
}
