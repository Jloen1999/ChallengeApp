package es.uex.challengeapp.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.uex.challengeapp.model.ProgresoReto;
import es.uex.challengeapp.model.Reto;
import es.uex.challengeapp.model.Reto.Tipo;
import es.uex.challengeapp.model.RetoComplejo;
import es.uex.challengeapp.model.RetoSimple;
import es.uex.challengeapp.model.Subtarea;
import es.uex.challengeapp.model.Usuario;
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

	@GetMapping
	public String index(Model model) {
		List<Reto> retosNovedosos = retoService.getNovedososRetos();
		model.addAttribute("retosNovedosos", retosNovedosos);
		return "index";
	}

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
		if (reto.getTipo() == Tipo.SIMPLE) {
			RetoSimple retoSimple = (RetoSimple) reto;
			retoSimple.setProgresoArray(progresoActual);
			progreso = ((float) retoSimple.getProgresoArray() / retoSimple.getCantidad()) * 100;
		} else if (reto.getTipo() == Tipo.COMPLEJO) {
			RetoComplejo retoComplejo = (RetoComplejo) reto;
			if (subtareaId != null) {
				Subtarea subtarea = subtareaService.buscarPorId(subtareaId);
				if (subtarea.getEstado() != Subtarea.Estado.COMPLETADA) {
					subtarea.setEstado(Subtarea.Estado.COMPLETADA);
				}
			}

			long totalSubtareas = retoComplejo.getSubtareas().size();
			long completadas = retoComplejo.getSubtareas().stream()
					.filter(s -> s.getEstado() == Subtarea.Estado.COMPLETADA).count();

			progreso = ((float) completadas / totalSubtareas) * 100;
		}

		ProgresoReto progresoReto = progresoRetoService.buscarProgresoReto(usuario, reto);
		progresoReto.setReto(reto);
		progresoReto.setUsuario(usuario);
		progresoReto.setFechaActualizacion(new Date(System.currentTimeMillis()));
		progresoReto.setProgresoActual(progreso);

		progresoRetoService.actualizarProgreso(progresoReto);

		return "redirect:/usuario/reto/" + retoId;
	}
}