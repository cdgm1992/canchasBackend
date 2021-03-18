package dominio.cancha.servicio

import akka.Done
import cats.data.EitherT
import cats.implicits.catsSyntaxEitherId
import dominio.cancha.modelo.Cancha
import dominio.cancha.puerto.dao.CanchaDao
import dominio.cancha.puerto.repositorio.CanchaRepositorio
import dominio.modelos.{Error, Infraestructura}
import monix.eval.Task
import monix.execution.Scheduler
import org.scalatest.{AsyncWordSpec, MustMatchers}
import org.specs2.mock.Mockito

import scala.concurrent.Await
import scala.concurrent.duration._


class SerivioEliminarCanchaTest extends AsyncWordSpec with MustMatchers with Mockito {

  val mockRepoCancha = mock[CanchaRepositorio]
  val mockDaoCancha = mock[CanchaDao]
  val servicio = new ServicioEliminarCancha
  implicit val s: Scheduler = Scheduler.global

  "ServicioEliminarCancha" can {
    "genera error" when {
      "la cancha no existe previamente" in {
        val idUsuario = 1L;
        mockDaoCancha.findById(anyLong) returns EitherT(Task(Error(Infraestructura, "No existe el la cancha con id: 1").asLeft[Cancha]))

        val respuesta = servicio.ejecutar(idUsuario)(mockRepoCancha, mockDaoCancha).value
        val resultado = Await.result(respuesta.runToFuture, 2 seconds)

        resultado.isLeft mustBe true
        resultado match {
          case Right(_) => fail("resultado inesperado")
          case Left(error) => error.asInstanceOf[Error].mensaje mustBe "No existe el la cancha con id: 1"
        }
      }

      "ocurre un error al ejecutar la sentencia en BD" in {
        val idUsuario = 1L;
        mockDaoCancha.findById(anyLong) returns EitherT(Task(Cancha(1L, "cancha1", 12).asRight[Error]))
        val errorEsperado = "error inesperado"
        mockRepoCancha.eliminar(anyLong) returns EitherT(Task(Error(Infraestructura, errorEsperado).asLeft[Done]))

        val respuesta = servicio.ejecutar(idUsuario)(mockRepoCancha, mockDaoCancha).value
        val resultado = Await.result(respuesta.runToFuture, 2 seconds)

        resultado.isLeft mustBe true
        resultado match {
          case Right(_) => fail("resultado inesperado")
          case Left(error) => error.asInstanceOf[Error].mensaje mustBe errorEsperado
        }
      }
    }

    "elimina usuario" when {
      "la cancha existe previamente" in {
        val idUsuario = 1L;
        mockDaoCancha.findById(anyLong) returns EitherT(Task(Cancha(1L, "cancha1", 12).asRight[Error]))
        mockRepoCancha.eliminar(anyLong) returns EitherT(Task(Done.asRight[Error]))

        val respuesta = servicio.ejecutar(idUsuario)(mockRepoCancha, mockDaoCancha).value
        val resultado = Await.result(respuesta.runToFuture, 2 seconds)

        resultado.isRight mustBe true
        resultado.toOption.isDefined mustBe true
        resultado.toOption.get mustBe Right(Done)
      }
    }
  }

}
