package org.istic.synthlab.core.modules.functions;

import org.istic.synthlab.core.IComponent;

public class Multiply extends AbstractFunction {
    public Multiply(IComponent component) {
        super(component, new com.jsyn.unitgen.Multiply());
    }
}
