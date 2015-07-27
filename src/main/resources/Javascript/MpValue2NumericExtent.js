var MpValue2NumericExtent = function() {
	this.inputValues = [ 1, 1000, 10000 ];
	this.outputExtents = [ 1, 10, 100 ];
	this.interpolationMode = true;
}

MpValue2NumericExtent.prototype.setPalette = function(inputValues,
		outputExtents, interpolationMode) {
	if (inputValues.length === outputExtents.length) {
		this.inputValues = inputValues;
		this.outputExtents = outputExtents;
		this.interpolationMode = interpolationMode;
	}
}

MpValue2NumericExtent.prototype.transform = function(inputValue) {

	if (typeof (inputValue) === 'number') {

		var numericExtent = 0;

		if (inputValue <= this.inputValues[0]) {
			numericExtent = this.outputExtents[0];

			return numericExtent;
		}

		for (i = 1; i < this.inputValues.length; i++) {
			var higherValue = this.inputValues[i];
			if (inputValue <= higherValue) {
				if (!this.interpolationMode) {
					numericExtent = this.outputExtents[i - 1];

					return numericExtent;
				} else {
					var lowerValue = this.inputValues[i - 1];
					var interpolationFactor = ((inputValue - lowerValue) / (higherValue - lowerValue));

					numericExtent = interpolateExtent(
							this.outputExtents[i - 1], this.outputExtents[i],
							interpolationFactor);

					return numericExtent;
				}
			}
		}

		numericExtent = this.outputExtents[inputValues.length - 1];

		return numericExtent;
	}
}

function interpolateExtent(lowerOutputExtent, higherOutputExtent,
		interpolationFactor) {
	var interpolatedExtent = lowerOutputExtent + interpolationFactor
			* (higherOutputExtent - lowerOutputExtent);
	return interpolatedExtent;
}

var numericMapper = new MpValue2NumericExtent();
