package org.istic.synthlab.ui.controls;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * Allow direct insertion into a fxml file
 *
 * Example :
 *      <Potentiometer fx-id:="my potentiometer" />
 *
 * @author Thibaut Rousseau <thibaut.rousseau@outlook.com>
 * @author Stephane Mangin <stephane[dot]mangin[at]freesbee[dot]fr>
 */
public class Potentiometer extends Pane {

    @FXML
    private Label title;
    @FXML
    private Label maxValue;
    @FXML
    private Label minValue;
    @FXML
    private Button rotatorDial;
    @FXML
    private Button rotatorHandle;

    private static final double HEIGHT = 36;
    private static final double WIDTH = 36;
    private static final double MIN = -230;
    private static final double MAX = 50;

    private final DoubleProperty value = new SimpleDoubleProperty(0);

    public DoubleProperty valueProperty() {
        return value;
    }

    public double getValue() {
        return value.get();
    }

    public void setValue(final double newValue) {
        value.set(newValue);
    }

    /**
     * This constructor loads the FXML associated to the potentiometer, binds the event handlers and initializes the view
     */
    public Potentiometer() {
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui/controls/potentiometer.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();
        } catch (final IOException exception) {
            throw new RuntimeException(exception);
        }

        setPrefHeight(HEIGHT);
        setPrefWidth(WIDTH);
        rotatorDial.setOnMouseEntered(new MaximiseEventHandler());
        rotatorHandle.setOnMouseEntered(new MaximiseEventHandler());
        rotatorDial.setOnMouseDragged(new DragKnobEventHandler());
        rotatorDial.setOnScroll(new ScrollKnobEventHandler());
        rotateHandle(MIN);
    }

    private class MaximiseEventHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(final MouseEvent event) {
            rotatorDial.setScaleX(1.8);
            rotatorDial.setScaleY(1.8);
            rotatorHandle.setScaleX(1.8);
            rotatorHandle.setScaleY(1.8);
            rotatorDial.setOnMouseExited(new MinimizeEventHandler());
            rotatorHandle.setOnMouseExited(new MinimizeEventHandler());
            event.consume();
        }
    }

    private class MinimizeEventHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(final MouseEvent event) {
            rotatorDial.setScaleX(1);
            rotatorDial.setScaleY(1);
            rotatorHandle.setScaleX(1);
            rotatorHandle.setScaleY(1);
            event.consume();
        }
    }

    private class ScrollKnobEventHandler implements EventHandler<ScrollEvent> {
        @Override
        public void handle(final ScrollEvent event) {
            rotateHandle(rotatorHandle.getRotate() + 5 * Math.signum(event.getDeltaY()));
            event.consume();
        }
    }

    private class DragKnobEventHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(final MouseEvent event) {
            double x = event.getX(), y = event.getY();

            // Out of bounds values are possible
            // They might break something so we normalize them
            if (x < 0) {
                x = 0;
            }
            else if (x > WIDTH) {
                x = WIDTH;
            }

            if (y < 0) {
                y = 0;
            }
            else if (y > HEIGHT) {
                y = HEIGHT;
            }

            // Move the coordinates so that next calculations are easier
            x -= WIDTH/2;
            y -= HEIGHT/2;

            // Calculate the angle between (x ; y) and the center of the circle
            //
            //        |
            //   - -  |  + -
            //        |
            // ------ 0 ------ x
            //        |
            //   - +  |  + +
            //        |
            //        y
            //
            // tan(x, y) = y/x (ie. opposite/adjacent)
            rotateHandle(Math.toDegrees(Math.atan2(y, x)));
            event.consume();
        }
    }

    /**
     * Rotate the handle by taking in consideration the MIN and MAX values
     * @param degrees The angle to which the handle will be rotated, relative to the start position, not the current one
     */
    private void rotateHandle(final double degrees) {
        System.out.println(degrees);
        if (degrees >= MIN && degrees <= MAX) {
            rotatorHandle.setRotate(degrees);
            System.out.println((degrees-(MIN)) / (MAX-MIN));
            setValue((degrees-(MIN)) / (MAX-MIN));
        }
    }

    public void setTitle(String value) {
        title.setText(value);
    }

    public void setMaxValue(double value) {
        maxValue.setText(Double.toString(value));
    }

    public void setMinValue(double value) {
        minValue.setText(Double.toString(value));
    }
}
