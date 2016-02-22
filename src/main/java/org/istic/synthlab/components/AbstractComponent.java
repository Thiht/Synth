package org.istic.synthlab.components;

import org.istic.synthlab.core.modules.io.IInput;
import org.istic.synthlab.core.modules.io.IOutput;
import org.istic.synthlab.core.modules.modulators.IModulator;
import org.istic.synthlab.core.modules.modulators.ModulatorType;
import org.istic.synthlab.core.services.Factory;
import org.istic.synthlab.core.utils.parametrization.PotentiometerType;

/**
 * The abstract class component.
 *
 * This class abstracts port accesses.

 *
 * 'Abstract components' abstract representation
 * ---------------------------------------------
 *
 *            External view (public access to inputs and outputs)
 * VIRTUAL   +-----------------------------------------------------------------------------------------------+  VIRTUAL
 * PORTS     |                  Internal view (protected access to sources/sinks ports                       |  PORTS
 *           |     IModulator   +-------------------------------------------------------+    IModulator      |
 *         +-+-+   +--------+   |                 Module view                           |    +---------+   +-+-+
 *   input |   +---> Bypass +---> source+------+  +-----------------------+    +-->sink +----> Amp     +--->   |output
 *         +-+-+   +--------+   |              +-->in1                    |    |        |    | Mod     |   +-+-+
 *           |                  |                 |                   out1+----+        |    | Linear  |     |
 *           |     +--------+   |             +--->in2 Modules (jsyn)     |             |    +---------+     |
 *         +-+-+   |Freq    |   |             |   |                       |             |                    |
 *      fm |   +--->Mod     +---> sourceFm+-+ |   |                       |             |                    |
 *         +-+-+   |Exp     |   |           | |   +-----------------------+             |                    |
 *           |     +--------+   |           | |                                         |                    |
 *           |     +--------+   |           | +------------------+                      |                    |
 *         +-+-+   |Amp     |   |           |                    |                      |                    |
 *      am |   +--->Mod     +---> SourceAm  |     IModulator     |                      |                    |
 *         +-+-+   |Linear  |   |           |     +---------+    |                      |                    |
 *           |     +--------+   |           |     |Freq     |    |                      |                    |
 *           |                  |           +> --->Mod      +----+                      |     IModulator     |
 *         +-+-+   +--------+   |                 |Linear   |                           |     +---------+  +-+-+
 *   gateIn|   +---> Bypass +---> SourceGate      +---------+                  sinkGate +-----> Bypass  +-->   |gateOut
 *         +-+-+   +--------+   |                                                       |     +---------+  +-+-+
 *           |                  +-------------------------------------------------------+                    |
 *           |                                                                                               |
 *           +-----------------------------------------------------------------------------------------------+
 *                                                                                    Made with : http://asciiflow.com/
 * Abstraction by getter (see code):
 *  Component's inputs linked to IModulator's inputs by getter
 *  Component's outputs linked to IModulator's outputs by getter
 *  Component's sources linked to IModulator's outputs by getter
 *  Component's sinks linked to IModulator's inputs by getter
 *
 *
 * @author Stephane Mangin <stephane[dot]mangin[at]freesbee[dot]fr>
 *
 */
public abstract class AbstractComponent implements IComponent {

    /**
     * The Name.
     */
    private String name;

    private IModulator inputByPass;
    private IModulator frequencyModulator;
    private IModulator amplitudeModulator;
    private IModulator inputGateModulator;
    private IModulator outputModulator;
    private IModulator outputGateModulator;
    private IModulator gainModulator;
    private IModulator inputByPassMixer1;
    private IModulator inputByPassMixer2;
    private IModulator inputByPassMixer3;
    private IModulator inputByPassMixer4;

    private int id = 0;

