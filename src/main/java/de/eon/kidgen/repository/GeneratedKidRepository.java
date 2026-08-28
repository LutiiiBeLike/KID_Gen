package de.eon.kidgen.repository;

import de.eon.kidgen.entity.GeneratedKid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Saves the KID audit history in PostgreSQL.
 */
@Repository
public interface GeneratedKidRepository extends JpaRepository<GeneratedKid, Long> {
}
