package ec.edu.espe.examen.controlador;

import ec.edu.espe.examen.dto.CierreTurnoDTO;
import ec.edu.espe.examen.dto.InicioTurnoDTO;
import ec.edu.espe.examen.dto.TransaccionDTO;
import ec.edu.espe.examen.modelo.TurnoCaja;
import ec.edu.espe.examen.servicio.TurnoServicio;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/turnos")
public class TurnoControlador {
    private final TurnoServicio turnoServicio;

    public TurnoControlador(TurnoServicio turnoServicio) {
        this.turnoServicio = turnoServicio;
    }

    @PostMapping("/iniciar")
    public ResponseEntity<TurnoCaja> iniciarTurno(@RequestBody InicioTurnoDTO inicioTurnoDTO) {
        try {
            TurnoCaja turno = this.turnoServicio.iniciarTurno(inicioTurnoDTO);
            return ResponseEntity.ok(turno);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/{codigoTurno}/transaccion")
    public ResponseEntity<Void> procesarTransaccion(@PathVariable String codigoTurno, @RequestBody TransaccionDTO transaccionDTO) {
        try {
            this.turnoServicio.procesarTransaccion(codigoTurno, transaccionDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{codigoTurno}/cerrar")
    public ResponseEntity<TurnoCaja> cerrarTurno(@PathVariable String codigoTurno, @RequestBody CierreTurnoDTO cierreTurnoDTO) {
        try {
            TurnoCaja turno = this.turnoServicio.cerrarTurno(codigoTurno, cierreTurnoDTO);
            return ResponseEntity.ok(turno);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
