package es.uex.challengeapp.service;

import java.util.List;
import java.util.Optional;

import es.uex.challengeapp.model.Usuario;

public interface UsuarioService {

	Usuario registrarUsuario(Usuario usuario);

	Usuario autenticarUsuario(String correo, String contrasena);

	Usuario buscarUsuarioPorNombre(String nombreAmigo);

	Usuario obtenerUsuarioPorId(Long id);

	List<Usuario> buscarPorNombreOCorreo(String criterioBusqueda);

	Optional<Usuario> buscarUsuarioPorEmail(String email);
	
	List<Usuario> obtenerTodosLosUsuarios();
}