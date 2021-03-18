package infraestructura.cancha.adaptador

import slick.jdbc.MySQLProfile.api._
import dominio.cancha.modelo.Cancha

class CanchaTableMapper(tag : Tag) extends  Table[Cancha](tag,"canchas"){
  def id = column[Long]("id", O.PrimaryKey,O.AutoInc)
  def nombre = column[String]("nombre")
  def numeroMaximoJugadores = column[Int]("numero_max_jugadore")


  def * = (id, nombre, numeroMaximoJugadores) <> ((Cancha.apply _).tupled, Cancha.unapply)
}

object CanchaTableMapper {
  var canchas = TableQuery[CanchaTableMapper]
}

