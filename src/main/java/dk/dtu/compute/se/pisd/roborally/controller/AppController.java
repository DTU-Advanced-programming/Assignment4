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

import dk.dtu.compute.se.pisd.designpatterns.observer.Observer;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Phase;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.Dialog;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * The main controller for the RoboRally application. This class manages the game's lifecycle,
 * including starting new games, saving/loading games, stopping the game, and exiting the application.
 * It also implements the Observer interface to observe changes in the game state.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class AppController implements Observer {

    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);
    final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey", "magenta");
    final private List<String> BOARD_OPTIONS = Arrays.asList("Simple", "Advanced");

    final private RoboRally roboRally;

    private GameController gameController;

    public AppController(@NotNull RoboRally roboRally) {
        this.roboRally = roboRally;
    }
    /**
     * Starts a new game by resetting the board and initializing players.
     * This method prepares the game for a fresh start, typically by creating a new board,
     * resetting players, and setting the initial game phase.
     * @see Board
     * @see Player
     */
    public void newGame() {
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
        dialog.setTitle("Player number");
        dialog.setHeaderText("Select number of players");
        Optional<Integer> result = dialog.showAndWait();

        ChoiceDialog<String> dialog2 = new ChoiceDialog<>(BOARD_OPTIONS.get(0), BOARD_OPTIONS);
        dialog2.setTitle("Board selection");
        dialog2.setHeaderText("Select which board to use");
        Optional<String> result2 = dialog2.showAndWait();

        if (result.isPresent()) {
            if (gameController != null) {
                // The UI should not allow this, but in case this happens anyway.
                // give the user the option to save the game or abort this operation!
                if (!stopGame()) {
                    return;
                }
            }

            // XXX the board should eventually be created programmatically or loaded from a file
            //     here we just create an empty board with the required number of players.
            Board board = BoardFactory.getInstance().createBoard(result2.get());
            gameController = new GameController(board);
            int no = result.get();
            for (int i = 0; i < no; i++) {
                Player player = new Player(board, PLAYER_COLORS.get(i), "Player " + (i + 1));
                board.addPlayer(player);
                player.setSpace(board.getSpace(i % board.width, i));
            }

            // XXX V2
            gameController.startProgrammingPhase();

            roboRally.createBoardView(gameController);

        }
    }

    /**
     * Saves the current game state. This method is currently a placeholder and needs to be implemented
     * in future versions of the application.
     *
     * @todo Implement game saving functionality.
     */
    public void saveGame() {
        // TODO V4a: needs to be implemented
    }

    /**
     * Loads a saved game state. This method is currently a placeholder and needs to be implemented
     * in future versions of the application. For now, it starts a new game if no game is running.
     *
     * @todo Implement game loading functionality.
     */
    public void loadGame() {
        // TODO V4a: needs to be implemented
        // for now, we just create a new game
        if (gameController == null) {
            newGame();
        }
    }

    /**
     * Stop playing the current game, giving the user the option to save
     * the game or to cancel stopping the game. The method returns true
     * if the game was successfully stopped (with or without saving the
     * game); returns false, if the current game was not stopped. In case
     * there is no current game, false is returned.
     *
     * @return true if the current game was stopped, false otherwise
     */
    public boolean stopGame() {
        if (gameController != null) {

            // here we save the game (without asking the user).
            saveGame();

            gameController = null;
            roboRally.createBoardView(null);
            return true;
        }
        return false;
    }
    /**
     * Exits the game, performing any necessary cleanup or shutdown operations.
     * This method ensures that the game is properly terminated, releasing resources
     * and saving any required data before exiting.
     */
    public void exit() {
        if (gameController != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Exit RoboRally?");
            alert.setContentText("Are you sure you want to exit RoboRally?");
            Optional<ButtonType> result = alert.showAndWait();

            if (!result.isPresent() || result.get() != ButtonType.OK) {
                return; // return without exiting the application
            }
        }

        // If the user did not cancel, the RoboRally application will exit
        // after the option to save the game
        if (gameController == null || stopGame()) {
            Platform.exit();
        }
    }
    /**
     * Checks if a game is currently running.
     *
     * @return true if a game is running, false otherwise.
     */
    public boolean isGameRunning() {
        return gameController != null;
    }


    /**
     * Updates the observer with changes from the observed subject. This method is part of the
     * Observer interface and is currently a placeholder for future implementation.
     *
     * @param subject The subject that triggered the update.
     */
    @Override
    public void update(Subject subject) {
        // XXX do nothing for now
    }

}
