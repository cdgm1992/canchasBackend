package dominio.cancha.servicio

import akka.Done
import cats.data.EitherT
import cats.implicits.catsSyntaxEitherId
import dominio.cancha.modelo.Cancha
import dominio.cancha.puerto.dao.CanchaDao
import dominio.cancha.puerto.repositorio.CanchaRepositorio
import dominio.modelos.{Dominio, Error}
import monix.eval.Task
import monix.execution.Scheduler
import org.scalatest.{AsyncWordSpec, MustMatchers}
import org.specs2.mock.Mockito

import scala.concurrent.Await
import scala.concurrent.duration._

class SerivioCrearCanchaTest extends AsyncWordSpec with MustMatchers with Mockito {

  val mockRepoCancha = mock[CanchaRepositorio]
  val mockDaoCancha = mock[CanchaDao]
  val mockServicioValidarCreacion = mock[ServicioValidarCreacionCancha]
  val servicio = new ServicioCrearCancha
  implicit val s: Scheduler = Scheduler.global


  "ServicioCrearCancha" can {
    "genera error" when {
      "no se pueden consultar las canchas existentes" in {
        val canchaACrear = Cancha(2L, "canchaACrear", 12)
        val errorConsultCanchas = "Error al consultar canchas"
        mockDaoCancha.listar() returns EitherT(Task(Error(Dominio, errorConsultCanchas).asLeft[List[Cancha]]))

        val respuesta = servicio.ejecutar(canchaACrear)(mockDaoCancha, mockRepoCancha, mockServicioValidarCreacion).value
        val resultado = Await.result(respuesta.runToFuture, 2 seconds)

        resultado.isLeft mustBe true
        resultado match {
          case Left(error) => error.asInstanceOf[Error].mensaje mustBe errorConsultCanchas
          case _ => fail("resultado inesperado")
        }
      }

      "no pasa las validaciones del servicio de valiadacion" in {
        val canchas = List(Cancha(1L, "canchaExistente", 12));
        val canchaACrear = Cancha(2L, "canchaACrear", 12)
        mockDaoCancha.listar() returns EitherT(Task(canchas.asRight[Error]))
        val errorServicio = "Error del servico de validacion"
        mockServicioValidarCreacion.validar(canchaACrear, canchas) returns EitherT(Task(Error(Dominio, errorServicio).asLeft[Done]))

        val respuesta = servicio.ejecutar(canchaACrear)(mockDaoCancha, mockRepoCancha, mockServicioValidarCreacion).value
        val resultado = Await.result(respuesta.runToFuture, 2 seconds)

        resultado.isLeft mustBe true
        resultado match {
          case Right(_) => fail("resultado inesperado")
          case Left(error) => error.asInstanceOf[Error].mensaje mustBe errorServicio
        }
      }

      "ocurre un error al insertar en BD" in {
        val canchas = List(Cancha(1L, "canchaExistente", 12));
        val canchaACrear = Cancha(2L, "canchaACrear", 12)
        mockDaoCancha.listar() returns EitherT(Task(canchas.asRight[Error]))
        mockServicioValidarCreacion.validar(canchaACrear, canchas) returns EitherT(Task(Done.asRight[Error]))
        val errorInsertandoBD = "error al insertar en BD"
        mockRepoCancha.crear(canchaACrear) returns EitherT(Task(Error(Dominio, errorInsertandoBD).asLeft[Cancha]))

        val respuesta = servicio.ejecutar(canchaACrear)(mockDaoCancha, mockRepoCancha, mockServicioValidarCreacion).value
        val resultado = Await.result(respuesta.runToFuture, 2 seconds)

        resultado.isLeft mustBe true
        resultado match {
          case Right(_) => fail("resultado inesperado")
          case Left(error) => error.asInstanceOf[Error].mensaje mustBe errorInsertandoBD
        }
      }
    }

    "crea cancha" when {
      "pasa servicio de valdacion y no ocurren errores en la persistencia" in {
        val canchas = List(Cancha(1L, "canchaExistente", 12));
        val canchaACrear = Cancha(2L, "canchaACrear", 12)
        mockDaoCancha.listar() returns EitherT(Task(canchas.asRight[Error]))
        mockServicioValidarCreacion.validar(canchaACrear, canchas) returns EitherT(Task(Done.asRight[Error]))
        val errorInsertandoBD = "error al insertar en BD"
        mockRepoCancha.crear(canchaACrear) returns EitherT(Task(canchaACrear.asRight[Error]))

        val respuesta = servicio.ejecutar(canchaACrear)(mockDaoCancha, mockRepoCancha, mockServicioValidarCreacion).value
        val resultado = Await.result(respuesta.runToFuture, 2 seconds)

        resultado.isRight mustBe true
        resultado.toOption.isDefined mustBe true
        resultado.toOption.get mustBe Right(canchaACrear)
      }
    }

  }
}
