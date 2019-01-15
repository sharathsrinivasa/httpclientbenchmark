package benchmark.reporter

import java.io.File

import scala.io.Source
import scala.util.Try

object App {

  val formatter = java.text.NumberFormat.getInstance

  case class TestCollateral(typ: String, client: String, method: String, results: File)

  def main(args: Array[String]): Unit = {

    if (args.length != 1) {
      System.err.println("Usage: <dir>")
      System.exit(1)
    }

    val dir = new File(args(0))

    if (!dir.isDirectory) {
      System.err.println(s"Expected ${dir.getAbsolutePath} to be a directory.")
      System.exit(1)
    }

    val testCollateral = for {
      runDir                 <- dir.listFiles.toList
      f                      <- runDir.listFiles
      typ :: test :: revName = f.getName.split("""\.""").dropRight(1).reverse.toList
      fullPath               = revName.reverse
      name                   = fullPath.takeRight(2).head
    } yield TestCollateral(typ, name, test, f)

    println(
      """
         <html>
         <head>
         <style>
         table, th, td {
           border: 1px solid black;
         }
         table {
           border-collapse: collapse;
           padding: 3px;
         }
         </style>
         </head>
      """.stripMargin)

    val errorRates =
      testCollateral
      .filter  { _.typ == "errorRate" }
      .groupBy { _.method }

    println(html("Error Rates", errorRates))

    val timings =
      testCollateral
        .filter  { _.typ == "timing" }
        .groupBy { _.method }

    println(html("Timings", timings))

    println("</html>")
  }

  // Yeah, this is getting pretty weak modeling:(:(
  private def html(title: String, collateral: Map[String, List[TestCollateral]]): String =
    s"""<h1>$title</h1>\n${
      collateral
        .toList
        .sortBy { case (m, _) => m }
        .map    { case (m, tcs) => html(m, tcs)
    }.mkString("\n")}"""

  private def html(method: String, collateral: List[TestCollateral]): String = {
    // don't have to close the file???
    val fileLines = Source.fromFile(collateral.head.results).getLines
    val headers = fileLines.next.split(",")
    def getData(f: File) = Source.fromFile(f).getLines.toList.last.split(",")
    val data = collateral.map { tc => (tc.client, getData(tc.results)) }
    s"""
<h3>$method</h3>
<table>
  <th>Client</th>${headers.map { h => s"<th>$h</th>"  }.mkString("") }
    ${data.map { case (client, data) =>
      s"<tr><td>$client</td>${data.map {d => s"<td align=right>${tryFormat(d)}</td>"}.mkString("")}</tr>"
    }.mkString("\n")}
</table>
""".stripMargin
  }

  // best attempt at formatting values that _may_ be numbers.
  // Clearly, this impl is not worried about performance!
  private def tryFormat(maybeNum: String): String = {
    Try       { maybeNum.toInt }
      .orElse { Try(maybeNum.toFloat) }
      .map    { n => formatter.format(n) }
      .getOrElse(maybeNum)
  }
}
