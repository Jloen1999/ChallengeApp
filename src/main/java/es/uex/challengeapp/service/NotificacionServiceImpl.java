package es.uex.challengeapp.service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uex.challengeapp.model.Notificacion;
import es.uex.challengeapp.model.Notificacion.TipoNotificacion;
import es.uex.challengeapp.model.Reto;
import es.uex.challengeapp.model.Usuario;
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

	@Override
	public void eliminarNotificacion(Long id) {
		notificacionRepository.deleteById(id);
	}

	@Override
	public boolean negociarAmistad(Usuario usuario, Usuario amigoUsuario, String tipo) {
		Notificacion notificacion = new Notificacion();
		String mensaje = "";
		if (tipo == "ENVIAR_SOLCITUD") {
			mensaje = "¡El usuario " + usuario.getCorreo() + " te ha enviado una solicitud de amistad!";
			notificacion.setTipoNotificacion(TipoNotificacion.SOLICITUD_AMISTAD);
		} else if (tipo == "ACEPTAR_SOLICITUD") {
			notificacion.setTipoNotificacion(TipoNotificacion.ACEPTACION_AMISTAD);
			mensaje = "¡El usuario " + usuario.getCorreo() + " ha aceptado tu solicitud de amistad!";
		}

		notificacion.setMensaje(mensaje);
		notificacion.setLeido(false);
		notificacion.setFechaEnvio(new Date(System.currentTimeMillis()));

		notificacion.setUsuario(amigoUsuario);
        return crearNotificacion(notificacion) != null;
    }

	@Override
	public Notificacion buscarPorId(Long notificacionId) {
		return notificacionRepository.findById(notificacionId).orElse(null);
	}

	@Override
	public List<Notificacion> obtenerNotificacionesPorReto(Reto reto) {
		return notificacionRepository.findByReto(reto);
	}

}
