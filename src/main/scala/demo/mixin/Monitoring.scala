package demo.mixin

import loci.transmitter.rescala._
import loci.serializer.upickle._
import rescala.default._
import loci._

import scala.concurrent.Future

@multitier trait Monitoring {
  self: Command =>

  @peer type Monitor <: Receiver { type Tie <: Multiple[Monitored] }
  @peer type Monitored <: { type Tie <: Single[Monitor] }

  val connected: Signal[Int] on Monitor = placed { Signal.dynamic {
    remote[Monitored].connected().size
  }}

  val forceSave: Event[Unit] on Monitor = placed {
    val testFetch = shutdown.asLocalFromAll
    Evt[Unit]()
  }

//  def monitoredTimedOut(monitored: Remote[Monitored]): Unit on Monitor = {
//
//  }
}
