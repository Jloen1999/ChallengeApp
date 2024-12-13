package es.uex.challengeapp.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.uex.challengeapp.model.Notificacion;
import es.uex.challengeapp.model.Notificacion.TipoNotificacion;
import es.uex.challengeapp.model.ProgresoReto;
import es.uex.challengeapp.model.Reto;
import es.uex.challengeapp.model.Reto.Tipo;
import es.uex.challengeapp.model.RetoComplejo;
import es.uex.challengeapp.model.RetoSimple;
import es.uex.challengeapp.model.Subtarea;
import es.uex.challengeapp.model.Usuario;
import es.uex.challengeapp.service.EstadisticaService;
import es.uex.challengeapp.service.NotificacionService;
import es.uex.challengeapp.service.ProgresoRetoService;
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
				int completados = (int)Math.round((progresoReto.getProgresoActual() * retoComplejo.getSubtareas().size()) / 100.0);
				
				int cont=0;
				for(Subtarea subtarea:retoComplejo.getSubtareas()) {
					if(cont<completados) {
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
			
			for(Subtarea subtarea:retoComplejo.getSubtareas()) {
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
		
		comprobarCompletado(usuario,reto);

		return "redirect:/usuario/reto/" + retoId;
	}

	private void comprobarCompletado(Usuario usuario, Reto reto) {
		if(progresoRetoService.estaCompletado(usuario, reto)) {
			Notificacion notificacion=new Notificacion();
			
			notificacion.setFechaEnvio(new Date(System.currentTimeMillis()));
			notificacion.setReto(reto);
			notificacion.setUsuario(usuario);
			notificacion.setTipoNotificacion(TipoNotificacion.RETO_COMPLETADO);
			
			String mensaje="¡Enhorabuena, has completado el reto: '"+reto.getNombre()+"'!";
			
			notificacion.setMensaje(mensaje);
			
			notificacionService.crearNotificacion(notificacion);
		}
	}


}