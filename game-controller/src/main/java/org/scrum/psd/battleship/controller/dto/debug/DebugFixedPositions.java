package org.scrum.psd.battleship.controller.dto.debug;

import org.scrum.psd.battleship.controller.GameController;
import org.scrum.psd.battleship.controller.dto.Letter;
import org.scrum.psd.battleship.controller.dto.Position;
import org.scrum.psd.battleship.controller.dto.ShipPosition;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Create a list of fixed positions for convenience of debugging
 * - for fixed ships placement
 * - for fixed set of shoots (computer moves)
 */
public class DebugFixedPositions {
    public static final String DEFAULT_FIXED_POSITIONS = "a1v c1v e1v g1v e7v";
    public static final String DEFAULT_MISSES = "a7 b6 c5 e4 h2";

    public final List<Position> positions;
    private Iterator<Position> iterator;

    public DebugFixedPositions(String positions) {
        this.positions = Collections.unmodifiableList(
                Arrays.stream(positions.split(" "))
                        .map(x -> new Position(Letter.values()[x.charAt(0) - 'a'], Integer.parseInt(x.substring(1))))
                        .collect(Collectors.toList()));
        this.iterator = this.positions.iterator();
    }

    public Position next() {
        if (!this.iterator.hasNext()) {
            this.iterator = this.positions.iterator();
        }
        return this.iterator.next();
    }

    public static List<ShipPosition> getDefaultFleetSetupPositions() {
        return Arrays.stream(DEFAULT_FIXED_POSITIONS.split(" ")).map(GameController::parseShipPosition).collect(Collectors.toList());
    }

    public static DebugFixedPositions getDefaultFleetSetupPositionsAtRandomIncludingMisses() {
        List<String> positions = Arrays.asList((DEFAULT_FIXED_POSITIONS + " " + DEFAULT_MISSES).replaceAll("[hv]\\b", "").split(" "));
        Collections.shuffle(positions);
        return new DebugFixedPositions(String.join(" ", positions));
    }
}
