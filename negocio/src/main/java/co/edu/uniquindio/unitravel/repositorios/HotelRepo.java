package co.edu.uniquindio.unitravel.repositorios;

import co.edu.uniquindio.unitravel.entidades.Caracteristica;
import co.edu.uniquindio.unitravel.entidades.Cliente;
import co.edu.uniquindio.unitravel.entidades.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepo extends JpaRepository<Hotel,Integer> {

    @Query("select h from Hotel h where h.nombre like %:nombre%")
    List<Hotel> listarHotelesPorNombre(String nombre);

    @Query("select h from Hotel h where h.estrellas = :estrellas")
    List<Hotel> listarHotelesPorEstrellas(Integer estrellas);

    @Query("select h from Hotel h where h.ciudad.nombre = :ciudad")
    List<Hotel> listarHotelesPorCiudad(String ciudad);

    Hotel findByNombreAndDireccion(String nombre, String direccion);

    @Query("select h from Hotel h where h.nombre like concat('%', :nombre , '%') ")
    List<Hotel> buscarHotelNombre(String nombre);

    @Query("select h from Hotel h where h.estrellas = ?1")
    List<Hotel> obtenerHotelCategoria(int numeroEstrellas);

    @Query("select h from Hotel h where h.ciudad.nombre like %?1% and h.estado = 'A'")
    List<Hotel>obtenerHotelesNombre(String nombreCiudad);

    /*
        Cree una consulta que permita contar el número de hoteles que hay por cada ciudad. Use GROUP BY.
     */
    @Query("select h.ciudad.nombre, count(h) from Hotel h group by h.ciudad")
    List<Object []> contarHotelesPorCiudad();

    /*
        Cree una consulta que permita determinar qué hoteles no tienen comentarios. Use IS EMPTY.
     */
    @Query("select h from Hotel h where h.comentarios is empty")
    List<Hotel>ObtenerHotelesSinComentarios();

    /*
        Cree una consulta que devuelva una lista con todos los hoteles que contengan en su nombre una
        cadena de búsqueda. Use LIKE.
     */
    @Query("select h from Hotel h where h.nombre like %?1% ")
    List<Hotel>obtenerHotelesPorNombre(String cadena);

    /*
        Cree una consulta que devuelva los hoteles de una ciudad específica ordenados por precio. De
        menor a mayor
     */
    @Query("select ho from Hotel ho  inner join  ho.habitaciones hb where ho.ciudad.nombre = ?1 order by hb.precio desc")
    List<Hotel> obtenerHotelesCiudadOrdenado(String nombreCiudad);

    /*
        Escriba una consulta que retorne la calificación promedio de un hotel.
     */
    @Query("select h, avg(com.calificacion) from Hotel h inner join h.comentarios com group by h")
    List<Object[]> promedioCalificacionHotel();
    @Query("select h from Hotel h where h.estado = 'A'")
    List<Hotel> obtenerListaHoteles ();


    @Query("select c from Hotel h join h.caracteristicas c where h.codigo = :codigo")
    List<Caracteristica> obtenerCaracteristicasHotel(Integer codigo);

    @Query("select h from Hotel h where h.estrellas >=:numeroEstrellas")
    List<Hotel> listarHotelesPorNumeroEstrellas(int numeroEstrellas);
}
