package es.uex.challengeapp.service;

import java.util.List;

import es.uex.challengeapp.model.Notificacion;
import es.uex.challengeapp.model.Usuario;

public interface NotificacionService {

	Notificacion crearNotificacion(Notificacion notificacion);
	
	List<Notificacion> obtenerNotificacionesPorUsuario(Long usuarioId);

	void eliminarNotificacion(Long id);

	boolean negociarAmistad(Usuario usuario, Usuario amigoUsuario, String tipo);

	Notificacion buscarPorId(Long notificacionId);
}
