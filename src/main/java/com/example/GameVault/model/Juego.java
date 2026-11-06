package com.example.GameVault.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad Juego: cada videojuego de la bóveda.
 * Se guarda en la tabla 'juegos'.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "juegos")
public class Juego {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(length = 1000)
    private String descripcion;

    // Nombre del archivo local o URL de Cloudinary
    @Column(name = "portada_url")
    private String portadaUrl;

    @Column
    private Double precio;

    @Column
    private String categoria;

    // MUCHOS juegos pertenecen a UN usuario (N:1).
    // @JoinColumn crea la columna 'usuario_id' (llave foránea) en la tabla 'juegos'.
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
