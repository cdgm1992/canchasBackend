package infraestructura.cancha.controlador

import monix.execution.Scheduler
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import aplicacion.cancha.cosulta.{ManejadorBuscarCancha, ManejadorListarCancha}
import dominio.cancha.modelo.Cancha
import dominio.modelos.Error._

import javax.inject.Inject

class ConsultaCanchaControlador @Inject()(cc: ControllerComponents,
                                          manejadorListar: ManejadorListarCancha,
                                          manejadorBuscar: ManejadorBuscarCancha) extends AbstractController(cc) {
  implicit val schedulerExecution = Scheduler.global

  def listar() = Action.async { implicit request: Request[AnyContent] =>
    manejadorListar.ejecutar()
      .fold(
      error => BadRequest(Json.toJson(error)),
      _ match {
        case Nil => NoContent
        case canchas => Ok(Json.toJson(canchas)).as("application/json")
      }
    ).runToFuture
  }

  def buscarCancha(id: Long) = Action.async { implicit request: Request[AnyContent] =>
    manejadorBuscar.ejecutar(id)
      .fold(
        error => BadRequest(Json.toJson(error)),
        _ match {
          case cancha: Cancha => Ok(Json.toJson(cancha)).as("application/json")
        }
      ).runToFuture
  }
}
