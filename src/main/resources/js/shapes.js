var Point = function(x, y, z) {
	this.x, this.y, this.z, 
	this.x = x;
	this.y = y;
	this.z = z;
}

function transformAxis(point) {
	var temp = point.y;
	point.y = point.z;
	point.z = -temp;
    return point;
}

var Circle = function() {
	this.circlePoints;
	this.circlePoints = new Array();
}

Circle.prototype.generateCircle = function(radius, thetaInRad) {
	var point;
	var angle;
	
	var arrayLength = 2*thetaInRad + 1;
	this.circlePoints = new Array(arrayLength);
    
    for(var circleIndex=0; circleIndex<arrayLength; circleIndex++){
        angle = (circleIndex * Math.PI)/thetaInRad;
        point = generatePoint(radius,angle);
        this.circlePoints[circleIndex] = point;
    }
    return this.circlePoints;
}

function generatePoint (radius, angleInRad) {
    var x = radius * Math.cos(angleInRad);
    var y = radius * Math.sin(angleInRad);
    var z = 0.0;
    var point = new Point(x, y, z);
    return point;
}


var Ellipse = function() {
	this.ellipsePoints;
	this.ellipsePoints = new Array();
}

function generateEllipsePoint(a, b, angleInRad) {
    var x = a * Math.cos(angleInRad);
    var y = b * Math.sin(angleInRad);
    var z = 0.0;
    var point = new Point(x, y, z);
    return point;
}

Ellipse.prototype.generateEllipse = function(a, b, thetaInRad) {
	var point;
	var angle;
	
	var arrayLength = thetaInRad + 1;
	this.ellipsePoints = new Array(arrayLength);
    
    for(var ellipseIndex=0; ellipseIndex<arrayLength; ellipseIndex++){
        angle = (ellipseIndex * Math.PI)/thetaInRad;
        point = generateEllipsePoint(a, b, angle);
        this.ellipsePoints[ellipseIndex] = point;
    }
    return this.ellipsePoints;
}

var Ribbon = function() {
	this.ribbonPoints;
	this.ribbonPoints = new Array();
}

Ribbon.prototype.generateRibbon = function(radius, distance, thetaInRad, step) {
	var point;
	var angle;
	
	var turns = Math.round(distance/step)+1;
    var y = -(distance/2);
	
	turns = parseInt(turns);
	this.ribbonPoints = new Array(turns);
    
    for(var ribbonIndex=0; ribbonIndex<turns; ribbonIndex++, y+=step){
        angle = (ribbonIndex * Math.PI)/thetaInRad;
        point = generatePoint(radius, angle);
		point = transformAxis(point);
		point.y = y;
        this.ribbonPoints[ribbonIndex] = point;
    }
    return this.ribbonPoints;
}