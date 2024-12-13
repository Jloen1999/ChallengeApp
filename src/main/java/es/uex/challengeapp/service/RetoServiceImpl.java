package es.uex.challengeapp.service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uex.challengeapp.model.ParticipantesReto;
import es.uex.challengeapp.model.ProgresoReto;
import es.uex.challengeapp.model.Reto;
import es.uex.challengeapp.model.Usuario;
import es.uex.challengeapp.repository.RetoRepository;

@Service
public class RetoServiceImpl implements RetoService {
	@Autowired
	private RetoRepository retoRepository;

	@Autowired
	private ProgresoRetoService progresoRetoService;

	@Autowired
	private ParticipantesRetoService participantesRetoService;

	@Autowired
	private AmistadService amistadService;

	@Override
	public List<Reto> getNovedososRetos() {
		return retoRepository.findByNovedadTrue();
	}

	@Override
	public Reto crearReto(Reto reto) {
		return retoRepository.save(reto);
	}

	@Override
	public List<Reto> obtenerRetosCreadosPorUsuario(Long usuarioId) {
		return retoRepository.findAllByCreadorId(usuarioId);
	}

	@Override
	public Reto obtenerReto(Long id) {
		return retoRepository.findById(id).orElse(null);
	}

	@Override
	public List<Reto> obtenerTodosLosRetos() {
		return retoRepository.findAll();
	}

	@Override
	public float tiempoEnCompletado(Usuario usuario, Reto reto) {
		ProgresoReto progresoReto = progresoRetoService.buscarProgresoReto(usuario, reto);
		ParticipantesReto participantesReto = participantesRetoService.obtenerParticipacionReto(usuario, reto);
		float tiempo = 0.0f;

		if (progresoReto != null && participantesReto != null) {
			Date fechaInicio;

			if (participantesReto.getFechaUnion() != null) {
				fechaInicio = participantesReto.getFechaUnion();
			} else {
				fechaInicio = reto.getFechaCreacion();
			}

			Instant inicio = fechaInicio.toInstant();
			Instant fin = progresoReto.getFechaActualizacion().toInstant();

			Duration duracion = Duration.between(inicio, fin);
			Long tiempoLong = duracion.getSeconds();
			tiempo = (float) (tiempoLong / 3600);
		}

		return tiempo;
	}

	@Override
	public List<Reto> obtenerRetosNovedosos() {
		gestionarRetosNovedosos();
		return retoRepository.findByNovedadTrue();
	}

	@Override
	public List<Reto> obtenerRetosPrivados(Usuario usuario) {
		return retoRepository.findByCreadorAndVisibilidad(usuario, false);
	}

	// FUNCIONES PRIVADAS AUXILIARES
	private void gestionarRetosNovedosos() {
		List<Reto> retosNovedosos = retoRepository.findByNovedadTrue();
		LocalDateTime fechaActual = LocalDateTime.now();

		for (Reto reto : retosNovedosos) {
			Date fechaCreacionDate = reto.getFechaCreacion();
			LocalDateTime fechaCreacion = fechaCreacionDate.toInstant().atZone(ZoneId.systemDefault())
					.toLocalDateTime();

			if (fechaCreacion.plusDays(5).isBefore(fechaActual)) {
				reto.setNovedad(false);
				retoRepository.save(reto);
			}
		}
	}

	@Override
	public List<Reto> mostrarRetosPrivadosAmigos(Usuario userActual) {
		List<Usuario> amigos = amistadService.obtenerAmigos(userActual.getId());
		List<Reto> retosPrivados = new ArrayList<>();

		for (Usuario amigo : amigos) {
			retosPrivados.addAll(obtenerRetosPrivados(amigo));
		}

		return retosPrivados;
	}

}