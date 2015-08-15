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