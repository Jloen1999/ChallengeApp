package es.uex.challengeapp.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.uex.challengeapp.model.Amistad;
import es.uex.challengeapp.model.Notificacion;
import es.uex.challengeapp.model.Reto;
import es.uex.challengeapp.model.Usuario;
import es.uex.challengeapp.service.AmistadService;
import es.uex.challengeapp.service.NotificacionService;
import es.uex.challengeapp.service.ParticipantesRetoService;
import es.uex.challengeapp.service.RetoService;
import es.uex.challengeapp.service.UsuarioService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario/amigos")
public class AmistadController {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private AmistadService amistadService;

	@Autowired
	private NotificacionService notificacionService;

	@Autowired
	private RetoService retoService;

	@Autowired
	private ParticipantesRetoService participantesRetoService;

	@PostMapping("/anadirAmigo")
	public String anadirAmigo(@RequestParam String nombreAmigo, HttpSession session,
			RedirectAttributes redirectAttributes) {
		Usuario user = (Usuario) session.getAttribute("userActual");
		if (user != null) {
			Usuario amigo = usuarioService.buscarUsusarioPorNombre(nombreAmigo);
			if (amigo != null) {
				Amistad amistad = new Amistad();
				amistad.setUsuario1(user);
				amistad.setUsuario2(amigo);
				amistadService.anadirAmigo(amistad);
				generarNotificacion(user, amigo, "ADICION_AMISTAD");
				redirectAttributes.addFlashAttribute("mensaje",
						"El usuario " + nombreAmigo + " ha sido añadido correctamente a la lista de amigos");
				redirectAttributes.addFlashAttribute("tipoMensaje", "success");
			} else {
				redirectAttributes.addFlashAttribute("mensaje", "Este usuario no existe");
				redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
			}
			return "redirect:/usuario/amigos";
		}
		return "redirect:/login";
	}

	@GetMapping("/borrarAmigo/{id}")
	public String borrarAmigo(@PathVariable Integer id, HttpSession session, Model model) {
		Usuario userActual = (Usuario) session.getAttribute("userActual");
		if (userActual != null) {
			amistadService.eliminarAmistad(userActual.getId(), id);
			Usuario amigo = usuarioService.obtenerUsuarioPorId(id);
			generarNotificacion(userActual, amigo, "BORRAR_AMISTAD");
			List<Usuario> listaAmigos = amistadService.obtenerAmigos(userActual.getId());
			model.addAttribute("amigos", listaAmigos);
			return "amigos";
		}
		return "redirect:/login";
	}

	@GetMapping("/dashboardAmigo/{id}")
	public String verPerfilAmigo(@PathVariable Integer id, Model model, HttpSession session) {
		Usuario userActual = (Usuario) session.getAttribute("userActual");

		if (userActual != null) {
			Usuario otroUsuario = usuarioService.obtenerUsuarioPorId(id);
			List<Reto> retosCreados = retoService.obtenerRetosCreadosPorUsuario(Long.valueOf(id));
			List<Reto> retosUnidos = participantesRetoService.obtenerRetosDeUsuario(Long.valueOf(id));
			List<Usuario> amigos = amistadService.obtenerAmigos(id);
			// Estadisticas estadisticas = estadisticasService.obtenerEstadisticasPorId(id);

			model.addAttribute("usuario", otroUsuario);
			model.addAttribute("retosCreados", retosCreados);
			model.addAttribute("retosUnidos", retosUnidos);
			model.addAttribute("amigos", amigos);
			// model.addAttribute("estadisticas", estadisticas);

			return "dashboardAmigo";
		}
		return "redirect:/login";
	}

	// FUNCIONES PRIVADAS AUXLIARES
	private void generarNotificacion(Usuario userActual, Usuario userAmigo, String tipoNotificacion) {
		Notificacion notificacion = new Notificacion();

		String mensaje = "";
		if ("ADICION_AMISTAD".equals(tipoNotificacion)) {
			mensaje = "¡Has añadido a un nuevo amigo: " + userAmigo.getNombre() + "!";
		} else if ("BORRAR_AMISTAD".equals(tipoNotificacion)) {
			mensaje = "¡Has eliminado a " + userAmigo.getNombre() + " de tu lista de amigos!";
		}

		notificacion.setMensaje(mensaje);
		notificacion.setLeido(false);
		notificacion.setFechaEnvio(new Date(System.currentTimeMillis()));

		notificacion.setUsuario(userActual);

		notificacionService.crearNotificacion(notificacion);

	}

}
