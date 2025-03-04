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

    private Board createSimpleBoard() {
        Board board = new Board(8,8, "Simple");
        return addWallsAndBelts(board);
    }

    private Board createAdvancedBoard() {
        Board board = new Board(9,9, "Simple");
        return addWallsAndBelts(board);
        // extra for new features for advanced board
    }

    private Board addWallsAndBelts(Board board) {

        Space space = board.getSpace(0,0);
        space.getWalls().add(Heading.SOUTH);
        ConveyorBelt action = new ConveyorBelt();
        action.setHeading(Heading.WEST);
        space.getActions().add(action);

        space = board.getSpace(1,0);
        space.getWalls().add(Heading.NORTH);
        action = new ConveyorBelt();
        action.setHeading(Heading.WEST);
        space.getActions().add(action);

        space = board.getSpace(1,1);
        space.getWalls().add(Heading.WEST);
        action = new ConveyorBelt();
        action.setHeading(Heading.NORTH);
        space.getActions().add(action);

        Checkpoint action1;
        space = board.getSpace(1,4);
        action1  = new Checkpoint();
        action1.setNumber(1);
        space.getActions().add(action1);

        space = board.getSpace(4,4);
        action1  = new Checkpoint();
        action1.setNumber(2);
        space.getActions().add(action1);

        space = board.getSpace(5,5);
        space.getWalls().add(Heading.SOUTH);
        action = new ConveyorBelt();
        action.setHeading(Heading.WEST);
        space.getActions().add(action);

        space = board.getSpace(6,5);
        action = new ConveyorBelt();
        action.setHeading(Heading.WEST);
        space.getActions().add(action);

        return board;
    }
}
