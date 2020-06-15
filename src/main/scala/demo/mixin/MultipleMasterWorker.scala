package demo.mixin

import loci.transmitter.rescala._
import loci.serializer.upickle._
import rescala.default._
import loci._

import scala.concurrent.Future

@multitier trait MultipleMasterWorker[T] {
  @peer type Master <: { type Tie <: Multiple[Worker] }
  @peer type Worker <: { type Tie <: Single[Master] }

  def run(task: String): Future[String] on Master = on[Master] { implicit! =>
    Future.successful("")
  }

  private def execute(task: String): String on Worker = on[Worker] { implicit! => "" }
}

@multitier trait MonitoredMasterWorker[T] extends MultipleMasterWorker[T] with Monitoring {
  self: Command =>

  @peer type Master <: Monitor with Receiver {
    type Tie <: Multiple[Worker] with Multiple[Monitored] with Multiple[Sender]
  }

  @peer type Worker <: Monitored { type Tie <: Single[Master] with Single[Monitor] }
  @peer type Client <: Sender {
    // Why does Client need to include all tie specifications of Sender while Master
    // doesn't need to include QuerySource and ControlIssuer for the Receiver peer type?
    //
    // Considering that super peers of Sender and Receiver could be inferred for the Master example
    // based on the Multiple[Sender] relation, shouldn't this be the same for Single[Receiver]
    //
    // Dropping super relation to Oracle and Actor out, breaks compilation. So the ties need to be pulled up.
    type Tie <: Single[Receiver] with Single[Oracle] with Single[Actor] with Single[Monitor]
  }
}


@multitier object app extends MonitoredMasterWorker[String] with Command
