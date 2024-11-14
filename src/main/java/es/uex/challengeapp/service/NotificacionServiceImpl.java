package es.uex.challengeapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uex.challengeapp.model.Notificacion;
import es.uex.challengeapp.repository.NotificacionRepository;

@Service
public class NotificacionServiceImpl implements NotificacionService {

	@Autowired
	private NotificacionRepository notificacionRepository;
	
	@Override
	public Notificacion crearNotificacion(Notificacion notificacion) {
		return notificacionRepository.save(notificacion);
	}

	@Override
	public List<Notificacion> obtenerNotificacionesPorUsuario(Long usuarioId) {
		return notificacionRepository.findByUsuarioId(usuarioId);
	}

}
