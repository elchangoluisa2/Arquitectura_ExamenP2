package ec.edu.espe.examen.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InicioTurnoDTO {
    private String codigoCaja;
    private String codigoCajero;
    private List<DenominacionDTO> montoInicial;

    public InicioTurnoDTO() {
    }

    public String getCodigoCaja() {
        return codigoCaja;
    }

    public void setCodigoCaja(String codigoCaja) {
        this.codigoCaja = codigoCaja;
    }

    public String getCodigoCajero() {
        return codigoCajero;
    }

    public void setCodigoCajero(String codigoCajero) {
        this.codigoCajero = codigoCajero;
    }

    public List<DenominacionDTO> getMontoInicial() {
        return montoInicial;
    }

    public void setMontoInicial(List<DenominacionDTO> montoInicial) {
        this.montoInicial = montoInicial;
    }
}
