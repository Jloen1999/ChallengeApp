package es.uex.challengeapp.service;

import es.uex.challengeapp.model.Usuario;

public interface UsuarioService {

	Usuario registrarUsuario(Usuario usuario);

	Usuario autenticarUsuario(String correo, String contrasena);
}