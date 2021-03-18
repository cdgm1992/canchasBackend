package infraestructura.cancha.controlador

import aplicacion.cancha.comando.ComandoCancha
import controllers.Assets.OK
import dominio.cancha.modelo.Cancha
import infraestructura.base.AppTestKit
import org.scalatest.{BeforeAndAfter, MustMatchers}
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.test.FakeRequest
import play.api.test.Helpers.{DELETE, POST, contentAsJson, defaultAwaitTimeout, route, status, writeableOf_AnyContentAsEmpty, writeableOf_AnyContentAsJson}

class ControladorCanchaComandoTest extends AppTestKit with  MustMatchers with BeforeAndAfter {

  "ConsultaCanchaControlador" can {
    "guardar cancha" when {
      "se invoca el servicio" in {
        val comando = ComandoCancha("cancha 1", 12)
        val requestGuardar = FakeRequest(POST, "/canchas").withJsonBody(Json.toJson(comando))

        val responseGuardar = route(app, requestGuardar).get

        contentAsJson(responseGuardar).validate[Cancha] match {
          case JsError(errors) => fail(s"respuesta inesperada  ${errors.map(s => s" path ${s._1} ${s._2}")}")
          case JsSuccess(value, path) => value.nombre mustBe "cancha 1"
        }
      }
    }

    "eliminar cancha" when {
      "se invoca el serviciod e eliminar" in {
        val request = FakeRequest(DELETE, "/canchas/1")

        val response = route(app, request).get

        status(response) mustBe (OK)
      }
    }
  }

}
