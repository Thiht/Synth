package org.istic.synthlab.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import org.istic.synthlab.core.modules.io.IInput;
import org.istic.synthlab.core.modules.io.IOutput;
import org.istic.synthlab.core.services.Factory;
import org.istic.synthlab.core.services.Register;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * FX controller of core.fxml
 * Represents the global view of the application
 * @author Thibaut Rousseau <thibaut.rousseau@outlook.com>
 */
public class CoreController implements Initializable, IObserver {
    /**
     * The default number of rows in the grid
     */
    public static final int DEFAULT_NB_ROWS = 5;
    /**
     * The default number of cols in the grid
     */
    public static final int DEFAULT_NB_COLS = 5;
    /**
     * The default height of the rows in the grid
     */
    public static final int DEFAULT_ROWS_PREF_HEIGHT = 200;
    /**
     * The default width of the columns in the grid
     */
    public static final int DEFAULT_COLS_PREF_WIDTH = 300;

    private static final String DRAG_N_DROP_MOVE_GUARD = "panda";

    @FXML
    private BorderPane borderPane;
    @FXML
    private ListView<Node> listView;
    @FXML
    private GridPane gridPane;
    @FXML
    private TextArea textarea;
    @FXML
    private Button pauseButton;
    @FXML
    private Button playButton;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> synthesizer = scheduler.scheduleAtFixedRate(()->{}, 0, 10, TimeUnit.MILLISECONDS);

    /**
     * This method initializes the list view and the grid
     * @param location Not used
     * @param resources Not used
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeListView();
        initializeGridView();
        ConnectionManager.addObserver(this);
    }

    @Override
    public void update(Map<IOutput, IInput> arg) {
        String total = "";
        Set keys = arg.keySet();
        for (Object key : keys) {
            IOutput origin = (IOutput) key;
            IInput destination = arg.get(origin);
            total += origin.toString() + " ---------> " + destination.toString() + "\n";
        }
        textarea.setText(total);
    }

    @Override
    public void drawLine(HashMap<Line, HashMap<IOutput, IInput>> arg) {
        Set keys = arg.keySet();
        for(Object key : keys){
            borderPane.getChildren().add((Line)key);
        }
    }

    private void initializeListView() {
        final ObservableList<Node> data = FXCollections.observableArrayList();
        for (String component: findAllPackagesStartingWith("org.istic.synthlab.components")) {
            URL image = getClass().getResource("/ui/components/" + component + "/images/small.png");
            if (image == null) {
                continue;
            }
            Pane pane = new Pane();
            ImageView imageView = new ImageView(new Image(image.toString()));
            imageView.preserveRatioProperty();
            imageView.setFitWidth(100);
            imageView.setFitHeight(50);
            imageView.setSmooth(true);
            imageView.setId(component);
            pane.getChildren().add(imageView);
            pane.setOnDragDetected(new DragDetectedListItemEventHandler());
            data.add(pane);
        }

        listView.setItems(data);
    }

    private void initializeGridView() {
        for (int row = 0; row < DEFAULT_NB_ROWS; row++) {
            final RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPrefHeight(DEFAULT_ROWS_PREF_HEIGHT);
            gridPane.getRowConstraints().add(rowConstraints);
        }

        for (int col = 0; col < DEFAULT_NB_COLS; col++) {
            final ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPrefWidth(DEFAULT_COLS_PREF_WIDTH);
            gridPane.getColumnConstraints().add(columnConstraints);
        }

        // Fill the GridPane with Panes
        for (int row = 0; row < DEFAULT_NB_ROWS; row++) {
            for (int col = 0; col < DEFAULT_NB_COLS; col++) {
                final Pane p = new Pane();

                p.setOnDragOver(new DragOverPaneEventHandler());
                p.setOnDragEntered(new DragEnteredPaneEventHandler());
                p.setOnDragExited(new DragExitedPaneEventHandler());
                p.setOnDragDropped(new DragDroppedPaneEventHandler());

                gridPane.add(p, col, row);
            }
        }
    }

    /**
     * Close the application
     */
    @FXML
    public void onActionClose() {
        scheduler.shutdown();

        System.out.println("Shut ?"+scheduler.isShutdown());
        Platform.exit();
    }

