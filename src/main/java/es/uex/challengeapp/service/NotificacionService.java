package es.uex.challengeapp.service;

import java.util.List;

import es.uex.challengeapp.model.Notificacion;

public interface NotificacionService {

	Notificacion crearNotificacion(Notificacion notificacion);
	
	List<Notificacion> obtenerNotificacionesPorUsuario(Long usuarioId);

	void eliminarNotificacion(Long id);
}
