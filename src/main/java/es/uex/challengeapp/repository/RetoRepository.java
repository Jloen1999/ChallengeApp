package es.uex.challengeapp.repository;

import es.uex.challengeapp.model.Reto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RetoRepository extends JpaRepository<Reto, Long> {
    List<Reto> findByNovedadTrue();
}