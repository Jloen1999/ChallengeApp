package es.uex.challengeapp.service;

import es.uex.challengeapp.model.Usuario;
import es.uex.challengeapp.repository.UsuarioRepository;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	private final UsuarioRepository usuarioRepository;

	public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public Usuario registrarUsuario(Usuario usuario) {
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
	public Usuario obtenerUsuarioPorId(Integer id) {
		return usuarioRepository.findById(Long.valueOf(id)).orElse(null);
	}

	@Override
	public List<Usuario> buscarPorNombreOCorreo(String criterioBusqueda) {
		return usuarioRepository.findByNombreContainingIgnoreCaseOrCorreoContainingIgnoreCase(criterioBusqueda,
				criterioBusqueda);
	}

	@Override
	public Usuario buscarUsuarioPorEmail(String email) {
		return usuarioRepository.findByCorreo(email).orElse(null);
	}

}
