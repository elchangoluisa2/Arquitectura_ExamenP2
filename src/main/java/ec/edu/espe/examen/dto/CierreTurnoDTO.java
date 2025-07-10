package ec.edu.espe.examen.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CierreTurnoDTO {
    private List<DenominacionDTO> montoFinal;
}
