package es.uex.challengeapp.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.uex.challengeapp.model.Notificacion;
import es.uex.challengeapp.model.Notificacion.TipoNotificacion;
import es.uex.challengeapp.model.ProgresoReto;
import es.uex.challengeapp.model.Punto;
import es.uex.challengeapp.model.Recompensa;
import es.uex.challengeapp.model.Recompensa.TipoMedalla;
import es.uex.challengeapp.model.Reto;
import es.uex.challengeapp.model.Reto.Tipo;
import es.uex.challengeapp.model.RetoComplejo;
import es.uex.challengeapp.model.RetoSimple;
import es.uex.challengeapp.model.Subtarea;
import es.uex.challengeapp.model.Usuario;
import es.uex.challengeapp.service.EstadisticaService;
import es.uex.challengeapp.service.NotificacionService;
import es.uex.challengeapp.service.ProgresoRetoService;
import es.uex.challengeapp.service.PuntoService;
import es.uex.challengeapp.service.RecompensaService;
import es.uex.challengeapp.service.RetoService;
import es.uex.challengeapp.service.SubtareaService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario/reto")
public class RetoController {
	@Autowired
	private RetoService retoService;

	@Autowired
	private ProgresoRetoService progresoRetoService;

	@Autowired
	private SubtareaService subtareaService;

	@Autowired
	private EstadisticaService estadisticaService;

	@Autowired
	private NotificacionService notificacionService;

	@Autowired
	private RecompensaService recompensaService;

	@Autowired
	private PuntoService puntoService;

	@PostMapping("/actualizarProgreso")
	public String actualizarProgreso(@RequestParam("retoId") Integer retoId,
			@RequestParam(value = "subtareaId", required = false) Integer subtareaId,
			@RequestParam(value = "progresoArray", required = false) Integer progresoActual, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("userActual");
		if (usuario == null) {
			return "redirect:/login";
		}

		float progreso = 0.0f;

		Reto reto = retoService.obtenerReto(Long.valueOf(retoId));
		ProgresoReto progresoReto = progresoRetoService.buscarProgresoReto(usuario, reto);
		if (reto.getTipo() == Tipo.SIMPLE) {
			RetoSimple retoSimple = (RetoSimple) reto;
			retoSimple.setProgresoArray(progresoActual);
			progreso = ((float) retoSimple.getProgresoArray() / retoSimple.getCantidad()) * 100;
		} else if (reto.getTipo() == Tipo.COMPLEJO) {
			RetoComplejo retoComplejo = (RetoComplejo) reto;

			if (progresoReto.getProgresoActual() != null) {
				int completados = (int) Math
						.round((progresoReto.getProgresoActual() * retoComplejo.getSubtareas().size()) / 100.0);

				int cont = 0;
				for (Subtarea subtarea : retoComplejo.getSubtareas()) {
					if (cont < completados) {
						subtarea.setEstado(Subtarea.Estado.COMPLETADA);
					}
					cont++;
				}
			}

			if (subtareaId != null) {
				Subtarea subtarea = subtareaService.buscarPorId(subtareaId);
				if (subtarea.getEstado() != Subtarea.Estado.COMPLETADA) {
					subtarea.setEstado(Subtarea.Estado.COMPLETADA);
				}
			}

			long totalSubtareas = retoComplejo.getSubtareas().size();
			long completadas = retoComplejo.getSubtareas().stream()
					.filter(s -> s.getEstado() == Subtarea.Estado.COMPLETADA).count();

			for (Subtarea subtarea : retoComplejo.getSubtareas()) {
				if (subtarea.getEstado() != Subtarea.Estado.PENDIENTE) {
					subtarea.setEstado(Subtarea.Estado.PENDIENTE);
				}
			}

			progreso = ((float) completadas / totalSubtareas) * 100;
		}

		progresoReto.setReto(reto);
		progresoReto.setUsuario(usuario);
		progresoReto.setFechaActualizacion(new Date(System.currentTimeMillis()));
		progresoReto.setProgresoActual(progreso);

		progresoRetoService.actualizarProgreso(progresoReto);
		estadisticaService.actualizarEstadistica(usuario);

		comprobarCompletado(usuario, reto);

		return "redirect:/usuario/reto/" + retoId;
	}

