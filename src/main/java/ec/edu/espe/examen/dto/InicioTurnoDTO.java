package ec.edu.espe.examen.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class InicioTurnoDTO {
    private String codigoCaja;
    private String codigoCajero;
    private List<DenominacionDTO> montoInicial;
}
