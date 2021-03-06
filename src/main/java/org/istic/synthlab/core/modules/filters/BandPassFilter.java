package org.istic.synthlab.core.modules.filters;

import com.jsyn.unitgen.FilterBandPass;
import com.jsyn.unitgen.FilterStateVariable;
import com.jsyn.unitgen.UnitFilter;
import org.istic.synthlab.components.IComponent;

/**
 * The adapter of the band pass filter Jsyn
 *
 * @author Stephane Mangin <stephane[dot]mangin[at]freesbee[dot]fr>
 *
 */
public class BandPassFilter extends AbstractFilter {
    /**
     * Instantiates a new Band pass filter adapter.
     * @param component
     */
    public BandPassFilter(IComponent component) {
        super(component, new FilterStateVariable(), FilterType.ALLPASS);
    }

}
