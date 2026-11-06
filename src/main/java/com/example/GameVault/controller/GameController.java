package com.example.GameVault.controller;

import com.example.GameVault.model.Juego;
import com.example.GameVault.model.Usuario;
import com.example.GameVault.service.JuegoService;
import com.example.GameVault.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class GameController {

    // "Base de datos" simulada en memoria
    private static final List<Juego> juegosDb = new ArrayList<>();
    private static long idCounter = 1;


    @Autowired
    private JuegoService juegoService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/fragments-demo")
    public String fragmentsDemo() {
        return "fragments-demo";
    }

    @GetMapping({"/", "/juegos"})
    public String listarJuegos(@RequestParam(name ="buscar", required = false) String buscar, Model model) {
        // adicion para BD MYSQL
        if(buscar != null && !buscar.isEmpty()){
            model.addAttribute("juegos", juegoService.buscarPorTitulo(buscar));
            model.addAttribute("busquedaActual", buscar);

        }else{
            model.addAttribute("juegos", juegoService.listarTodos());
        }
        return "juegos";
    }

    @GetMapping("/juegos/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("juego", new Juego());
        return "formulario";
    }

    @PostMapping("/juegos")
    public String guardarJuego(@RequestParam("titulo") String titulo,
                               @RequestParam("descripcion") String descripcion,
                               @RequestParam("precio") Double precio,
                               @RequestParam("categoria") String categoria,
                               @RequestParam("portada") MultipartFile portada, HttpSession httpSession) {

        Juego nuevoJuego = new Juego();
        nuevoJuego.setTitulo(titulo);
        nuevoJuego.setDescripcion(descripcion);
        nuevoJuego.setPrecio(precio);
        nuevoJuego.setCategoria(categoria);

        nuevoJuego.setUsuario(usuarioService.obtenerUsuarioLogueado());
        juegoService.guardarJuego(nuevoJuego, portada);
        // Patrón PRG (Post/Redirect/Get)
        return "redirect:/mis-juegos";
    }

    @GetMapping("/mis-juegos")
    public String dashboard(Model model){
        Usuario logueado = usuarioService.obtenerUsuarioLogueado();
        if (logueado == null) {
            return "redirect:/login";
        }
        model.addAttribute("juegos", juegoService.obtenerJuegosPorUsuario(logueado.getId()));
        model.addAttribute("usuario", logueado);

        return "mis-juegos";
    }

    @GetMapping("/juegos/editar/{id}")
    public String editarJuego(@PathVariable Long id, Model model) {
        Juego juego = juegoService.obtenerJuegoPorId(id);

        // OBTENER USUARIO LOGUEADO PARA SOLO ACTUALIZAR LOS JUEGOS PROPIOS.
         Usuario logueado = usuarioService.obtenerUsuarioLogueado();
        if(juego!= null && juego.getUsuario() != null && juego.getUsuario().getId().equals(logueado.getId())){
            model.addAttribute("juego", juego);
            return "formulario";
        }
        return "redirect:/mis-juegos"; // para regresar a la dashboard de la sesion actual
    }

    @PostMapping("/juegos/editar/{id}")
    public String actualizarJuego(@PathVariable Long id,
                                  @RequestParam("titulo") String titulo,
                                  @RequestParam("descripcion") String descripcion,
                                  @RequestParam("precio")Double precio,
                                  @RequestParam("categoria") String categoria,
                                  @RequestParam(value ="portada", required = false) MultipartFile portada) {

        Juego juegoExistente = juegoService.obtenerJuegoPorId(id);
        Usuario logueado = usuarioService.obtenerUsuarioLogueado();

        if (juegoExistente != null && juegoExistente.getUsuario() != null
                && juegoExistente.getUsuario().getId().equals(logueado.getId())) {
            juegoExistente.setTitulo(titulo);
            juegoExistente.setDescripcion(descripcion);
            juegoExistente.setPrecio(precio);
            juegoExistente.setCategoria(categoria);
            juegoService.guardarJuego(juegoExistente, portada);
        }
        return "redirect:/mis-juegos";
    }

    @GetMapping("/juego/{id}")
    public String detalleJuego(@PathVariable Long id, Model model) {
        // el modelo es donde ponemos toda la información que desplegamos
        Juego juego = juegoService.obtenerJuegoPorId(id);
        if (juego== null){
            return "redirect:/juegos";
        }
        model.addAttribute("juego", juego);

        return "detalle-juego";
    }

    @PostMapping("/juegos/eliminar/{id}")
    public String eliminarJuego(@PathVariable Long id) {
        Juego juego = juegoService.obtenerJuegoPorId(id);
        Usuario logueado = usuarioService.obtenerUsuarioLogueado();

        if(juego != null && juego.getUsuario() != null && juego.getUsuario().getId().equals(logueado.getId())){
            juegoService.eliminarJuego(id);
        }

        return "redirect:/mis-juegos"; // mi dashboard
    }
}
