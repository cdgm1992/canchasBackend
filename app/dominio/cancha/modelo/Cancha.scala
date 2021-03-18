package dominio.cancha.modelo

import play.api.libs.json.Json

final case class Cancha(id: Long , nombre: String, numeroMaximoJugadores: Int)

object Cancha {
  implicit  val canchaJson = Json.format[Cancha]
}

