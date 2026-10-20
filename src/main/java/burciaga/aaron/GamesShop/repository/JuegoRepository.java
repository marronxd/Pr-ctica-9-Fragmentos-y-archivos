package burciaga.aaron.GamesShop.repository;

import burciaga.aaron.GamesShop.models.Juego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JuegoRepository extends JpaRepository<Juego,Long> {

    List<Juego> findByTituloContainingIgnoreCase(String titulo);

    List<Juego> findByDescripcionContainingIgnoreCase(String descripcion);

    List<Juego> findByTituloStartsWithIgnoreCase(String titulo);

    @Query("SELECT j FROM Juego j where j.precio <: precioComparacion and LOWER(j.titulo) LIKE CONCAT(:letra,'%')")
    List<Juego> buscarPrecioMenorAQueEmpiecePor(@Param("precioComparacion") Double precioComparacion, @Param("letra") String letra);
}
