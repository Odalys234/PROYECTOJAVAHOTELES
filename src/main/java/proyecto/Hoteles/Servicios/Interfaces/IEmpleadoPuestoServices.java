package proyecto.Hoteles.Servicios.Interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import proyecto.Hoteles.Entidades.Empleado;
import proyecto.Hoteles.Entidades.empleadoPuesto;

import java.util.List;
import java.util.Optional;

public interface IEmpleadoPuestoServices {
    Page<empleadoPuesto> BuscarTodosPaginados(Pageable pageable);
    List<empleadoPuesto> ObtenerTodos();
    Optional<empleadoPuesto> BuscarPorId(Integer id);
    empleadoPuesto CrearOeditar(empleadoPuesto empleadoPuesto);
    void EliminarPorId(Integer id);
}
