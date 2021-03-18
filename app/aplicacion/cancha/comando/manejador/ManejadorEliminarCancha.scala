package aplicacion.cancha.comando.manejador

import akka.Done
import dominio.cancha.servicio.ServicioEliminarCancha
import dominio.modelos.Tipos.EitherTResult
import infraestructura.cancha.adaptador.dao.CanchaDaoMySql
import infraestructura.cancha.adaptador.repositorio.RepositorioCanchaMySql

import javax.inject.Inject

class ManejadorEliminarCancha @Inject()(servicioEliminarCancha: ServicioEliminarCancha,
                                        repositorio: RepositorioCanchaMySql, dao: CanchaDaoMySql) {

  def ejecutar(id: Long) :EitherTResult[Done] = servicioEliminarCancha.ejecutar(id)(repositorio,dao)
}
