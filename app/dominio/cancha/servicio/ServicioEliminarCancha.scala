package dominio.cancha.servicio

import akka.Done
import dominio.cancha.puerto.dao.CanchaDao
import dominio.cancha.puerto.repositorio.CanchaRepositorio
import dominio.modelos.Tipos.EitherTResult

class ServicioEliminarCancha {
    def ejecutar(id: Long)(repositorio: CanchaRepositorio, dao: CanchaDao): EitherTResult[Done] = for {
      _ <- dao.findById(id)
      elimando <- repositorio.eliminar(id)
    } yield elimando
}
