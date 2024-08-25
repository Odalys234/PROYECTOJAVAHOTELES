package proyecto.Hoteles.Servicios.Interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import proyecto.Hoteles.Entidades.Cliente;
import proyecto.Hoteles.Entidades.Habitacion;
import proyecto.Hoteles.Entidades.Reserva;

import java.util.List;
import java.util.Optional;

public interface IReservaServices {
    Page<Reserva> BuscarTodosPaginados(Pageable pageable);
    List<Reserva> ObtenerTodos();
    Optional<Reserva> BuscarPorId(Integer id);
  Reserva CrearOeditar(Reserva reserva);
    void EliminarPorId(Integer id);
    List<Habitacion> obtenerTodasLasHabitaciones();
    List<Cliente> obtenerTodosLosClientes();
        List<Reserva> BuscarReservasPorClienteId(Integer clienteId);

}
