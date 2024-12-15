package es.uex.challengeapp.controller;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.uex.challengeapp.model.ParticipantesReto;
import es.uex.challengeapp.model.ProgresoReto;
import es.uex.challengeapp.model.Reto;
import es.uex.challengeapp.model.Reto.Tipo;
import es.uex.challengeapp.model.RetoDTO;
import es.uex.challengeapp.model.Usuario;
import es.uex.challengeapp.service.EstadisticaService;
import es.uex.challengeapp.service.ParticipantesRetoService;
import es.uex.challengeapp.service.ProgresoRetoService;
import es.uex.challengeapp.service.PuntoService;
import es.uex.challengeapp.service.RecompensaService;
import es.uex.challengeapp.service.RetoService;
import es.uex.challengeapp.service.UsuarioService;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private RetoService retoService;

	@Autowired
	private ParticipantesRetoService participantesRetoService;

	@Autowired
	private EstadisticaService estadisticaService;

	@Autowired
	private ProgresoRetoService progresoRetoService;

	@Autowired
	private RecompensaService recompensaService;

	@Autowired
	private PuntoService puntoService;

	@GetMapping("/")
	public String home(Model model, HttpSession session) {
		Usuario userActual = (Usuario) session.getAttribute("userActual");
		model.addAttribute("estaLogueado", userActual != null);

		List<Reto> retos = retoService.obtenerRetos(userActual);
		List<Reto> retosNovedosos = retoService.obtenerRetosNovedosos(userActual);
		List<Reto> retosPopulares = participantesRetoService.obtenerRetosMasParticipantes();

		model.addAttribute("retos", retos);
		model.addAttribute("retosNovedosos", retosNovedosos);
		model.addAttribute("retosPopulares", retosPopulares);

		if (userActual != null) {
			List<Reto> retosPrivados = retoService.mostrarRetosPrivadosAmigos(userActual);
			model.addAttribute("retosPrivados", retosPrivados);
		}

		estadisticaService.actualizarTodasLasEstadisticas();

		return "index";
	}

	@GetMapping("/buscarRetos")
	public ResponseEntity<List<RetoDTO>> buscarRetos(@RequestParam String criterioBusqueda) {
		List<Reto> retos = retoService.buscarPorNombre(criterioBusqueda);

		List<RetoDTO> retosDTO = retos.stream()
				.map(reto -> new RetoDTO(reto.getId(), reto.getNombre(), reto.getDescripcion()))
				.collect(Collectors.toList());

		return ResponseEntity.ok(retosDTO);
	}

	@PostMapping("/registro")
	public String registrarUsuario(@ModelAttribute Usuario usuario, Model model) {
		Usuario registrado = usuarioService.registrarUsuario(usuario);
		if (registrado != null) {
			model.addAttribute("mensaje", "Usuario registrado con éxito. Inicia sesión.");
			return "redirect:/login";
		}
		model.addAttribute("error", "Error al registrar usuario.");
		return "registro";
	}

	@GetMapping("/login")
	public String mostrarFormularioLogin(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "login";
	}

	@GetMapping("/logout")
	public String cerrarSesion(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

	@PostMapping("/")
	public String login(@RequestParam String correo, @RequestParam String contrasena, Model model,
			HttpSession session) {
		Usuario usuario = usuarioService.autenticarUsuario(correo, contrasena);
		if (usuario != null) {
			session.setAttribute("userActual", usuario);
			return "redirect:/";
		}
		model.addAttribute("usuario", new Usuario());
		model.addAttribute("error", "Correo o contraseña incorrectos.");
		return "login";
	}

	@GetMapping("/verEstadisticas")
	public String mostrarEstadisticasGenerales(Model model) {
		// Obtener todos los usuarios y retos
		List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
		List<Reto> retos = retoService.obtenerTodosLosRetos();

		// Estadísticas generales
		long numeroUsuarios = usuarios.size();
		long numeroUsuariosConRetosCompletados = usuarios.stream()
				.filter(usuario -> !progresoRetoService.obtenerRetosCompletadosPorUsuario(usuario).isEmpty()).count();
		long numeroRetos = retos.size();
		long numeroRetosCompletados = retos.stream().filter(
				reto -> usuarios.stream().anyMatch(usuario -> progresoRetoService.estaCompletado(usuario, reto)))
				.count();

		// Tiempo promedio para completar retos
		double tiempoPromedioCompletado = usuarios.stream().mapToDouble(usuario -> {
			Duration tiempo = progresoRetoService.obtenerTiempoTotalDeCompletado(usuario);
			long totalRetosCompletados = progresoRetoService.obtenerRetosCompletadosPorUsuario(usuario).size();
			double tiempoEnHoras = (double) tiempo.toMinutes() / 60;
			if (totalRetosCompletados > 0) {
				return Math.round((tiempoEnHoras / totalRetosCompletados) * 100.0) / 100.0;
			}
			return 0;
		}).average().orElse(0);

		// Ranking: Usuarios con más puntos
		List<Usuario> usuariosPorPuntos = usuarios.stream()
				.sorted((u1, u2) -> Integer.compare(puntoService.totalPuntosUsuario(u2),
						puntoService.totalPuntosUsuario(u1)))
				.limit(10) // Limitar a top 10
				.toList();

		// Ranking: Usuarios con más medallas
		List<Usuario> usuariosPorMedallas = usuarios.stream().sorted((u1, u2) -> {
			long medallasU1 = calcularPonderacionMedallas(u1);
			long medallasU2 = calcularPonderacionMedallas(u2);
			return Long.compare(medallasU2, medallasU1);
		}).limit(10).toList();

		// Crear un desglose detallado de las medallas
		List<Map<String, Object>> rankingMedallasDetalle = usuariosPorMedallas.stream().map(usuario -> {
			Map<String, Object> detalle = new HashMap<>();
			detalle.put("usuario", usuario);
			detalle.put("diamantes", recompensaService.obtenerRecompensasDiamanteUsuario(usuario).size());
			detalle.put("oros", recompensaService.obtenerRecompensasOroUsuario(usuario).size());
			detalle.put("platas", recompensaService.obtenerRecompensasPlataUsuario(usuario).size());
			detalle.put("bronces", recompensaService.obtenerRecompensasBronceUsuario(usuario).size());
			detalle.put("total", calcularPonderacionMedallas(usuario));
			return detalle;
		}).toList();

		// Ranking: Usuarios con más retos completados
		List<Usuario> usuariosPorRetosCompletados = usuarios.stream()
				.sorted((u1, u2) -> Integer.compare(progresoRetoService.obtenerRetosCompletadosPorUsuario(u2).size(),
						progresoRetoService.obtenerRetosCompletadosPorUsuario(u1).size()))
				.limit(10).toList();

		// Ranking: Promedio en retos simples y complejos
		List<Usuario> usuariosPorPromedioSimples = usuarios.stream()
				.sorted((u1, u2) -> Float.compare(calcularPromedioTiempoPorTipo(u2, Tipo.SIMPLE),
						calcularPromedioTiempoPorTipo(u1, Tipo.SIMPLE)))
				.limit(10).toList();

		List<Usuario> usuariosPorPromedioComplejos = usuarios.stream()
				.sorted((u1, u2) -> Float.compare(calcularPromedioTiempoPorTipo(u2, Tipo.COMPLEJO),
						calcularPromedioTiempoPorTipo(u1, Tipo.COMPLEJO)))
				.limit(10).toList();

		// Popularidad de retos
		List<Reto> retosMasPopulares = participantesRetoService.obtenerRetosMasParticipantes();
		List<Reto> retosMenosPopulares = retos.stream()
				.sorted(Comparator.comparingInt(
						reto -> participantesRetoService.obtenerParticipantesDeReto(reto.getId().longValue()).size()))
				.limit(10).toList();

		// Añadir datos al modelo
		model.addAttribute("numeroUsuarios", numeroUsuarios);
		model.addAttribute("numeroUsuariosConRetosCompletados", numeroUsuariosConRetosCompletados);
		model.addAttribute("numeroRetos", numeroRetos);
		model.addAttribute("numeroRetosCompletados", numeroRetosCompletados);
		model.addAttribute("tiempoPromedioCompletado", tiempoPromedioCompletado);

		model.addAttribute("usuariosPorPuntos", usuariosPorPuntos);
		model.addAttribute("rankingMedallasDetalle", rankingMedallasDetalle);
		model.addAttribute("usuariosPorRetosCompletados", usuariosPorRetosCompletados);
		model.addAttribute("usuariosPorPromedioSimples", usuariosPorPromedioSimples);
		model.addAttribute("usuariosPorPromedioComplejos", usuariosPorPromedioComplejos);

		model.addAttribute("retosMasPopulares", retosMasPopulares);
		model.addAttribute("retosMenosPopulares", retosMenosPopulares);

		// Añadir también los servicios que se van a usar
		model.addAttribute("controller", this);
		model.addAttribute("puntoService", puntoService);
		model.addAttribute("progresoRetoService", progresoRetoService);
		model.addAttribute("participantesRetoService", participantesRetoService);

		return "estadisticas";
	}

	// MÉTODOS PRIVADOS AUXILIARES
	private long calcularPonderacionMedallas(Usuario usuario) {
		return recompensaService.obtenerRecompensasDiamanteUsuario(usuario).size() * 4
				+ recompensaService.obtenerRecompensasOroUsuario(usuario).size() * 3
				+ recompensaService.obtenerRecompensasPlataUsuario(usuario).size() * 2
				+ recompensaService.obtenerRecompensasBronceUsuario(usuario).size();
	}

	public float calcularPromedioTiempoPorTipo(Usuario usuario, Tipo tipo) {
		List<Reto> retos = progresoRetoService.obtenerRetosCompletadosPorUsuario(usuario);
		List<Reto> retosFiltrados = retos.stream().filter(reto -> reto.getTipo() == tipo).toList();

		if (retosFiltrados.isEmpty()) {
			return 0;
		}

		double totalTiempo = retosFiltrados.stream().mapToDouble(reto -> {
			ParticipantesReto participacion = participantesRetoService.obtenerParticipacionReto(usuario, reto);
			Instant inicio = null;

			if (participacion != null && participacion.getFechaUnion() != null) {
				inicio = participacion.getFechaUnion().toInstant();
			} else {
				inicio = reto.getFechaCreacion().toInstant();
			}

			ProgresoReto progreso = progresoRetoService.buscarProgresoReto(usuario, reto);
			if (progreso.getFechaActualizacion() != null && inicio != null) {
				Duration duracion = Duration.between(inicio, progreso.getFechaActualizacion().toInstant());
				return duracion.toMinutes();
			}

			return 0;
		}).sum();

		float promedioEnHoras = (float) (totalTiempo / retosFiltrados.size()) / 60;
		return Math.round(promedioEnHoras * 100.0f) / 100.0f;
	}

}