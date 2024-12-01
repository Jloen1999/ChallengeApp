package es.uex.challengeapp.repository;

import es.uex.challengeapp.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
    
    Optional<Usuario> findById(Long id);
    
    Optional<Usuario> findByNombre(String nombre);

}
