package stepDefinitions;

import io.cucumber.java.BeforeStep;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.junit.Cucumber;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.testng.Assert;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@RunWith(Cucumber.class)
public class BAStepDefinition {

    private static String BASE_URL;
    private static String USERNAME;
    private static String PASSWORD;
    public static String jsonString;
    public static Response response;
    public static String jwt;
    public static String refresh_token;

    @BeforeStep
    public void setUp() throws IOException {

        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream(
                System.getProperty("user.dir") + "/src/test/resources/config.properties");

        properties.load(fileInputStream);
        BASE_URL = properties.getProperty("devURL");
        USERNAME = properties.getProperty("username");
        PASSWORD = properties.getProperty("password");

        RestAssured.baseURI = BASE_URL;
    }

    @Given("^The agent is an authorized user$")
    public void the_agent_is_an_authorized_user() throws IOException {

//        Properties properties = new Properties();
//        FileInputStream fileInputStream = new FileInputStream(
//                System.getProperty("user.dir") + "/src/test/resources/config.properties");
//
//        properties.load(fileInputStream);
//        BASE_URL = properties.getProperty("devURL");
//        USERNAME = properties.getProperty("username");
//        PASSWORD = properties.getProperty("password");
//
//        RestAssured.baseURI = BASE_URL;

        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");

        response = request.body("{\n" +
                "    \"username\": " + USERNAME + ",\n" +
                "    \"password\": " + PASSWORD + "\n" +
                "}")
                .post("/login");

        jsonString = response.asString();
        jwt = JsonPath.from(jsonString).get("jwt");
        refresh_token = JsonPath.from(jsonString).get("refresh_token");
        Assert.assertNotNull(jwt);
        Assert.assertNotNull((refresh_token));
        Assert.assertEquals(response.getStatusCode(), 200);
        System.out.println("JWT: " + jwt);
        System.out.println("refresh_token: " + refresh_token);
    }

    @Given("^The user with the correct jwt$")
    public void the_user_with_the_correct_jwt() {

        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");

        response = request.body("{\n" +
                "    \"refresh_token\":\"" + refresh_token + "\" \n" +
                "}")
                .post("/reauth");

        jsonString = response.asString();
        jwt = JsonPath.from(jsonString).get("jwt");
        System.out.println(jwt);

    }

    @When("^Sends the query for best answer$")
    public void sends_the_query_for_best_answer() {

        String search_query = "how to watch netflix on my samsung tv";

        System.out.println(jwt);

        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.header("Authorization", "Bearer " + jwt);

        response = request.body("{\n" +
                "    \"text\":\"" + search_query + "\" \n" +
                "}")
                .post("/search");

        jsonString = response.asString();
        System.out.println(jsonString);
    }

    @Then("^The user can see the best answer$")
    public void the_user_can_see_the_best_answer() {
    }

    @And("^the status code received is '200' success$")
    public void the_status_code_received_is_200_success() {
    }

}
