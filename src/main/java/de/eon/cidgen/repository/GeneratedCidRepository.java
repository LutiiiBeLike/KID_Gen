package de.eon.cidgen.repository;

import de.eon.cidgen.entity.GeneratedCid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Saves the CID audit history in PostgreSQL.
 */
@Repository
public interface GeneratedCidRepository extends JpaRepository<GeneratedCid, Long> {
}
