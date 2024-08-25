package proyecto.Hoteles.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.Hoteles.Entidades.Reserva;

import java.util.List;

public interface IReservaRepository extends JpaRepository<Reserva, Integer> {
    List<Reserva> findByClienteId(Integer clienteId);
}
