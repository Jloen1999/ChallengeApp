package es.uex.challengeapp.service;

import java.util.List;

import es.uex.challengeapp.model.Reto;

public interface RetoService {

	List<Reto> getNovedososRetos();

	Reto crearReto(Reto reto);

	List<Reto> obtenerRetosCreadosPorUsuario(Long usuarioId);

	Reto obtenerReto(Long id);
	
	List<Reto> obtenerTodosLosRetos();
}
