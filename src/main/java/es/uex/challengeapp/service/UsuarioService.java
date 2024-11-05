package es.uex.challengeapp.service;

import es.uex.challengeapp.model.Usuario;
import es.uex.challengeapp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario registrarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario autenticarUsuario(String correo, String contrasena) {
        return usuarioRepository.findByCorreo(correo)
            .filter(u -> u.getContrasena().equals(contrasena))
            .orElse(null);
    }
}
