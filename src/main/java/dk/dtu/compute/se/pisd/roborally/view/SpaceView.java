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
package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.Checkpoint;
import dk.dtu.compute.se.pisd.roborally.controller.ConveyorBelt;
import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class SpaceView extends StackPane implements ViewObserver {

    final public static int SPACE_HEIGHT = 40; // 60; // 75;
    final public static int SPACE_WIDTH = 40;  // 60; // 75;

    public final Space space;


    public SpaceView(@NotNull Space space) {
        this.space = space;

        // XXX the following styling should better be done with styles
        this.setPrefWidth(SPACE_WIDTH);
        this.setMinWidth(SPACE_WIDTH);
        this.setMaxWidth(SPACE_WIDTH);

        this.setPrefHeight(SPACE_HEIGHT);
        this.setMinHeight(SPACE_HEIGHT);
        this.setMaxHeight(SPACE_HEIGHT);

        if ((space.x + space.y) % 2 == 0) {
            this.setStyle("-fx-background-color: white;");
        } else {
            this.setStyle("-fx-background-color: black;");
        }

        // updatePlayer();

        // This space view should listen to changes of the space
        space.attach(this);
        update(space);
    }

    private void updatePlayer() {
        Player player = space.getPlayer();
        if (player != null) {
            Polygon arrow = new Polygon(0.0, 0.0,
                    10.0, 20.0,
                    20.0, 0.0 );
            try {
                arrow.setFill(Color.valueOf(player.getColor()));
            } catch (Exception e) {
                arrow.setFill(Color.MEDIUMPURPLE);
            }

            //player.getHeading =
            //where south = 0, west = 1, north = 2, east = 3.
            //ordinal = An ordinal number is a number that indicates the position or order of an object or a person
            //It finds out where the arrow is headed and rotates 90 * x times towards that direction
            arrow.setRotate((90*player.getHeading().ordinal())%360);
            this.getChildren().add(arrow);
        }
    }

    @Override
    public void updateView(Subject subject) {
        if (subject == this.space) {
            this.getChildren().clear();
            for (Heading direction : this.space.getWalls()) {    //imported HEADING enum
                this.getChildren().add(waller(direction));
            }
            for (FieldAction FA : this.space.getActions()) {
                if (FA instanceof ConveyorBelt cb) {
                    Polygon arrow = new Polygon(0.0, 0.0,
                            10.0, 20.0,
                            20.0, 0.0);
                    arrow.setFill(Color.GRAY);
                    arrow.setRotate((90 * cb.getHeading().ordinal()) % 360);
                    this.getChildren().add(arrow);
                } else if (FA instanceof Checkpoint cp) {
                    this.getChildren().add(getCircle());
                    Text numberText = new Text(Integer.toString(cp.getNumber()));  // Adjust for centering
                    this.getChildren().add(numberText);
                }
            }

            //Represent all the symbols such as checkpoints and conveyor belts
            //Work on conveyor belt, checkpoints and walls symbols design

            // XXX A3: drawing walls and action on the space (could be done
            //         here); it would be even better if fixed things on
            //         spaces  are only drawn once (and not on every update)

            updatePlayer();
        }
    }

    private Canvas waller(@NotNull Heading dir) {
        Canvas canvas =new Canvas(SPACE_WIDTH, SPACE_HEIGHT);
        GraphicsContext gc =canvas.getGraphicsContext2D();
        gc.setStroke(Color.RED);
        gc.setLineWidth(5);
        gc.setLineCap(StrokeLineCap.ROUND);
        gc.strokeLine(2, SPACE_HEIGHT-2,SPACE_WIDTH-2, SPACE_HEIGHT-2);
        canvas.setRotate((90 * dir.ordinal()) % 360);
        return canvas;
    }

    public static Polygon getCircle() {
        Polygon polygon = new Polygon();
        for (int i = 0; i < 360; i++) {
            double angle = 2 * Math.PI * i / 360;
            double x = 20 * Math.cos(angle);
            double y = 20 * Math.sin(angle);
            polygon.getPoints().addAll(x, y);
        }
        polygon.setStroke(Color.BLUE);
        polygon.setFill(Color.YELLOW);  // Transparent fill
        return polygon;
    }
}
