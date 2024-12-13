package es.uex.challengeapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.uex.challengeapp.model.ParticipantesReto;
import es.uex.challengeapp.model.Reto;
import es.uex.challengeapp.model.Usuario;

@Repository
public interface ParticipantesRetoRepository extends JpaRepository<ParticipantesReto, Long> {

	@Query("SELECT pr.reto FROM ParticipantesReto pr WHERE pr.usuario.id = :usuarioId")
	List<Reto> findRetosByUsuarioId(@Param("usuarioId") Long usuarioId);

	@Query("SELECT pr.usuario FROM ParticipantesReto pr WHERE pr.reto.id = :retoId")
	List<Usuario> findUsuariosByRetoId(@Param("retoId") Long retoId);

	@Query("SELECT DISTINCT p.usuario FROM ParticipantesReto p")
	List<Usuario> findAllDistinctUsuarios();

	boolean existsByUsuarioIdAndRetoId(Long usuarioId, Long retoId);

	Optional<ParticipantesReto> findByUsuarioAndReto(Usuario usuario, Reto reto);

	@Query("SELECT p.reto, COUNT(p) as participantes " + "FROM ParticipantesReto p " + "GROUP BY p.reto "
			+ "ORDER BY participantes DESC")
	List<Object[]> obtenerRetosMasParticipantes();

}
