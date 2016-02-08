package org.istic.synthlab.core.modules.modulators;

import org.istic.synthlab.core.modules.io.IInput;
import org.istic.synthlab.core.modules.io.IOutput;

/**
 *
 * @author Stephane Mangin <stephane[dot]mangin[at]freesbee[dot]fr>
 */
public interface IModulator {

    /**
     * Return the input
     *
     * @return IInput
     */
    IInput getInput();

    /**
     * Return the output
     *
     * @return IOutput
     */
    IOutput getOutput();

    /**
     * Returns an amplitude.
     *
     * @return double
     */
    double getValue();

    /**
     * Set the amplitude.
     *
     */
    void setValue(double value);

    String getName();
}