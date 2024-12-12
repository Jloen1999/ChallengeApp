package es.uex.challengeapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uex.challengeapp.model.RetoComplejo;

@Repository
public interface RetoComplejoRepository extends JpaRepository<RetoComplejo, Integer> {

}
