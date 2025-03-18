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
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a conveyor belt on the game board. Conveyor belts automatically move players
 * in a specific direction when they land on a space containing a conveyor belt. This class
 * extends FieldAction and implements the logic for moving players.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class ConveyorBelt extends FieldAction {

    private Heading heading;

    /**
     * Returns the heading of the conveyor belt, which determines the direction in which players
     * are moved.
     *
     * @return The heading of the conveyor belt.
     */
    public Heading getHeading() {
        return heading;
    }

    /**
     * Sets the heading of the conveyor belt, which determines the direction in which players
     * are moved.
     *
     * @param heading The heading of the conveyor belt.
     */
    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    /**
     * Executes the conveyor belt action when a player lands on the space containing the conveyor
     * belt. This method moves the player to the neighboring space in the specified direction.
     *
     * @param gameController The game controller managing the game logic.
     * @param space The space containing the conveyor belt.
     * @return true if the player was successfully moved, false otherwise.
     */
    @Override
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
        if (space.getPlayer() != null) {
            Space newSpace = gameController.board.getNeighbour(space, heading);
            try {
                gameController.moveToSpace(space.getPlayer(), newSpace, heading);
            } catch (GameController.ImpossibleMoveException e) {
                // when pushing not possible due to wall
                System.out.println(e.getMessage() + e.player + "while on ConveyorBelt");
                return false;
            }

        }
        return true;
    }
}
