package proyecto.Hoteles.Servicios.Implementaciones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import proyecto.Hoteles.Entidades.empleadoPuesto;
import proyecto.Hoteles.Repositorios.IEmpleadoPuestoRepository;
import proyecto.Hoteles.Servicios.Interfaces.IEmpleadoPuestoServices;


import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoPuestoServices implements IEmpleadoPuestoServices {
    @Autowired
    public IEmpleadoPuestoRepository empleadoPuestoRepository;


    @Override
    public Page<empleadoPuesto> BuscarTodosPaginados(Pageable pageable) {
        return empleadoPuestoRepository.findByOrderByEmpleadoDesc(pageable);
    }

    @Override
    public List<empleadoPuesto> ObtenerTodos() {
        return empleadoPuestoRepository.findAll();
    }

    @Override
    public Optional<empleadoPuesto> BuscarPorId(Integer id) {
        return empleadoPuestoRepository.findById(id);
    }

    @Override
    public empleadoPuesto CrearOeditar(empleadoPuesto empleadoPuesto) {
        return empleadoPuestoRepository.save(empleadoPuesto);
    }

    @Override
    public void EliminarPorId(Integer id) {
empleadoPuestoRepository.deleteById(id);
    }
}
