package aplicacion.cancha.comando.fabrica

import aplicacion.cancha.comando.ComandoCancha
import dominio.cancha.modelo.Cancha

object FabricaCancha {
  def crear (comando : ComandoCancha): Cancha = {
    Cancha(id = 0L, nombre = comando.nombre, numeroMaximoJugadores = comando.numeroMaximoJugadores)
  }
}
