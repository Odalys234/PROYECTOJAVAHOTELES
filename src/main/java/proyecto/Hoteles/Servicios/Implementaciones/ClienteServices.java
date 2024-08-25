package proyecto.Hoteles.Servicios.Implementaciones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import proyecto.Hoteles.Entidades.Cliente;
import proyecto.Hoteles.Repositorios.IClienteRepository;
import proyecto.Hoteles.Servicios.Interfaces.IClienteServices;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteServices implements IClienteServices {
    @Autowired
    public IClienteRepository clienteRepository;

    @Override
    public Page<Cliente> BuscarTodosPaginados(Pageable pageable) {
        return clienteRepository.findAll(pageable);
    }

    @Override
    public List<Cliente> ObtenerTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public Optional<Cliente> BuscarPorId(Integer id) {
        return clienteRepository.findById(id);
    }

    @Override
    public Cliente CrearOeditar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public void EliminarPorId(Integer id) {
clienteRepository.deleteById(id);
    }
}
