package dominio.cancha.puerto.repositorio

import akka.Done
import dominio.cancha.modelo.Cancha
import dominio.modelos.Tipos.EitherTResult

trait CanchaRepositorio {
  def crear(cancha: Cancha) : EitherTResult[Cancha]
  def eliminar(id: Long): EitherTResult[Done]
}
