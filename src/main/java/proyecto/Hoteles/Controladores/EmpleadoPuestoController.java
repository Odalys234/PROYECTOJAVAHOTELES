package proyecto.Hoteles.Controladores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import proyecto.Hoteles.Entidades.Empleado;
import proyecto.Hoteles.Entidades.Puesto;
import proyecto.Hoteles.Entidades.empleadoPuesto;
import proyecto.Hoteles.Servicios.Implementaciones.PuestoServices;
import proyecto.Hoteles.Servicios.Interfaces.IEmpleadoPuestoServices;
import proyecto.Hoteles.Servicios.Interfaces.IEmpleadoServices;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/empleadoPuesto")
public class EmpleadoPuestoController {

    @Autowired
    private IEmpleadoPuestoServices empleadoPuestoServices;

    @Autowired
    private IEmpleadoServices empleadoServices;

    @Autowired
    private PuestoServices puestoServices;

    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Page<empleadoPuesto> empleadoPuestos = empleadoPuestoServices.BuscarTodosPaginados(pageable);
        model.addAttribute("empleadoPuestos", empleadoPuestos);

        int totalPage = empleadoPuestos.getTotalPages();
        if (totalPage > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPage)
                    .boxed()
                    .collect(Collectors.toList());

            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "empleadoPuesto/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("empleadoPuesto", new empleadoPuesto());
        model.addAttribute("empleados", empleadoServices.ObtenerTodos());
        model.addAttribute("puestos", puestoServices.ObtenerTodos());
        return "empleadoPuesto/create";
    }

    @PostMapping("/save")
    public String save(
            @RequestParam Integer empleadoId,
            @RequestParam Integer puestoId,
            RedirectAttributes attributes) {
        Empleado empleado = empleadoServices.BuscarPorId(empleadoId).orElse(null);
        Puesto puesto = puestoServices.BuscarPorId(puestoId).orElse(null);
        if (empleado != null && puesto != null) {
            empleadoPuesto empleadoPuesto = new empleadoPuesto();
            empleadoPuesto.setEmpleado(empleado);
            empleadoPuesto.setPuesto(puesto);
            empleadoPuestoServices.CrearOeditar(empleadoPuesto);
            attributes.addFlashAttribute("msg", "Asignación guardada correctamente");
        }
        return "redirect:/empleadoPuesto";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model) {
        empleadoPuesto empleadoPuesto = empleadoPuestoServices.BuscarPorId(id).orElse(null);
        model.addAttribute("empleadoPuesto", empleadoPuesto);
        return "empleadoPuesto/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        empleadoPuesto empleadoPuesto = empleadoPuestoServices.BuscarPorId(id).orElse(null);
        model.addAttribute("empleados", empleadoServices.ObtenerTodos());
        model.addAttribute("puestos", puestoServices.ObtenerTodos());
        model.addAttribute("empleadoPuesto", empleadoPuesto);
        return "empleadoPuesto/edit";
    }

    @PostMapping("/update")
    public String update(@RequestParam Integer id, @RequestParam Integer empleadoId,
                         @RequestParam Integer puestoId,
                         RedirectAttributes attributes) {
        Empleado empleado = empleadoServices.BuscarPorId(empleadoId).orElse(null);
        Puesto puesto = puestoServices.BuscarPorId(puestoId).orElse(null);
        if (empleado != null && puesto != null) {
            empleadoPuesto empleadoPuesto = new empleadoPuesto();
            empleadoPuesto.setId(id);
            empleadoPuesto.setEmpleado(empleado);
            empleadoPuesto.setPuesto(puesto);
            empleadoPuestoServices.CrearOeditar(empleadoPuesto);
            attributes.addFlashAttribute("msg", "Asignación modificada correctamente");
        }
        return "redirect:/empleadoPuesto";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id, Model model) {
        empleadoPuesto empleadoPuesto = empleadoPuestoServices.BuscarPorId(id).orElse(null);
        model.addAttribute("empleadoPuesto", empleadoPuesto);
        return "empleadoPuesto/delete";
    }

    @PostMapping("/delete")
    public String delete(empleadoPuesto empleadoPuesto, RedirectAttributes attributes) {
        empleadoPuestoServices.EliminarPorId(empleadoPuesto.getId());
        attributes.addFlashAttribute("msg", "Asignación eliminada correctamente");
        return "redirect:/empleadoPuesto";
    }
}