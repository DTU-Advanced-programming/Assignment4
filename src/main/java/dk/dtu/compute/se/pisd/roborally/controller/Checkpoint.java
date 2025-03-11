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
 * This class represents a conveyor belt on a space.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
// XXX A3
public class Checkpoint extends FieldAction {

    private int number;

    public void setNumber(int num) {number = num;}

    public int getNumber() {return number;}
    
    
    public boolean isLast = false;
    
    public void setLast(boolean last) {
    	this.isLast = last;
    }
    
    public boolean getLast() {
    	return isLast;
    }


    @Override
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
    	if (isLast) {
			//TODO check to see if the player has won by checking if its the last checkpoint
    		//Game state should be changed to Phase.FINISHED and a dialog box with current player has won should appear
		}
    	if (space.getPlayer() != null && space.getPlayer().getCurrentCheckpoint() == number - 1) {
			space.getPlayer().setCurrentCheckpoint(number);
			return true;
		}
    	
    	
    	
        return false;
    }

}
