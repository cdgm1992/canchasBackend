package dominio.cancha.servicio

import dominio.modelos.ValidacionDominio

sealed trait ValidacionDominioCancha extends ValidacionDominio

case object JugadoresImpar extends  ValidacionDominioCancha {
  override def mensaje = s"No es posible crear una cancha para jugadores totales impar"
}

case object JugadoresMenorADiez extends  ValidacionDominioCancha {
  override def mensaje = s"No es posible crear una cancha para jugadores totales menor a Diez"
}

case object NombreRepetido extends  ValidacionDominioCancha {
  override def mensaje = s"No es posible crear una cancha con ese nombre, ya que este se encuentra repetido"
}
