package bdd.features.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.scrum.psd.battleship.controller.GameController;
import org.scrum.psd.battleship.controller.dto.Letter;
import org.scrum.psd.battleship.controller.dto.Position;
import org.scrum.psd.battleship.controller.dto.Ship;
import org.scrum.psd.battleship.controller.dto.ShipPart;

public class ShipValidationSteps {
    private Ship ship;
    private boolean validationResult;

    @Given("^I have a (.*?) ship with (.*?) positions$")
    public void i_have_a_size_ship_with_ship_parts_with_positions(int size, int positions) throws Throwable {
        ship = new Ship();

        ship.setSize(size);
        for (int i = 0; i < positions; i++) {
            ship.getShipParts().add(new ShipPart(new Position(Letter.A, i)));
        }
    }

    @When("^I check if the ship is valid$")
    public void i_check_if_the_ship_is_valid() throws Throwable {
        validationResult = GameController.isShipValid(ship);
    }


    @Then("^the result should be (.*?)$")
    public void the_result_should_be(boolean expectedValidationResult) throws Throwable {
        Assert.assertEquals(expectedValidationResult, validationResult);
    }
}
