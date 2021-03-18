package aplicacion.cancha.comando

import play.api.libs.json.Json

case class ComandoCancha(nombre: String, numeroMaximoJugadores: Int)

object  ComandoCancha {
  implicit val comandoCanchaFormater = Json.format[ComandoCancha]
}