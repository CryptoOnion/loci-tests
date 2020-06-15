package demo.mixin

import loci.communicator.tcp.TCP
import loci.{Instance, connect, listen, multitier}

import loci.transmitter.rescala._
import loci.serializer.upickle._
import rescala.default._
import loci._

import scala.concurrent.Future

object Server extends App {
  val x = true

  multitier start new Instance[app.Master](
    listen[app.Client] { TCP(22323) }
  )
}

//object Worker extends App {
//  multitier start new Instance[combined.Worker](
//    connect[combined.Master] { TCP("localhost", 22323) }
//  )
//}
//
//object Client extends App {
//  multitier start new Instance[combined.Client](
//    connect[combined.Master] { TCP("localhost", 22323) }
//  )
//}
