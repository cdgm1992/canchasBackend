package dominio.modelos

import play.api.libs.json.Json

final case class Error(tipoError: TipoError, mensaje: String)

object Error {
  implicit val formatError = Json.format[Error]
}