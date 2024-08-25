package proyecto.Hoteles.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.Hoteles.Entidades.Cliente;

public interface IClienteRepository extends JpaRepository<Cliente,Integer> {
}
