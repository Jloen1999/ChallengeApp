package es.uex.challengeapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uex.challengeapp.model.Notificacion;
import es.uex.challengeapp.model.Reto;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
	List<Notificacion> findByUsuarioId(Long id);

	List<Notificacion> findByReto(Reto reto);
}
