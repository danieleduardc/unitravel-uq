
package co.edu.uniquindio.unitravel.repositorios;

import co.edu.uniquindio.unitravel.entidades.Cliente;
import co.edu.uniquindio.unitravel.entidades.Persona;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepo extends JpaRepository<Cliente,String> {

    @Query("select c from Cliente c where c.nombre = :nombre")
    List<Cliente> listarClientesPorNombre(String nombre);

    @Query("select c from Cliente c where c.email = :email and c.password = :password")
    Optional<Cliente> autenticacion(String email, String password);

    @Query("select c from Cliente c where c.cedula = :cedula")
    Optional<Cliente> listarClientesPorCedula(String cedula);

    Persona findByEmail(String correo);

    @Override
    Page<Cliente> findAll(Pageable pageable);

    @Query("select c from Cliente c where c.email = :email")
    Persona obtenerClienteDadoCorreo(String email);
}
