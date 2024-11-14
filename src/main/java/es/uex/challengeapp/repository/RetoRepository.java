package es.uex.challengeapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uex.challengeapp.model.Reto;

@Repository
public interface RetoRepository extends JpaRepository<Reto, Long> {
	List<Reto> findByNovedadTrue();
	List<Reto> findAllByCreadorId(Long creadorId);
}