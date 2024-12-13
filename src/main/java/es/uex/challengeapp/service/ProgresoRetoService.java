package es.uex.challengeapp.service;

import java.time.Duration;
import java.util.List;

import es.uex.challengeapp.model.ProgresoReto;
import es.uex.challengeapp.model.Reto;
import es.uex.challengeapp.model.Usuario;

public interface ProgresoRetoService {

	ProgresoReto actualizarProgreso(ProgresoReto progresoReto);

	ProgresoReto buscarProgresoReto(Usuario usuario, Reto reto);

	List<Reto> obtenerRetosCompletadosPorUsuario(Usuario usuario);
	
	boolean estaCompletado(Usuario usuario,Reto reto);

	List<Reto> obtenerRetosFallidos(Usuario usuario);

	Duration obtenerTiempoTotalDeCompletado(Usuario usuario);

}
