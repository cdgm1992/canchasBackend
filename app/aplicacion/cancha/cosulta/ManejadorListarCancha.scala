package aplicacion.cancha.cosulta


import dominio.cancha.modelo.Cancha

import dominio.modelos.Tipos.EitherTResult
import infraestructura.cancha.adaptador.dao.CanchaDaoMySql

import javax.inject.Inject

class ManejadorListarCancha @Inject()(dao :CanchaDaoMySql) {

    def ejecutar() :EitherTResult[List[Cancha]] = dao.listar()

}
