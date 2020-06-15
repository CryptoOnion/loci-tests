package demo.mixin

import loci.transmitter.rescala._
import loci.serializer.upickle._
import rescala.default._
import loci._

//@multitier trait FakeCommand extends Command {
//  self: Monitoring =>
//
//  override val shutdown: Var[Boolean] on ControlIssuer = Var(false)
//
//  def main(): Unit on Sender = {
//    shutdown.set(true)
//  }
//}

@multitier trait Command {
  self: Monitoring =>

  @peer type Receiver <: Monitor {
    type Tie <: Multiple[Sender] with Multiple[Monitored]
  }

  @peer type Sender <: {
    type Tie <: Single[Receiver] with Single[Monitor]
  }

  val dataSource: Signal[Int] on Receiver = connected

  val commandOutput: Signal[String] on Sender = dataSource.asLocal.map("Connected: " + _)
  val shutdown: Signal[Boolean] on Sender = Var(false)
}
