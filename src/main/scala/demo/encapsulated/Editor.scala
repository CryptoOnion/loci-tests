package demo.encapsulated

import loci.transmitter.rescala._
import loci.serializer.upickle._
import rescala.default._
import loci._

@multitier trait Editor {
  @peer type Client <: backup.Processor with logging.ChangeSource {
    type Tie <: Single[Server] with Single[backup.Storage] with Single[logging.Aggregator]
  }
  @peer type Server <: backup.Storage with logging.Aggregator {
    type Tie <: Single[Client] with Single[backup.Processor] with Single[logging.ChangeSource]
  }

  val backup: Backup
  val logging: Logging

  val saveButton: Evt[Unit] on Client = Evt[Unit]()
  val document: Signal[String] on Client = placed { Var.empty[String] }
  val uiLog: Signal[Seq[String]] on Client = logging.logMessages.asLocal
}

@multitier object editor extends Editor {
  @multitier object backup extends FileBackup
  @multitier object logging extends Logging {
      val bridgeSaveEvent: Evt[Unit] on ChangeSource = placed { Evt[Unit]() }
      val saveEvent: Event[Unit] on ChangeSource = bridgeSaveEvent

      val bridgeDoc: Var[String] on ChangeSource = placed { Var.empty[String] }
      val injectedDoc: Signal[String] on ChangeSource = bridgeDoc
  }

  def main(): Unit on Client = {
    saveButton observe { logging.bridgeSaveEvent fire _ }
    document.changed.observe { logging.bridgeDoc set _ }

    uiLog observe println
  }
}
