package warriordatabase;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.core.scenario.Scenario;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.util.concurrent.ThreadLocalRandom;
public class warriorDatabaseSimulation extends Simulation {
    // Add the HttpProtocolBuilder
    HttpProtocolBuilder httpProtocol = http.baseUrl("http://127.0.0.1:5000/")
        .acceptHeader("application/json")
        .contentTypeHeader("application/json");

    // Add the ScenarioBuilder
    ScenarioBuilder myScenario = scenario("My Scenario")
        .exec(
            http("Request 1").get("/"),
            pause(2),
            http("Request 2")
                .get("warrior?t=Neo")
                .check(status().is(200))
        );

    // Add the setUp block
    {
        setUp(
            myScenario.injectOpen(constantUsersPerSec(2).during(60))
        ).protocols(httpProtocol);
    }
}
