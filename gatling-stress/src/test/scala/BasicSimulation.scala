import io.gatling.core.Predef._
import io.gatling.http.Predef._

class BasicSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl(
      "http://localhost:8080"
    )

  val scn = scenario("BasicSimulation")
    .exec(http("get_hello")
    .get("/hello"))

  setUp(
    scn.inject(atOnceUsers(10 * 1000))
  ).protocols(httpProtocol)
}
