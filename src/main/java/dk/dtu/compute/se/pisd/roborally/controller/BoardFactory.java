package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 * A factory for creating boards. The factory itself is implemented as a singleton.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
// XXX A3: might be used for creating a first slightly more interesting board.
public class BoardFactory {

    /**
     * The single instance of this class, which is lazily instantiated on demand.
     */
    static private BoardFactory instance = null;

    /**
     * Constructor for BoardFactory. It is private in order to make the factory a singleton.
     */
    private BoardFactory() {
    }

    /**
     * Returns the single instance of this factory. The instance is lazily
     * instantiated when requested for the first time.
     *
     * @return the single instance of the BoardFactory
     */
    public static BoardFactory getInstance() {
        if (instance == null) {
            instance = new BoardFactory();
        }
        return instance;
    }

    /**
     * Creates a new board of given name of a board, which indicates
     * which type of board should be created. For now the name is ignored.
     *
     * @param name the given name board
     * @return the new board corresponding to that name
     */
    public Board createBoard(String name) {

        Board board;
        if ("Simple".equals(name)) {
            board = createSimpleBoard();
        } else if ("Advanced".equals(name)) {
            board = createAdvancedBoard();
        } else {
            board = new Board(8,8, "<none>");
        }

        return board;
    }
    /**
     * Creates a simple game board for testing or initialization.
     * @return A new {@link Board} with default configurations.
     * @see Board
     */
    private Board createSimpleBoard() {
        Board board = new Board(6,6, "Simple");
        return simpleBoard(board);
    }
    /**
     * Creates an advanced game board with complex configurations.
     * @return A new {@link Board} with advanced setups.
     * @see Board
     */
    private Board createAdvancedBoard() {
        Board board = new Board(12,6, "Advanced");
        return advancedBoard(board);
        // extra for new features for advanced board
    }
    /**
     * Adds walls and conveyor belts to the provided board.
     * @param board The board to modify. Must not be {@code null}.
     * @return The modified board with walls and belts added.
     * @see Board
     */
    private Board simpleBoard(Board board) {

        Space space = board.getSpace(0,0);
        space.getWalls().add(Heading.SOUTH);
        ConveyorBelt action = new ConveyorBelt();
        action.setHeading(Heading.WEST);
        space.getActions().add(action);

        space = board.getSpace(1,0);
        action = new ConveyorBelt();
        action.setHeading(Heading.SOUTH);
        space.getActions().add(action);

        space = board.getSpace(4,0);
        space.getWalls().add(Heading.EAST);

        space = board.getSpace(3,1);
        action = new ConveyorBelt();
        action.setHeading(Heading.WEST);
        space.getActions().add(action);

        space = board.getSpace(4,1);
        Checkpoint action1  = new Checkpoint();
        action1.setNumber(2);
        action1.setLast(true);
        space.getActions().add(action1);

        space = board.getSpace(1,2);
        space.getWalls().add(Heading.EAST);

        space = board.getSpace(4,2);
        action = new ConveyorBelt();
        action.setHeading(Heading.SOUTH);
        space.getActions().add(action);

        space = board.getSpace(1,3);
        action = new ConveyorBelt();
        action.setHeading(Heading.NORTH);
        space.getActions().add(action);

        space = board.getSpace(3,3);
        space.getWalls().add(Heading.EAST);

        space = board.getSpace(1,4);
        action1  = new Checkpoint();
        action1.setNumber(1);
        space.getActions().add(action1);

        space = board.getSpace(2,4);
        action = new ConveyorBelt();
        action.setHeading(Heading.EAST);
        space.getActions().add(action);

        space = board.getSpace(0,5);
        space.getWalls().add(Heading.EAST);

        space = board.getSpace(4,5);
        action = new ConveyorBelt();
        action.setHeading(Heading.NORTH);
        space.getActions().add(action);

        space = board.getSpace(5,5);
        space.getWalls().add(Heading.NORTH);
        action = new ConveyorBelt();
        action.setHeading(Heading.EAST);
        space.getActions().add(action);


        return board;
    }

