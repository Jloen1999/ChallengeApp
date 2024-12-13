package es.uex.challengeapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uex.challengeapp.model.Estadistica;
import es.uex.challengeapp.model.Usuario;

@Repository
public interface EstadisticaRepository extends JpaRepository<Estadistica, Integer> {

	Optional<Estadistica> findByUsuario(Usuario usuario);

}
