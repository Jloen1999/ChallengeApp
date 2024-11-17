package es.uex.challengeapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.uex.challengeapp.model.ParticipantesReto;
import es.uex.challengeapp.model.Reto;
import es.uex.challengeapp.model.Usuario;

public interface ParticipantesRetoRepository extends JpaRepository<ParticipantesReto, Long> {

	@Query("SELECT pr.reto FROM ParticipantesReto pr WHERE pr.usuario.id = :usuarioId")
	List<Reto> findRetosByUsuarioId(@Param("usuarioId") Long usuarioId);
	
	@Query("SELECT pr.usuario FROM ParticipantesReto pr WHERE pr.reto.id = :retoId")
	List<Usuario> findUsuariosByRetoId(@Param("retoId") Long retoId);

}
