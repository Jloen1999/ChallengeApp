package es.uex.challengeapp.service;

import java.util.List;

import es.uex.challengeapp.model.Recompensa;
import es.uex.challengeapp.model.Recompensa.TipoMedalla;
import es.uex.challengeapp.model.Usuario;

public interface RecompensaService {

	List<Recompensa> obtenerRecompensasDeUsuario(Usuario userActual);

	List<Recompensa> obtenerRecompensasBronceUsuario(Usuario usuario);
	
	List<Recompensa> obtenerRecompensasPlataUsuario(Usuario usuario);
	
	List<Recompensa> obtenerRecompensasOroUsuario(Usuario usuario);
	
	List<Recompensa> obtenerRecompensasDiamanteUsuario(Usuario usuario);

	Recompensa guardarRecompensa(Recompensa recompensa);

	List<Recompensa> obtenerRecompensasPorTipoYUsuario(TipoMedalla tipo, Usuario usuario);


}
