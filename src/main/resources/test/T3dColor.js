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