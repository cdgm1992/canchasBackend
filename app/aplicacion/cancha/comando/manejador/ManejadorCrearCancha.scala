package aplicacion.cancha.comando.manejador

import aplicacion.cancha.comando.ComandoCancha
import aplicacion.cancha.comando.fabrica.FabricaCancha
import dominio.cancha.modelo.Cancha
import dominio.cancha.servicio.{ServicioCrearCancha, ServicioValidarCreacionCancha}
import dominio.modelos.Tipos.EitherTResult
import infraestructura.cancha.adaptador.dao.CanchaDaoMySql
import infraestructura.cancha.adaptador.repositorio.RepositorioCanchaMySql

import javax.inject.Inject

class ManejadorCrearCancha @Inject()(servicioCrearCancha: ServicioCrearCancha,
                                     dao: CanchaDaoMySql, repositorio: RepositorioCanchaMySql,
                                     servicioValidacion: ServicioValidarCreacionCancha) {

  def ejecutar(comando: ComandoCancha) :EitherTResult[Cancha] = {
    val cancha = FabricaCancha.crear(comando)
    servicioCrearCancha.ejecutar(cancha)(dao,repositorio,servicioValidacion)
  }
}
