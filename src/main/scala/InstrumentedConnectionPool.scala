package connectionpool

import com.yammer.metrics.{Counter, Timer}
import com.yammer.jmx.JmxManaged

import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.TimeUnit

class InstrumentedConnectionPool[Conn](name:              String,
                                       connectionFactory: ConnectionFactory[Conn],
                                       max:               Int = 20,
                                       timeout:           Int = 500000)
  extends SimpleConnectionPool[Conn](connectionFactory, max, timeout)
  with JmxManaged {

  val activeConnections = new Counter

  enableJMX("ConnectionPool-%s".format(name)) { jmx =>
    jmx.addCounter("activeConnections", activeConnections)
  }
  
  override def apply[A]()(f: Conn => A): A = {
    val connection = borrow
    activeConnections.inc

    try {
      f(connection)
    } finally {
      giveBack(connection)
      activeConnections.dec
    }
  }
}