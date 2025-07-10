package ec.edu.espe.examen.dto;

import lombok.Data;

import java.util.List;

@Data
public class CierreTurnoDTO {
    private List<DenominacionDTO> montoFinal;

    public List<DenominacionDTO> getMontoFinal() {
        return montoFinal;
    }

    public void setMontoFinal(List<DenominacionDTO> montoFinal) {
        this.montoFinal = montoFinal;
    }
}
