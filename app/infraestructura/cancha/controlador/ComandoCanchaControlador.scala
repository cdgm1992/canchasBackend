package infraestructura.cancha.controlador

import aplicacion.cancha.comando.ComandoCancha
import aplicacion.cancha.comando.manejador.{ManejadorCrearCancha, ManejadorEliminarCancha}
import monix.execution.Scheduler
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

import javax.inject.Inject
import scala.concurrent.Future

class ComandoCanchaControlador @Inject()(cc: ControllerComponents,
                                         manejadorCrearCancha: ManejadorCrearCancha,
                                         manejadorEliminarCancha: ManejadorEliminarCancha) extends AbstractController(cc) {

  implicit val schedulerExecution = Scheduler.global

  def crear = Action(parse.json).async { request => {
    request.body.validate[ComandoCancha].fold(
      _ => Future.successful(BadRequest),
      canchaComando => {
        manejadorCrearCancha.ejecutar(canchaComando).fold(
          error => BadRequest(Json.toJson(error)),
          cancha => Ok(Json.toJson(cancha))
        )
      }.runToFuture
    )
  }
  }

  def eliminar(id: Long) = Action.async { _ =>
    manejadorEliminarCancha.ejecutar(id).fold(
      error => BadRequest(Json.toJson(error)),
      _ => Ok
    ).runToFuture
  }
}

