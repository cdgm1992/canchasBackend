package infraestructura.cancha.adaptador.dao

import cats.data.EitherT
import dominio.cancha.modelo.Cancha
import dominio.cancha.puerto.dao.CanchaDao
import dominio.modelos.Tipos.EitherTResult
import dominio.modelos.{Dominio, Error, Infraestructura}
import infraestructura.cancha.adaptador.CanchaTableMapper.canchas
import monix.eval.Task
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class CanchaDaoMySql @Inject()(val dbConfigProvider: DatabaseConfigProvider)
                              (implicit executionContext: ExecutionContext) extends CanchaDao with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  override def listar(): EitherTResult[List[Cancha]] = {
    val query = canchas.result
    EitherT(
      Task.fromFuture(
        db.run(query)
          .map(x => Right(x.toList))
          .recover {
            case _: Throwable => Left(Error(Dominio, s"Error al consultar canchas"))
          }
      )
    )
  }

  override def findById(id: Long): EitherTResult[Cancha] = {
    val query = canchas.filter(cancha => cancha.id === id).result
    EitherT(
      Task.fromFuture(
        db.run(query)
          .map(x => Right(x.head))
          .recover {
            case _: Throwable => Left(Error(Dominio, s"No existe la cancha con id: ${id}"))
          }
      )
    )
  }
}