    /**
     * Pause the synthesizer
     */
    @FXML
    public void onPause() {
        pauseButton.setDisable(true);
        playButton.setDisable(false);

        synthesizer.cancel(true);
        Factory.createSynthesizer().stop();
    }

    /**
     * Start the synthesizer
     */
    @FXML
    public void onPlay() {
        pauseButton.setDisable(false);
        playButton.setDisable(true);

        Factory.createSynthesizer().start();
        Register.startComponents();

        synthesizer = scheduler.scheduleAtFixedRate(()->{
            try{
                Factory.createSynthesizer().sleepFor(10);
            } catch (Exception e) {
                System.out.println("msg"+e.getMessage());
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    private class DragDetectedListItemEventHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            System.out.println("Drag detected");
            final Pane pane = (Pane) event.getSource();
            ImageView view = (ImageView) pane.getChildren().get(0);
            final Dragboard db = view.startDragAndDrop(TransferMode.COPY);
            final ClipboardContent content = new ClipboardContent();
            content.putString(view.getId());
            db.setContent(content);
            event.consume();
        }
    }

    private class DragOverPaneEventHandler implements EventHandler<DragEvent> {
        @Override
        public void handle(DragEvent event) {
            final Pane p = (Pane) event.getSource();
            if (event.getGestureSource() != p && event.getDragboard().hasString() && p.getChildren().size() == 0) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        }
    }

    private class DragEnteredPaneEventHandler implements EventHandler<DragEvent> {
        @Override
        public void handle(DragEvent event) {
            final Pane p = (Pane) event.getSource();
            if (event.getGestureSource() != p && event.getDragboard().hasString()) {
                p.setStyle("-fx-border-color: #CCC; -fx-border-style: dashed; -fx-border-width: 3px;");
            }
            event.consume();
        }
    }

    private class DragExitedPaneEventHandler implements EventHandler<DragEvent> {
        @Override
        public void handle(DragEvent event) {
            final Pane p = (Pane) event.getSource();
            p.setStyle("-fx-border: none;");
            event.consume();
        }
    }

    private class DragDroppedPaneEventHandler implements EventHandler<DragEvent> {
        @Override
        public void handle(DragEvent event) {
            final Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                final Pane p = (Pane) event.getSource();
                if (db.getString().equals(DRAG_N_DROP_MOVE_GUARD)) {
                    p.getChildren().add((Node) event.getGestureSource());
                    success = true;
                }
                else {
                    try {
                        final Node node = FXMLLoader.load(getClass().getResource("/ui/components/" + db.getString().toLowerCase() + "/view.fxml"));
                        node.setOnDragDetected(new DragDetectedComponentEventHandler());
                        p.getChildren().add(node);
                        success = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            event.setDropCompleted(success);
            event.consume();
        }
    }

    private class DragDetectedComponentEventHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            final Node node = (Node) event.getSource();
            final Dragboard db = node.startDragAndDrop(TransferMode.COPY);
            final ClipboardContent content = new ClipboardContent();
            content.putString(DRAG_N_DROP_MOVE_GUARD);
            db.setContent(content);
            event.consume();
        }
    }

    /**
     * Finds all package names starting with prefix
     * @return Set of package names
     */
    public Set<String> findAllPackagesStartingWith(String prefix) {
        List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(prefix))));
        Set<Class<? extends Object>> classes = reflections.getSubTypesOf(Object.class);

        Set<String> packageNameSet = new TreeSet<String>();
        for (Class classInstance : classes) {
            String packageName = classInstance.getPackage().getName();
            if (packageName.startsWith(prefix)) {
                packageName = packageName.split("\\.")[packageName.split("\\.").length-1].toLowerCase();
                packageNameSet.add(packageName);
            }
        }
        return packageNameSet;
    }
}
