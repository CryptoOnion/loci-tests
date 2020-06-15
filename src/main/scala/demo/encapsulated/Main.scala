package demo.encapsulated

import loci.communicator.tcp.TCP
import loci.{Instance, connect, listen, multitier}

object Server extends App {
  multitier start new Instance[editor.Server](
    listen[editor.Client] { TCP(22323) }
  )
}

object Client extends App {
  multitier start new Instance[editor.Client](
    connect[editor.Server] { TCP("localhost", 22323) }
  )
}
