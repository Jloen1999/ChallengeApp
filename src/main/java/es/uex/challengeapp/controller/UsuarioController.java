package es.uex.challengeapp.controller;

import es.uex.challengeapp.model.Reto;
import es.uex.challengeapp.model.Usuario;
import es.uex.challengeapp.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping("/registro")
	public String mostrarFormularioRegistro(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "registro";
	}

	@GetMapping("/dashboard")
	public String mostrarPerfilUsuario() {
		return "dashboard";
	}

	@GetMapping("/estadisticas")
	public String mostrarEstadisticasUsuario() {
		return "estadisticas";
	}

	@GetMapping("/misRetos")
	public String mostrarRetosUsuario() {
		return "misRetos";
	}

	@GetMapping("/reto")
	public String mostrarReto() {
		return "reto";
	}

	@GetMapping("/amigos")
	public String mostrarAmigos() {
		return "amigos";
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

	@PostMapping("/dashboard")
	public String login(@RequestParam String correo, @RequestParam String contrasena, Model model) {
		Usuario usuario = usuarioService.autenticarUsuario(correo, contrasena);
		if (usuario != null) {
			model.addAttribute("usuario", usuario);
			return "dashboard"; // Página principal del usuario
		}
		model.addAttribute("usuario", new Usuario());
		model.addAttribute("error", "Correo o contraseña incorrectos.");
		return "login";
	}
}
