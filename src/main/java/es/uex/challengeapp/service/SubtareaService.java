package es.uex.challengeapp.service;

import es.uex.challengeapp.model.Subtarea;

public interface SubtareaService {

	Subtarea guardarSubtarea(Subtarea subtarea);
	
	Subtarea buscarPorId(Integer subtareaId);
	
}
