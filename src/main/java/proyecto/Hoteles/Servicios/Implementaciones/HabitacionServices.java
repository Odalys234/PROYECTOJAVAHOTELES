package proyecto.Hoteles.Servicios.Implementaciones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import proyecto.Hoteles.Entidades.Habitacion;
import proyecto.Hoteles.Repositorios.IHabitacionRepository;
import proyecto.Hoteles.Servicios.Interfaces.IHabitacionServices;

import java.util.List;
import java.util.Optional;

@Service
public class HabitacionServices implements IHabitacionServices {
    @Autowired
    public IHabitacionRepository habitacionRepository;

    @Override
    public Page<Habitacion> BuscarTodosPaginados(Pageable pageable) {
        return habitacionRepository.findAll(pageable);
    }

    @Override
    public List<Habitacion> ObtenerTodos() {
        return habitacionRepository.findAll();
    }

    @Override
    public Optional<Habitacion> BuscarPorId(Integer id) {
        return habitacionRepository.findById(id);
    }

    @Override
    public Habitacion CrearOeditar(Habitacion habitacion) {
        return habitacionRepository.save(habitacion);
    }

    @Override
    public void EliminarPorId(Integer id) {
habitacionRepository.deleteById(id);
    }
}
