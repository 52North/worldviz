package org.n52.v3d.worldviz.triturusextensions.mappers;

import java.util.ArrayList;
import java.util.List;

import org.n52.v3d.triturus.core.T3dException;
import org.n52.v3d.triturus.core.T3dProcMapper;
import org.n52.v3d.triturus.t3dutil.T3dColor;

/**
 * This mapper is meant to determine numericExtent-values. First the mapping
 * between input data and output extent values shall be set (e.g. 5000(input) =
 * 1 (MIN extent) and 100000 (input) = 100 (MAX extent)). Then the extent-output
 * of any other input value will be either a static value corresponding to the
 * defined extentMapping or calculated through linear interpolation (e.g. 50000
 * (input) =~ 47.89 (output)).<br/>
 * <br/>
 * A typical use case of this mapper may be calculating a scale-factor. For
 * instance, in the 3D-scene-decription-language X3D you might wish to set the
 * size of an object to scale the visualization object in a certain coordinate
 * direction.<br/>
 * Another example might be the calculation of the extruded size of an object. <br/>
 * <br/>
 * Note that you should definitely call
 * {@link #setPalette(double[], double[], boolean)} to match mapping values to
 * your use case before you transform any values. Otherwise a classical palette
 * will be set with following values:
 * <code>inputValues = [1, 1000, 10000]; outputExtents = [1, 10, 100]</code>
 * 
 * 
 * @author Christian Danowski
 * 
 */
public class MpValue2NumericExtent extends T3dProcMapper {

	private List<Double> inputValues = new ArrayList<Double>();
	private List<Double> outputExtents = new ArrayList<Double>();
	private boolean interpolationMode = true;

	public MpValue2NumericExtent() {
		this.setClassicalPalette();
	}

	/**
	 * Provides the corresponding numericExtent-parameter for the input value. Note
	 * that you should have called/noticed the
	 * {@link #setPalette(double[], T3dColor[], boolean)} and
	 * {@link #setInterpolMode(boolean)} beforehand.
	 * 
	 * @param inputValue
	 *            a double-value for which the corresponding numericExtent is
	 *            determined.
	 * @return the numericExtent
	 * @throws T3dException
	 */
	public double transform(double inputValue) throws T3dException {
		if (inputValue <= ((Double) inputValues.get(0)).doubleValue())
			return outputExtents.get(0);
		for (int i = 1; i < inputValues.size(); i++) {
			double higherValue = ((Double) inputValues.get(i)).doubleValue();
			if (inputValue <= higherValue) {
				if (!this.getInterpolMode())
					return outputExtents.get(i - 1);
				else {

					double lowerValue = ((Double) inputValues.get(i - 1))
							.doubleValue();
					double interpolationFactor = (double) ((inputValue - lowerValue) / (higherValue - lowerValue));
					return this.interpolateExtent(outputExtents.get(i - 1),
							outputExtents.get(i), interpolationFactor);
				}
			}
		}
		return outputExtents.get(inputValues.size() - 1);
	}

	/**
	 * Sets the numericExtent palette and thus the mapping of input-values to
	 * output-numericExtent-values. Here, the input value <i>inputValues[i]</i> will be
	 * mapped to the output numericExtent <i>outputExtents[i]</i>. Dependent on
	 * the given interpolation mode, the mapper will interpolate between the
	 * numericExtent values, or for the overall interval <tt>inputValues[i] &lt; h &lt;=
	 * inputValues[i + 1]</tt> a uniform numericExtent value <tt>outputExtents[i]
	 * </tt> will be assigned.
	 * 
	 * @param inputValues
	 *            Array which holds input values
	 * @param outputExtentValues
	 *            Array which holds the corresponding numericExtent values
	 * @param pInterpolMode
	 *            <i>true</i> for linear interpolation , <i>false</i> for
	 *            uniform classes.
	 */
	public void setPalette(double[] inputValues, double[] outputExtentValues,
			boolean pInterpolMode) {
		if (inputValues.length != outputExtentValues.length)
			throw new T3dException("Illegal mapping specification ("
					+ inputValues.length + " != " + outputExtentValues.length
					+ ".");

		this.inputValues.clear();
		this.outputExtents.clear();

		for (int i = 0; i < inputValues.length; i++) {
			this.inputValues.add(new Double(inputValues[i]));
			this.outputExtents.add(outputExtentValues[i]);
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
		double numericExtents[] = { 1, 10, 100 };

		this.setPalette(inputs, numericExtents, true);
	}

	private double interpolateExtent(Double lowerOutputExtent,
			Double higherOutputExtent, double interpolationFactor) {
		double interpolatedExtent = lowerOutputExtent + interpolationFactor
				* (higherOutputExtent - lowerOutputExtent);
		return interpolatedExtent;
	}

	@Override
	public String log() {
		// TODO Auto-generated method stub
		return null;
	}

}
