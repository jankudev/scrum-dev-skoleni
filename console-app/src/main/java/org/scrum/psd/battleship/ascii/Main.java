package org.scrum.psd.battleship.ascii;

import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi;
import org.scrum.psd.battleship.controller.GameController;
import org.scrum.psd.battleship.controller.Validator;
import org.scrum.psd.battleship.controller.dto.*;
import org.scrum.psd.battleship.controller.dto.debug.DebugFixedPositions;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private static List<Ship> myFleet;
    protected static List<Ship> enemyFleet;

    private static GameGridHistory yourMovesHistory;
    private static GameGridHistory enemyMovesHistory;
    private static ColoredPrinter console;

    // set by env property "debug=true" (java -jar xyz.jar -Ddebug=true), used for fast startup
    private static final boolean debugMode = System.getenv("debug") != null && Boolean.parseBoolean(System.getenv("debug"));
    private static final DebugFixedPositions debugEnemyShootsOnAllFixedPositions = DebugFixedPositions.getDefaultFleetSetupPositionsAtRandomIncludingMisses();

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
        printNewline();
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

    private static void printCanonWithHistoryGrid() {
        console.println(Color.PURPLE.getColoredText("                  __     " + Color.WHITE.getColoredText(" your moves          enemy moves")));
        console.println(Color.PURPLE.getColoredText("                 /  \\   " + Color.WHITE.getColoredText("  A B C D E F G H     A B C D E F G H")));
        console.println(Color.PURPLE.getColoredText("           .-.  |    |  " + Color.WHITE.getColoredText("1 ") + yourMovesHistory.getRowAsString(1)) + Color.WHITE.getColoredText("   1 ") + enemyMovesHistory.getRowAsString(1));
        console.println(Color.PURPLE.getColoredText("   *    _.-'  \\  \\__/   " + Color.WHITE.getColoredText("2 ") + yourMovesHistory.getRowAsString(2)) + Color.WHITE.getColoredText("   2 ") + enemyMovesHistory.getRowAsString(2));
        console.println(Color.PURPLE.getColoredText("    \\.-'       \\        " + Color.WHITE.getColoredText("3 ") + yourMovesHistory.getRowAsString(3)) + Color.WHITE.getColoredText("   3 ") + enemyMovesHistory.getRowAsString(3));
        console.println(Color.PURPLE.getColoredText("   /          _/        " + Color.WHITE.getColoredText("4 ") + yourMovesHistory.getRowAsString(4)) + Color.WHITE.getColoredText("   4 ") + enemyMovesHistory.getRowAsString(4));
        console.println(Color.PURPLE.getColoredText("  |      _  /\" \"        " + Color.WHITE.getColoredText("5 ") + yourMovesHistory.getRowAsString(5)) + Color.WHITE.getColoredText("   5 ") + enemyMovesHistory.getRowAsString(5));
        console.println(Color.PURPLE.getColoredText("  |     /_'             " + Color.WHITE.getColoredText("6 ") + yourMovesHistory.getRowAsString(6)) + Color.WHITE.getColoredText("   6 ") + enemyMovesHistory.getRowAsString(6));
        console.println(Color.PURPLE.getColoredText("   \\    \\_/             " + Color.WHITE.getColoredText("7 ") + yourMovesHistory.getRowAsString(7)) + Color.WHITE.getColoredText("   7 ") + enemyMovesHistory.getRowAsString(7));
        console.println(Color.PURPLE.getColoredText("    \" \"\" \"\" \"\" \"        " + Color.WHITE.getColoredText("8 ") + yourMovesHistory.getRowAsString(8)) + Color.WHITE.getColoredText("   8 ") + enemyMovesHistory.getRowAsString(8));
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

        do {
            // step: player shoots
            printCanonWithHistoryGrid();
            printNewline();
            console.println("Player, it's your turn");

            Position position = getInput(scanner, "Enter coordinates for your shot :");
            printNewline();
            boolean isHit = GameController.checkIsHit(enemyFleet, position);
            if (isHit) {
                beep();
                printBang();

                yourMovesHistory.markState(position, CellState.HIT);
            } else {
                yourMovesHistory.markState(position, CellState.MISS);
            }

            console.println(GameController.getHitMessage(isHit, true));
            printNewline();

            printEnemyFleetState();
            printNewline();

            // check gameover
            if (GameController.isFleetDestroyed(enemyFleet)) {
                printPlayerWins();
                printSeparator();
                System.exit(0);
            }
            console.println("It's the enemy's turn... Brace yourself for impact!");

            // step: enemy shoots
            position = enemyShoots();
            isHit = GameController.checkIsHit(myFleet, position);
            printNewline();

            if (isHit) {
                beep();
                printBang();

                enemyMovesHistory.markState(position, CellState.HIT);
                printNewline();
            } else {
                enemyMovesHistory.markState(position, CellState.MISS);
            }
            console.println(String.format("Computer shoot in %s%s and %s", position.getColumn(), position.getRow(), GameController.getHitMessage(isHit, false)));

            printNewline();
            printMyFleetState();

            // check gameover - loose
            if (GameController.isFleetDestroyed(myFleet)) {
                printPlayerLooses();
                printSeparator();
                System.exit(0);
            }

            printNewline();
            printSeparator();
            printNewline();
        } while (true);
    }

    private static Position enemyShoots() {
        if (!debugMode) {
            return GameController.getRandomPosition(GameController.boardWidth, GameController.boardHeight);
        }
        return debugEnemyShootsOnAllFixedPositions.next();
    }

    private static void printNewline() {
        console.println(" ");
    }

    private static void printPlayerWins() {
        printNewline();
        console.println(Color.GREEN.getColoredText("   ...../ )"));
        console.println(Color.GREEN.getColoredText("   .....' /"));
        console.println(Color.GREEN.getColoredText("   ---' (_____"));
        console.println(Color.GREEN.getColoredText("   ......... ((__)"));
        console.println(Color.GREEN.getColoredText("   ..... _ ((___)"));
        console.println(Color.GREEN.getColoredText("   ....... -'((__)"));
        console.println(Color.GREEN.getColoredText("   --.___((_)"));
        printNewline();
        console.println(Color.GREEN.getColoredText(" YOU WIN THE GAME !!!"));
        printNewline();
    }

    private static void printPlayerLooses() {
        printNewline();
        console.println(Color.RED.getColoredText("   ,    ,    /\\   /\\"));
        console.println(Color.RED.getColoredText("  /( /\\ )\\  _\\ \\_/ /_"));
        console.println(Color.RED.getColoredText("  |\\_||_/| < \\_   _/ >"));
        console.println(Color.RED.getColoredText("  \\______/  \\|0   0|/"));
        console.println(Color.RED.getColoredText("    _\\/_   _(_  ^  _)_"));
        console.println(Color.RED.getColoredText("   ( () ) /`\\|V\"\"\"V|/`\\"));
        console.println(Color.RED.getColoredText("     {}   \\  \\_____/  /"));
        console.println(Color.RED.getColoredText("     ()   /\\   )=(   /\\"));
        console.println(Color.RED.getColoredText("     {}  /  \\_/\\=/\\_/  \\"));
        printNewline();
        console.println(Color.RED.getColoredText("        YOU LOOSE !!!"));
        printNewline();
    }

    private static void printEnemyFleetState() {
        console.println(Color.WHITE.getColoredText("Enemy fleet status:"));
        printFleetState(enemyFleet);
    }

    private static void printMyFleetState() {
        console.println(Color.WHITE.getColoredText("Your fleet status:"));
        printFleetState(myFleet);
    }

    private static Position getInput(Scanner scanner, String text) {
        for (int i = 0; i < 10; i++) {
            console.println(text);
            String input = scanner.next();
            try {
                return GameController.parsePosition(input);
            } catch (Exception e) {
                console.println(Color.RED.getColoredText("Invalid input: ") + input);
            }
        }

        throw new RuntimeException("Max retries reached, exiting");
    }


    private static ShipPosition getInputForShip(Scanner scanner, String text, int size) {

        List<Position> taken = Main.myFleet.stream().flatMap(x -> x.getPositions().stream()).collect(Collectors.toList());
        for (int i = 0; i < 10; i++) {
            console.println(text);
            String input = scanner.next();
            try {
                ShipPosition shipPosition = GameController.parseShipPosition(input);
                List<Position> shipPositions = GameController.getShipPositions(shipPosition.getPosition(), shipPosition.isVertical(), size);
                if (!Validator.isValidShipPlacement(shipPositions, taken)) {
                    throw new RuntimeException("CONFLICT");
                }
                return shipPosition;
            } catch (Exception e) {
                console.println(Color.RED.getColoredText("Invalid input: ") + input);
            }
        }

        throw new RuntimeException("Max retries reached, exiting");
    }

    private static void printSeparator() {
        console.println(Color.YELLOW.getColoredText("####################################################################"));
    }

    private static void beep() {
        console.print("\007");
    }

    private static void InitializeGame() {
        if (!debugMode) {
            InitializeMyFleet();
            InitializeEnemyFleet();
        } else {
            FixedPositionInitializeMyFleet();
            FixedPositionInitializeEnemyFleet();
        }

        yourMovesHistory = new GameGridHistory();
        enemyMovesHistory = new GameGridHistory();
    }

    private static void FixedPositionInitializeMyFleet() {
        assert debugMode : "should be run only in debug mode";
        myFleet = GameController.initializeShips();

        console.println("Auto-positioning your ships - each ship on single row, each starting on position 1");
        placeShipsInFleetOnFixedPositions(myFleet);
    }

    private static void FixedPositionInitializeEnemyFleet() {
        assert debugMode : "should be run only in debug mode";

        enemyFleet = GameController.initializeShips();

        console.println("Auto-positioning enemy ships - each ship on single row, each starting on position 1");
        placeShipsInFleetOnFixedPositions(enemyFleet);
    }

    /**
     * Placing ships in fleet in order on fixed positions each ship on single row (letter)
     * and columns starting from position 1
     *
     * @param fleet
     */
    private static void placeShipsInFleetOnFixedPositions(List<Ship> fleet) {
        Iterator<ShipPosition> fixedPositions = DebugFixedPositions.getDefaultFleetSetupPositions().iterator();
        for (Ship ship : fleet) {
            ShipPosition position = fixedPositions.next();
            ship.addShipPart(position);
        }
    }

    private static void InitializeMyFleet() {
        Scanner scanner = new Scanner(System.in);
        myFleet = GameController.initializeShips();

        console.println("Please position your fleet (Game board has size from A to H and 1 to 8) :");

        for (Ship ship : myFleet) {
            printNewline();
            console.println(String.format("Please enter the positions for the %s (size: %s)", ship.getName(), ship.getSize()));

            ShipPosition positionInput = getInputForShip(scanner, String.format("Enter start position for the %s (size: %s)", ship.getName(), ship.getSize()),  ship.getSize());
            ship.addShipPart(positionInput);

        }
    }

    protected static void InitializeEnemyFleet() {
        enemyFleet = GameController.initializeShips();
        GameController.generateShipsRandomPositions(enemyFleet, GameController.boardWidth, GameController.boardHeight);
    }


    private static void printFleetState(List<Ship> fleet) {
        Optional<Integer> maxNameLength = fleet.stream().map(x -> x.getName().length()).max(Integer::compare);
        int length = maxNameLength.orElse(3);

        for (Ship ship : fleet) {
            console.println(" " + String.format("%1$" + length + "s", ship.getName()) + " (" + ship.getSize() + ") -> " + formatShipState(ship));
        }
    }

    private static String formatShipState(Ship ship) {
        return ship.isSunk() ? Color.RED.getColoredText("sunk") : Color.GREEN.getColoredText("still operational");
    }
}

