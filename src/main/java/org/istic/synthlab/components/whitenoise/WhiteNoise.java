package org.istic.synthlab.components.whitenoise;

import org.istic.synthlab.components.AbstractComponent;
import org.istic.synthlab.core.modules.io.IOutput;
import org.istic.synthlab.core.modules.whitenoise.IWhiteNoise;
import org.istic.synthlab.core.services.Factory;

/**
 * @author  Ngassam Noumi Paola npaolita[at]yahoo[dot]fr
 */

/**
 * The model of White noise component
 * It creates a  white noise module to produce a signal conforming to the definition of white noise.
 * */
public class WhiteNoise extends AbstractComponent  {

    private IWhiteNoise noise;

    /**
     * Instantiates a new component.
     *
     * @param name the name
     */
    public WhiteNoise(String name) {
        super(name);
        noise = Factory.createWhiteNoise(this);
        noise.getOutput().connect(getSink());
    }

    @Override
    public boolean isActivated(){
        return noise.isActivated();
    }

    @Override
    public void activate() {
        noise.activate();
    }

    @Override
    public void deactivate() {
        noise.deactivate();
    }

    @Override
    public IOutput getOutput(){
        return noise.getOutput();
    }
}