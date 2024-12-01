package es.uex.challengeapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.uex.challengeapp.model.Amistad;
import es.uex.challengeapp.model.AmistadId;
import es.uex.challengeapp.model.Usuario;

@Repository
public interface AmistadRepository extends JpaRepository<Amistad, AmistadId> {

	@Query("SELECT a.usuario2 FROM Amistad a WHERE a.usuario1.id = :usuarioId")
	List<Usuario> findAmigosByUsuarioId(@Param("usuarioId") int usuarioId);

	@Modifying
	@Query("DELETE FROM Amistad a WHERE a.usuario1.id = :usuarioId1 AND a.usuario2.id = :usuarioId2")
	void eliminarAmistad(@Param("usuarioId1") Integer usuarioId1, @Param("usuarioId2") Integer usuarioId2);

}
