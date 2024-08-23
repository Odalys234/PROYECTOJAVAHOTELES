package proyecto.Hoteles.Repositorios;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.Hoteles.Entidades.empleadoPuesto;

public interface IEmpleadoPuestoRepository extends JpaRepository<empleadoPuesto, Integer> {
    Page<empleadoPuesto> findByOrderByEmpleadoDesc(Pageable pageable);
}
