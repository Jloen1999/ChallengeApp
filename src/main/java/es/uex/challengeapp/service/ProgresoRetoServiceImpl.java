package es.uex.challengeapp.service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
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
import es.uex.challengeapp.repository.ProgresoRetoRepository;

@Service
public class ProgresoRetoServiceImpl implements ProgresoRetoService {

	@Autowired
	private ProgresoRetoRepository progresoRetoRepository;

	@Autowired
	private ParticipantesRetoService participantesRetoService;

	@Override
	public ProgresoReto actualizarProgreso(ProgresoReto progresoReto) {
		return progresoRetoRepository.save(progresoReto);
	}

	@Override
	public ProgresoReto buscarProgresoReto(Usuario usuario, Reto reto) {
		return progresoRetoRepository.findByUsuarioAndReto(usuario, reto).orElse(new ProgresoReto());
	}

	@Override
	public List<Reto> obtenerRetosCompletadosPorUsuario(Usuario usuario) {
		List<Reto> todosLosRetos = participantesRetoService.obtenerRetosDeUsuario(Long.valueOf(usuario.getId()));
		List<Reto> retosCompletados = new ArrayList<Reto>();

		for (Reto reto : todosLosRetos) {
			if (estaCompletado(usuario, reto)) {
				retosCompletados.add(reto);
			}

		}

		return retosCompletados;
	}

	@Override
	public boolean estaCompletado(Usuario usuario, Reto reto) {
		ProgresoReto progresoReto = buscarProgresoReto(usuario, reto);
		if (progresoReto.getProgresoActual() == 100) {
			return true;
		}
		return false;
	}

	@Override
	public boolean estaEnProgreso(Usuario usuario, Reto reto) {
		ProgresoReto progresoReto = buscarProgresoReto(usuario, reto);
		if (progresoReto.getProgresoActual() > 0 && progresoReto.getProgresoActual() < 100) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean estaFallido(Usuario usuario, Reto reto) {
	    LocalDate fechaReto = reto.getFechaFinalizacion().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	    LocalDate fechaActual = LocalDate.now();
	    
	    return !estaCompletado(usuario, reto) && fechaActual.isAfter(fechaReto);
	}


	@Override
	public List<Reto> obtenerRetosFallidos(Usuario usuario) {
		List<Reto> todosLosRetos = participantesRetoService.obtenerRetosDeUsuario(Long.valueOf(usuario.getId()));
		List<Reto> retosFallidos = new ArrayList<Reto>();

		for (Reto reto : todosLosRetos) {
			if(estaFallido(usuario, reto)) {
				retosFallidos.add(reto);
			}
		}

		return retosFallidos;
	}

	@Override
	public Duration obtenerTiempoTotalDeCompletado(Usuario usuario) {
		List<Reto> todosLosRetos = participantesRetoService.obtenerRetosDeUsuario(Long.valueOf(usuario.getId()));
		Duration totalDuration = Duration.ZERO;
		for (Reto reto : todosLosRetos) {
			if (estaCompletado(usuario, reto)) {
				ProgresoReto progresoReto = buscarProgresoReto(usuario, reto);
				ParticipantesReto participantesReto = participantesRetoService.obtenerParticipacionReto(usuario, reto);

				Date fechaInicio;

				if (participantesReto.getFechaUnion() != null) {
					fechaInicio = participantesReto.getFechaUnion();
				} else {
					fechaInicio = reto.getFechaCreacion();
				}

				Instant inicio = fechaInicio.toInstant();
				Instant fin = progresoReto.getFechaActualizacion().toInstant();

				Duration duracion = Duration.between(inicio, fin);

				totalDuration = totalDuration.plus(duracion);
			}
		}
		return totalDuration;
	}

	@Override
	public ProgresoReto reiniciarReto(Usuario usuario, Reto reto) {
		ProgresoReto progresoReto=buscarProgresoReto(usuario, reto);
		progresoReto.setProgresoActual(0.0f);
		return actualizarProgreso(progresoReto);
	}

	@Override
	public void borrarProgresoReto(Usuario usuario, Reto reto) {
		ProgresoReto progresoReto=buscarProgresoReto(usuario, reto);
		progresoRetoRepository.delete(progresoReto);
	}

}