	// FUNCIONES PRIVADAS AUXILIARES
	private void comprobarCompletado(Usuario usuario, Reto reto) {
	    if (progresoRetoService.estaCompletado(usuario, reto)) {
	        crearNotificacionRetoCompletado(usuario, reto);
	        asignarPuntosPorReto(usuario, reto);
	        gestionarRecompensas(usuario, progresoRetoService.obtenerRetosCompletadosPorUsuario(usuario));
	    }
	}

	private void crearNotificacionRetoCompletado(Usuario usuario, Reto reto) {
	    Notificacion notificacion = new Notificacion();
	    notificacion.setFechaEnvio(new Date());
	    notificacion.setReto(reto);
	    notificacion.setUsuario(usuario);
	    notificacion.setTipoNotificacion(TipoNotificacion.RETO_COMPLETADO);
	    notificacion.setMensaje("¡Enhorabuena, has completado el reto: '" + reto.getNombre() + "'!");
	    notificacionService.crearNotificacion(notificacion);
	}

	private void asignarPuntosPorReto(Usuario usuario, Reto reto) {
	    Punto punto = new Punto();
	    punto.setDescripcion("Completar reto '" + reto.getNombre() + "'");
	    float tiempoCompletado = retoService.tiempoEnCompletado(usuario, reto);
	    punto.setCantidad(reto.getTipo() == Tipo.SIMPLE
	        ? (tiempoCompletado < 24 ? 100 : 10)
	        : (tiempoCompletado < 720 ? 500 : 50));
	    punto.setUsuario(usuario);
	    puntoService.registrarPuntos(punto);
	}

	private void gestionarRecompensas(Usuario usuario, List<Reto> retosCompletados) {
	    int totalSimples = (int) retosCompletados.stream().filter(r -> r.getTipo() == Tipo.SIMPLE).count();
	    int totalComplejos = retosCompletados.size() - totalSimples;

	    gestionarMedallas(usuario, TipoMedalla.BRONCE, totalSimples / 5 + totalComplejos / 2);
	    gestionarMedallas(usuario, TipoMedalla.PLATA, recompensaService.obtenerRecompensasBronceUsuario(usuario).size() / 5);
	    gestionarMedallas(usuario, TipoMedalla.ORO, recompensaService.obtenerRecompensasPlataUsuario(usuario).size() / 5);
	    gestionarMedallasDiamante(usuario);
	}

	private void gestionarMedallas(Usuario usuario, TipoMedalla tipo, int totalRecompensas) {
	    List<Recompensa> medallas = recompensaService.obtenerRecompensasPorTipoYUsuario(tipo, usuario);
	    for (int i = medallas.size(); i < totalRecompensas; i++) {
	        guardarNuevaRecompensa(usuario, tipo);
	    }
	}

	private void gestionarMedallasDiamante(Usuario usuario) {
	    int totalDiamantes = puntoService.totalPuntosUsuario(usuario) / 2000;
	    List<Recompensa> medallasDiamante = recompensaService.obtenerRecompensasDiamanteUsuario(usuario);
	    for (int i = medallasDiamante.size(); i < totalDiamantes; i++) {
	        guardarNuevaRecompensa(usuario, TipoMedalla.DIAMANTE);
	    }
	}

	private void guardarNuevaRecompensa(Usuario usuario, TipoMedalla tipo) {
	    Recompensa recompensa = new Recompensa();
	    recompensa.setTipo(tipo);
	    recompensa.setUsuario(usuario);
	    recompensaService.guardarRecompensa(recompensa);
	}


}