package dominio.cancha.servicio

import akka.Done
import cats.data.EitherT
import cats.data.Validated.Invalid
import cats.implicits.{catsSyntaxValidatedIdBinCompat0, _}
import dominio.cancha.modelo.Cancha
import dominio.modelos.Tipos.{EitherTResult, ValidationResult}
import dominio.modelos.{Dominio, Error}
import monix.eval.Task

import scala.annotation.tailrec

class ServicioValidarCreacionCancha {

  private val CantidadMinimaJugadoresCancha = 10

  private def validarCantidadJugadoresMayorADiez(jugadores: Int): ValidationResult[Unit] =
    if (jugadores >= CantidadMinimaJugadoresCancha) ().validNec else JugadoresMenorADiez.invalidNec

  private def validarCantidadJugadoresTotalImpar(jugadores: Int): ValidationResult[Unit] =
    if (jugadores % 2 == 0) ().validNec else JugadoresImpar.invalidNec

  @tailrec
  private def validarNombreNoRepetido(listaCanchas: List[Cancha], nombre: String): ValidationResult[Unit] =
    listaCanchas match {
      case Nil => ().validNec
      case head :: tail => if (head.nombre.equals(nombre)) NombreRepetido.invalidNec else validarNombreNoRepetido(tail, nombre)
    }

  private def validarAsync(cancha: Cancha, listCanchas: List[Cancha]): ValidationResult[Unit] = {
    (validarCantidadJugadoresMayorADiez(cancha.numeroMaximoJugadores),
      validarCantidadJugadoresTotalImpar(cancha.numeroMaximoJugadores),
      validarNombreNoRepetido(listCanchas, cancha.nombre)).mapN((_, _, _) => Unit)
  }

  def validar(cancha: Cancha, listCanchas: List[Cancha]): EitherTResult[Done] = {
    EitherT(
      Task.now(
        validarAsync(cancha, listCanchas) match {
           case invalid: Invalid[_] =>
              invalid.fold(
                errores => Left(Error(Dominio, errores.map(_.mensaje).mkString_(","))),
                _ => Right(Done)
              )
          case _ => Right(Done)
        }
      )
    )
  }

}

