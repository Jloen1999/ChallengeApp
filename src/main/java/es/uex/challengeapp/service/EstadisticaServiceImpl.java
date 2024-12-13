package es.uex.challengeapp.service;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uex.challengeapp.model.Estadistica;
import es.uex.challengeapp.model.Reto;
import es.uex.challengeapp.model.Usuario;
import es.uex.challengeapp.repository.EstadisticaRepository;

@Service
public class EstadisticaServiceImpl implements EstadisticaService {

	@Autowired
	private EstadisticaRepository estadisticaRepository;

	@Autowired
	private ProgresoRetoService progresoRetoService;

	@Autowired
	private ParticipantesRetoService participantesRetoService;

	@Override
	public Estadistica actualizarEstadistica(Usuario usuario) {
		Estadistica estadistica = obtenerEstadisticaPorUsuario(usuario);

		List<Reto> todosLosRetos = participantesRetoService.obtenerRetosDeUsuario(Long.valueOf(usuario.getId()));
		List<Reto> retosCompletados = progresoRetoService.obtenerRetosCompletadosPorUsuario(usuario);
		List<Reto> retosFallidos = progresoRetoService.obtenerRetosFallidos(usuario);
		Duration totalTiempoCompletado = progresoRetoService.obtenerTiempoTotalDeCompletado(usuario);

		int totalRetos = todosLosRetos.size();
		int totalRetosCompletados = retosCompletados.size();
		int totalRetosFallidos = retosFallidos.size();

		float progresoPromedio = (((float) totalRetosCompletados / totalRetos) * 100);
		progresoPromedio = Math.round(progresoPromedio * 100.0f) / 100.0f;

		long segundosTotales = totalTiempoCompletado.getSeconds();
		float horasTotales = (float) segundosTotales / 3600;
		float tiempoPromedio = 0.0f;
		if (totalRetosCompletados != 0) {
			tiempoPromedio = horasTotales / totalRetosCompletados;
		}

		estadistica.setTotalRetos(totalRetos);
		estadistica.setRetosCompletados(totalRetosCompletados);
		estadistica.setRetosFallidos(totalRetosFallidos);
		estadistica.setProgresoPromedio(progresoPromedio);
		estadistica.setTiempoPromedio(tiempoPromedio);
		estadistica.setUsuario(usuario);

		return estadisticaRepository.save(estadistica);
	}

	@Override
	public Estadistica obtenerEstadisticaPorUsuario(Usuario usuario) {
		return estadisticaRepository.findByUsuario(usuario).orElse(new Estadistica());
	}

	@Override
	public void actualizarTodasLasEstadisticas() {
		List<Usuario> todosLosUsuarios = participantesRetoService.obtenerTodosLosUsuarios();

		for (Usuario usuario : todosLosUsuarios) {
			actualizarEstadistica(usuario);
		}
	}

}
