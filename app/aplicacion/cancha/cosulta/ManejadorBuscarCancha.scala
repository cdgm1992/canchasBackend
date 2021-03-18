package aplicacion.cancha.cosulta

import dominio.cancha.modelo.Cancha
import dominio.cancha.puerto.dao.CanchaDao
import dominio.modelos.Tipos.EitherTResult
import infraestructura.cancha.adaptador.dao.CanchaDaoMySql

import javax.inject.Inject

class ManejadorBuscarCancha @Inject()(dao :CanchaDaoMySql) {

    def ejecutar(idCancha: Long) :EitherTResult[Cancha] = dao.findById(idCancha)

}
