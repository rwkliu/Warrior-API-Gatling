package warriordatabase;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.core.scenario.Scenario;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;
public class warriorDatabaseSimulation extends Simulation {
    // Add the HttpProtocolBuilder
    HttpProtocolBuilder httpProtocol = http.baseUrl("http://127.0.0.1/")
        .acceptHeader("application/json")
        .contentTypeHeader("application/json");

    // Add the ScenarioBuilder
    ScenarioBuilder myScenario = scenario("My Scenario")
        .exec(
            http("Find Neo")
                .get("warrior?t=Neo")
                .check(status().is(200)),
            pause(1),
            http("Add new warrior")
                .post("/warrior")
                .body(StringBody("{\"id\": \"196d5c01-6aae-49aa-a7cd-c525198933dc\",\n" +
                    "    \"name\": \"Agent Smith\",\n" +
                    "    \"dob\": \"1970-01-01\",\n" +
                    "    \"fight_skills\": [\"KungFu\", \"Karate\", \"Hapkido\"] }"))
                .check(status().is(201)),
            pause(1),
            http("Find Neo by id")
                .get("warrior/3b323968-73d8-47a3-a84d-9db948e2bd6b")
                .check(status().is(200)),
            pause(1),
            http("Count Warriors")
                .get("/counting-warriors")
                .check(status().is(200))
        );

    // Add the setUp block
    {
        setUp(
            myScenario.injectOpen(rampUsersPerSec(1).to(500).during(30))
        ).protocols(httpProtocol).maxDuration(Duration.ofMinutes(1));
    }
}
