package proyecto.Hoteles.Servicios.Implementaciones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import proyecto.Hoteles.Entidades.Cliente;
import proyecto.Hoteles.Entidades.Habitacion;
import proyecto.Hoteles.Entidades.Reserva;
import proyecto.Hoteles.Repositorios.IClienteRepository;
import proyecto.Hoteles.Repositorios.IHabitacionRepository;
import proyecto.Hoteles.Repositorios.IReservaRepository;
import proyecto.Hoteles.Servicios.Interfaces.IReservaServices;

import java.util.List;
import java.util.Optional;

@Service
public class ReservaServices implements IReservaServices {
    @Autowired
    private IReservaRepository reservaRepository;
    @Autowired
    private IHabitacionRepository habitacionRepository;

    @Autowired
    private IClienteRepository clienteRepository;

    @Override
    public Page<Reserva> BuscarTodosPaginados(Pageable pageable) {
        return reservaRepository.findAll(pageable);
    }

    @Override
    public List<Reserva> ObtenerTodos() {
        return reservaRepository.findAll();
    }

    @Override
    public Optional<Reserva> BuscarPorId(Integer id) {
        return reservaRepository.findById(id);
    }

    @Override
    public Reserva CrearOeditar(Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    @Override
    public void EliminarPorId(Integer id) {
reservaRepository.deleteById(id);
    }
    @Override
    public List<Habitacion> obtenerTodasLasHabitaciones() {
        return habitacionRepository.findAll();
    }

    @Override
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }

    @Override
    public List<Reserva> BuscarReservasPorClienteId(Integer clienteId) {
        return reservaRepository.findByClienteId(clienteId);
    }
}
