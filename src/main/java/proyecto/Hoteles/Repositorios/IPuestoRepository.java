package proyecto.Hoteles.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.Hoteles.Entidades.Puesto;

public interface IPuestoRepository extends JpaRepository<Puesto, Integer> {
}
