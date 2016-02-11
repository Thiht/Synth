package org.istic.synthlab.core;

/**
 * @author  Group1
 *
 * The interface Component
 */
public interface IComponent {

    void activate();
    void deactivate();
    boolean isActivated();
    void init();
    void run();

}
