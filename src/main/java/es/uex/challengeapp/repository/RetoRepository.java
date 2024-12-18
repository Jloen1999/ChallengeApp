package es.uex.challengeapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uex.challengeapp.model.Reto;
import es.uex.challengeapp.model.Usuario;

@Repository
public interface RetoRepository extends JpaRepository<Reto, Long> {
	
	List<Reto> findByNovedadTrue();

	List<Reto> findAllByCreadorId(Long creadorId);
	
	Optional<Reto> findById(Long id);

	List<Reto> findByCreadorAndVisibilidad(Usuario usuario, boolean b);

	List<Reto> findByNombreContainingIgnoreCase(String criterioBusqueda);
}