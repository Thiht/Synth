package org.istic.synthlab.core.modules.oscillators;


import org.istic.synthlab.core.Potentiometer;
import org.istic.synthlab.core.Resource;
import org.istic.synthlab.core.modules.io.IInput;
import org.istic.synthlab.core.modules.io.IOutput;

public interface IOscillator extends Resource {
    IInput getInput();

    IOutput getOutput();

    Potentiometer gePotentiometer();
}