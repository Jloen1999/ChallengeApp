package es.uex.challengeapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.uex.challengeapp.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    @Query("SELECT u FROM Usuario u WHERE u.correo = :correo")
    Optional<Usuario> findByCorreo(@Param("correo") String correo);
    
    Optional<Usuario> findById(Long id);

    Optional<Usuario> findByNombre(String nombre);
    
    List<Usuario> findByNombreContainingIgnoreCaseOrCorreoContainingIgnoreCase(String nombre, String correo);

}
