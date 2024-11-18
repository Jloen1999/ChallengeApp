package es.uex.challengeapp;

import es.uex.challengeapp.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class ChallengeappApplication {
    private final ScheduledExecutorService scheduledExecutorService;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public ChallengeappApplication(ScheduledExecutorService scheduledExecutorService, UsuarioRepository usuarioRepository) {
        this.scheduledExecutorService = scheduledExecutorService;
        this.usuarioRepository = usuarioRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(ChallengeappApplication.class, args);
    }

    @PostConstruct
    public void startScheduledTask() {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            System.out.println("Usuarios en la base de datos: " + usuarioRepository.count());
        }, 1, 2, TimeUnit.MINUTES);
    }

}
