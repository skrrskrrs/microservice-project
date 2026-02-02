package customer_service.idempotency.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IdempotencyRepository extends JpaRepository<Idempotency, UUID> {
    /**
     * Versucht, einen neuen Idempotency-Key einzufügen.
     *
     * PostgreSQL: INSERT ... ON CONFLICT DO NOTHING sorgt dafür,
     * dass bei einem Konflikt (bereits existierender Key) kein Fehler
     * geworfen wird und kein Eintrag verändert wird.
     *
     * Rückgabewert:
     * 1 → neuer Datensatz wurde eingefügt (erster Request)
     * 0 → Key existiert bereits (Request wurde schon verarbeitet)
     *
     * @param id der Idempotency-Key
     * @return 1, wenn neu eingefügt; 0, wenn bereits existiert
     */
    @Modifying
    @Query(
            value = "INSERT INTO idempotency (id) VALUES (:id) ON CONFLICT DO NOTHING",
            nativeQuery = true
    )
    int insertIfNotExists(@Param("id") UUID id);
}
