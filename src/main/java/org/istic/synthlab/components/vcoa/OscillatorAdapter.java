package org.istic.synthlab.components.vcoa;

import com.jsyn.unitgen.UnitOscillator;

/**
 * Created by stephane on 02/02/16.
 */
public class OscillatorAdapter implements Oscillator {

    private UnitOscillator unitOscillator;

    public OscillatorAdapter(UnitOscillator unitOscillator) {
        this.unitOscillator = unitOscillator;
    }
}