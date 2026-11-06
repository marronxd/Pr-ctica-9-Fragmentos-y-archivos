package com.example.GameVault.service;


import com.example.GameVault.model.Juego;
import com.example.GameVault.repository.JuegoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class JuegoService {

    // aplicar singleton springboot ya hace la inyeccipon de dependencia
    @Autowired
    private JuegoRepository juegoRepository;
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";
    public List<Juego> listarTodos(){
        return juegoRepository.findAll();
    }


    public Juego obtenerJuegoPorId(Long id){
        return juegoRepository.findById(id).orElse(null);
    }

    public void eliminarJuego(Long id){
        juegoRepository.deleteById(id);
    }

    public void guardarJuego(Juego juego, MultipartFile portada){

        String nombreArchivo = "default.png";

        // Verificar si el archivo no está vacío
        if(!portada.isEmpty()){
            try{

                Path uploadPath = Paths.get(UPLOAD_DIR); // optención de ruta del archivo

                if(!Files.exists(uploadPath)){
                    Files.createDirectories(uploadPath); // si no existe, crear dirección
                }

                // GENERA ID IRREPETIBLE
                nombreArchivo = UUID.randomUUID().toString() + "_" + portada.getOriginalFilename();
                Path filePath = uploadPath.resolve(nombreArchivo); // concatena upload path y nombre archivo

                // GUARDA LA IMAGEN EN EL PROYECTO
                Files.copy(portada.getInputStream(), filePath);

            }catch(IOException ex){
                ex.printStackTrace();
                // Redirigir apropiadamente en el proyecto
            }
        }
        if (juego.getPortadaUrl() == null) {
            juego.setPortadaUrl("default.png");   // solo juegos nuevos sin imagen
        }

        juego.setPortadaUrl(nombreArchivo);

        // GUARDA EL TEXTO EN LA BASE DE DATOS
        juegoRepository.save(juego);
    }

    public List<Juego> buscarPorTitulo(String palabra){
        if(palabra == null || palabra.trim().isEmpty()){
            return listarTodos();
        }
        return juegoRepository.findByTituloContainingIgnoreCase(palabra);
    }

    public List<Juego> obtenerJuegosPorUsuario(Long usuarioID){

        return juegoRepository.findByUsuarioId(usuarioID);
    }
}
