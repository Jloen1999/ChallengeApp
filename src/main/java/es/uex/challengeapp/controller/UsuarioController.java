package es.uex.challengeapp.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.uex.challengeapp.model.Reto;
import es.uex.challengeapp.model.Reto.Estado;
import es.uex.challengeapp.model.Usuario;
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
		return "redirect:/usuario/login";
	}

	@GetMapping("/estadisticas")
	public String mostrarEstadisticasUsuario() {
		return "estadisticas";
	}

	@GetMapping("/misRetos")
	public String mostrarRetosUsuario(Model model, HttpSession session) {
		Usuario userActual = (Usuario) session.getAttribute("userActual");
		if (userActual != null) {
			List<Reto> retos = retoService.obtenerRetosPorUsuario(Long.valueOf(userActual.getId()));
			model.addAttribute("retos", retos);
			return "misRetos";
		}
		return "redirect:/usuario/login";
	}

	@GetMapping("/reto")
	public String mostrarReto() {
		return "reto";
	}

	@GetMapping("/amigos")
	public String mostrarAmigos() {
		return "amigos";
	}

	@GetMapping("/home")
	public String mostrarInicio() {
		return "home";
	}

	@GetMapping("/notificaciones")
	public String mostrarNotificacionesUsuario() {
		return "notificaciones";
	}

	@PostMapping("/registro")
	public String registrarUsuario(@ModelAttribute Usuario usuario, Model model) {
		Usuario registrado = usuarioService.registrarUsuario(usuario);
		if (registrado != null) {
			model.addAttribute("mensaje", "Usuario registrado con éxito. Inicia sesión.");
			return "redirect:/usuario/login";
		}
		model.addAttribute("error", "Error al registrar usuario.");
		return "registro";
	}

	@GetMapping("/login")
	public String mostrarFormularioLogin(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "login";
	}

	@GetMapping("/crearReto")
	public String mostrarFormularioCrearReto(Model model) {
		model.addAttribute("reto", new Reto());
		return "crearReto";
	}

	@PostMapping("/home")
	public String login(@RequestParam String correo, @RequestParam String contrasena, Model model,
			HttpSession session) {
		Usuario usuario = usuarioService.autenticarUsuario(correo, contrasena);
		if (usuario != null) {
			session.setAttribute("userActual", usuario);
			model.addAttribute("usuario", usuario);
			return "home"; // Página principal del usuario
		}
		model.addAttribute("usuario", new Usuario());
		model.addAttribute("error", "Correo o contraseña incorrectos.");
		return "login";
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
			} else {
				model.addAttribute("error", "Error al crear el reto.");
			}
			model.addAttribute("reto", new Reto());
			return "crearReto";
		}
		return "redirect:/login";
	}
}
