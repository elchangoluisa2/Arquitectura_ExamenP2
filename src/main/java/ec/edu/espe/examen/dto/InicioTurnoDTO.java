package ec.edu.espe.examen.dto;

import lombok.*;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class InicioTurnoDTO {
    private String codigoCaja;
    private String codigoCajero;
    private List<DenominacionDTO> montoInicial;

}
