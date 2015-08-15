var MpSimpleHypsometricColor = function() {
	this.inputValues = [ -20, 0, 400, 1500, 3000 ];
	this.outputColors = [ new T3dColor("RGB", 0, 0.4, 0),
			new T3dColor("RGB", 0, 0.8, 0), new T3dColor("RGB", 1.0, 1.0, 0.5),
			new T3dColor("RGB", 0.78, 0.27, 0),
			new T3dColor("RGB", 0.82, 0.2, 0) ];
	this.interpolationMode = true;
}

MpSimpleHypsometricColor.prototype.setPalette = function(inputValues,
		outputColors, interpolationMode) {
	if (inputValues.length === outputColors.length) {
		this.inputValues = inputValues;
		this.outputColors = outputColors;
		this.interpolationMode = interpolationMode;
	}
}

MpSimpleHypsometricColor.prototype.transform = function(inputValue) {
	if (inputValue <= this.inputValues[0])
		return this.outputColors[0];
	for (i = 1; i < this.inputValues.length; i++) {
		var hi = this.inputValues[i];
		if (inputValue <= hi) {
			if (!this.interpolationMode)
				return this.outputColors[i - 1];
			else {
				var lo = this.inputValues[i - 1];
				var factor = ((inputValue - lo) / (hi - lo));
				return interpolateHSV(this.outputColors[i - 1],
						this.outputColors[i], factor);
			}
		}
	}
	return this.outputColors[inputValues.length - 1];
}

function interpolateHSV(pColFrom, pColTo, pFactor) {
	var hue = pColFrom.getHue() + pFactor
			* (pColTo.getHue() - pColFrom.getHue());
	var sat = pColFrom.getSaturation() + pFactor
			* (pColTo.getSaturation() - pColFrom.getSaturation());
	var val = pColFrom.getValue() + pFactor
			* (pColTo.getValue() - pColFrom.getValue());
	var alf = pColFrom.mAlpha + pFactor
			* (pColTo.mAlpha - pColFrom.mAlpha);
	return new T3dColor("HSV", hue, sat, val, alf);
}

var colorMapper = new MpSimpleHypsometricColor();