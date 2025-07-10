package ec.edu.espe.examen.repositorio;

import ec.edu.espe.examen.modelo.TransaccionTurno;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransaccionTurnoRepositorio extends MongoRepository<TransaccionTurno, String> {
    List<TransaccionTurno> findByCodigoTurno(String codigoTurno);
}
