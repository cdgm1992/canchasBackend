package dominio.modelos

import play.api.libs.json.{Format, JsString, Json, Reads, Writes}

sealed trait TipoError
final case object Aplicacion extends TipoError
final case object Dominio extends TipoError
final case object Infraestructura extends TipoError

object TipoError {
  implicit lazy val tipoErrorFormat: Format[TipoError] = Format[TipoError](
    Reads[TipoError](j => j.validate[String].map(s => TipoError(s))),
    Writes[TipoError](t => JsString(TipoError.unapply(t).toString)))

  def apply(value: String): TipoError = value match {
    case "Aplicacion" => Aplicacion
    case "Dominio" => Dominio
    case "Infraestructura" => Infraestructura
  }

  def unapply(value: TipoError)= value match {
    case Aplicacion => "Aplicacion"
    case Dominio  => "Dominio"
    case Infraestructura   => "Infraestructura"
  }
}
