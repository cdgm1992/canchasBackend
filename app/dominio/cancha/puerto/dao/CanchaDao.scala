package dominio.cancha.puerto.dao

import dominio.cancha.modelo.Cancha
import dominio.modelos.Tipos.EitherTResult

trait CanchaDao {
  def listar() : EitherTResult[List[Cancha]]
  def findById(id: Long): EitherTResult[Cancha]
}
