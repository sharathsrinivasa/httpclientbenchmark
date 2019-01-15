import java.io.File

import scala.sys.process._

val usage = "Usage: (test --no-reporter (all | <client>) [<host> <port> <executions> <workers>]) | server | list | report <dir> | help"

val suiteFileDir = "testng"

// yah, some of this sucks, but I don't want to reach for a CLI-parser lib unless I revisit
// this a few more times. (Even though you can make AWESOME CLIs with Scala:)

val disableReporterFlag = "--no-reporter"
val disableDropwizard = args.contains(disableReporterFlag)

args.filter { _ != disableReporterFlag }.toList match {
  case List("test", "all", h, p, e, w) => runAllTests(h, p.toInt, Some(e.toInt), Some(w.toInt))
  case List("test", c    , h, p, e, w) => runTest(c, h, p.toInt, Some(e.toInt), Some(w.toInt))
  case List("test", "all")             => runAllTests("localhost", 8080)
  case List("test", client)            => runTest(client, "localhost", 8080)
  case List("list")                    => listClients()
  case List("server")                  => runServer()
  case List("report", dir)             => generateReport(dir)
  case _                               => exitWithUsage()
}

def jarPath(name: String) = s"docker-app/lib/$name-1.0.0-SNAPSHOT-jar-with-dependencies.jar"

// Leaving dir as string for now...
def generateReport(dir: String): Unit = {
  s"""java -jar ${jarPath("reporter")} $dir""".!
}

def runAllTests(
  host: String,
  port: Int,
  executions: Option[Int] = None,
  workers: Option[Int] = None
): Unit = clients().foreach { c => runTest(c, host, port, executions, workers) }

def runTest(
  client: String,
  host: String,
  port: Int,
  executions: Option[Int] = None,
  workers: Option[Int] = None
): Unit = {
  val testFile = s"$suiteFileDir/$client.xml"

  val reporterSeconds = if (disableDropwizard) (60 * 60) else 30

  val sysProps = List(
    ("host"               , Some(host)),
    ("port"               , Some(port)),
    ("test.executions"    , executions),
    ("test.workers"       , workers),
    ("dropwizard.seconds" , Some(reporterSeconds))
  ).collect { case (n, Some(v)) => (n, v) }
    .map { case (n, v) => s"-Dbm.$n=$v" } 

  val jarName = jarPath(s"${client}-benchmark")
  val cmd = s"""java ${sysProps.mkString(" ")} -jar ${jarPath(s"$client-benchmark")} -usedefaultlisteners false $testFile"""
  println(s"Executing:  $cmd")
  cmd.!
}

def exitWithUsage(): Unit = {
  System.err.println(usage)
  System.exit(1)
}

def clients(): Seq[String] = {
  new File(suiteFileDir).listFiles()
    .map     { _.getName }
    .filter  { _.endsWith(".xml") }
    .map     { _.split("""\.""").dropRight(1).mkString(".") }
    .sorted
}

def listClients(): Unit = clients().foreach { println }

def runServer(): Unit = {
  s"""java -jar ${jarPath("mock-application")}""".run()
}
