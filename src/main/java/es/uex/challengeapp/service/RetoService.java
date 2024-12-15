package es.uex.challengeapp.service;

import java.util.List;

import es.uex.challengeapp.model.Reto;
import es.uex.challengeapp.model.Usuario;

public interface RetoService {

	List<Reto> getNovedososRetos();

	Reto guardarReto(Reto reto);

	List<Reto> obtenerRetosCreadosPorUsuario(Long usuarioId);

	Reto obtenerReto(Long id);
	
	List<Reto> obtenerRetos(Usuario userActual);
	
	float tiempoEnCompletado(Usuario usuario, Reto reto);

	List<Reto> obtenerRetosNovedosos(Usuario userActual);
	
	List<Reto> obtenerRetosPrivados(Usuario usuario);

	List<Reto> mostrarRetosPrivadosAmigos(Usuario userActual);

	void eliminarReto(Reto reto);

	List<Reto> buscarPorNombre(String criterioBusqueda);

	List<Reto> obtenerTodosLosRetos();
}
