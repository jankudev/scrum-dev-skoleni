package org.scrum.psd.battleship.ascii;

import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi;
import org.scrum.psd.battleship.controller.GameController;
import org.scrum.psd.battleship.controller.dto.Color;
import org.scrum.psd.battleship.controller.dto.Letter;
import org.scrum.psd.battleship.controller.dto.Position;
import org.scrum.psd.battleship.controller.dto.Ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private static List<Ship> myFleet;
    protected static List<Ship> enemyFleet;
    private static ColoredPrinter console;

    public static void main(String[] args) {
        console = new ColoredPrinter.Builder(1, false).background(Ansi.BColor.BLACK).foreground(Ansi.FColor.WHITE).build();

        console.setForegroundColor(Ansi.FColor.MAGENTA);
        printBattleshipLogo();
        console.setForegroundColor(Ansi.FColor.WHITE);

        InitializeGame();

        StartGame();
    }

    private static void printBattleshipLogo() {
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
    }

    private static void printCanon() {
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
    }

    private static void printBang() {
        console.println(Color.RED.getColoredText("                \\         .  ./"));
        console.println(Color.RED.getColoredText("              \\      .:\" \";'.:..\" \"   /"));
        console.println(Color.RED.getColoredText("                  (M^^.^~~:.'\" \")."));
        console.println(Color.RED.getColoredText("            -   (/  .    . . \\ \\)  -"));
        console.println(Color.RED.getColoredText("               ((| :. ~ ^  :. .|))"));
        console.println(Color.RED.getColoredText("            -   (\\- |  \\ /  |  /)  -"));
        console.println(Color.RED.getColoredText("                 -\\  \\     /  /-"));
        console.println(Color.RED.getColoredText("                   \\  \\   /  /"));
    }

    private static void StartGame() {
        Scanner scanner = new Scanner(System.in);

        console.print("\033[2J\033[;H");
        printCanon();

        do {
            console.println("");
            console.println("Player, it's your turn");
            Position position = getInput(scanner, "Enter coordinates for your shot :");
            printSeparator();
            boolean isHit = GameController.checkIsHit(enemyFleet, position);
            if (isHit) {
                beep();
                printBang();
            }

            console.println(GameController.getHitMessage(isHit, true));
            printSeparator();

            printEnemyFleetState();
            printSeparator();

            position = getRandomPosition();
            isHit = GameController.checkIsHit(myFleet, position);
            console.println("");

            if (isHit) {
                beep();
                printBang();

                printSeparator();
            }
            console.println(String.format("Computer shoot in %s%s and %s", position.getColumn(), position.getRow(), GameController.getHitMessage(isHit, false)));
        } while (true);
    }

    private static void printEnemyFleetState() {
        console.println("");
        console.println("");
        console.println(Color.WHITE.getColoredText("Enemy fleet status:"));
        console.println("");
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

    protected static void InitializeEnemyFleet() {
        enemyFleet = GameController.initializeShips();

        for (Ship ship : enemyFleet
        ) {
            boolean canBeGenerated = false;
            do {
                Position startPoint = getRandomPosition();
                boolean isVertical = new Random().nextBoolean();

                boolean canContinue = (isVertical && (startPoint.getRow() + ship.getSize()) <= 8) ||
                        (!isVertical & (startPoint.getColumn().getIndex() + ship.getSize()) <= 8);

                if (canContinue) {
                    List<Position> ganeratedFields = new ArrayList<>();
                    if (isVertical) {
                        for (int i = 0; i < ship.getSize(); i++) {
                            ganeratedFields.add(new Position(startPoint.getColumn(), startPoint.getRow() + i));
                        }
                    } else {
                        for (int i = 0; i < ship.getSize(); i++) {
                            ganeratedFields.add(new Position(Letter.getByIndex(startPoint.getColumn().getIndex() + i), startPoint.getRow()));
                        }
                    }

                    canBeGenerated = ganeratedFields.stream()
                            .noneMatch(shipPosition -> enemyFleet.stream().map(Ship::getPositions).collect(Collectors.toList()).contains(shipPosition));

                    if (canBeGenerated)
                        ship.getPositions().addAll(ganeratedFields);
                }
            } while (!canBeGenerated);
        }
    }

    private static void printFleetState(List<Ship> fleet) {
        for (Ship ship : fleet) {
            console.println("  " + ship.getName() + " (" + ship.getSize()+ ") -> " + formatShipState(ship));
        }
    }

    private static String formatShipState(Ship ship) {
        return ship.isSunk() ? Color.RED.getColoredText("sunk") : Color.GREEN.getColoredText("still operational");
    }

}
