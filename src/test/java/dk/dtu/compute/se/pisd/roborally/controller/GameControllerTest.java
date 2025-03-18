package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameControllerTest {

    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;

    private GameController gameController;

    @BeforeEach
    void setUp() {
        Board board = new Board(TEST_WIDTH, TEST_HEIGHT);
        gameController = new GameController(board);
        for (int i = 0; i < 6; i++) {
            Player player = new Player(board, null,"Player " + i);
            board.addPlayer(player);
            player.setSpace(board.getSpace(i, i));
            player.setHeading(Heading.values()[i % Heading.values().length]);
        }
        board.setCurrentPlayer(board.getPlayer(0));
    }

    @AfterEach
    void tearDown() {
        gameController = null;
    }

    /**
     * Test for Assignment V1 (can be deleted later once V1 was shown to the teacher)
     */
    @Test
    void testV1() {
        Board board = gameController.board;

        Player player = board.getCurrentPlayer();
        gameController.moveCurrentPlayerToSpace(board.getSpace(0, 4));

        Assertions.assertEquals(player, board.getSpace(0, 4).getPlayer(), "Player " + player.getName() + " should be on Space (0,4)!");
    }


        //The following tests should be used later for assignment V2

    @Test
    void moveCurrentPlayerToSpace() {
        Board board = gameController.board;
        Player player1 = board.getPlayer(0);
        Player player2 = board.getPlayer(1);

        gameController.moveCurrentPlayerToSpace(board.getSpace(0, 4));

        Assertions.assertEquals(player1, board.getSpace(0, 4).getPlayer(), "Player " + player1.getName() + " should beSpace (0,4)!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
        Assertions.assertEquals(player2, board.getCurrentPlayer(), "Current player should be " + player2.getName() +"!");
    }

    @Test
    void moveForward() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.moveForward(current);

        Assertions.assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should beSpace (0,1)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
    }

    @Test
    void fastForward(){
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.fastForward(current);

        Assertions.assertEquals(current, board.getSpace(0, 2).getPlayer(), "Player " + current.getName() + " should beSpace (0,2)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
    }

    @Test
    void turnLeft(){
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.turnLeft(current);

        Assertions.assertEquals(Heading.EAST, current.getHeading(), "Player 0 should be heading EAST!");
    }

    @Test
    void turnRight() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.turnRight(current);

        Assertions.assertEquals(Heading.WEST, current.getHeading(), "Player 0 should be heading WEST!");
    }

    @Test
    void moveBackward() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.moveBackward(current);

        Assertions.assertEquals(current, board.getSpace(0, 7).getPlayer(), "Player " + current.getName() + " should beSpace (0,7)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
    }

    @Test
    void uTurn() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.uTurn(current);

        Assertions.assertEquals(Heading.NORTH, current.getHeading(), "Player 0 should be heading NORTH!");
    }

    @Test
    void l_button() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.l_button(current);

        Assertions.assertEquals(Heading.EAST, current.getHeading(), "Player 0 should be heading EAST!");
    }

    @Test
    void r_button() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.r_button(current);

        Assertions.assertEquals(Heading.WEST, current.getHeading(), "Player 0 should be heading WEST!");
    }



    @Test
    void testWalls() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        current.setSpace(board.getSpace(0, 0));
        current.setHeading(Heading.EAST);

        Space wallSpace = board.getSpace(1, 0);
        wallSpace.getWalls().add(Heading.WEST);

        gameController.moveForward(current);

        Assertions.assertEquals(current, board.getSpace(0, 0).getPlayer(),
                "Player " + current.getName() + " should still be on Space (0,0)!");

        Assertions.assertNull(wallSpace.getPlayer(),
                "Space (1,0) should be empty because the wall blocked the player!");
    }
    @Test
    void testWallNorth() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        current.setSpace(board.getSpace(1, 1));
        current.setHeading(Heading.NORTH);

        Space wallSpace = board.getSpace(1, 0);
        wallSpace.getWalls().add(Heading.SOUTH);

        gameController.moveForward(current);

        Assertions.assertEquals(current, board.getSpace(1, 1).getPlayer(),
                "Player " + current.getName() + " should still be on Space (1,1)!");

        Assertions.assertNull(wallSpace.getPlayer(),
                "Space (1,0) should be empty because the wall blocked the player!");
    }
    @Test
    void testMultipleWalls() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        current.setSpace(board.getSpace(2, 2));
        current.setHeading(Heading.SOUTH);

        Space wallSpaceSouth = board.getSpace(2, 3);
        wallSpaceSouth.getWalls().add(Heading.NORTH);

        Space wallSpaceEast = board.getSpace(3, 2);
        wallSpaceEast.getWalls().add(Heading.WEST);

        gameController.moveForward(current);

        Assertions.assertEquals(current, board.getSpace(2, 2).getPlayer(),
                "Player " + current.getName() + " should still be on Space (2,2)!");

        current.setHeading(Heading.EAST);
        gameController.moveForward(current);

        Assertions.assertEquals(current, board.getSpace(2, 2).getPlayer(),
                "Player " + current.getName() + " should still be on Space (2,2)!");
    }
    @Test
    void testConveyorBelts() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        Space space = board.getSpace(0,0);
        ConveyorBelt action1 = new ConveyorBelt();
        action1.setHeading(Heading.SOUTH);
        space.getActions().add(action1);

        Assertions.assertEquals(current, board.getSpace(0, 0).getPlayer(), "Player " + current.getName() + " should beSpace (0,0)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        gameController.doAllAction();
        Assertions.assertEquals(current, board.getSpace(0,1).getPlayer(), "Space (0,1) should be empty!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertEquals(action1.getHeading(),Heading.SOUTH);
    }

    @Test
    void testCheckpoints() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        Space space = board.getSpace(0,1);
        Checkpoint action1  = new Checkpoint();
        action1.setNumber(1);
        action1.setLast(true);
        space.getActions().add(action1);

        gameController.moveForward(current);
        gameController.doAllAction();
        Assertions.assertEquals(1, current.getCurrentCheckpoint());
        Assertions.assertTrue(action1.isLast);
        Assertions.assertEquals(1, action1.getNumber());
        Assertions.assertTrue(action1.getLast());
    }

    // TODO and there should be more tests added for the different assignments eventually

    /**
     * Test case for the startProgrammingPhase method. This test verifies that:
     * - The game phase is set to PROGRAMMING.
     * - The current player is reset to the first player.
     * - The step counter is reset to 0.
     * - All program fields are cleared.
     * - Random command cards are generated for each player's hand.
     */
    @Test
    void testStartProgrammingPhase() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.startProgrammingPhase();

        Assertions.assertEquals(Phase.PROGRAMMING, board.getPhase(),
                "The game phase should be PROGRAMMING after starting the programming phase!");

        Assertions.assertEquals(board.getPlayer(0), board.getCurrentPlayer(),
                "The current player should be reset to the first player!");

        Assertions.assertEquals(0, board.getStep(),
                "The step counter should be reset to 0!");

        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            for (int j = 0; j < Player.NO_REGISTERS; j++) {
                Assertions.assertNull(player.getProgramField(j).getCard(),
                        "Program field " + j + " for player " + player.getName() + " should be cleared!");
            }
        }

        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            for (int j = 0; j < Player.NO_CARDS; j++) {
                Assertions.assertNotNull(player.getCardField(j).getCard(),
                        "Card field " + j + " for player " + player.getName() + " should have a random command card!");
            }
        }
    }
    /**
     * Test case for the finishProgrammingPhase method. This test verifies that:
     * - The game phase is set to ACTIVATION.
     * - The current player is reset to the first player.
     * - The step counter is reset to 0.
     * - All program fields are hidden except for the first register's fields.
     */
    @Test
    void testFinishProgrammingPhase() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.startProgrammingPhase();

        gameController.finishProgrammingPhase();

        Assertions.assertEquals(Phase.ACTIVATION, board.getPhase(),
                "The game phase should be ACTIVATION after finishing the programming phase!");

        Assertions.assertEquals(board.getPlayer(0), board.getCurrentPlayer(),
                "The current player should be reset to the first player!");

        Assertions.assertEquals(0, board.getStep(),
                "The step counter should be reset to 0!");

        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            for (int j = 0; j < Player.NO_REGISTERS; j++) {
                if (j == 0) {
                    Assertions.assertTrue(player.getProgramField(j).isVisible(),
                            "Program field " + j + " for player " + player.getName() + " should be visible!");
                } else {
                    Assertions.assertFalse(player.getProgramField(j).isVisible(),
                            "Program field " + j + " for player " + player.getName() + " should be hidden!");
                }
            }
        }
    }
    /**
     * Test case for the executeNextStep method. This test verifies that:
     * - The current player's command is executed correctly.
     */
    @Test
    void testExecuteNextStep() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.startProgrammingPhase();

        Player player1 = board.getPlayer(0);
        player1.getProgramField(0).setCard(new CommandCard(Command.FORWARD));
        player1.getProgramField(1).setCard(new CommandCard(Command.RIGHT));
        player1.getProgramField(2).setCard(new CommandCard(Command.FORWARD));

        Player player2 = board.getPlayer(1);
        player2.getProgramField(0).setCard(new CommandCard(Command.LEFT));
        player2.getProgramField(1).setCard(new CommandCard(Command.FORWARD));
        player2.getProgramField(2).setCard(new CommandCard(Command.FAST_FORWARD));

        // Finish the programming phase
        gameController.finishProgrammingPhase();

        // Execute the first step
        gameController.executeNextStep();

        // Assert that the first player's command was executed
        Assertions.assertEquals(player1, board.getSpace(0, 1).getPlayer(),
                "Player 1 should be on Space (0,1) after executing FORWARD command!");

        // Assert that the current player is now the second player
        Assertions.assertEquals(player2, board.getCurrentPlayer(),
                "The current player should be Player 2 after executing the first step!");

        // Execute the second step
        gameController.executeNextStep();

        // Assert that the second player's command was executed
        Assertions.assertEquals(player2, board.getSpace(1, 1).getPlayer(),
                "Player 2 should be on Space (1,1) after executing LEFT command!");


    }
}