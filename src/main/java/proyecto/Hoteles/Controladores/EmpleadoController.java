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
import proyecto.Hoteles.Entidades.Empleado;
import proyecto.Hoteles.Servicios.Interfaces.IEmpleadoServices;
import proyecto.Hoteles.Utilidades.EmpleadoExportPDF;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/empleados")
public class EmpleadoController {

    @Autowired
    private IEmpleadoServices empleadoServices;

    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Page<Empleado> empleados = empleadoServices.BuscarTodosPaginados(pageable);
        model.addAttribute("empleados", empleados);

        int totalPage = empleados.getTotalPages();
        if (totalPage > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPage)
                    .boxed()
                    .collect(Collectors.toList());

            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "empleado/index";
    }

    @GetMapping("/create")
    public String create(Empleado empleado) {
        return "empleado/create";
    }

    @PostMapping("/save")
    public String save(Empleado empleado, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            model.addAttribute("empleado", empleado);
            attributes.addFlashAttribute("error", "No se pudo guardar el empleado debido a un error");
            return "empleado/create";
        }
        empleadoServices.CrearOeditar(empleado);
        attributes.addFlashAttribute("msg", "Empleado creado correctamente");
        return "redirect:/empleados";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model) {
        Empleado empleado = empleadoServices.BuscarPorId(id).orElse(null);
        if (empleado == null) {
            return "redirect:/empleados";
        }
        model.addAttribute("empleado", empleado);
        return "empleado/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Empleado empleado = empleadoServices.BuscarPorId(id).orElse(null);
        if (empleado == null) {
            return "redirect:/empleados";
        }
        model.addAttribute("empleado", empleado);
        return "empleado/edit";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id, Model model) {
        Empleado empleado = empleadoServices.BuscarPorId(id).orElse(null);
        if (empleado == null) {
            return "redirect:/empleados";
        }
        model.addAttribute("empleado", empleado);
        return "empleado/delete";
    }

    @PostMapping("/delete")
    public String delete(Empleado empleado, RedirectAttributes attributes) {
        empleadoServices.EliminarPorId(empleado.getId());
        attributes.addFlashAttribute("msg", "Empleado eliminado correctamente");
        return "redirect:/empleados";
    }
    @GetMapping("/exportarPDF")
    public void exportarEmpleados(HttpServletResponse response) throws IOException {
        // Establece el tipo de contenido de la respuesta como "application/pdf"
        response.setContentType("application/pdf");

        // Crea un objeto SimpleDateFormat para formatear la fecha actual en un formato específico
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");

        // Obtiene la fecha y hora actual formateada como cadena
        String fechaActual = dateFormatter.format(new Date());

        // Define el encabezado "Content-Disposition" de la respuesta
        // Este encabezado indica que el contenido debe mostrarse en línea (en el navegador) y sugiere un nombre de archivo para el PDF
        String cabecera = "Content-Disposition";
        String valor = "inline; filename=Empleados_" + fechaActual + ".pdf";
        response.setHeader(cabecera, valor);

        // Obtiene una lista de todos los empleados desde el servicio de empleados
        List<Empleado> empleados = empleadoServices.ObtenerTodos();
        // Crea una instancia de la clase EmpleadoExportPDF, pasando la lista de empleados como parámetro
        EmpleadoExportPDF exporter = new EmpleadoExportPDF(empleados);
        // Llama al método Exportar para generar el PDF y enviarlo en la respuesta HTTP
        exporter.Exportar(response);
    }

}