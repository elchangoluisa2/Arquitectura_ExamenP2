package ec.edu.espe.examen.modelo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Denominacion {
    private Integer valor;
    private Integer cantidad;
    private BigDecimal monto;
}
