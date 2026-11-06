package com.example.GameVault.repository;

import com.example.GameVault.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Busca un usuario por su correo; se usa al iniciar sesión.
    // Optional porque el correo puede no existir.
    Optional<Usuario> findByEmail(String email);
}
