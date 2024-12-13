package es.uex.challengeapp.service;

import es.uex.challengeapp.model.Estadistica;
import es.uex.challengeapp.model.Usuario;

public interface EstadisticaService {

	Estadistica actualizarEstadistica(Usuario usuario);
	
	Estadistica obtenerEstadisticaPorUsuario(Usuario usuario);

	void actualizarTodasLasEstadisticas();

	Estadistica guardarEstadistica(Estadistica estadistica);

}
