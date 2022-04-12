package org.scrum.psd.battleship.ascii;

import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi;
import org.scrum.psd.battleship.controller.GameController;
import org.scrum.psd.battleship.controller.dto.Color;
import org.scrum.psd.battleship.controller.dto.Letter;
import org.scrum.psd.battleship.controller.dto.Position;
import org.scrum.psd.battleship.controller.dto.Ship;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private static List<Ship> myFleet;
    private static List<Ship> enemyFleet;
    private static ColoredPrinter console;

    public static void main(String[] args) {
        console = new ColoredPrinter.Builder(1, false).background(Ansi.BColor.BLACK).foreground(Ansi.FColor.WHITE).build();

        console.setForegroundColor(Ansi.FColor.MAGENTA);
        console.println("                                     |__");
        console.println("                                     |\\/");
        console.println("                                     ---");
        console.println("                                     / | [");
        console.println("                              !      | |||");
        console.println("                            _/|     _/|-++'");
        console.println("                        +  +--|    |--|--|_ |-");
        console.println("                     { /|__|  |/\\__|  |--- |||__/");
        console.println("                    +---------------___[}-_===_.'____                 /\\");
        console.println("                ____`-' ||___-{]_| _[}-  |     |_[___\\==--            \\/   _");
        console.println(" __..._____--==/___]_|__|_____________________________[___\\==--____,------' .7");
        console.println("|                        Welcome to Battleship                         BB-61/");
        console.println(" \\_________________________________________________________________________|");
        console.println("");
        console.setForegroundColor(Ansi.FColor.WHITE);

        InitializeGame();

        StartGame();
    }

    private static void StartGame() {
        Scanner scanner = new Scanner(System.in);

        console.print("\033[2J\033[;H");
        console.println(Color.PURPLE.getColoredText("                  __"));
        console.println(Color.PURPLE.getColoredText("                 /  \\"));
        console.println(Color.PURPLE.getColoredText("           .-.  |    |"));
        console.println(Color.PURPLE.getColoredText("   *    _.-'  \\  \\__/"));
        console.println(Color.PURPLE.getColoredText("    \\.-'       \\"));
        console.println(Color.PURPLE.getColoredText("   /          _/"));
        console.println(Color.PURPLE.getColoredText("  |      _  /\" \""));
        console.println(Color.PURPLE.getColoredText("  |     /_\'"));
        console.println(Color.PURPLE.getColoredText("   \\    \\_/"));
        console.println(Color.PURPLE.getColoredText("    \" \"\" \"\" \"\" \""));

        do {
            console.println("");
            console.println("Player, it's your turn");
            Position position = getInput(scanner, "Enter coordinates for your shot :");
            printSeparator();
            boolean isHit = GameController.checkIsHit(enemyFleet, position);
            if (isHit) {
                beep();

                console.println(Color.RED.getColoredText("                \\         .  ./"));
                console.println(Color.RED.getColoredText("              \\      .:\" \";'.:..\" \"   /"));
                console.println(Color.RED.getColoredText("                  (M^^.^~~:.'\" \")."));
                console.println(Color.RED.getColoredText("            -   (/  .    . . \\ \\)  -"));
                console.println(Color.RED.getColoredText("               ((| :. ~ ^  :. .|))"));
                console.println(Color.RED.getColoredText("            -   (\\- |  \\ /  |  /)  -"));
                console.println(Color.RED.getColoredText("                 -\\  \\     /  /-"));
                console.println(Color.RED.getColoredText("                   \\  \\   /  /"));

                printEnemyFleetState();
            }

            console.println(GameController.getHitMessage(isHit, true));
            printSeparator();
            position = getRandomPosition();
            isHit = GameController.checkIsHit(myFleet, position);
            console.println("");

            if (isHit) {
                beep();

                console.println(Color.RED.getColoredText("                \\         .  ./"));
                console.println(Color.RED.getColoredText("              \\      .:\" \";'.:..\" \"   /"));
                console.println(Color.RED.getColoredText("                  (M^^.^~~:.'\" \")."));
                console.println(Color.RED.getColoredText("            -   (/  .    . . \\ \\)  -"));
                console.println(Color.RED.getColoredText("               ((| :. ~ ^  :. .|))"));
                console.println(Color.RED.getColoredText("            -   (\\- |  \\ /  |  /)  -"));
                console.println(Color.RED.getColoredText("                 -\\  \\     /  /-"));
                console.println(Color.RED.getColoredText("                   \\  \\   /  /"));
                printSeparator();

                printEnemyFleetState();
            }
            console.println(String.format("Computer shoot in %s%s and %s", position.getColumn(), position.getRow(), GameController.getHitMessage(isHit, false)));
        } while (true);
    }

    private static void printEnemyFleetState() {
        console.println("");
        console.println("Enemy fleet status:");
        printFleetState(enemyFleet);
    }

    private static Position getInput(Scanner scanner, String text) {
        for (int i = 0; i < 10; i++) {
            String input = scanner.next();
            try {
                console.println(text);
                return parsePosition(input);
            } catch (Exception e) {
                console.println(Color.RED.getColoredText("Invalid input: ") + input);
            }
        }

        throw new RuntimeException("Max retries reached, exiting");
    }

    private static void printSeparator() {
        console.println(Color.YELLOW.getColoredText("##################################################"));
    }

    private static void beep() {
        console.print("\007");
    }

    protected static Position parsePosition(String input) {
        Letter letter = Letter.valueOf(input.toUpperCase().substring(0, 1));
        int number = Integer.parseInt(input.substring(1));
        return new Position(letter, number);
    }

    private static Position getRandomPosition() {
        int rows = 8;
        int lines = 8;
        Random random = new Random();
        Letter letter = Letter.values()[random.nextInt(lines)];
        int number = random.nextInt(rows);
        Position position = new Position(letter, number);
        return position;
    }

    private static void InitializeGame() {
        InitializeMyFleet();

        InitializeEnemyFleet();
    }

    private static void InitializeMyFleet() {
        Scanner scanner = new Scanner(System.in);
        myFleet = GameController.initializeShips();

        console.println("Please position your fleet (Game board has size from A to H and 1 to 8) :");

        for (Ship ship : myFleet) {
            console.println("");
            console.println(String.format("Please enter the positions for the %s (size: %s)", ship.getName(), ship.getSize()));
            for (int i = 1; i <= ship.getSize(); i++) {
                Position positionInput = getInput(scanner, String.format("Enter position %s of %s (i.e A3):", i, ship.getSize()));
                ship.addPosition(positionInput);
            }
        }
    }

    private static void InitializeEnemyFleet() {
        enemyFleet = GameController.initializeShips();

        enemyFleet.get(0).getPositions().add(new Position(Letter.B, 4));
        enemyFleet.get(0).getPositions().add(new Position(Letter.B, 5));
        enemyFleet.get(0).getPositions().add(new Position(Letter.B, 6));
        enemyFleet.get(0).getPositions().add(new Position(Letter.B, 7));
        enemyFleet.get(0).getPositions().add(new Position(Letter.B, 8));

        enemyFleet.get(1).getPositions().add(new Position(Letter.E, 6));
        enemyFleet.get(1).getPositions().add(new Position(Letter.E, 7));
        enemyFleet.get(1).getPositions().add(new Position(Letter.E, 8));
        enemyFleet.get(1).getPositions().add(new Position(Letter.E, 9));

        enemyFleet.get(2).getPositions().add(new Position(Letter.A, 3));
        enemyFleet.get(2).getPositions().add(new Position(Letter.B, 3));
        enemyFleet.get(2).getPositions().add(new Position(Letter.C, 3));

        enemyFleet.get(3).getPositions().add(new Position(Letter.F, 8));
        enemyFleet.get(3).getPositions().add(new Position(Letter.G, 8));
        enemyFleet.get(3).getPositions().add(new Position(Letter.H, 8));

        enemyFleet.get(4).getPositions().add(new Position(Letter.C, 5));
        enemyFleet.get(4).getPositions().add(new Position(Letter.C, 6));
    }

    private static void printFleetState(List<Ship> fleet) {
        for (Ship ship : fleet) {
            console.println("Ship: " + ship.getName() + " - " + formatShipState(ship));
        }
    }

    private static String formatShipState(Ship ship) {
        return ship.isSunk() ? "sunk" : "alive";
    }
}
