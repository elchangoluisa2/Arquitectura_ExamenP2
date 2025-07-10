package ec.edu.espe.examen.controlador;

import ec.edu.espe.examen.dto.CierreTurnoDTO;
import ec.edu.espe.examen.dto.InicioTurnoDTO;
import ec.edu.espe.examen.dto.TransaccionDTO;
import ec.edu.espe.examen.modelo.TurnoCaja;
import ec.edu.espe.examen.servicio.TurnoServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/turnos")
@Tag(name = "Gestión de Turnos de Caja", description = "API para manejar la apertura, transacciones y cierre de turnos de caja.")
public class TurnoControlador {
    private final TurnoServicio turnoServicio;

    public TurnoControlador(TurnoServicio turnoServicio) {
        this.turnoServicio = turnoServicio;
    }

    @Operation(summary = "Iniciar un nuevo turno de caja", description = "Crea un nuevo turno para un cajero en una caja específica, registrando el monto inicial de efectivo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Turno iniciado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TurnoCaja.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, ej. ya existe un turno abierto para el cajero/caja",
                    content = @Content)
    })
    @PostMapping("/iniciar")
    public ResponseEntity<TurnoCaja> iniciarTurno(@RequestBody InicioTurnoDTO inicioTurnoDTO) {
        try {
            TurnoCaja turno = this.turnoServicio.iniciarTurno(inicioTurnoDTO);
            return ResponseEntity.ok(turno);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    @Operation(summary = "Procesar una transacción", description = "Registra una transacción de INGRESO o EGRESO de efectivo para un turno abierto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transacción procesada exitosamente", content = @Content),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, ej. tipo de transacción incorrecto", content = @Content),
            @ApiResponse(responseCode = "404", description = "Turno no encontrado o ya está cerrado", content = @Content)
    })
    @PostMapping("/{codigoTurno}/transaccion")
    public ResponseEntity<Void> procesarTransaccion(@PathVariable String codigoTurno, @RequestBody TransaccionDTO transaccionDTO) {
        try {
            this.turnoServicio.procesarTransaccion(codigoTurno, transaccionDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Cerrar un turno de caja", description = "Finaliza un turno, comparando el efectivo final contado con el saldo calculado. Genera una alerta si hay diferencias.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Turno cerrado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TurnoCaja.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content),
            @ApiResponse(responseCode = "404", description = "Turno no encontrado o ya está cerrado", content = @Content)
    })
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
