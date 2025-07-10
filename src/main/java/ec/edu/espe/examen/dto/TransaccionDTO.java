package ec.edu.espe.examen.dto;

import lombok.Data;

import java.util.List;

@Data
public class TransaccionDTO {
    private String tipoTransaccion;
    private List<DenominacionDTO> denominaciones;
}
