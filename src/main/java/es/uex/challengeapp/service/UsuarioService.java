package es.uex.challengeapp.service;

import java.util.List;

import es.uex.challengeapp.model.Usuario;

public interface UsuarioService {

	Usuario registrarUsuario(Usuario usuario);

	Usuario autenticarUsuario(String correo, String contrasena);

	Usuario buscarUsusarioPorNombre(String nombreAmigo);

	Usuario obtenerUsuarioPorId(Integer id);

	List<Usuario> buscarPorNombreOCorreo(String criterioBusqueda);
}