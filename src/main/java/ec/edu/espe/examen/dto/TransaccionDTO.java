package ec.edu.espe.examen.dto;

import lombok.Data;

import java.util.List;

@Data
public class TransaccionDTO {
    private String tipoTransaccion;
    private List<DenominacionDTO> denominaciones;

    public TransaccionDTO() {
    }

    public String getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(String tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public List<DenominacionDTO> getDenominaciones() {
        return denominaciones;
    }

    public void setDenominaciones(List<DenominacionDTO> denominaciones) {
        this.denominaciones = denominaciones;
    }
}
