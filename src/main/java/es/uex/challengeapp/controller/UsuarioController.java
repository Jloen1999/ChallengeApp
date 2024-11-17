package es.uex.challengeapp.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.uex.challengeapp.model.Comentario;
import es.uex.challengeapp.model.Notificacion;
import es.uex.challengeapp.model.ParticipantesReto;
import es.uex.challengeapp.model.Reto;
import es.uex.challengeapp.model.Reto.Estado;
import es.uex.challengeapp.model.Usuario;
import es.uex.challengeapp.service.ComentarioService;
import es.uex.challengeapp.service.NotificacionService;
import es.uex.challengeapp.service.ParticipantesRetoService;
import es.uex.challengeapp.service.RetoService;
import es.uex.challengeapp.service.UsuarioService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private RetoService retoService;

	@Autowired
	private NotificacionService notificacionService;

	@Autowired
	private ParticipantesRetoService participantesRetoService;

	@Autowired
	private ComentarioService comentarioService;

	@GetMapping("/registro")
	public String mostrarFormularioRegistro(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "registro";
	}

	@GetMapping("/dashboard")
	public String mostrarPerfilUsuario(Model model, HttpSession session) {
		Usuario userActual = (Usuario) session.getAttribute("userActual");
		if (userActual != null) {
			model.addAttribute("nombreUsuario", userActual.getNombre());
			return "dashboard";
		}
		return "redirect:/login";
	}

	@GetMapping("/estadisticas")
	public String mostrarEstadisticasUsuario() {
		return "estadisticas";
	}

	// TODO
	@GetMapping("/misRetos")
	public String mostrarRetosUsuario(Model model, HttpSession session) {
		Usuario userActual = (Usuario) session.getAttribute("userActual");
		if (userActual != null) {
			List<Reto> retos = participantesRetoService.obtenerRetosDeUsuario(Long.valueOf(userActual.getId()));
			model.addAttribute("retos", retos);
			return "misRetos";
		}
		return "redirect:/login";
	}

	@GetMapping("/retosCreados")
	public String mostrarRetosCreados(Model model, HttpSession session) {
		Usuario userActual = (Usuario) session.getAttribute("userActual");
		if (userActual != null) {
			List<Reto> retos = retoService.obtenerRetosCreadosPorUsuario(Long.valueOf(userActual.getId()));
			model.addAttribute("retos", retos);
			return "retosCreados";
		}
		return "redirect:/login";
	}

	@GetMapping("/amigos")
	public String mostrarAmigos(HttpSession session) {
		Usuario userActual = (Usuario) session.getAttribute("userActual");
		if (userActual != null) {
			return "amigos";
		}
		return "redirect:/login";
	}

	@GetMapping("/notificaciones")
	public String mostrarNotificacionesUsuario(Model model, HttpSession session) {
		Usuario userActual = (Usuario) session.getAttribute("userActual");
		if (userActual != null) {
			List<Notificacion> notificaciones = notificacionService
					.obtenerNotificacionesPorUsuario(Long.valueOf(userActual.getId()));
			model.addAttribute("notificaciones", notificaciones);
			return "notificaciones";
		}
		return "redirect:/login";
	}

	@GetMapping("/crearReto")
	public String mostrarFormularioCrearReto(Model model, HttpSession session) {
		Usuario userActual = (Usuario) session.getAttribute("userActual");
		if (userActual != null) {
			model.addAttribute("reto", new Reto());
			return "crearReto";
		}
		return "redirect:/login";
	}

	@PostMapping("/crearReto")
	public String crearReto(@ModelAttribute Reto reto, Model model, HttpSession session) {
		Usuario userActual = (Usuario) session.getAttribute("userActual");
		if (userActual != null) {
			reto.setCreador(userActual);
			reto.setEstado(Estado.PENDIENTE);
			reto.setNovedad(true);
			reto.setPorcentajeProgreso(0.0f);
			reto.setFechaCreacion(new Date(System.currentTimeMillis()));
			Reto creado = retoService.crearReto(reto);
			if (creado != null) {
				model.addAttribute("mensaje", "Reto añadido correctamente.");
				generarNotificacion(userActual, reto, "CREACION_RETO");
			} else {
				model.addAttribute("error", "Error al crear el reto.");
			}
			model.addAttribute("reto", new Reto());
			return "crearReto";
		}
		return "redirect:/login";
	}

	@GetMapping("/unirse")
	public String unirseAunReto(@RequestParam Integer retoId, Model model, HttpSession session) {
		Usuario userActual = (Usuario) session.getAttribute("userActual");
		if (userActual != null) {
			Reto reto = retoService.obtenerReto(Long.valueOf(retoId));
			if (reto != null) {
				boolean yaUnido = participantesRetoService.unidoAlReto(Long.valueOf(userActual.getId()),
						Long.valueOf(retoId));

				if (yaUnido) {
					model.addAttribute("mensaje", "Ya estás unido a este reto.");
					return "redirect:/usuario/reto/" + retoId;
				}

				ParticipantesReto participantesReto = new ParticipantesReto();
				participantesReto.setUsuario(userActual);
				participantesReto.setReto(reto);

				participantesRetoService.unirseAReto(participantesReto);
				generarNotificacion(userActual, reto, "UNION_RETO");
				model.addAttribute("mensaje", "Te has unido correctamente");
				return "redirect:/usuario/reto/" + retoId;
			}
		}
		return "redirect:/login";
	}

	@GetMapping("/reto/{id}")
	public String mostrarReto(@PathVariable Integer id, Model model) {
		Reto reto = retoService.obtenerReto(Long.valueOf(id));
		if (reto != null) {
			List<Usuario> participantes = participantesRetoService.obteneParticipantesDeReto(Long.valueOf(id));
			List<Comentario> comentarios = comentarioService.obtenerComentariosPorReto(Long.valueOf(id));
			model.addAttribute("reto", reto);
			model.addAttribute("participantes", participantes);
			model.addAttribute("comentarios", comentarios);
		}
		return "reto";
	}

	@PostMapping("/comentar")
	public String comentar(@RequestParam String comentarioTexto, @RequestParam Integer retoId, HttpSession session) {
		Usuario usuarioActual = (Usuario) session.getAttribute("userActual");
		if (usuarioActual != null) {
			Comentario comentario = new Comentario();
			comentario.setTexto(comentarioTexto);
			comentario.setFecha(new Date(System.currentTimeMillis()));
			comentario.setUsuario(usuarioActual);
			Reto reto = retoService.obtenerReto(Long.valueOf(retoId));
			if (reto != null) {
				comentario.setReto(reto);
				comentarioService.hacerComentario(comentario);
				return "redirect:/usuario/reto/" + retoId;
			}
		}
		return "redirect:/login";
	}

	// FUNCIONES PRIVADAS AUXLIARES
	private void generarNotificacion(Usuario userActual, Reto reto, String tipoNotificacion) {
		Notificacion notificacion = new Notificacion();

		String mensaje = "";
		if ("CREACION_RETO".equals(tipoNotificacion)) {
			mensaje = "¡Has creado un nuevo reto: " + reto.getNombre() + "!";
		} else if ("UNION_RETO".equals(tipoNotificacion)) {
			mensaje = "¡Te has unido al reto: " + reto.getNombre() + "!";
		}

		notificacion.setMensaje(mensaje);
		notificacion.setLeido(false);
		notificacion.setFechaEnvio(new Date(System.currentTimeMillis()));

		notificacion.setUsuario(userActual);
		notificacion.setReto(reto);

		notificacionService.crearNotificacion(notificacion);

	}
}
