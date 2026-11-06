package com.example.GameVault.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad Usuario: las personas que pueden iniciar sesión y tener juegos en la bóveda.
 * Se guarda en la tabla 'usuarios'.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    // unique = true: no puede haber dos cuentas con el mismo correo
    @Column(nullable = false, unique = true)
    private String email;

    // Se guarda cifrada con BCrypt (ver UsuarioService), nunca en texto plano
    @Column(nullable = false)
    private String contrasenia;
}
