package proyecto.Hoteles.Servicios.Implementaciones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import proyecto.Hoteles.Entidades.Empleado;
import proyecto.Hoteles.Repositorios.IEmpleadoRepository;
import proyecto.Hoteles.Servicios.Interfaces.IEmpleadoServices;

import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoServices implements IEmpleadoServices {
    @Autowired
    private IEmpleadoRepository empleadoRepository;

    @Override
    public Page<Empleado> BuscarTodosPaginados(Pageable pageable) {
        return empleadoRepository.findAll(pageable);
    }

    @Override
    public List<Empleado> ObtenerTodos() {
        return empleadoRepository.findAll();
    }

    @Override
    public Optional<Empleado> BuscarPorId(Integer id) {
        return empleadoRepository.findById(id);
    }

    @Override
    public Empleado CrearOeditar(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    @Override
    public void EliminarPorId(Integer id) {
    empleadoRepository.deleteById(id);
    }
}
