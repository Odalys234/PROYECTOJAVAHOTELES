package proyecto.Hoteles.Entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "habitacion_id")
    private Habitacion habitacion;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @NotNull(message = "La fecha de entrada es obligatoria")
    private LocalDateTime fechaEntrada;

    @NotNull(message = "La fecha de salida es obligatoria")
    private LocalDateTime fechaSalida;

    private BigDecimal montoTotal;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public @NotNull(message = "La fecha de entrada es obligatoria") LocalDateTime getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(@NotNull(message = "La fecha de entrada es obligatoria") LocalDateTime fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public @NotNull(message = "La fecha de salida es obligatoria") LocalDateTime getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(@NotNull(message = "La fecha de salida es obligatoria") LocalDateTime fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }
}
