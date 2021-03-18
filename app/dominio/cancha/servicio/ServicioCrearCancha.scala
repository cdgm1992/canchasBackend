package dominio.cancha.servicio

import dominio.cancha.modelo.Cancha
import dominio.cancha.puerto.dao.CanchaDao
import dominio.cancha.puerto.repositorio.CanchaRepositorio
import dominio.modelos.Tipos.EitherTResult


class ServicioCrearCancha  {

  def ejecutar(cancha: Cancha)(dao: CanchaDao, repositorio: CanchaRepositorio,
                               servicioValidacion: ServicioValidarCreacionCancha):  EitherTResult[Cancha] = {
      for {
        canchasExistentes <- dao.listar()
        _ <- servicioValidacion.validar(cancha, canchasExistentes)
        canchaCreada <- repositorio.crear(cancha)
      } yield canchaCreada
  }
}


