package es.uex.challengeapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uex.challengeapp.model.Comentario;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

}
