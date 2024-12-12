package es.uex.challengeapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uex.challengeapp.model.RetoSimple;


@Repository
public interface RetoSimpleRepository extends JpaRepository<RetoSimple, Integer> {

}
