package org.scrum.psd.battleship.ascii;

import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi;
import org.scrum.psd.battleship.controller.GameController;
import org.scrum.psd.battleship.controller.dto.Color;
import org.scrum.psd.battleship.controller.dto.Letter;
import org.scrum.psd.battleship.controller.dto.Position;
import org.scrum.psd.battleship.controller.dto.Ship;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private static List<Ship> myFleet;
    protected static List<Ship> enemyFleet;
    private static ColoredPrinter console;

    // set by env property "debug=true" (java -jar xyz.jar -Ddebug=true), used for fast startup
    private static final boolean debugMode = System.getenv("debug") != null && Boolean.parseBoolean(System.getenv("debug"));
    private static final DebugFixedPositions debugEnemyShootsOnAllFixedPositions = DebugFixedPositions.getDefaultFleetSetupPositionsAtRandom();

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
        console.println(Color.PURPLE.getColoredText("  |     /_'"));
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

        do {
            // step: player shoots
            printCanon();
            printNewline();
            console.println("Player, it's your turn");

            Position position = getInput(scanner, "Enter coordinates for your shot :");
            printSeparator();
            boolean isHit = GameController.checkIsHit(enemyFleet, position);
            if (isHit) {
                beep();
                printBang();
            }

            console.println(GameController.getHitMessage(isHit, true));
            printNewline();

            printEnemyFleetState();
            printNewline();
            console.println("It's the enemy's turn... Brace yourself for impact!");
            printSeparator();

            // check gameover
            if (GameController.isFleetDestroyed(enemyFleet)) {
                printPlayerWins();
                printSeparator();
                System.exit(0);
            }

            // step: enemy shoots
            position = enemyShoots();
            isHit = GameController.checkIsHit(myFleet, position);
            printNewline();

            if (isHit) {
                beep();
                printBang();

                printNewline();
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

            printSeparator();
        } while (true);
    }

    private static Position enemyShoots() {
        if (!debugMode) {
            return getRandomPosition();
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
        console.println(Color.RED.getColoredText(" ░█▀▀ ░█▀█ ░█ ░█▀▀ ░░█▀▀ ░█▀█ ░█ ░█"));
        console.println(Color.RED.getColoredText(" ░█▀▀ ░█▀▀ ░█ ░█ ░░░░█▀▀ ░█▀█ ░█ ░█"));
        console.println(Color.RED.getColoredText(" ░▀▀▀ ░▀ ░░░▀ ░▀▀▀ ░░▀ ░░░▀░▀ ░▀ ░▀▀▀"));
        printNewline();
        console.println(Color.RED.getColoredText("            YOU LOOSE !!!"));
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
        if (!debugMode) {
            InitializeMyFleet();
            InitializeEnemyFleet();
        } else {
            FixedPositionInitializeMyFleet();
            FixedPositionInitializeEnemyFleet();
        }
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
     * @param fleet
     */
    private static void placeShipsInFleetOnFixedPositions(List<Ship> fleet) {
        DebugFixedPositions fixedPositions = DebugFixedPositions.getDefaultFleetSetupPositions();
        for (Ship ship : fleet) {
            for (int j = 0; j < ship.getSize(); j++) {
                ship.getPositions().add(fixedPositions.next());
            }
        }
    }

    private static void InitializeMyFleet() {
        Scanner scanner = new Scanner(System.in);
        myFleet = GameController.initializeShips();

        console.println("Please position your fleet (Game board has size from A to H and 1 to 8) :");

        for (Ship ship : myFleet) {
            printNewline();
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

/**
 * Create a list of fixed positions for convenience of debugging
 * - for fixed ships placement
 * - for fixed set of shoots (computer moves)
 */
class DebugFixedPositions {
    public static final String DEFAULT_FIXED_POSITIONS = "a1 a2 a3 a4 a5 b1 b2 b3 b4 c1 c2 c3 d1 d2 d3 e1 e2";

    public final List<Position> positions;
    private Iterator<Position> iterator;

    public DebugFixedPositions(String positions) {
        this.positions = Collections.unmodifiableList(
                Arrays.stream(positions.split(" "))
                .map(x -> new Position(Letter.getByIndex(x.charAt(0)-'a'), Integer.parseInt(x.substring(1))))
                .collect(Collectors.toList()));
        this.iterator = this.positions.iterator();
    }

    public Position next() {
        if (!this.iterator.hasNext()) {
            this.iterator = this.positions.iterator();
        }
        return this.iterator.next();
    }

    public static DebugFixedPositions getDefaultFleetSetupPositions() {
        return new DebugFixedPositions(
                DEFAULT_FIXED_POSITIONS
        );
    };

    public static DebugFixedPositions getDefaultFleetSetupPositionsAtRandom() {
        List<String> positions = Arrays.asList(DEFAULT_FIXED_POSITIONS.split(" "));
        Collections.shuffle(positions);
        return new DebugFixedPositions(String.join(" ", positions));
    };
}
