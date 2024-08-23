package proyecto.Hoteles.Servicios.Implementaciones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import proyecto.Hoteles.Entidades.Puesto;
import proyecto.Hoteles.Repositorios.IPuestoRepository;
import proyecto.Hoteles.Servicios.Interfaces.IPuestoServices;

import java.util.List;
import java.util.Optional;


@Service
public class PuestoServices implements IPuestoServices {
    @Autowired
    private IPuestoRepository puestoRepository;

    @Override
    public Page<Puesto> BuscarTodosPaginados(Pageable pageable) {
        return puestoRepository.findAll(pageable);
    }

    @Override
    public List<Puesto> ObtenerTodos() {
        return puestoRepository.findAll();
    }

    @Override
    public Optional<Puesto> BuscarPorId(Integer id) {
        return puestoRepository.findById(id);
    }

    @Override
    public Puesto CrearOeditar(Puesto puesto) {
        return puestoRepository.save(puesto);
    }

    @Override
    public void EliminarPorId(Integer id) {
puestoRepository.deleteById(id);
    }
}
