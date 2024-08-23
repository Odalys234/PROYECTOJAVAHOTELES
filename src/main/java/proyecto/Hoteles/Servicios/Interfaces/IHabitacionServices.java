package proyecto.Hoteles.Servicios.Interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import proyecto.Hoteles.Entidades.Habitacion;


import java.util.List;
import java.util.Optional;

public interface IHabitacionServices {
    Page<Habitacion> BuscarTodosPaginados(Pageable pageable);
    List<Habitacion> ObtenerTodos();
    Optional<Habitacion> BuscarPorId(Integer id);
    Habitacion CrearOeditar(Habitacion habitacion);
    void EliminarPorId(Integer id);
}
