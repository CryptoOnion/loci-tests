package demo.encapsulated

import loci._
import loci.transmitter.rescala._
import loci.serializer.upickle._
import rescala.default._

@multitier trait Logging {

  @peer type ChangeSource <: { type Tie <: Single[Aggregator] }
  @peer type Aggregator <: { type Tie <: Single[ChangeSource] }

  val injectedDoc: Signal[String] on ChangeSource
  val saveEvent: Event[Unit] on ChangeSource

  val in: Event[String] on Aggregator = injectedDoc.asLocal.changed
  val logMessages: Signal[Seq[String]] on Aggregator = in.fold(Seq.empty[String])((x, y) => x :+ y)

}
