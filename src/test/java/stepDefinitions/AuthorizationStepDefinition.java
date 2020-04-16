package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import io.cucumber.junit.Cucumber;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.response.Response;
import org.junit.runner.RunWith;
import org.testng.Assert;

@RunWith(Cucumber.class)
public class AuthorizationStepDefinition {

    private static final String USERNAME = "bcgdvqa+1@gmail.com";
    private static final String PASSWORD = "Ft6@8K6xp";
    private static final String BAD_USERNAME = "xyz@gmail.com";
    private static final String BAD_PASSWORD = "password";
    private static final String BASE_URL = "https://dev-backend.ayatana.app";

    private static String jsonString;
    private static Response response;
    private static String jwt;
    private static String refresh_token;

    @Given("^The user with the right credentials$")
    public void the_user_is_at_the_current_url() {

        RestAssured.baseURI = BASE_URL;
    }

    @When("^Sends the correct authorization request$")
    public void sends_the_correct_authorization_request() {

        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
//        response = request.body("{ \"username\":\"" + USERNAME + "\", \"password\":\"" + PASSWORD + "\"}")
//                .post("/login");

        response = request.body("{\n" +
                "    \"username\":\"" + USERNAME + "\",\n" +
                "    \"password\":\"" + PASSWORD + "\" \n" +
                "}")
                .post("/login");

        jsonString = response.asString();
    }

    @Then("^The user generates the jwt and refresh_token$")
    public void the_user_generates_the_the_jwt_and_refreshtoken() {

        jwt = JsonPath.from(jsonString).get("jwt");
        refresh_token = JsonPath.from(jsonString).get("refresh_token");
        Assert.assertNotNull(jwt);
        Assert.assertNotNull((refresh_token));
        System.out.println("JWT: " + jwt);
        System.out.println("refresh_token: " + refresh_token);
    }


    @And("^The status code received is '200' success$")
    public void the_status_code_received_is_200() {

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
        System.out.println("\nReturned Status Code for correct credentials: " + statusCode);
    }

    @Given("^The user with the wrong credentials$")
    public void the_user_with_the_wrong_credentials() {

    }

    @When("^Sends the wrong authorization request$")
    public void sends_the_wrong_authorization_request() {
        RequestSpecification request1 = RestAssured.given();
        request1.header("Content-Type", "application/json");
        response = request1.body("{ \"username\":\"" + BAD_USERNAME + "\", \"password\":\"" + BAD_PASSWORD + "\"}")
                .post("/login");

        jsonString = response.asString();
    }

    @Then("^The user cannot generate the jwt and refresh_token$")
    public void the_user_cannot_generate_the_jwt_and_refreshtoken() {

        jwt = JsonPath.from(jsonString).get("jwt");
        refresh_token = JsonPath.from(jsonString).get("refresh_token");
        Assert.assertNull(jwt);
        Assert.assertNull(refresh_token);
    }

    @And("^the status code received is '401' Unauthorized$")
    public void the_status_code_received_is_401_unauthorized() {

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 401);
        System.out.println("\nReturned Status Code for wrong credentials: " + statusCode);
    }


}
