package proyecto.Hoteles.Servicios.Interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import proyecto.Hoteles.Entidades.Puesto;


import java.util.List;
import java.util.Optional;

public interface IPuestoServices {
    Page<Puesto> BuscarTodosPaginados(Pageable pageable);
    List<Puesto> ObtenerTodos();
    Optional<Puesto> BuscarPorId(Integer id);
    Puesto CrearOeditar(Puesto puesto);
    void EliminarPorId(Integer id);
}
