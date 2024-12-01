package es.uex.challengeapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.uex.challengeapp.model.Reto;
import es.uex.challengeapp.model.Usuario;
import es.uex.challengeapp.service.RetoService;
import es.uex.challengeapp.service.UsuarioService;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private RetoService retoService;

	@GetMapping("/")
	public String home(Model model, HttpSession session) {
		Usuario userActual = (Usuario) session.getAttribute("userActual");
		model.addAttribute("estaLogueado", userActual != null);

		List<Reto> retos = retoService.obtenerTodosLosRetos();
		model.addAttribute("retos", retos);

		return "index";
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

	@PostMapping("/")
	public String login(@RequestParam String correo, @RequestParam String contrasena, Model model,
			HttpSession session) {
		Usuario usuario = usuarioService.autenticarUsuario(correo, contrasena);
		if (usuario != null) {
			session.setAttribute("userActual", usuario);
			model.addAttribute("usuario", usuario);
			model.addAttribute("estaLogueado", true);
			List<Reto> retos = retoService.obtenerTodosLosRetos();
			model.addAttribute("retos", retos);
			return "index";
		}
		model.addAttribute("usuario", new Usuario());
		model.addAttribute("error", "Correo o contraseña incorrectos.");
		return "login";
	}
}