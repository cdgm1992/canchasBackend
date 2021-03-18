package infraestructura.cancha.adaptador.repositorio

import akka.Done
import cats.data.EitherT
import cats.implicits.catsSyntaxEitherId
import dominio.cancha.modelo.Cancha
import dominio.cancha.puerto.repositorio.CanchaRepositorio
import dominio.modelos.{Error, Infraestructura}
import dominio.modelos.Tipos.EitherTResult
import infraestructura.cancha.adaptador.CanchaTableMapper.canchas
import monix.eval.Task
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class RepositorioCanchaMySql @Inject()(val dbConfigProvider: DatabaseConfigProvider)(
  implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] with CanchaRepositorio {
  import profile.api._

  override def crear(cancha: Cancha): EitherTResult[Cancha] =  EitherT {
    val query = canchas += cancha
    Task.fromFuture(db.run(query)
      .map(idCancha => Right(cancha.copy(id = idCancha)))
    ).onErrorRecover {
      case err: Throwable => Left(Error(Infraestructura, err.getMessage))
    }
  }

  override def eliminar(id: Long): EitherTResult[Done]=  EitherT {
    val query = canchas.filter(_.id === id).delete
    Task.fromFuture(db.run(query)
      .map(_ => Right(Done))
    ).onErrorRecover {
      case err: Throwable => Left(Error(Infraestructura, err.getMessage))
    }
  }
}
