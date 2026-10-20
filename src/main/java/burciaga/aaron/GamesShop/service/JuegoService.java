package burciaga.aaron.GamesShop.service;

import burciaga.aaron.GamesShop.models.Juego;
import burciaga.aaron.GamesShop.repository.JuegoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class JuegoService {

    // aplicar singleton springboot ya hace la inyeccipon de dependencia
    @Autowired
    private JuegoRepository juegoRepository;

    public List<Juego> listarTodos(){
        return juegoRepository.findAll();
    }

    public void guardarJuego(Juego juego, MultipartFile portada){
        String urlImagen = "default.png";

        if(!portada.isEmpty()){
            // codigo de la practica 9
        }
        // aqui manejar excpcion
        juego.setPortadaUrl(urlImagen);
        juegoRepository.save(juego);
    }

    public List<Juego> buscarPorTitulo(String palabra){
        if(palabra == null || palabra.trim().isEmpty()){
            return listarTodos();
        }
        return juegoRepository.findByTituloContainingIgnoreCase(palabra);
    }
}
