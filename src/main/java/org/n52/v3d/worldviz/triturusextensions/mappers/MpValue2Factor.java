package org.n52.v3d.worldviz.triturusextensions.mappers;

import java.util.ArrayList;
import java.util.List;

import org.n52.v3d.triturus.core.T3dException;
import org.n52.v3d.triturus.core.T3dProcMapper;
import org.n52.v3d.triturus.t3dutil.T3dColor;

/**
 * This mapper is meant to determine factor-values. First the mapping between
 * input data and output factor values shall be set (e.g. 5000(input) = 1 (MIN
 * factor) and 100000 (input) = 100 (MAX factor)). Then the factor-output of any
 * other input value will be either a static value corresponding to the defined
 * factorMapping or calculated through linear interpolation (e.g. 50000 (input)
 * =~ 47.89 (output)).<br/>
 * <br/>
 * A typical use case of this mapper may be calculating a scale-factor. For
 * instance, in the 3D-scene-decription-language X3D you might wish to set the
 * size of an object to scale the visualization object in a certain coordinate
 * direction.<br/>
 * <br/>
 * Note that you should definitely call
 * {@link #setPalette(double[], double[], boolean)} to match mapping values to
 * your use case before you transform any values. Otherwise a classical palette
 * will be set with following values:
 * <code>inputValues = [1, 1000, 10000]; outputFactors = [1, 10, 100]</code>
 * 
 * 
 * @author Christian Danowski
 * 
 */
public class MpValue2Factor extends T3dProcMapper {

	private List<Double> inputValues = new ArrayList<Double>();
	private List<Double> outputFactors = new ArrayList<Double>();
	private boolean interpolationMode = true;

	public MpValue2Factor() {
		this.setClassicalPalette();
	}

	/**
	 * Provides the corresponding factor-parameter for the input value. Note
	 * that you should have called/noticed the
	 * {@link #setPalette(double[], T3dColor[], boolean)} and
	 * {@link #setInterpolMode(boolean)} beforehand.
	 * 
	 * @param inputValue
	 *            a double-value for which the corresponding factor is
	 *            determined.
	 * @return the factor
	 * @throws T3dException
	 */
	public double transform(double inputValue) throws T3dException {
		if (inputValue <= ((Double) inputValues.get(0)).doubleValue())
			return outputFactors.get(0);
		for (int i = 1; i < inputValues.size(); i++) {
			double higherValue = ((Double) inputValues.get(i)).doubleValue();
			if (inputValue <= higherValue) {
				if (!this.getInterpolMode())
					return outputFactors.get(i - 1);
				else {
					// Farbe interpolieren und zur�ckgeben:
					double lowerValue = ((Double) inputValues.get(i - 1))
							.doubleValue();
					double interpolationFactor = (double) ((inputValue - lowerValue) / (higherValue - lowerValue));
					return this.interpolateFactor(outputFactors.get(i - 1),
							outputFactors.get(i), interpolationFactor);
				}
			}
		}
		return outputFactors.get(inputValues.size() - 1);
	}

	/**
	 * Sets the factor palette and thus the mapping of input-values to
	 * output-factor-values. Here, the input value <i>inputValues[i]</i> will be
	 * mapped to the output factor <i>outputFactorValues[i]</i>. Dependent on
	 * the given interpolation mode, the mapper will interpolate between the
	 * factor values, or for the overall interval <i>inputValues[i] &lt; h &lt;=
	 * inputValues[i + 1]</tt> a uniform factor value <i>outputFactorValues[i]>
	 * <tt> will be assigned.
	 * 
	 * @param inputValues
	 *            Array which holds input values
	 * @param outputFactorValues
	 *            Array which holds the corresponding factor values
	 * @param pInterpolMode
	 *            <i>true</i> for linear interpolation , <i>false</i> for
	 *            uniform classes.
	 */
	public void setPalette(double[] inputValues, double[] outputFactorValues,
			boolean pInterpolMode) {
		if (inputValues.length != outputFactorValues.length)
			throw new T3dException(
					"Illegal hypsometric color map specification ("
							+ inputValues.length + " != "
							+ outputFactorValues.length + ".");

		this.inputValues.clear();
		this.outputFactors.clear();

		for (int i = 0; i < inputValues.length; i++) {
			this.inputValues.add(new Double(inputValues[i]));
			this.outputFactors.add(outputFactorValues[i]);
		}

		this.setInterpolMode(pInterpolMode);
	}

	public boolean getInterpolMode() {
		return interpolationMode;
	}

	public void setInterpolMode(boolean pInterpolMode) {
		interpolationMode = pInterpolMode;
	}

	private void setClassicalPalette() {
		double inputs[] = { 1, 1000, 10000 };
		double factors[] = { 1, 10, 100 };

		this.setPalette(inputs, factors, true);
	}

	private double interpolateFactor(Double lowerOutputFactor,
			Double higherOutputFactor, double interpolationFactor) {
		double interpolatedFactor = lowerOutputFactor + interpolationFactor
				* (higherOutputFactor - lowerOutputFactor);
		return interpolatedFactor;
	}

	@Override
	public String log() {
		// TODO Auto-generated method stub
		return null;
	}

}
