package es.uex.challengeapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uex.challengeapp.model.Punto;
import es.uex.challengeapp.model.Usuario;

@Repository
public interface PuntoRepository extends JpaRepository<Punto, Integer>{

	List<Punto> findByUsuario(Usuario usuario);

}
