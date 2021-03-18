package dominio.modelos

import cats.data.{EitherT, ValidatedNec}
import dominio.cancha.servicio.ValidacionDominioCancha
import monix.eval.Task

object Tipos {
  type EitherTResult[A] = EitherT[Task, Error, A]
  type ValidationResult[A] = ValidatedNec[ValidacionDominio, A]
}
