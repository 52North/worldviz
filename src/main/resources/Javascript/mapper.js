var T3dColor = function(a, b, c, d, e) {
	this.mRed, this.mGreen, this.mBlue, this.mAlpha;
	var mColMod = 1;

	if (a === undefined && b === undefined && c === undefined && d === undefined) {
		this.mRed = 1.0;
		this.mGreen = 1.0;
		this.mBlue = 1.0;
		this.mAlpha = 1.0;
	} else if (typeof (a) === 'string') {

		if (a == "HSV") {
			mColMod = 2;
			var h = b, s = c, v = d;
			h = parseFloat(h);
			s = parseFloat(s);
			v = parseFloat(v);
			if (s == 0) {
				this.mRed = v;
				this.mGreen = v;
				this.mBlue = v;
			} else {
				sixth = Math.PI / 3.0;
				h /= sixth;
				i = parseInt(h);
				f = parseFloat(h - i);
				p = parseFloat(v * (1 - s));
				q = parseFloat(v * (1 - s * f));
				t = parseFloat(v * (1 - s * (1 - f)));

				if (i == 0) {
					this.mRed = v;
					this.mGreen = t;
					this.mBlue = p;
				} else if (i == 1) {
					this.mRed = q;
					this.mGreen = v;
					this.mBlue = p;
				} else if (i == 2) {
					this.mRed = p;
					this.mGreen = v;
					this.mBlue = t;
				} else if (i == 3) {
					this.mRed = p;
					this.mGreen = q;
					this.mBlue = v;
				} else if (i == 4) {
					this.mRed = t;
					this.mGreen = p;
					this.mBlue = v;
				} else {
					this.mRed = v;
					this.mGreen = p;
					this.mBlue = q;
				}
			}
		} else {
			mColMod = 1;
			this.mRed = parseFloat(b);
			this.mGreen = parseFloat(c);
			this.mBlue = parseFloat(d);
		}
		if (e == undefined) {
			this.mAlpha = 1.0;
		} else {
			this.mAlpha = parseFloat(e);
		}
	} else {
		if (d === undefined) {
			this.mAlpha = 1.0;
		} else {
			this.mAlpha = parseFloat(d);
		}
		this.mRed = parseFloat(a);
		this.mGreen = parseFloat(b);
		this.mBlue = parseFloat(c);
	}
}

T3dColor.prototype.getHue = function() {
	var mRed = this.mRed, mGreen = this.mGreen, mBlue = this.mBlue;
	var min_value = Math.min(mRed, mGreen, mBlue);
	var max_value = Math.max(mRed, mGreen, mBlue);
	var delta = max_value - min_value;
	if (delta <= 0.0) {
		return 0.0;
	} else {
		var H;
		if (mRed == max_value) {
			H = (mGreen - mBlue) / delta;
		} else if (mGreen == max_value) {
			H = 2.0 + (mBlue - mRed) / delta;
		} else {
			H = 4.0 + (mRed - mGreen) / delta;
		}
		var sixth = (Math.PI / 3.0);
		H *= sixth;
		if (H < 0.0) {
			H += 6.0 * sixth;
		}
		return H;
	}
}

T3dColor.prototype.getSaturation = function() {
	var mRed = this.mRed, mGreen = this.mGreen, mBlue = this.mBlue;
	var min_value = Math.min(mRed, mGreen, mBlue);
	var max_value = Math.max(mRed, mGreen, mBlue);
	var delta = max_value - min_value;
	if (max_value != 0.0) {
		return (delta / max_value);
	} else {
		return 0.0;
	}
}

T3dColor.prototype.getValue = function() {
	var lMax = this.mRed;
	if (this.mGreen > lMax)
		lMax = this.mGreen;
	if (this.mBlue > lMax)
		lMax = this.mBlue;

	return lMax;
}

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
	return this.outputColors[this.inputValues.length - 1];
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

function interpolateExtent(lowerOutputExtent, higherOutputExtent,interpolationFactor) {
	var interpolatedExtent = lowerOutputExtent + interpolationFactor* (higherOutputExtent - lowerOutputExtent);
	return interpolatedExtent;
}
