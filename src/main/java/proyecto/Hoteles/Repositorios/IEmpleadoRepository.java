package proyecto.Hoteles.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.Hoteles.Entidades.Empleado;

public interface IEmpleadoRepository extends JpaRepository<Empleado, Integer> {
}
