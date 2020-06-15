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
    type Tie <: Single[Receiver] with Single[Monitor]
  }
}


@multitier object app extends MonitoredMasterWorker[String] with Command
