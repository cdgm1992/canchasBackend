package infraestructura.base

import com.typesafe.config.ConfigFactory
import dominio.cancha.puerto.dao.CanchaDao
import dominio.cancha.puerto.repositorio.CanchaRepositorio
import infraestructura.cancha.adaptador.dao.CanchaDaoMySql
import infraestructura.cancha.adaptador.repositorio.RepositorioCanchaMySql
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api._
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.inject.{ bind}


import scala.concurrent.{ExecutionContext}

abstract class TestKit extends PlaySpec {
  implicit val ec = ExecutionContext.global
}

abstract class AppTestKit extends TestKit with GuiceOneAppPerSuite {

  final protected val appBuilder = new GuiceApplicationBuilder()

  final val conf: Configuration = Configuration(ConfigFactory.load("application-test.conf"))

  override def fakeApplication(): Application = appBuilder.configure(conf)
    .overrides(
      bind(classOf[CanchaRepositorio]).to(classOf[RepositorioCanchaMySql]),
      bind(classOf[CanchaDao]).to(classOf[CanchaDaoMySql])).build()
}

