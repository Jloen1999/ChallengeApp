package es.uex.challengeapp.service;

import java.util.List;

import es.uex.challengeapp.model.RetoComplejo;
import es.uex.challengeapp.model.Subtarea;

public interface RetoComplejoService {

	RetoComplejo guardarRetoComplejo(RetoComplejo retoComplejo);

	List<Subtarea> obtenerSubtareas(RetoComplejo retoComplejo);

}
