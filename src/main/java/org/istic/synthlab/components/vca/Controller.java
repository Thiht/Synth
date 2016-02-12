package org.istic.synthlab.components.vca;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.istic.synthlab.core.AbstractController;
import org.istic.synthlab.ui.ConnectionManager;
import org.istic.synthlab.ui.controls.Potentiometer;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Dechaud John Marc on 2/8/16.
 */
public class Controller extends AbstractController implements Initializable {

    private final Vca vca = new Vca("VCA " + numInstance++);
    private static int numInstance = 0;

    @FXML
    private ImageView input;
    @FXML
    private ImageView am;
    @FXML
    private ImageView output;
    @FXML
    private Potentiometer gain;
    @FXML
    private Potentiometer amplitude;
    @FXML
    private ImageView circleEvent;
    @FXML
    private Button close;
    @FXML
    private AnchorPane vcaPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        vcaPane.setId("vcaPane"+numInstance);
        close.setStyle("-fx-background-image: url('/ui/images/closeIconMin.png');-fx-background-color: white;");
        output.addEventHandler(MouseEvent.MOUSE_CLICKED, new GetIdWithClick());
        input.addEventHandler(MouseEvent.MOUSE_CLICKED, new GetIdWithClick());
        am.addEventHandler(MouseEvent.MOUSE_CLICKED, new GetIdWithClick());

        amplitude.valueProperty().addListener((observable, oldValue, newValue) -> {
            vca.setAmplitudeModulatorValue((double) newValue);
        });

        gain.valueProperty().addListener((observable, oldValue, newValue) -> {
            vca.setGainModulatorValue((double) newValue);
        });
    }

    @FXML
    public void connectAm(final MouseEvent event) {
        ConnectionManager.makeDestination(vca, (Node) event.getSource(), vca.getAm());
    }

    @FXML
    public void connectInput(final MouseEvent event) {
        ConnectionManager.makeDestination(vca, (Node) event.getSource(), vca.getInput());
    }

    @FXML
    public void connectOutput(final MouseEvent event) {
        ConnectionManager.makeOrigin(vca, (Node) event.getSource(), vca.getOutput());
    }

    /**
     * Get the object clicked in the view and cast it into a Circle object
     */
    private class GetIdWithClick implements EventHandler<Event> {
        @Override
        public void handle(Event event) {
            circleEvent = (ImageView) event.getSource();
        }
    }

    /**
     * Method call when the close button is clicked.
     * Send the instance and the main pane to the deleteComponent method of the ConnectionManager
     */
    @FXML
    public void closeIt(){
        ConnectionManager.deleteComponent(vca, vcaPane);
    }
}
