package com.example.GameVault.service;

import com.example.GameVault.model.Usuario;
import com.example.GameVault.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Lógica de usuarios: registro y autenticación.
 */
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    // para la relacion de una sesion con un usuario
    @Autowired
    private HttpSession session;

    /**
     * Registra un usuario guardando su contraseña cifrada con BCrypt.
     */
    public Usuario registrarUsuario(Usuario usuario) {
        usuario.setContrasenia(passwordEncoder.encode(usuario.getContrasenia()));
        return usuarioRepository.save(usuario);
    }

    /**
     * Devuelve el usuario si el correo existe y la contraseña coincide; si no, null.
     */
    public Usuario autenticarUsuario(String email, String password) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            // matches() cifra la contraseña escrita y la compara con el hash guardado
            if (passwordEncoder.matches(password, usuario.getContrasenia())) {
                return usuario;
            }
        }
        return null;
    }

    public Usuario obtenerUsuarioLogueado(){
        Long usuarioID = (Long) session.getAttribute("usuario_id");

        if(usuarioID != null){

            // VERIFICA PRIMERO EN LA BD SI EXISTE ANTES DE USARLO
            Optional<Usuario> usuario = usuarioRepository.findById(usuarioID);
            if(usuario.isPresent()){
                return usuario.get();
            }
        }
        return null;
    }
}
