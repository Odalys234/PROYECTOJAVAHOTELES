package proyecto.Hoteles.Controladores;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import proyecto.Hoteles.Entidades.Cliente;
import proyecto.Hoteles.Servicios.Interfaces.IClienteServices;
import proyecto.Hoteles.Utilidades.ClienteExportPDF;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private IClienteServices clienteServices;

    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Page<Cliente> clientes = clienteServices.BuscarTodosPaginados(pageable);
        model.addAttribute("clientes", clientes);

        int totalPage = clientes.getTotalPages();
        if (totalPage > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPage)
                    .boxed()
                    .collect(Collectors.toList());

            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "cliente/index";
    }

    @GetMapping("/create")
    public String create(Cliente cliente) {
        return "cliente/create";
    }

    @PostMapping("/save")
    public String save(Cliente cliente, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            model.addAttribute("cliente", cliente);
            attributes.addFlashAttribute("error", "No se pudo guardar el cliente debido a un error");
            return "cliente/create";
        }
        clienteServices.CrearOeditar(cliente);
        attributes.addFlashAttribute("msg", "Cliente creado correctamente");
        return "redirect:/clientes";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model) {
        Cliente cliente = clienteServices.BuscarPorId(id).orElse(null);
        if (cliente == null) {
            return "redirect:/clientes";
        }
        model.addAttribute("cliente", cliente);
        return "cliente/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Cliente cliente = clienteServices.BuscarPorId(id).orElse(null);
        if (cliente == null) {
            return "redirect:/clientes";
        }
        model.addAttribute("cliente", cliente);
        return "cliente/edit";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id, Model model) {
        Cliente cliente = clienteServices.BuscarPorId(id).orElse(null);
        if (cliente == null) {
            return "redirect:/clientes";
        }
        model.addAttribute("cliente", cliente);
        return "cliente/delete";
    }

    @PostMapping("/delete")
    public String delete(Cliente cliente, RedirectAttributes attributes) {
        clienteServices.EliminarPorId(cliente.getId());
        attributes.addFlashAttribute("msg", "Cliente eliminado correctamente");
        return "redirect:/clientes";
    }

    @GetMapping("/exportarPDF")
    public void exportarClientes(HttpServletResponse response) throws IOException {
        // Establece el tipo de contenido de la respuesta como "application/pdf"
        response.setContentType("application/pdf");

        // Crea un objeto SimpleDateFormat para formatear la fecha actual en un formato específico
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");

        // Obtiene la fecha y hora actual formateada como cadena
        String fechaActual = dateFormatter.format(new Date());

        // Define el encabezado "Content-Disposition" de la respuesta
        // Este encabezado indica que el contenido debe mostrarse en línea (en el navegador) y sugiere un nombre de archivo para el PDF
        String cabecera = "Content-Disposition";
        String valor = "inline; filename=Clientes_" + fechaActual + ".pdf";
        response.setHeader(cabecera, valor);

        // Obtiene una lista de todos los clientes desde el servicio de clientes
        List<Cliente> clientes = clienteServices.ObtenerTodos();
        // Crea una instancia de la clase ClienteExportPDF, pasando la lista de clientes como parámetro
        ClienteExportPDF exporter = new ClienteExportPDF(clientes);
        // Llama al método Exportar para generar el PDF y enviarlo en la respuesta HTTP
        exporter.Exportar(response);
    }
}
