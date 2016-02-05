package org.istic.synthlab.components.out;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import org.istic.synthlab.core.AbstractController;
import org.istic.synthlab.core.ui.ConnectionManager;
import org.istic.synthlab.core.ui.CoreController;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by stephane on 02/02/16.
 */
public class Controller extends AbstractController implements Initializable {

    @FXML
    Circle input;
    private Circle circleEvent;
    private Out componentOut = new Out("Out"+numInstance++);
    private static int numInstance = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        input.addEventHandler(MouseEvent.MOUSE_CLICKED, new getIdWithClick());
        componentOut.getLineOut().start();
    }

    @FXML
    void connectIn(){
        IHMConnectionManager.makeDestination(circleEvent, componentOut.getIInput());
        ConnectionManager.makeDestination(componentOut.getIInput());
    }

    private class getIdWithClick implements EventHandler<Event> {
        @Override
        public void handle(Event event) {
            circleEvent = (Circle)event.getSource();
        }
    }
}
