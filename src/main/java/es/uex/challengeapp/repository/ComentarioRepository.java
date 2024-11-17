package es.uex.challengeapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uex.challengeapp.model.Comentario;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

	List<Comentario> findByRetoId(Long retoId);

}
