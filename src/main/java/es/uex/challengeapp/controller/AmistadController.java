package es.uex.challengeapp.controller;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.uex.challengeapp.model.Amistad;
import es.uex.challengeapp.model.Estadistica;
import es.uex.challengeapp.model.Notificacion;
import es.uex.challengeapp.model.Notificacion.TipoNotificacion;
import es.uex.challengeapp.model.Reto;
import es.uex.challengeapp.model.Usuario;
import es.uex.challengeapp.model.UsuarioDTO;
import es.uex.challengeapp.service.AmistadService;
import es.uex.challengeapp.service.EstadisticaService;
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
	private EstadisticaService estadisticaService;

	@Autowired
	private ParticipantesRetoService participantesRetoService;

	@PostMapping("/aceptarAmistad/{email}")
	public String aceptarAmistad(@PathVariable String email, @RequestParam Long notificacionId, HttpSession session,
			RedirectAttributes redirectAttributes) {
		Usuario user = (Usuario) session.getAttribute("userActual");
		if (user != null) {
			// Buscar al usuario que envió la solicitud de amistad por su email
			Optional<Usuario> amigo = usuarioService.buscarUsuarioPorEmail(email);
			if (amigo.isPresent()) {
				// Crear amistad en ambos sentidos
				Amistad amistad1 = new Amistad();
				amistad1.setUsuario1(user);
				amistad1.setUsuario2(amigo.get());
				amistadService.anadirAmigo(amistad1);

				Amistad amistad2 = new Amistad();
				amistad2.setUsuario1(amigo.get());
				amistad2.setUsuario2(user);
				amistadService.anadirAmigo(amistad2);

				generarNotificacion(user, amigo.get(), "ADICION_AMISTAD");
				notificacionService.negociarAmistad(user, amigo.get(), "ACEPTAR_SOLICITUD");

				Notificacion notificacion = notificacionService.buscarPorId(notificacionId);
				if (notificacion != null) {
					notificacion.setLeido(true);
					notificacionService.crearNotificacion(notificacion);
				}

				redirectAttributes.addFlashAttribute("mensaje",
						"Has aceptado la solicitud de amistad de " + amigo.get().getNombre());
				redirectAttributes.addFlashAttribute("tipoMensaje", "success");
			} else {
				redirectAttributes.addFlashAttribute("mensaje", "El usuario no existe o no envió la solicitud");
				redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
			}
			return "redirect:/usuario/notificaciones";
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
			// List<Usuario> listaAmigos = amistadService.obtenerAmigos(userActual.getId());
			// model.addAttribute("amigos", listaAmigos);
			return "redirect:/usuario/amigos";
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
			Estadistica estadistica = estadisticaService.obtenerEstadisticaPorUsuario(otroUsuario);
			String tiempoConvertido = convertirTiempoPromedio(estadistica.getTiempoPromedio());

			model.addAttribute("usuario", otroUsuario);
			model.addAttribute("retosCreados", retosCreados);
			model.addAttribute("retosUnidos", retosUnidos);
			model.addAttribute("amigos", amigos);
			model.addAttribute("estadisticas", estadistica);
			model.addAttribute("tiempoConvertido", tiempoConvertido);

			return "dashboardAmigo";
		}
		return "redirect:/login";
	}

	@GetMapping("/buscar")
	public ResponseEntity<List<UsuarioDTO>> buscarUsuarios(@RequestParam String criterioBusqueda) {
		List<Usuario> usuarios = usuarioService.buscarPorNombreOCorreo(criterioBusqueda);

		List<UsuarioDTO> usuariosDTO = usuarios.stream()
				.map(usuario -> new UsuarioDTO(usuario.getId(), usuario.getNombre(), usuario.getCorreo()))
				.collect(Collectors.toList());

		return ResponseEntity.ok(usuariosDTO);
	}

	@PostMapping("/enviarSolicitud/{idAmigo}")
	public String enviarSolicitudAmistad(@PathVariable Integer idAmigo, HttpSession session, Model model) {
		Usuario user = (Usuario) session.getAttribute("userActual");
		Usuario amigoUsuario = usuarioService.obtenerUsuarioPorId(idAmigo);

		notificacionService.negociarAmistad(user, amigoUsuario, "ENVIAR_SOLCITUD");

		return "redirect:/usuario/amigos";
	}

	// FUNCIONES PRIVADAS AUXLIARES
	private void generarNotificacion(Usuario userActual, Usuario userAmigo, String tipoNotificacion) {
		Notificacion notificacion = new Notificacion();

		String mensaje = "";
		if ("ADICION_AMISTAD".equals(tipoNotificacion)) {
			mensaje = "¡Has añadido a un nuevo amigo: " + userAmigo.getNombre() + "!";
			notificacion.setTipoNotificacion(TipoNotificacion.ACEPTACION_AMISTAD);
		} else if ("BORRAR_AMISTAD".equals(tipoNotificacion)) {
			mensaje = "¡Has eliminado a " + userAmigo.getNombre() + " de tu lista de amigos!";
			notificacion.setTipoNotificacion(TipoNotificacion.ELIMINACION_AMISTAD);
		}

		notificacion.setMensaje(mensaje);
		notificacion.setLeido(false);
		notificacion.setFechaEnvio(new Date(System.currentTimeMillis()));

		notificacion.setUsuario(userActual);

		notificacionService.crearNotificacion(notificacion);
	}

	private String convertirTiempoPromedio(float tiempoPromedio) {
		if (tiempoPromedio == 0) {
			return "0 minutos"; // Si es 0, se muestra como "0 minutos"
		}

		// Convertimos el tiempo en horas a días, horas y minutos
		int dias = (int) (tiempoPromedio / 24);
		int horas = (int) (tiempoPromedio % 24);
		int minutos = (int) ((tiempoPromedio - (int) tiempoPromedio) * 60);

		StringBuilder tiempoFormateado = new StringBuilder();

		// Solo agregamos días si no son 0
		if (dias > 0) {
			tiempoFormateado.append(dias).append(" días");
		}

		// Solo agregamos horas si no son 0
		if (horas > 0) {
			if (tiempoFormateado.length() > 0) {
				tiempoFormateado.append(", ");
			}
			tiempoFormateado.append(horas).append(" horas");
		}

		// Solo agregamos minutos si no son 0
		if (minutos > 0) {
			if (tiempoFormateado.length() > 0) {
				tiempoFormateado.append(", ");
			}
			tiempoFormateado.append(minutos).append(" minutos");
		}

		// Si todo es 0, mostramos "0 minutos"
		if (tiempoFormateado.length() == 0) {
			tiempoFormateado.append("0 minutos");
		}

		return tiempoFormateado.toString();
	}

}
