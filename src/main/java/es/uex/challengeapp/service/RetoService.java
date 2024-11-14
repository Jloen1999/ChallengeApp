package es.uex.challengeapp.service;

import java.util.List;

import es.uex.challengeapp.model.Reto;

public interface RetoService {

	List<Reto> getNovedososRetos();

	Reto crearReto(Reto reto);

	List<Reto> obtenerRetosPorUsuario(Long usuarioId);
}
