package proyecto.Hoteles.Servicios.Interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import proyecto.Hoteles.Entidades.Cliente;


import java.util.List;
import java.util.Optional;

public interface IClienteServices {
    Page<Cliente> BuscarTodosPaginados(Pageable pageable);
    List<Cliente> ObtenerTodos();
    Optional<Cliente> BuscarPorId(Integer id);
    Cliente CrearOeditar(Cliente cliente);
    void EliminarPorId(Integer id);
}
