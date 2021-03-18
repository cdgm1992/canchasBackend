package dominio.cancha.servicio

import akka.Done
import dominio.cancha.modelo.Cancha
import dominio.modelos.Error
import monix.execution.Scheduler
import org.scalatest.{AsyncWordSpec, MustMatchers}
import org.specs2.control.Properties.aProperty

import scala.concurrent.Await
import scala.concurrent.duration._

class ServicoValidarCreacionCanchaTest extends AsyncWordSpec with MustMatchers {

  val servicio = new ServicioValidarCreacionCancha
  implicit val s: Scheduler = Scheduler.global

  "ServicioValidarCreacionCancha" can {
    "Generar error por validacion" when {
      "el nombre de la mesa a crear es repetido" in {
        val canchas = List(Cancha(1L, "canchaRepetida", 14));
        val canchaRepetida = Cancha(2L, "canchaRepetida", 12)

        val respuesta = servicio.validar(canchaRepetida, canchas).value
        val resultado = Await.result(respuesta.runToFuture, 2 seconds)

        resultado.isLeft mustBe true
        resultado match {
          case Right(_) => fail("resultado inesperado")
          case Left(error) => error.asInstanceOf[Error].mensaje mustBe NombreRepetido.mensaje
        }
      }

      "el número de jugadores máximo de la cancha es impar" in {
        val canchas = List(Cancha(1L, "canchaExistente", 14));
        val canchaJugadoresImpar = Cancha(2L, "canchaJugadoresImpar", 11)

        val respuesta = servicio.validar(canchaJugadoresImpar, canchas).value
        val resultado = Await.result(respuesta.runToFuture, 2 seconds)

        resultado.isLeft mustBe true
        resultado match {
          case Right(_) => fail("resultado inesperado")
          case Left(error) => error.asInstanceOf[Error].mensaje mustBe JugadoresImpar.mensaje
        }
      }

      "el número de jugadores máximo de la cancha es menor que 10" in {
        val canchas = List(Cancha(1L, "canchaExistente", 14));
        val canchaJugadoresMenorA10 = Cancha(2L, "canchaJugadoresMenorA10", 8)

        val respuesta = servicio.validar(canchaJugadoresMenorA10, canchas).value
        val resultado = Await.result(respuesta.runToFuture, 2 seconds)

        resultado.isLeft mustBe true
        resultado match {
          case Right(_) => fail("resultado inesperado")
          case Left(error) => error.asInstanceOf[Error].mensaje mustBe JugadoresMenorADiez.mensaje
        }
      }

      "el número de jugadores máximo de la cancha es menor que 10 e impar" in {
        val canchas = List(Cancha(1L, "canchaExistente", 14));
        val canchaJugadoresMenorA10eImpar = Cancha(2L, "canchaJugadoresMenorA10eImpar", 9)

        val respuesta = servicio.validar(canchaJugadoresMenorA10eImpar, canchas).value
        val resultado = Await.result(respuesta.runToFuture, 2 seconds)

        resultado.isLeft mustBe true
        resultado match {
          case Right(_) => fail("resultado inesperado")
          case Left(error) => error.asInstanceOf[Error].mensaje mustBe (JugadoresMenorADiez.mensaje concat "," concat JugadoresImpar.mensaje)
        }
      }

      "el número de jugadores máximo de la cancha es menor que 10 y nombre cancha repetido" in {
        val canchas = List(Cancha(1L, "canchaRepetida", 14));
        val canchaJugadoresMenorA10yNombreRepetido = Cancha(2L, "canchaRepetida", 8)

        val respuesta = servicio.validar(canchaJugadoresMenorA10yNombreRepetido, canchas).value
        val resultado = Await.result(respuesta.runToFuture, 2 seconds)

        resultado.isLeft mustBe true
        resultado match {
          case Right(_) => fail("resultado inesperado")
          case Left(error) => error.asInstanceOf[Error].mensaje mustBe (JugadoresMenorADiez.mensaje concat "," concat NombreRepetido.mensaje)
        }
      }

      "el número de jugadores máximo de la cancha es menor que 10 e impar, y nombre cancha repetido" in {
        val canchas = List(Cancha(1L, "canchaRepetida", 14));
        val canchaJugadoresMenorA10ImparyNombreRepetido = Cancha(2L, "canchaRepetida", 9)

        val respuesta = servicio.validar(canchaJugadoresMenorA10ImparyNombreRepetido, canchas).value
        val resultado = Await.result(respuesta.runToFuture, 2 seconds)

        resultado.isLeft mustBe true
        resultado match {
          case Right(_) => fail("resultado inesperado")
          case Left(error) => error.asInstanceOf[Error].mensaje mustBe (JugadoresMenorADiez.mensaje concat "," concat JugadoresImpar.mensaje concat "," concat NombreRepetido.mensaje)
        }
      }

      "el número de jugadores máximo de la cancha es impar y nombre cancha repetido" in {
        val canchas = List(Cancha(1L, "canchaRepetida", 14));
        val canchaJugadoresMenorA10yNombreRepetido = Cancha(2L, "canchaRepetida", 11)

        val respuesta = servicio.validar(canchaJugadoresMenorA10yNombreRepetido, canchas).value
        val resultado = Await.result(respuesta.runToFuture, 2 seconds)

        resultado.isLeft mustBe true
        resultado match {
          case Right(_) => fail("resultado inesperado")
          case Left(error) => error.asInstanceOf[Error].mensaje mustBe (JugadoresImpar.mensaje concat "," concat NombreRepetido.mensaje)
        }
      }
    }

    "Pasar todas las validaciones" when {
      "el nombre de la cancha a valdiar no existe, el numero de jugadores de la cancha a validar es par y mayor a 10" in {
        val canchas = List(Cancha(1L, "canchaRepetida", 14));
        val canchaAValidar = Cancha(2L, "canchaAValidar", 12)

        val respuesta = servicio.validar(canchaAValidar, canchas).value
        val resultado = Await.result(respuesta.runToFuture, 2 seconds)

        resultado.isRight mustBe true
        resultado.toOption.isDefined mustBe true
        resultado mustBe Right(Done)

      }
      "el nombre de la cancha a valdiar no existe, el numero de jugadores de la cancha a validar es par e igual a 10" in {
        val canchas = List(Cancha(1L, "canchaRepetida", 14));
        val canchaAValidar = Cancha(2L, "canchaAValidar", 10)

        val respuesta = servicio.validar(canchaAValidar, canchas).value
        val resultado = Await.result(respuesta.runToFuture, 2 seconds)

        resultado.isRight mustBe true
        resultado.toOption.isDefined mustBe true
        resultado mustBe Right(Done)
      }
    }
  }

}
