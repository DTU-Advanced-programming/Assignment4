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

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Phase;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a checkpoint on the game board. Checkpoints are used to track player progress
 * and determine when a player has completed the game. This class extends FieldAction and
 * implements the logic for updating player progress when they land on a checkpoint.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class Checkpoint extends FieldAction {

    private int number;

    /**
     * Sets the checkpoint number. The checkpoint number is used to track the sequence in which
     * players must reach the checkpoints.
     *
     * @param num The checkpoint number.
     */
    public void setNumber(int num) {number = num;}

    /**
     * Returns the checkpoint number.
     *
     * @return The checkpoint number.
     */
    public int getNumber() {return number;}
    
    
    public boolean isLast = false;

    /**
     * Sets whether this checkpoint is the last one in the game. The last checkpoint is used
     * to determine when a player has won the game.
     *
     * @param last true if this is the last checkpoint, false otherwise.
     */
    public void setLast(boolean last) {
    	this.isLast = last;
    }

    /**
     * Returns whether this checkpoint is the last one in the game.
     *
     * @return true if this is the last checkpoint, false otherwise.
     */
    public boolean getLast() {
    	return isLast;
    }

    /**
     * Executes the checkpoint action when a player lands on the space containing the checkpoint.
     * This method updates the player's progress and checks if the player has reached the last
     * checkpoint to determine if they have won the game.
     *
     * @param gameController The game controller managing the game logic.
     * @param space The space containing the checkpoint.
     * @return true if the action was successfully executed, false otherwise.
     */
    @Override
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
        if (isLast && space.getPlayer() != null && space.getPlayer().getCurrentCheckpoint() == number - 1) {
            space.getPlayer().setCurrentCheckpoint(number);
            space.board.setPhase(Phase.FINISHED);
            space.board.setWinner(space.getPlayer());
            return true;
        }
    	if (space.getPlayer() != null && space.getPlayer().getCurrentCheckpoint() == number - 1) {
			space.getPlayer().setCurrentCheckpoint(number);
			return true;
		}
        return false;
    }

}
