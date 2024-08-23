package proyecto.Hoteles.Entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "puestos")
public class Puesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre del puesto es obligatorio")
    private String nombre;

    @NotBlank(message = "La descripción del puesto es obligatoria")
    private String descripcion;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public @NotBlank(message = "El nombre del puesto es obligatorio") String getNombre() {
        return nombre;
    }

    public void setNombre(@NotBlank(message = "El nombre del puesto es obligatorio") String nombre) {
        this.nombre = nombre;
    }

    public @NotBlank(message = "La descripción del puesto es obligatoria") String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(@NotBlank(message = "La descripción del puesto es obligatoria") String descripcion) {
        this.descripcion = descripcion;
    }
}
