package org.istic.synthlab.core.utils.parametrization;


/**
 *
 * The class Params.
 *
 * @param <T> the type parameter
 *
 * @author Stephane Mangin <stephane[dot]mangin[at]freesbee[dot]fr>
 * @author Cyprien Gottstein <gottstein[dot]cyprien[at]gmail[dot]com>
 */
public class GenericsParam<T> {

    private final T defaultValue;
    private T value;
    private String label;

    /**
     * Instantiates a new Params.
     *
     * @param label the label
     * @param value the value
     */
    public GenericsParam(String label, T value) {
        this.label = label;
        this.value = value;
        this.defaultValue = value;
    }

    /**
     * Reset to the default value
     *
     */
    public void reset() {
        this.value = this.defaultValue;
    }
    /**
     * Gets label.
     *
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets label.
     *
     * @param label the label
     */
    public void setLabel(String label) {
        this.label = label;
    }


    /**
     * Gets value.
     *
     * @return the value
     */
    public T getValue() {
        return value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue(T value) {
        this.value = value;
    }
}
