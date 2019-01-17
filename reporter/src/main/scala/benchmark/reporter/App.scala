package benchmark.reporter

import java.io.File

import scala.io.Source
import scala.util.{Success, Try}

object App {

  val decimalFormatter = {
    val fmt = java.text.NumberFormat.getInstance
    fmt.setMaximumFractionDigits(2)
    fmt.setMinimumFractionDigits(2)
    fmt
  }

  val intFormatter = java.text.NumberFormat.getInstance

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
         }
         th, td {
           padding-top: 3px;
           padding-bottom: 3px;
           padding-left: 5px;
           padding-right: 5px;
         }
         </style>
         </head>
      """.stripMargin)

    val errorRates =
      testCollateral
      .filter  { _.typ == "errorRate" }
      .groupBy { _.method }

    val timings: Map[String, List[TestCollateral]] =
      testCollateral
        .filter  { _.typ == "timing" }
        .groupBy { _.method }

    val sections: Seq[(String, List[List[TestCollateral]])] =
      timings.toList.map { case (k, timingCollateral) =>
        val collateralList =
          errorRates.get(k)
            .map { errorRateCollateral => List(errorRateCollateral, timingCollateral) }
            .getOrElse(List(timingCollateral))
        (k, collateralList)
      }

    sections
      .sortBy { case (method, _) => method }
      .foreach { case (method, collateralLists) =>
        println(s"<h1>$method</h1>")
        collateralLists.foreach { collateralList =>
          collateralList.headOption.foreach { fst =>
            println(s"<h3>${fst.typ}</h3>")
          }
          println(table(collateralList))
        }
      }

    println("</html>")
  }

  private def table(collateral: List[TestCollateral]): String = {
    // don't have to close the file???
    val fileLines = Source.fromFile(collateral.head.results).getLines
    val headers = fileLines.next.split(",")
    def getData(f: File) = Source.fromFile(f).getLines.toList.last.split(",")
    val data = collateral.map { tc => (tc.client, getData(tc.results)) }
    s"""
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
    Try { maybeNum.toInt } match {
      case Success(i) => intFormatter.format(i)
      case _ =>
        Try { maybeNum.toDouble } match {
          case Success(d) => decimalFormatter.format(d)
          case _ => maybeNum
        }
    }
  }
}