    /**
     * Creates an advanced game board with more complex configurations. This board has a larger
     * size and additional features compared to the simple board.
     *
     * @return A new advanced board.
     */
    private Board advancedBoard(Board board) {


        Space space = board.getSpace(0,0);
        space.getWalls().add(Heading.SOUTH);
        ConveyorBelt action = new ConveyorBelt();
        action.setHeading(Heading.WEST);
        space.getActions().add(action);

        space = board.getSpace(1,0);
        action = new ConveyorBelt();
        action.setHeading(Heading.SOUTH);
        space.getActions().add(action);

        space = board.getSpace(4,0);
        space.getWalls().add(Heading.EAST);

        space = board.getSpace(3,1);
        action = new ConveyorBelt();
        action.setHeading(Heading.WEST);
        space.getActions().add(action);

        space = board.getSpace(4,1);
        Checkpoint action1  = new Checkpoint();
        action1.setNumber(2);
        space.getActions().add(action1);

        space = board.getSpace(1,2);
        space.getWalls().add(Heading.EAST);

        space = board.getSpace(4,2);
        action = new ConveyorBelt();
        action.setHeading(Heading.SOUTH);
        space.getActions().add(action);

        space = board.getSpace(1,3);
        action = new ConveyorBelt();
        action.setHeading(Heading.NORTH);
        space.getActions().add(action);

        space = board.getSpace(3,3);
        space.getWalls().add(Heading.EAST);

        space = board.getSpace(1,4);
        action1  = new Checkpoint();
        action1.setNumber(1);
        space.getActions().add(action1);

        space = board.getSpace(2,4);
        action = new ConveyorBelt();
        action.setHeading(Heading.EAST);
        space.getActions().add(action);

        space = board.getSpace(0,5);
        space.getWalls().add(Heading.EAST);

        space = board.getSpace(4,5);
        action = new ConveyorBelt();
        action.setHeading(Heading.NORTH);
        space.getActions().add(action);

        space = board.getSpace(5,5);
        space.getWalls().add(Heading.NORTH);
        action = new ConveyorBelt();
        action.setHeading(Heading.EAST);
        space.getActions().add(action);


        space = board.getSpace(6,0);
        space.getWalls().add(Heading.SOUTH);
        action = new ConveyorBelt();
        action.setHeading(Heading.WEST);
        space.getActions().add(action);

        space = board.getSpace(7,0);
        action = new ConveyorBelt();
        action.setHeading(Heading.SOUTH);
        space.getActions().add(action);

        space = board.getSpace(10,0);
        space.getWalls().add(Heading.EAST);

        space = board.getSpace(9,1);
        action = new ConveyorBelt();
        action.setHeading(Heading.WEST);
        space.getActions().add(action);

        space = board.getSpace(10,1);
        action1  = new Checkpoint();
        action1.setNumber(3);
        space.getActions().add(action1);

        space = board.getSpace(7,2);
        space.getWalls().add(Heading.EAST);

        space = board.getSpace(10,2);
        action = new ConveyorBelt();
        action.setHeading(Heading.SOUTH);
        space.getActions().add(action);

        space = board.getSpace(7,3);
        action = new ConveyorBelt();
        action.setHeading(Heading.NORTH);
        space.getActions().add(action);

        space = board.getSpace(9,3);
        space.getWalls().add(Heading.EAST);

        space = board.getSpace(7,4);
        action1  = new Checkpoint();
        action1.setNumber(4);
        action1.setLast(true);
        space.getActions().add(action1);

        space = board.getSpace(8,4);
        action = new ConveyorBelt();
        action.setHeading(Heading.EAST);
        space.getActions().add(action);

        space = board.getSpace(6,5);
        space.getWalls().add(Heading.EAST);

        space = board.getSpace(10,5);
        action = new ConveyorBelt();
        action.setHeading(Heading.NORTH);
        space.getActions().add(action);

        space = board.getSpace(11,5);
        space.getWalls().add(Heading.NORTH);
        action = new ConveyorBelt();
        action.setHeading(Heading.EAST);
        space.getActions().add(action);

        return board;
    }
}
