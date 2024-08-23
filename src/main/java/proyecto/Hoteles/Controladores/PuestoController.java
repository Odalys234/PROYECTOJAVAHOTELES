package proyecto.Hoteles.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import proyecto.Hoteles.Entidades.Puesto;
import proyecto.Hoteles.Servicios.Interfaces.IPuestoServices;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/puestos")
public class PuestoController {

    @Autowired
    private IPuestoServices puestoServices;

    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Page<Puesto> puestos = puestoServices.BuscarTodosPaginados(pageable);
        model.addAttribute("puestos", puestos);

        int totalPage = puestos.getTotalPages();
        if (totalPage > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPage)
                    .boxed()
                    .collect(Collectors.toList());

            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "puesto/index";
    }

    @GetMapping("/create")
    public String create(Puesto puesto) {
        return "puesto/create";
    }

    @PostMapping("/save")
    public String save(Puesto puesto, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            model.addAttribute("puesto", puesto);
            attributes.addFlashAttribute("error", "No se pudo guardar el puesto debido a un error");
            return "puesto/create";
        }
        puestoServices.CrearOeditar(puesto);
        attributes.addFlashAttribute("msg", "Puesto creado correctamente");
        return "redirect:/puestos";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model) {
        Puesto puesto = puestoServices.BuscarPorId(id).orElse(null);
        if (puesto == null) {
            return "redirect:/puestos";
        }
        model.addAttribute("puesto", puesto);
        return "puesto/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Puesto puesto = puestoServices.BuscarPorId(id).orElse(null);
        if (puesto == null) {
            return "redirect:/puestos";
        }
        model.addAttribute("puesto", puesto);
        return "puesto/edit";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id, Model model) {
        Puesto puesto = puestoServices.BuscarPorId(id).orElse(null);
        if (puesto == null) {
            return "redirect:/puestos";
        }
        model.addAttribute("puesto", puesto);
        return "puesto/delete";
    }

    @PostMapping("/delete")
    public String delete(Puesto puesto, RedirectAttributes attributes) {
        puestoServices.EliminarPorId(puesto.getId());
        attributes.addFlashAttribute("msg", "Puesto eliminado correctamente");
        return "redirect:/puestos";
    }
}
