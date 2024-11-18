package es.uex.challengeapp;

import es.uex.challengeapp.model.Usuario;
import es.uex.challengeapp.service.UsuarioServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyCommandLineRunner implements CommandLineRunner {

    private final UsuarioServiceImpl usuarioService;

    public MyCommandLineRunner(UsuarioServiceImpl usuarioService) {
        this.usuarioService = usuarioService;
    }
    @Override
    public void run(String... args) throws Exception{
        System.out.println("¡Hola desde CommandLineRunner");

        // Insertar dos usuarios en la base de datos
        Usuario usuario1 = new Usuario("jose", "jose@gmail.com", "jose", "Calle Linares");
        Usuario usuario2 = new Usuario("maria", "maria@gmail.com", "maria", "Calle Linares");

        // Guardar los usuarios en la base de datos
        usuarioService.registrarUsuario(usuario1);
        usuarioService.registrarUsuario(usuario2);
    }
}
