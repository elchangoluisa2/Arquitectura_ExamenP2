package ec.edu.espe.examen.repositorio;

import ec.edu.espe.examen.modelo.TurnoCaja;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TurnoCajaRepositorio extends MongoRepository<TurnoCaja, String> {
    Optional<TurnoCaja> findByCodigoCajaAndCodigoCajeroAndEstado(String codigoCaja, String codigoCajero, String estado);
    Optional<TurnoCaja> findByCodigoTurnoAndEstado(String codigoTurno, String estado);
}
