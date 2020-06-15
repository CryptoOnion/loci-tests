package demo.encapsulated

import loci._
import loci.transmitter.rescala._
import loci.serializer.upickle._

import scala.concurrent.Future
import rescala.default._

@multitier trait Backup {
  @peer type Processor <: { type Tie <: Single[Storage] }
  @peer type Storage <: { type Tie <: Single[Processor] }

  val opMetrics: Event[String] = Evt[String]()

  def store(id: Long, data: String): Unit on Processor
  def load(id: Long): Future[String] on Processor
}

@multitier trait FileBackup extends Backup {
  def store(id: Long, data: String): Unit on Processor = placed {
    // code that makes a remote call to insert() to send data to Storage
  }

  def load(id: Long): Future[String] on Processor = placed {
    // code that makes a remote call to query() to load data from Storage
    Future.successful("")
  }
}
