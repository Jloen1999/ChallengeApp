package es.uex.challengeapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uex.challengeapp.model.Recompensa;
import es.uex.challengeapp.model.Recompensa.TipoMedalla;
import es.uex.challengeapp.model.Usuario;

@Repository
public interface RecompensaRepository extends JpaRepository<Recompensa, Integer> {

	List<Recompensa> findByUsuario(Usuario userActual);
	
	List<Recompensa> findByTipoAndUsuario(TipoMedalla tipo, Usuario usuario);

}
