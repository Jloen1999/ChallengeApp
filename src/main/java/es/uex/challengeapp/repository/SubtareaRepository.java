package es.uex.challengeapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uex.challengeapp.model.Subtarea;

@Repository
public interface SubtareaRepository extends JpaRepository<Subtarea, Integer> {

	List<Subtarea> findByRetoComplejoId(Long retoComplejoId);

}
