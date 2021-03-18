package infraestructura.cancha.controlador

import aplicacion.cancha.comando.ComandoCancha
import dominio.cancha.modelo.Cancha
import infraestructura.base.AppTestKit
import org.scalatest.{BeforeAndAfter, MustMatchers}
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.test.FakeRequest
import play.api.test.Helpers.{OK, POST, contentAsJson, defaultAwaitTimeout, route, status, writeableOf_AnyContentAsEmpty, writeableOf_AnyContentAsJson}
import play.test.Helpers


class ControladorCanchaConsultaTest extends AppTestKit with  MustMatchers with BeforeAndAfter {

  "ConsultaCanchaControlador" can {
    "listar las canchas existentes" when {
      "se invoca el servicio de listar" in {
        before()
        val request = FakeRequest(Helpers.GET, "/canchas")
        val response = route(app, request).get

        status(response) mustBe (OK)
        contentAsJson(response).validate[List[Cancha]] match {
          case JsError(errors) => fail(s"respuesta inesperada  ${errors.map(s => s" path ${s._1} ${s._2}")}")
          case JsSuccess(Nil, path) => fail("lista vacia")
          case JsSuccess(value: List[Cancha], path) => value.head.nombre mustBe "cancha creada"
        }
      }
    }

    "consulta cancha por id" when {
      "se invoca el servicio y la cancha existe" in {

        val request = FakeRequest(Helpers.GET, "/canchas/2")
        val response = route(app, request).get

        status(response) mustBe (OK)
        contentAsJson(response).validate[Cancha] match {
          case JsError(errors) => fail(s"respuesta inesperada  ${errors.map(s => s" path ${s._1} ${s._2}")}")
          case JsSuccess(value, path) => value.nombre mustBe "cancha creada"
        }

      }
    }
  }

  private def before(): Unit = {
    val comando = ComandoCancha("cancha creada", 12)
    val requestGuardar = FakeRequest(POST, "/canchas").withJsonBody(Json.toJson(comando))
    val responseGuardar = route(app, requestGuardar).get

    contentAsJson(responseGuardar).validate[Cancha] match {
      case JsError(errors) => fail(s"respuesta inesperada  ${errors.map(s => s" path ${s._1} ${s._2}")}")
      case JsSuccess(value, path) => value.nombre mustBe "cancha creada"
    }
  }
}
