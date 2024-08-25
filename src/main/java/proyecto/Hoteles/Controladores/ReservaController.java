package proyecto.Hoteles.Controladores;

import com.lowagie.text.DocumentException;
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
import proyecto.Hoteles.Entidades.Habitacion;
import proyecto.Hoteles.Entidades.Reserva;
import proyecto.Hoteles.Servicios.Implementaciones.HabitacionServices;
import proyecto.Hoteles.Servicios.Interfaces.IClienteServices;
import proyecto.Hoteles.Servicios.Interfaces.IHabitacionServices;
import proyecto.Hoteles.Servicios.Interfaces.IReservaServices;
import proyecto.Hoteles.Utilidades.FacturaExportPDF;


import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private IReservaServices reservaServices;
    @Autowired
    private IClienteServices clienteServices;
    @Autowired
    private IHabitacionServices habitacionServices;

    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Page<Reserva> reservas = reservaServices.BuscarTodosPaginados(pageable);
        model.addAttribute("reservas", reservas);

        int totalPage = reservas.getTotalPages();
        if (totalPage > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPage)
                    .boxed()
                    .collect(Collectors.toList());

            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "reserva/index";
    }

    @GetMapping("/create")
    public String create(Reserva reserva, Model model) {
        // Asegúrate de cargar aquí la lista de habitaciones y clientes para mostrarlas en el formulario
        model.addAttribute("habitaciones", reservaServices.obtenerTodasLasHabitaciones());
        model.addAttribute("clientes", reservaServices.obtenerTodosLosClientes());
        return "reserva/create";
    }

    @PostMapping("/save")
    public String save(Reserva reserva, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            model.addAttribute("reserva", reserva);
            attributes.addFlashAttribute("error", "No se pudo guardar la reserva debido a un error");
            return "reserva/create";
        }



        reservaServices.CrearOeditar(reserva);
        attributes.addFlashAttribute("msg", "Reserva creada correctamente");
        return "redirect:/reservas";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model) {
        Reserva reserva = reservaServices.BuscarPorId(id).orElse(null);
        if (reserva == null) {
            return "redirect:/reservas";
        }
        model.addAttribute("reserva", reserva);
        return "reserva/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Reserva reserva = reservaServices.BuscarPorId(id).orElse(null);
        if (reserva == null) {
            return "redirect:/reservas";
        }
        model.addAttribute("reserva", reserva);
        // Asegúrate de cargar la lista de habitaciones y clientes para el formulario de edición
        model.addAttribute("habitaciones", reservaServices.obtenerTodasLasHabitaciones());
        model.addAttribute("clientes", reservaServices.obtenerTodosLosClientes());
        return "reserva/edit";
    }


    @PostMapping("/update")
    public String update(@RequestParam Integer id,
                         @RequestParam("habitacion.id") Integer habitacionId,
                         @RequestParam("cliente.id") Integer clienteId,
                         @RequestParam String fechaEntrada,
                         @RequestParam String fechaSalida,
                         @RequestParam BigDecimal montoTotal,
                         RedirectAttributes attributes) {
        Habitacion habitacion = habitacionServices.BuscarPorId(habitacionId).orElse(null);
        Cliente cliente = clienteServices.BuscarPorId(clienteId).orElse(null);

        if (habitacion != null && cliente != null) {
            Reserva reserva = reservaServices.BuscarPorId(id).orElse(null);
            if (reserva != null) {
                reserva.setHabitacion(habitacion);
                reserva.setCliente(cliente);
                reserva.setFechaEntrada(LocalDateTime.parse(fechaEntrada));
                reserva.setFechaSalida(LocalDateTime.parse(fechaSalida));
                reserva.setMontoTotal(montoTotal);

                reservaServices.CrearOeditar(reserva);
                attributes.addFlashAttribute("msg", "Reserva actualizada correctamente");
            }
        } else {
            attributes.addFlashAttribute("error", "Hubo un error al actualizar la reserva");
        }
        return "redirect:/reservas";
    }


    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id, Model model) {
        Reserva reserva = reservaServices.BuscarPorId(id).orElse(null);
        if (reserva == null) {
            return "redirect:/reservas";
        }
        model.addAttribute("reserva", reserva);
        return "reserva/delete";
    }

    @PostMapping("/delete")
    public String delete(Reserva reserva, RedirectAttributes attributes) {
        reservaServices.EliminarPorId(reserva.getId());
        attributes.addFlashAttribute("msg", "Reserva eliminada correctamente");
        return "redirect:/reservas";
    }
    @Autowired
    private FacturaExportPDF facturaExportPDF;

    @GetMapping("/facturas/generar")
    public void generarFactura(@RequestParam("reservaId") Integer reservaId, HttpServletResponse response) {
        try {
            facturaExportPDF.exportarFactura(response, reservaId);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }



   /* @GetMapping("/exportarPDF")
    public void exportarReservas(HttpServletResponse response) throws IOException {
        // Establece el tipo de contenido de la respuesta como "application/pdf"
        response.setContentType("application/pdf");

        // Crea un objeto SimpleDateFormat para formatear la fecha actual en un formato específico
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");

        // Obtiene la fecha y hora actual formateada como cadena
        String fechaActual = dateFormatter.format(new Date());

        // Define el encabezado "Content-Disposition" de la respuesta
        // Este encabezado indica que el contenido debe mostrarse en línea (en el navegador) y sugiere un nombre de archivo para el PDF
        String cabecera = "Content-Disposition";
        String valor = "inline; filename=Reservas_" + fechaActual + ".pdf";
        response.setHeader(cabecera, valor);

        // Obtiene una lista de todas las reservas desde el servicio de reservas
        List<Reserva> reservas = reservaServices.ObtenerTodos();
        // Crea una instancia de la clase ReservaExportPDF, pasando la lista de reservas como parámetro
        ReservaExportPDF exporter = new ReservaExportPDF(reservas);
        // Llama al método Exportar para generar el PDF y enviarlo en la respuesta HTTP
        exporter.Exportar(response);
    }*/
}
