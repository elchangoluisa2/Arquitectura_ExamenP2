package ec.edu.espe.examen.servicio;

import ec.edu.espe.examen.dto.CierreTurnoDTO;
import ec.edu.espe.examen.dto.DenominacionDTO;
import ec.edu.espe.examen.dto.InicioTurnoDTO;
import ec.edu.espe.examen.dto.TransaccionDTO;
import ec.edu.espe.examen.modelo.Denominacion;
import ec.edu.espe.examen.modelo.TransaccionTurno;
import ec.edu.espe.examen.modelo.TurnoCaja;
import ec.edu.espe.examen.repositorio.TransaccionTurnoRepositorio;
import ec.edu.espe.examen.repositorio.TurnoCajaRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TurnoServicio {
    private final TurnoCajaRepositorio turnoCajaRepositorio;
    private final TransaccionTurnoRepositorio transaccionTurnoRepositorio;

    public TurnoServicio(TurnoCajaRepositorio turnoCajaRepositorio, TransaccionTurnoRepositorio transaccionTurnoRepositorio) {
        this.turnoCajaRepositorio = turnoCajaRepositorio;
        this.transaccionTurnoRepositorio = transaccionTurnoRepositorio;
    }

    @Transactional
    public TurnoCaja iniciarTurno(InicioTurnoDTO inicioTurnoDTO) {
        this.turnoCajaRepositorio.findByCodigoCajaAndCodigoCajeroAndEstado(inicioTurnoDTO.getCodigoCaja(), inicioTurnoDTO.getCodigoCajero(), "ABIERTO")
                .ifPresent(t -> {
                    throw new RuntimeException("Ya existe un turno abierto para la caja y cajero especificados.");
                });

        TurnoCaja turno = new TurnoCaja();
        turno.setCodigoCaja(inicioTurnoDTO.getCodigoCaja());
        turno.setCodigoCajero(inicioTurnoDTO.getCodigoCajero());
        turno.setCodigoTurno(generarCodigoTurno(inicioTurnoDTO.getCodigoCaja(), inicioTurnoDTO.getCodigoCajero()));
        turno.setInicioTurno(LocalDateTime.now());

        BigDecimal montoInicial = calcularTotalDenominaciones(inicioTurnoDTO.getMontoInicial());
        turno.setMontoInicial(montoInicial);
        turno.setEstado("ABIERTO");
        turno.setTotalTransacciones(BigDecimal.ZERO);

        return this.turnoCajaRepositorio.save(turno);
    }

    @Transactional
    public void procesarTransaccion(String codigoTurno, TransaccionDTO transaccionDTO) {
        TurnoCaja turno = this.turnoCajaRepositorio.findByCodigoTurnoAndEstado(codigoTurno, "ABIERTO")
                .orElseThrow(() -> new RuntimeException("No se encontró un turno abierto con el código: " + codigoTurno));

        TransaccionTurno transaccion = new TransaccionTurno();
        transaccion.setCodigoCaja(turno.getCodigoCaja());
        transaccion.setCodigoCajero(turno.getCodigoCajero());
        transaccion.setCodigoTurno(codigoTurno);
        transaccion.setTipoTransaccion(transaccionDTO.getTipoTransaccion());
        transaccion.setFechaHora(LocalDateTime.now());
        List<Denominacion> denominaciones = transaccionDTO.getDenominaciones().stream().map(this::mapDenominacion).collect(Collectors.toList());
        transaccion.setDenominacion(denominaciones);
        BigDecimal montoTransaccion = calcularTotal(denominaciones);
        transaccion.setMontoTotal(montoTransaccion);

        this.transaccionTurnoRepositorio.save(transaccion);

        BigDecimal montoActualizado;
        if ("INGRESO".equals(transaccionDTO.getTipoTransaccion())) {
            montoActualizado = turno.getTotalTransacciones().add(montoTransaccion);
        } else if ("EGRESO".equals(transaccionDTO.getTipoTransaccion())) {
            montoActualizado = turno.getTotalTransacciones().subtract(montoTransaccion);
        } else {
            throw new RuntimeException("Tipo de transacción no válido: " + transaccionDTO.getTipoTransaccion());
        }
        turno.setTotalTransacciones(montoActualizado);
        this.turnoCajaRepositorio.save(turno);
    }

    @Transactional
    public TurnoCaja cerrarTurno(String codigoTurno, CierreTurnoDTO cierreTurnoDTO) {
        TurnoCaja turno = this.turnoCajaRepositorio.findByCodigoTurnoAndEstado(codigoTurno, "ABIERTO")
                .orElseThrow(() -> new RuntimeException("No se encontró un turno abierto con el código: " + codigoTurno));

        BigDecimal totalFinalContado = calcularTotalDenominaciones(cierreTurnoDTO.getMontoFinal());

        turno.setFinTurno(LocalDateTime.now());
        turno.setMontoFinal(totalFinalContado);
        turno.setEstado("CERRADO");

        BigDecimal totalCalculado = turno.getMontoInicial().add(turno.getTotalTransacciones());

        if (totalCalculado.compareTo(totalFinalContado) != 0) {
            BigDecimal diferencia = totalFinalContado.subtract(totalCalculado);
            String tipoDiferencia = diferencia.compareTo(BigDecimal.ZERO) > 0 ? "SOBRANTE" : "FALTANTE";
            turno.setObservacion("ALERTA: CIERRE CON DIFERENCIA. " + tipoDiferencia + ": " + diferencia.abs());
        } else {
            turno.setObservacion("CIERRE CUADRADO");
        }

        return this.turnoCajaRepositorio.save(turno);
    }

    private String generarCodigoTurno(String codigoCaja, String codigoCajero) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String fecha = LocalDate.now().format(formatter);
        return String.format("%s-%s-%s", codigoCaja, codigoCajero, fecha);
    }

    private Denominacion mapDenominacion(DenominacionDTO dto) {
        BigDecimal monto = new BigDecimal(dto.getValor() * dto.getCantidad());
        return Denominacion.builder()
                .valor(dto.getValor())
                .cantidad(dto.getCantidad())
                .monto(monto)
                .build();
    }

    private BigDecimal calcularTotal(List<Denominacion> denominaciones) {
        return denominaciones.stream()
                .map(Denominacion::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularTotalDenominaciones(List<DenominacionDTO> denominaciones) {
        return denominaciones.stream()
                .map(d -> new BigDecimal(d.getValor()).multiply(new BigDecimal(d.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


}
