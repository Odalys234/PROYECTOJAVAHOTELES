package proyecto.Hoteles.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import proyecto.Hoteles.Entidades.Habitacion;
import proyecto.Hoteles.Servicios.Interfaces.IHabitacionServices;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/habitaciones")
public class HabitacionController {

    @Value("${spring.upload.path}")
    private String uploadPath;

    @Autowired
    private IHabitacionServices habitacionServices;

    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(6);
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Page<Habitacion> habitaciones = habitacionServices.BuscarTodosPaginados(pageable);
        model.addAttribute("habitaciones", habitaciones);

        int totalPage = habitaciones.getTotalPages();
        if (totalPage > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPage)
                    .boxed()
                    .collect(Collectors.toList());

            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "habitacion/index";
    }

    @GetMapping("/create")
    public String create(Habitacion habitacion) {
        return "habitacion/create";
    }

    @PostMapping("/save")
    public String save(@RequestParam("file") MultipartFile file, Habitacion habitacion, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            model.addAttribute("habitacion", habitacion);
            attributes.addFlashAttribute("error", "No se pudo guardar la habitación debido a un error en los datos.");
            return "habitacion/create";
        }

        try {
            // Manejo de la carga de archivos
            if (!file.isEmpty()) {
                // Generar un nombre único para el archivo
                String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(uploadPath).resolve(uniqueFileName);
                Files.copy(file.getInputStream(), filePath);

                // Guardar la URL de la imagen en la base de datos
                habitacion.setImagenUrl(uniqueFileName);
            }

            habitacionServices.CrearOeditar(habitacion);
            attributes.addFlashAttribute("msg", "Habitación creada correctamente.");
        } catch (IOException e) {
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Error al guardar la imagen.");
            return "habitacion/create";
        }

        return "redirect:/habitaciones";
    }

    @PostMapping("/update")
    public String update(@RequestParam("file") MultipartFile file, Habitacion habitacion, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            model.addAttribute("habitacion", habitacion);
            attributes.addFlashAttribute("error", "No se pudo actualizar la habitación debido a un error en los datos.");
            return "habitacion/edit";
        }

        try {
            // Solo actualiza la imagen si se ha subido una nueva
            if (!file.isEmpty()) {
                // Generar un nombre único para el archivo
                String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(uploadPath).resolve(uniqueFileName);
                Files.copy(file.getInputStream(), filePath);

                // Guardar la URL de la nueva imagen en la base de datos
                habitacion.setImagenUrl(uniqueFileName);
            }

            habitacionServices.CrearOeditar(habitacion);
            attributes.addFlashAttribute("msg", "Habitación actualizada correctamente.");
        } catch (IOException e) {
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Error al actualizar la imagen.");
            return "habitacion/edit";
        }

        return "redirect:/habitaciones";
    }


    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model) {
        Habitacion habitacion = habitacionServices.BuscarPorId(id).orElse(null);
        if (habitacion == null) {
            return "redirect:/habitaciones";
        }
        model.addAttribute("habitacion", habitacion);
        return "habitacion/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Habitacion habitacion = habitacionServices.BuscarPorId(id).orElse(null);
        if (habitacion == null) {
            return "redirect:/habitaciones";
        }
        model.addAttribute("habitacion", habitacion);
        return "habitacion/edit";
    }



    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id, Model model) {
        Habitacion habitacion = habitacionServices.BuscarPorId(id).orElse(null);
        if (habitacion == null) {
            return "redirect:/habitaciones";
        }
        model.addAttribute("habitacion", habitacion);
        return "habitacion/delete";
    }

    @PostMapping("/delete")
    public String delete(Habitacion habitacion, RedirectAttributes attributes) {
        habitacionServices.EliminarPorId(habitacion.getId());
        attributes.addFlashAttribute("msg", "Habitación eliminada correctamente");
        return "redirect:/habitaciones";
    }
}
