package es.uex.challengeapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uex.challengeapp.model.ProgresoReto;
import es.uex.challengeapp.model.Reto;
import es.uex.challengeapp.model.Usuario;

@Repository
public interface ProgresoRetoRepository extends JpaRepository<ProgresoReto, Integer> {

	Optional<ProgresoReto> findByUsuarioAndReto(Usuario usuario, Reto reto);

}
