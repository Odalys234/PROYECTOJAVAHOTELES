package proyecto.Hoteles.Entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "empleados")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre del empleado es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido del empleado es obligatorio")
    private String apellido;

    @Email(message = "Ingrese un email válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    @NotNull(message = "El teléfono es obligatorio")
    private String telefono;

    @NotNull(message = "El salario es obligatorio")
    private Double salario;

    @NotBlank(message = "El horario es obligatorio")
    private String horario;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public @NotBlank(message = "El nombre del empleado es obligatorio") String getNombre() {
        return nombre;
    }

    public void setNombre(@NotBlank(message = "El nombre del empleado es obligatorio") String nombre) {
        this.nombre = nombre;
    }

    public @NotBlank(message = "El apellido del empleado es obligatorio") String getApellido() {
        return apellido;
    }

    public void setApellido(@NotBlank(message = "El apellido del empleado es obligatorio") String apellido) {
        this.apellido = apellido;
    }

    public @Email(message = "Ingrese un email válido") @NotBlank(message = "El email es obligatorio") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "Ingrese un email válido") @NotBlank(message = "El email es obligatorio") String email) {
        this.email = email;
    }

    public @NotNull(message = "El teléfono es obligatorio") String getTelefono() {
        return telefono;
    }

    public void setTelefono(@NotNull(message = "El teléfono es obligatorio") String telefono) {
        this.telefono = telefono;
    }

    public @NotNull(message = "El salario es obligatorio") Double getSalario() {
        return salario;
    }

    public void setSalario(@NotNull(message = "El salario es obligatorio") Double salario) {
        this.salario = salario;
    }

    public @NotBlank(message = "El horario es obligatorio") String getHorario() {
        return horario;
    }

    public void setHorario(@NotBlank(message = "El horario es obligatorio") String horario) {
        this.horario = horario;
    }
}