    /**
     * Instantiates a new component.
     *
     * @param name the name
     */
    public AbstractComponent(String name) {
        setName(name);

        // Define all modulators
        inputByPass = Factory.createModulator(
                "modIn", this,
                ModulatorType.BYPASS, null);
        frequencyModulator = Factory.createModulator(
                "modFreq", this,
                ModulatorType.FREQUENCY,
                PotentiometerType.EXPONENTIAL);
        amplitudeModulator = Factory.createModulator(
                "modAmp", this,
                ModulatorType.AMPLITUDE,
                PotentiometerType.LINEAR);
        inputGateModulator = Factory.createModulator(
                "modInGate", this,
                ModulatorType.BYPASS, null);
        outputModulator = Factory.createModulator(
                "modOut", this,
                ModulatorType.AMPLITUDE,
                PotentiometerType.LINEAR);
        outputGateModulator = Factory.createModulator(
                "modOutGate", this,
                ModulatorType.BYPASS, null);
        this.gainModulator = Factory.createModulator(
                "Gain", this,
                ModulatorType.GAIN,
                PotentiometerType.LINEAR);
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the name
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Rename.
     *
     * @param name the name
     */
    public void rename(String name) {
        setName(name);
    }

    @Override
    public void activate() {
    }

    @Override
    public void deactivate() {
    }

    @Override
    public boolean isActivated(){
        return true;
    }

    @Override
    public void init() {
    }

    @Override
    public void run() {

    }

    /**
     * Returns the input for external connexions
     * @return input of the component
     */
    @Override
    public IInput getInput() {
        return inputByPass.getInput();
    }

    /**
     * Returns the input for external connexions
     * @return gate input of the component
     */
    @Override
    public IInput getInputGate() {
        return inputGateModulator.getInput();
    }

    /**
     * Returns the input for external connexions
     * @return FM input of the component
     */
    @Override
    public IInput getFm() {
        return frequencyModulator.getInput();
    }

    /**
     * Returns the input for external connexions
     * @return AM input of the component
     */
    @Override
    public IInput getAm() {
        return amplitudeModulator.getInput();
    }

    /**
     * Returns the output for external connexions
     * @return output of the component
     */
    @Override
    public IOutput getOutput() {
        return outputModulator.getOutput();
    }

    /**
     * Returns the output for external connexions
     * @return gate output of the component
     */
    @Override
    public IOutput getOutputGate() {
        return outputGateModulator.getOutput();
    }

    /**
     * Returns the input for internal connexions
     * @return intern output of the component
     */
    protected IOutput getSource() {
        return inputByPass.getOutput();
    }

    /**
     * Returns the input for internal connexions
     * @return intern output of the component
     */
    protected IOutput getSourceInputMix1() {
        return inputByPassMixer1.getOutput();
    }

    /**
     * Returns the input for internal connexions
     * @return intern output of the component
     */
    protected IOutput getSourceInputMix2() {
        return inputByPassMixer2.getOutput();
    }

    /**
     * Returns the input for internal connexions
     * @return intern output of the component
     */
    protected IOutput getSourceInputMix3() {
        return inputByPassMixer3.getOutput();
    }

    /**
     * Returns the input for internal connexions
     * @return intern output of the component
     */
    protected IOutput getSourceInputMix4() {
        return inputByPassMixer4.getOutput();
    }

    /**
     * Returns the input for internal connexions
     * @return intern gate output of the component
     */
    protected IOutput getSourceGate() {
        return inputGateModulator.getOutput();
    }

    /**
     * Returns the input for internal connexions
     * @return intern FM output of the component
     */
    protected IOutput getSourceFm() {
        return frequencyModulator.getOutput();
    }

    /**
     * Returns the input for internal connexions
     * @return intern AM output of the component
     */
    protected IOutput getSourceAm() {
        return amplitudeModulator.getOutput();
    }

    /**
     * Returns the input for internal connexions
     * @return intern input of the component
     */
    protected IInput getSink() {
        return outputModulator.getInput();
    }

    /**
     * Returns the output for internal connexions
     * @return intern gate input of the component
     */
    protected IInput getSinkGate() {
        return outputGateModulator.getInput();
    }

    @Override
    public IModulator getInputByPass() { return inputByPass; }

    @Override
    public IModulator getFmModulator() {
        return frequencyModulator;
    }

    @Override
    public IModulator getAmModulator() {
        return amplitudeModulator;
    }

    @Override
    public IModulator getOutputModulator() {
        return outputModulator;
    }

    public IModulator getGainModulator() { return this.gainModulator; }

    public IModulator getInputModulatorMixer1() { return this.inputByPassMixer1; }
    public IModulator getInputModulatorMixer2() { return this.inputByPassMixer2; }
    public IModulator getInputModulatorMixer3() { return this.inputByPassMixer3; }
    public IModulator getInputModulatorMixer4() { return this.inputByPassMixer4; }

    @Override
    public String toString() {
        return this.getName() + "-" + id + "<" + this.hashCode() + ">";
    }

    @Override
    public void setId(int num) {
        id = num;
    }

    @Override
    public int getId() {
        return id;
    }
}
