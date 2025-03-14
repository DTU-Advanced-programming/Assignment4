/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.*;
import org.jetbrains.annotations.NotNull;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class GameController {

    final public Board board;

    public GameController(@NotNull Board board) {
        this.board = board;
    }

    /**
     * This is just some dummy controller operation to make a simple move to see something
     * happening on the board. This method should eventually be deleted!
     *
     * @param space the space to which the current player should move
     */
    public void moveCurrentPlayerToSpace(@NotNull Space space)  {
        // TODO V1: method should be implemented by the students:
        //   - the current player should be moved to the given space
        //     (if it is free())
        //   - and the current player should be set to the player
        //     following the current player
        //   - the counter of moves in the game should be increased by one
        //     if and when the player is moved (the counter and the status line
        //     message needs to be implemented at another place)

        if (space.getPlayer() != null) {
            // if no other player is present here, ignoring walls and other things
            return;
        }
        Player curr = board.getCurrentPlayer();
        curr.setSpace(space);
        board.setCurrentPlayer(board.getPlayer((board.getPlayerNumber(curr)+1)% board.getPlayersNumber()));
        board.setCounter(board.getCounter()+1);
    }


    /**
     * Starts the programming phase of the game.
     *
     * Sets the game phase to {@link Phase#PROGRAMMING}, resets the current player to the first player,
     * and initializes the step counter to 0. Clears all program fields and generates random command cards
     * for each player's hand.
     * This method prepares the game for players to program their robots by selecting and placing
     * command cards into their program registers.
     *
     * @see Phase
     * @see Player
     * @see CommandCardField
     * @see CommandCard
     * @see #generateRandomCommandCard()
     */
    public void startProgrammingPhase() {
        board.setPhase(Phase.PROGRAMMING);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);

        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            if (player != null) {
                for (int j = 0; j < Player.NO_REGISTERS; j++) {
                    CommandCardField field = player.getProgramField(j);
                    field.setCard(null);
                    field.setVisible(true);
                }
                for (int j = 0; j < Player.NO_CARDS; j++) {
                    CommandCardField field = player.getCardField(j);
                    field.setCard(generateRandomCommandCard());
                    field.setVisible(true);
                }
            }
        }
    }

    /**
     * Generates a random command card for use in the game.
     *
     * This method selects a random command from the available commands in the {@link Command} enum
     * and creates a new {@link CommandCard} with the selected command.
     * @return A randomly generated {@link CommandCard}.
     * @see Command
     * @see CommandCard
     */
    private CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);
    }

    /**
     * Ends the programming phase and transitions the game to the activation phase.
     *
     * This method hides all program fields, makes the first register's fields visible, and sets the game
     * phase to {@link Phase#ACTIVATION}. It also resets the current player and step counter to prepare
     * for the execution of the programmed commands.
     * @see Phase
     * @see #makeProgramFieldsInvisible()
     * @see #makeProgramFieldsVisible(int)
     */
    public void finishProgrammingPhase() {
        makeProgramFieldsInvisible();
        makeProgramFieldsVisible(0);
        board.setPhase(Phase.ACTIVATION);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);
    }

    /**
     * Makes the program fields visible for a specific register.
     *
     * This method ensures that the program fields for the specified register are visible for all players.
     * The register must be a valid index (between 0 and {@link Player#NO_REGISTERS} - 1).
     * @param register The index of the register whose program fields should be made visible.
     * Must be a valid register index (0 <= register < {@link Player#NO_REGISTERS}).
     * @see Player
     * @see CommandCardField
     */
    private void makeProgramFieldsVisible(int register) {
        if (register >= 0 && register < Player.NO_REGISTERS) {
            for (int i = 0; i < board.getPlayersNumber(); i++) {
                Player player = board.getPlayer(i);
                CommandCardField field = player.getProgramField(register);
                field.setVisible(true);
            }
        }
    }

    /**
     * Hides all program fields for all players.
     *
     * This method iterates through all players and sets the visibility of their program fields
     * to false. This is typically used at the end of the programming phase or during transitions
     * between game phases to hide the program fields.
     *
     * @see Player
     * @see CommandCardField
     */
    private void makeProgramFieldsInvisible() {
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            for (int j = 0; j < Player.NO_REGISTERS; j++) {
                CommandCardField field = player.getProgramField(j);
                field.setVisible(false);
            }
        }
    }

    // XXX V2
    public void executePrograms() {
        board.setStepMode(false);
        continuePrograms();
    }

    // XXX V2
    public void executeStep() {
        board.setStepMode(true);
        continuePrograms();
    }

    // XXX V2
    private void continuePrograms() {
        do {
            executeNextStep();
        } while (board.getPhase() == Phase.ACTIVATION && !board.isStepMode());
    }

    /**
     * Executes the next step in the program for the current player.
     *
     * This method retrieves the command card for the current step of the current player and executes
     * the associated command. It then advances to the next player or step, depending on the game state.
     * If all steps are completed, the game transitions back to the programming phase.
     *
     * @see Player
     * @see CommandCard
     * @see Command
     * @see #executeCommand(Player, Command)
     * @see #startProgrammingPhase()
     */
    private void executeNextStep() {
        Player currentPlayer = board.getCurrentPlayer();
        if ((board.getPhase() == Phase.ACTIVATION || board.getPhase() == Phase.INTERACTED)&& currentPlayer != null) {
            int step = board.getStep();
            if (step >= 0 && step < Player.NO_REGISTERS) {
                CommandCard card = currentPlayer.getProgramField(step).getCard();
                if (board.getPhase() != Phase.INTERACTED && card != null) {
                    if (card.command != Command.LEFT_OR_RIGHT) {
                        Command command = card.command;
                        executeCommand(currentPlayer, command);
                    } else {
                        board.setPhase(Phase.PLAYER_INTERACTION);
                        return;
                    }

                }
                int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
                    board.setPhase(Phase.ACTIVATION);
                if (nextPlayerNumber < board.getPlayersNumber()) {
                    board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
                } else {
                    doAllAction();
                    step++;
                    if (step < Player.NO_REGISTERS) {
                        makeProgramFieldsVisible(step);
                        board.setStep(step);
                        board.setCurrentPlayer(board.getPlayer(0));
                    } else {
                        startProgrammingPhase();
                    }
                }
            } else {
                // this should not happen
                assert false;
            }
        } else {
            // this should not happen
            assert false;
        }
    }

    private void doAllAction(){
        for (int x = 0; x<board.width; x++) {
            for (int y =0; y < board.height; y++) {
                Space space = board.getSpace(x, y);
                for (FieldAction fa : space.getActions()) {
                    fa.doAction(this, space);
                }
            }
        }
    }

    private void executeInteractiveCommand(@NotNull Player player, Command command) {
        if (player != null && player.board == board && command != null) {
            // for now only Left_or_Right, later case statements can be added

        }
    }

    /**
     * Executes a specific command for the given player.
     * This method performs the action associated with the provided command, such as moving the player
     * forward, turning left or right, or performing a fast forward. The command is executed only if
     * the player and command are valid.
     * @param player  The player for whom the command is executed. Must not be {@code null}.
     * @param command The command to execute. Must not be {@code null}.
     *
     * @see Player
     * @see Command
     * @see #moveForward(Player)
     * @see #turnRight(Player)
     * @see #turnLeft(Player)
     * @see #fastForward(Player)
     */
    private void executeCommand(@NotNull Player player, Command command) {
        if (player != null && player.board == board && command != null) {
            // XXX This is a very simplistic way of dealing with some basic cards and
            //     their execution. This should eventually be done in a more elegant way
            //     (this concerns the way cards are modelled as well as the way they are executed).

            switch (command) {
                case FORWARD:
                    this.moveForward(player);
                    break;
                case RIGHT:
                    this.turnRight(player);
                    break;
                case LEFT:
                    this.turnLeft(player);
                    break;
                case FAST_FORWARD:
                    this.fastForward(player);
                    break;
                case BACKWARD:
                    this.moveBackward(player);
                    break;
                case uTURN:
                    this.uTurn(player);
                    break;
                default:
                    // DO NOTHING (for now)
            }
        }
    }
    /**
     * Moves the player forward one space in their current heading, if no wall blocks the movement.
     *
     * @param player The player to move. Must not be {@code null}.
     *
     * @see Player
     * @see Space
     */
    public void moveForward(@NotNull Player player) {
        Space newSpace = board.getNeighbour(player.getSpace(),player.getHeading());
        try {
            moveToSpace(player, newSpace, player.getHeading());
        } catch (ImpossibleMoveException e) {
            // when pushing not possible due to wall
            System.out.println(e.getMessage() + e.player + "to move fwd");
        }

    }

    /**
     * Moves the player to the specified space.
     *
     * @param player The player to move. Must not be {@code null}.
     * @param space  The space to move the player to. Must not be {@code null}.
     * @see Player
     * @see Space
     */
    public void moveToSpace(@NotNull Player player, Space space, Heading heading) throws ImpossibleMoveException{
        if (space == null) {return;}    //for walls
        if (space.getPlayer() != null) {
            Space newSpace  = board.getNeighbour(space, heading);
            if (newSpace != null) {
                moveToSpace(space.getPlayer(), newSpace, heading);
            } else { throw new ImpossibleMoveException("Invalid move by: ",player); }
        }
        player.setSpace(space);
    }
    /**
     * Moves the player forward two spaces in their current heading, if no walls block the movement.
     *
     * @param player The player to move. Must not be {@code null}.
     * @see Player
     * @see #moveForward(Player)
     */
    public void fastForward(@NotNull Player player) {
        moveForward(player);
        moveForward(player);
    }
    /**
     * Rotates the player 90 degrees to the right (clockwise).
     * @param player The player to rotate. Must not be {@code null}.
     * @see Player
     * @see Heading#next()
     */
    public void turnRight(@NotNull Player player) {
        player.setHeading(player.getHeading().next());
    }
    /**
     * Rotates the player 90 degrees to the left (counter-clockwise).
     * @param player The player to rotate. Must not be {@code null}.
     * @see Player
     * @see Heading#prev()
     */
    public void turnLeft(@NotNull Player player) {
        player.setHeading(player.getHeading().prev());
    }
    /**
     * Moves the player backward one space in the opposite of their current heading, if no walls block the movement.
     * @param player The player to move. Must not be {@code null}.
     * @see Player
     * @see #moveForward(Player)
     */
    public void moveBackward(@NotNull Player player) {
        Space newSpace = board.getNeighbour(player.getSpace(),player.getHeading().next().next());
        try {
            moveToSpace(player, newSpace, player.getHeading().next().next());
        } catch (ImpossibleMoveException e) {
            // when pushing not possible due to wall
            System.out.println(e.getMessage() + e.player + "during bwd movement");
        }
    }
    /**
     * Rotates the player 180 degrees, effectively reversing their heading.
     * @param player The player to rotate. Must not be {@code null}.
     * @see Player
     * @see Heading#next()
     */
    public void uTurn(@NotNull Player player) {
        player.setHeading(player.getHeading().next().next());
    }

    public void l_button(@NotNull Player player) {
        turnLeft(player);
        board.setPhase(Phase.INTERACTED);
        continuePrograms();
    }

    public void r_button(@NotNull Player player) {
        turnRight(player);
        board.setPhase(Phase.INTERACTED);
        continuePrograms();
    }

    public boolean moveCards(@NotNull CommandCardField source, @NotNull CommandCardField target) {
        CommandCard sourceCard = source.getCard();
        CommandCard targetCard = target.getCard();
        if (sourceCard != null && targetCard == null) {
            target.setCard(sourceCard);
            source.setCard(null);
            return true;
        } else {
            return false;
        }
    }

    public static class ImpossibleMoveException extends ReflectiveOperationException {

        final public Player player;

        public ImpossibleMoveException(String message, Player player) {
            super(message);
            this.player = player;
        }
    }

    /**
     * A method called when no corresponding controller operation is implemented yet.
     * This should eventually be removed.
     */
    public void notImplemented() {
        // XXX just for now to indicate that the actual method is not yet implemented
        assert false;
    }

}
