package proyecto.Hoteles.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.Hoteles.Entidades.Habitacion;

public interface IHabitacionRepository extends JpaRepository<Habitacion, Integer> {
}
