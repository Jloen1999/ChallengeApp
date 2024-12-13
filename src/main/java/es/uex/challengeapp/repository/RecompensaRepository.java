package es.uex.challengeapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uex.challengeapp.model.Recompensa;

@Repository
public interface RecompensaRepository extends JpaRepository<Recompensa, Integer> {

}
