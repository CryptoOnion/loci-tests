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

@multitier trait Command extends QueryCommand with ControlCommand {
  self: Monitoring =>

  @peer type Receiver <: Oracle with Monitor with Actor {
    type Tie <: Multiple[Sender] with Multiple[QuerySource] with Multiple[Monitored] with Multiple[ControlIssuer]
  }

  @peer type Sender <: QuerySource with ControlIssuer {
    type Tie <: Single[Receiver] with Single[Oracle] with Single[Monitor] with Single[Actor]
  }
}

@multitier trait QueryCommand {
  self: Monitoring with ControlCommand =>

  @peer type Oracle <: Monitor { type Tie <: Multiple[QuerySource] with Multiple[Monitored] with Multiple[ControlIssuer] }
  @peer type QuerySource <: { type Tie <: Single[Oracle] with Single[Monitor] }

  val dataSource: Signal[Int] on Oracle = connected
  val commandOutput: Signal[String] on QuerySource = dataSource.asLocal.map("Connected: " + _)
}

@multitier trait ControlCommand {
  @peer type Actor <: { type Tie <: Multiple[ControlIssuer] }
  @peer type ControlIssuer <: { type Tie <: Single[Actor] }

  val shutdown: Signal[Boolean] on ControlIssuer = Var(false)
}
