package org.istic.synthlab.ui.plugins.cable;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.geometry.Bounds;
import javafx.scene.Node;

/**
 * @author augustin <augustin.bardou@gmail.com>
 */
public class Center {
    private ReadOnlyDoubleWrapper centerX = new ReadOnlyDoubleWrapper();
    private ReadOnlyDoubleWrapper centerY = new ReadOnlyDoubleWrapper();

    public Center(Node node) {
        calcCenter(node.localToScene(node.getBoundsInLocal()));
        node.boundsInParentProperty().addListener((observableValue, oldBounds, bounds) -> {
            calcCenter(bounds);
        });
    }



    private void calcCenter(Bounds _bounds) {
        centerX.set(_bounds.getMinX() + _bounds.getWidth()  / 2);
        centerY.set(_bounds.getMinY() + _bounds.getHeight() / 2);
    }

    public ReadOnlyDoubleProperty centerXProperty() {
        return centerX.getReadOnlyProperty();
    }

    public ReadOnlyDoubleProperty centerYProperty() {
        return centerY.getReadOnlyProperty();
    }
}
