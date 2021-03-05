package bookmytable.stepdefinitions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.springframework.beans.factory.annotation.Autowired;

import bookmytable.model.Admin;
import bookmytable.model.RestaurantOwner;
import bookmytable.service.AdminLoginService;
import bookmytable.service.AdminRegistrationService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LogInAsAdmin {
	
	boolean loggedIn = false;
	String exception;
	
	@Autowired
	AdminRegistrationService ARS = new AdminRegistrationService();
	
	@Autowired
	AdminLoginService ALS = new AdminLoginService();
	
	@Given("Administrator {string} exists")
	public void administrator_exists(String string) {
		// Write code here that turns the phrase above into concrete actions
	}

	@Given("Administrator {string} has email {string}, password {string}")
	public void administrator_has_email_password(String string, String string2, String string3) {
		// Write code here that turns the phrase above into concrete actions
//		System.out.println("Name: "+ string);
//		System.out.println("Email: "+ string2);
//		System.out.println("Password: "+ string3);
//		Admin admin = ARS.createAdmin(string, string2, string3);
	}

	@Given("Administrator {string} is logged out")
	public void administrator_is_logged_out(String string) {
		// Write code here that turns the phrase above into concrete actions
		loggedIn = false;
	}

	@When("Administrator {string} attempts to login using email {string} and password {string}")
	public void administrator_attempts_to_login_using_email_and_password(String string, String string2,
			String string3) {
		// Write code here that turns the phrase above into concrete actions
		Admin admin = ARS.createAdmin(string, string2, string3);
		String validEmail = admin.getEmail();
		String validPassword = admin.getPassword();
		loggedIn = ALS.loginAdmin(admin, validEmail, validPassword);
	}

	@Then("Administrator {string} will be logged in")
	public void administrator_will_be_logged_in(String string) {
		// Write code here that turns the phrase above into concrete actions
		assertTrue(loggedIn);
	}

	@Then("Administrator {string} will be redirected to the administrator profile")
	public void administrator_will_be_redirected_to_the_administrator_profile(String string) {
		// Write code here that turns the phrase above into concrete actions
	}

	@Given("Administrator {string} is already logged in")
	public void administrator_is_already_logged_in(String string) {
		// Write code here that turns the phrase above into concrete actions
		loggedIn = true;
	}
	
	@When("Administrator {string} tries to login using email {string} and password {string}")
	public void administrator_tries_to_login_using_email_and_password(String string, String string2,
			String string3) {
		// Write code here that turns the phrase above into concrete actions
		Admin admin = ALS.getAdminsByName(string).get(0);
		try{
			loggedIn = ALS.loginAdmin(admin, string2, string3);
		} catch(IllegalArgumentException e){
			loggedIn = false;
		}
	}

	@Then("{string} will remain logged in")
	public void will_remain_logged_in(String string) {
		// Write code here that turns the phrase above into concrete actions
		assertTrue(loggedIn);
	}

	@When("Administrator {string} attempts to login with credentials {string} and {string}")
	public void administrator_attempts_to_login_with_credentials_and(String string, String string2, String string3) {
		// Write code here that turns the phrase above into concrete actions
		Admin admin = ALS.getAdminsByName(string).get(0);
		try{
			loggedIn = ALS.loginAdmin(admin, string2, string3);
		} catch(IllegalArgumentException e){
			exception = e.getLocalizedMessage();
			loggedIn = false;
		}
	}

	@When("{string} does not match {string}")
	public void does_not_match(String string, String string2) {
		// Write code here that turns the phrase above into concrete actions
	}

	@Then("error message {string} is issued")
	public void error_message_is_issued(String string) {
		// Write code here that turns the phrase above into concrete actions
		assertEquals(string, exception);
	}

	@Then("Administrator {string} will remain logged out")
	public void administrator_will_remain_logged_out(String string) {
		// Write code here that turns the phrase above into concrete actions
		assertFalse(loggedIn);
	}

	@Then("Administrator {string} will remain on the login page")
	public void administrator_will_remain_on_the_login_page(String string) {
		// Write code here that turns the phrase above into concrete actions
	}
	
	@When("{string} matches {string}")
	public void matches(String string, String string2) {
	    // Write code here that turns the phrase above into concrete actions
	}
}
