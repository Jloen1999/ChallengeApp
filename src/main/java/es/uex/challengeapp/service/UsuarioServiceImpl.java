package es.uex.challengeapp.service;

import es.uex.challengeapp.model.Usuario;
import es.uex.challengeapp.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	private final UsuarioRepository usuarioRepository;

	public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public Usuario registrarUsuario(Usuario usuario) {
		// Comprobar si existe un usuario con el mismo correo
		if (usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
			return null;
		}
		return usuarioRepository.save(usuario);
	}

	@Override
	public Usuario autenticarUsuario(String correo, String contrasena) {
		return usuarioRepository.findByCorreo(correo).filter(u -> u.getContrasena().equals(contrasena)).orElse(null);
	}

	@Override
	public Usuario buscarUsuarioPorNombre(String nombreAmigo) {
		return usuarioRepository.findByNombre(nombreAmigo).orElse(null);
	}

	@Override
	public Usuario obtenerUsuarioPorId(Long id) {
		return usuarioRepository.findById(id).orElse(null);
	}

	@Override
	public List<Usuario> buscarPorNombreOCorreo(String criterioBusqueda) {
		return usuarioRepository.findByNombreContainingIgnoreCaseOrCorreoContainingIgnoreCase(criterioBusqueda,
				criterioBusqueda);
	}

	@Override
	public Optional<Usuario> buscarUsuarioPorEmail(String email) {
		return usuarioRepository.findByCorreo(email);
	}

	@Override
	public List<Usuario> obtenerTodosLosUsuarios() {
		return usuarioRepository.findAll();
	}

}
