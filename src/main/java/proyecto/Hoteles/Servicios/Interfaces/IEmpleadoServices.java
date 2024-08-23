package proyecto.Hoteles.Servicios.Interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import proyecto.Hoteles.Entidades.Empleado;

import java.util.List;
import java.util.Optional;

public interface IEmpleadoServices {
    Page<Empleado> BuscarTodosPaginados(Pageable pageable);
    List<Empleado> ObtenerTodos();
    Optional<Empleado> BuscarPorId(Integer id);
    Empleado CrearOeditar(Empleado empleado);
    void EliminarPorId(Integer id);
}
