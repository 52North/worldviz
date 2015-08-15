	function handleSingleClick(shape){
        var data_class = $(shape).attr("data-class"); 
            if(data_class == "feature"){
                var data_index = $(shape).attr("data-index"); 
                document.getElementById("lastClickedObject").innerHTML = data_class + " " + data_index;
                var indices = showRelationsForNode(data_index);
				highlightFeatures(indices);
				document.getElementById("feature"+data_index).setAttribute('diffuseColor',getCurrentColor());
				document.getElementById("feature"+data_index).setAttribute('emissiveColor',getCurrentGlow());
				document.getElementById("featureLabel"+data_index).setAttribute('render','true');
            }
			else if(data_class == "relation"){
				var shape_type = $(shape).attr("def");
				var first = shape.getAttribute("data-firstId");
				var second = shape.getAttribute("data-secondId");
                document.getElementById("lastClickedObject").innerHTML = data_class + " " +shape_type +" "+first + " "+ second;
				changeRelation(shape);
            }
            else{s
				document.getElementById("lastClickedObject").innerHTML = data_class + " false";
            }
    }
	
	function getRadioShape(){
		var radios = document.getElementsByName("shapeRadioButton");
		var shapeString = "";
		for (var i = 0, length = radios.length; i < length; i++) {
			if (radios[i].checked) {
				shapeString = radios[i].value;
				break;
			}
		}
		if(shapeString == ""){
			document.getElementById("ellipseRadioButton").checked = true;
			shapeString = "ellipseShape";
		}
		return shapeString;
	}

    function showRelationsForNode(nodeId) {
        var x = document.getElementsByTagName("shape");
        var i;
		var feature_id = [];
        for (i = 0; i < x.length; i++) {
            var data_class = x[i].getAttribute("data-class");
			var shapeString = getRadioShape();
            if(data_class == "relation"){
                var firstId = x[i].getAttribute("data-firstId");
                var secondId = x[i].getAttribute("data-secondId");
				var data_def = x[i].getAttribute("def");
                if( (data_def == shapeString || data_def == "arrowConeShape" || data_def == "arrowCylinderShape") && (firstId == nodeId || secondId == nodeId)){
                    x[i].render = "true";
					if(firstId == nodeId){
						feature_id.push(secondId);
					}
					else{
						feature_id.push(firstId);
					}
                }
                else{
                    x[i].render = "false";
                }
            }
        }
		var unique_id = [];
		for(var i in feature_id){
			if(unique_id.indexOf(feature_id[i]) === -1){
				unique_id.push(feature_id[i]);
			}
		}
		return unique_id.sort();
    }

    function showAllRelations() {
		var shapeString = getRadioShape();
        var x = document.getElementsByTagName("shape");
        var i;
        for (i = 0; i < x.length; i++) {
            var data_class = x[i].getAttribute("data-class");
			var data_def = x[i].getAttribute("def");
			if(data_class == "relation" && (data_def == shapeString || data_def == "arrowConeShape" || data_def == "arrowCylinderShape")){
                x[i].render = "true";
            }
			else if(data_class != "relation"){
				x[i].render = "true";
			}
			else{
				x[i].render = "false";
			}
        }
		restoreFeatures();
    }

	function addNewColor(){
		var table = document.getElementById("colorTable");

		var newInputColor = document.getElementById("NewInputColor").value;
		var newOutputColor = document.getElementById("NewOutputColor").value;

		if(newInputColor.split(" ").length == 1){
			newInputColor = parseFloat(newInputColor);
		}
		else{
			newInputColor = NaN;
		}
		
		if(!isNaN(newInputColor)){
			newOutputColor = hexToRgb(newOutputColor);
			var valuePresent = false;
			var i = 1;
			var table_length = table.rows.length;

			for(;i<table_length;i++){
				if (parseFloat(table.rows[i].cells[0].innerHTML) == newInputColor){
					valuePresent = true;
					break;
				}
			}

			if(!valuePresent){
				var rows = table.rows.length;
				var newRow = table.insertRow(rows);
				var inputColor = newRow.insertCell(0);
				var outputColor = newRow.insertCell(1);
				inputColor.innerHTML = newInputColor;
				outputColor.innerHTML = newOutputColor;
			}
			else{
				table.rows[i].cells[1].innerHTML = newOutputColor;
			}
			
			document.getElementById("NewInputColor").value = "";
			document.getElementById("NewOutputColor").value = "";
			document.getElementById("NewOutputColor").style.backgroundColor = "white";
		}		
	}
	
	function addNewWidth(){
		var done = false;
		var table = document.getElementById("widthTable");
	
		var newInputWidth = document.getElementById("NewInputWidth").value;
		var newOutputWidth = document.getElementById("NewOutputWidth").value;
		
		if( (newInputWidth.split(" ")).length == 1 && (newOutputWidth.split(" ")).length == 1){
			newInputWidth = parseFloat(newInputWidth);
			newOutputWidth = parseFloat(newOutputWidth);
		}
		else{
			newInputWidth = newOutputWidth = NaN;
		}
		
		if( !isNaN(newInputWidth) && !isNaN(newOutputWidth) ){
		
			var valuePresent = false;
			var i = 1;
			var table_length = table.rows.length;
			
			for(;i<table_length;i++){
				if (parseFloat(table.rows[i].cells[0].innerHTML) == newInputWidth){
					valuePresent = true;
					break;
				}
			}
			
			if(!valuePresent){
				var rows = table.rows.length;
				var newRow = table.insertRow(rows);
				var inputWidth = newRow.insertCell(0);
				var outputWidth = newRow.insertCell(1);
				inputWidth.innerHTML = newInputWidth;
				outputWidth.innerHTML = newOutputWidth;
			}
			else{
				table.rows[i].cells[1].innerHTML = newOutputWidth;
			}
			done = true;
		}
		if(done){
			document.getElementById("NewInputWidth").value = "";
			document.getElementById("NewOutputWidth").value = "";
		}
		
	}
	
	function deleteNewColor(){
		var table = document.getElementById("colorTable");
		var done = false;	
		var deleteColor = document.getElementById("DeleteColor").value;

		if( (deleteColor.split(" ")).length == 1 ){
			deleteColor = parseFloat(deleteColor);
		}
		
		if( !isNaN(deleteColor) ){
		
			var valuePresent = false;
			var i = 1;
			var table_length = table.rows.length;
			
			for(;i<table_length;i++){
				if (parseFloat(table.rows[i].cells[0].innerHTML) == deleteColor){
					valuePresent = true;
					break;
				}
			}
			
			if(valuePresent){
				table.deleteRow(i);
				done = true;
			}
		}
		if(done){
			document.getElementById("DeleteColor").value = "";
		}

	}
	
	function deleteNewWidth(){
		var table = document.getElementById("widthTable");
		var done = false;	
		var deleteWidth = document.getElementById("DeleteWidth").value;

		if( (deleteWidth.split(" ")).length == 1 ){
			deleteWidth = parseFloat(deleteWidth);
		}
		
		if( !isNaN(deleteWidth) ){
		
			var valuePresent = false;
			var i = 1;
			var table_length = table.rows.length;
			
			for(;i<table_length;i++){
				if (parseFloat(table.rows[i].cells[0].innerHTML) == deleteWidth){
					valuePresent = true;
					break;
				}
			}
			
			if(valuePresent){
				table.deleteRow(i);
				done = true;
			}
		}
		if(done){
			document.getElementById("DeleteWidth").value = "";
		}

	}

	function sortColorTable(f,n){ 
	  var rows = $('#colorTable tbody  tr').get(); 
 
	  rows.sort(function(a, b) { 
 
	  var A = $(a).children('td').eq(n).text().toUpperCase(); 
	  var B = $(b).children('td').eq(n).text().toUpperCase(); 
	   
	  A = parseFloat(A); 
	  B = parseFloat(B); 
 
	  if(A < B) { 
		return 1*f; 
	  } 
	  if(A > B) { 
		return -1*f; 
	  } 
	  return 0; 
	  }); 
 
	  $.each(rows, function(index, row) { 
		$('#colorTable').children('tbody').append(row); 
	  }); 
	} 
	 
	 
	function sortColorFunction(){ 
		var f_sl = 1; 
		f_sl *= -1; 
		var n = 0; 
		sortColorTable(f_sl,n); 
	}; 
	 
	function sortWidthTable(f,n){ 
	  var rows = $('#widthTable tbody  tr').get(); 
 
	  rows.sort(function(a, b) { 
 
	  var A = $(a).children('td').eq(n).text().toUpperCase(); 
	  var B = $(b).children('td').eq(n).text().toUpperCase(); 
	   
	  A = parseFloat(A); 
	  B = parseFloat(B); 
 
	  if(A < B) { 
		return 1*f; 
	  } 
	  if(A > B) { 
		return -1*f; 
	  } 
	  return 0; 
	  }); 
 
	  $.each(rows, function(index, row) { 
		$('#widthTable').children('tbody').append(row); 
	  }); 
	} 
	 
	 
	function sortWidthFunction(){ 
		var f_sl = 1; 
		f_sl *= -1; 
		var n = 0; 
		sortWidthTable(f_sl,n); 
	}; 
	
	function getInputColors(){ 
		var inputColors = []
		var table = document.getElementById("colorTable");
		var table_length = table.rows.length;
			
		for(var i = 1;i<table_length;i++){
			var inputColorValue = parseFloat(table.rows[i].cells[0].innerHTML);
			inputColors.push(inputColorValue);
		}
		return inputColors;
	};
	
	function getOutputColors(){ 
		var outputColors = []
		var table = document.getElementById("colorTable");
		var table_length = table.rows.length;
			
		for(var i = 1;i<table_length;i++){
			var outputColorValue = table.rows[i].cells[1].innerHTML;
			outputColorValue = outputColorValue.split(" ");
			var red = parseFloat(outputColorValue[0]);
			var green = parseFloat(outputColorValue[1]);
			var blue = parseFloat(outputColorValue[2]);
			var color = new T3dColor("RGB", red, green, blue, 1);
			outputColors.push(color);
		}
		return outputColors;
	};
	
	function getInputWidths(){ 
		var inputWidths = []
		var table = document.getElementById("widthTable");
		var table_length = table.rows.length;
			
		for(var i = 1;i<table_length;i++){
			var inputWidthValue = parseFloat(table.rows[i].cells[0].innerHTML);
			inputWidths.push(inputWidthValue);
		}
		return inputWidths;
	};

	function getOutputWidths(){ 
		var outputWidths = []
		var table = document.getElementById("widthTable");
		var table_length = table.rows.length;
			
		for(var i = 1;i<table_length;i++){
			var outputWidthValue = parseFloat(table.rows[i].cells[1].innerHTML);
			outputWidths.push(outputWidthValue);
		}
		return outputWidths;
	};
	
	function getColorMapper(){
		sortColorFunction();
		var inputColors = getInputColors();
		var outputColors = getOutputColors();
		
		var colorMapper = new MpSimpleHypsometricColor();
		colorMapper.setPalette(inputColors, outputColors, getLinearColorInterpolation());
		return colorMapper;
	}
	
	function getWidthMapper(){
		sortWidthFunction();
		var inputWidths = getInputWidths();
		var outputWidths = getOutputWidths();
		
		var widthMapper = new MpValue2NumericExtent();
		widthMapper.setPalette(inputWidths, outputWidths, getLinearWidthInterpolation());
		return widthMapper;
	}
	
	function changeColors(nodeId) {
		var colorMapper = getColorMapper();
        var x = document.getElementsByTagName("material");
        var i;
        for (i = 0; i < x.length; i++) {
			if(x[i].hasAttribute("data-weight")){
				var data_weight = x[i].getAttribute("data-weight");
				data_weight = parseFloat(data_weight);
				var transformedColor = colorMapper.transform(data_weight);
				var red = transformedColor.mRed;
				var green = transformedColor.mGreen;
				var blue = transformedColor.mBlue;
				var color = red + ' '+ green + ' ' + blue;
				x[i].setAttribute('diffuseColor', color);
			}
        }
    }
	
    function changeRelation(relation) {
        var x = document.getElementsByTagName("shape");
		var shape_type = relation.getAttribute("def");
		if(shape_type == "ellipseShape"){
			shape_type = "cylinderShape";
		}
		else if(shape_type == "cylinderShape"){
			shape_type = "ribbonShape";
		}
		else if(shape_type == "ribbonShape"){
			shape_type = "ellipseShape";
		}
		
		var first_id = relation.getAttribute("data-firstId");
		var second_id = relation.getAttribute("data-secondId");
		
        var i;
        for (i = 0; i < x.length; i++) {
            if(x[i].getAttribute("data-class") == "relation" &&
			x[i].getAttribute("data-firstId") == first_id &&
			x[i].getAttribute("data-secondId") == second_id &&
			x[i].getAttribute("def") == shape_type){
                    x[i].render = "true";
                
            }
        }
		if(shape_type != "arrowConeShape" && shape_type != "arrowCylinderShape"){
			relation.render = "false";
		}
    }
	
	function saveTextAsFile(){      
		var textToWrite = "<html>" + $('html').html() + "</html>";
		var textFileAsBlob = new Blob([textToWrite], {type:'text/plain'});
		var fileNameToSaveAs = "export.html";
		
		var downloadLink = document.createElement("a");
		downloadLink.download = fileNameToSaveAs;
		downloadLink.innerHTML = "Hidden Link";

		window.URL = window.URL || window.webkitURL;
		downloadLink.href = window.URL.createObjectURL(textFileAsBlob);
		downloadLink.onclick = destroyClickedElement;
		downloadLink.style.display = "none";
		document.body.appendChild(downloadLink);

		downloadLink.click();
	}

	function destroyClickedElement(event){
		document.body.removeChild(event.target);
	}
	
	function arrayDifference(a1, a2){
		var a=[], diff=[];
		for(var i=0;i<a1.length;i++){
			a[a1[i]]=true;
		}
		for(var i=0;i<a2.length;i++){
			if(a[a2[i]]){
				delete a[a2[i]];
			}
			else{
				a[a2[i]]=true;
			}
		}
		for(var k in a){
			diff.push(k);
		}
		return diff;
	}
	
	Array.range= function(a, b){
		var A= [];
		A[0]= a;
		step = 1;
		while(a+step<= b){
			A[A.length]= a+= step;
		}		
		return A;
	}
	
	function changeWidths() {
		var widthMapper = getWidthMapper();
        var x = document.getElementsByTagName("extrusion");
		
		var circle = new Circle();
		var ribbon = new Ribbon();
		var ellipse = new Ellipse();
		
        var i,weight,shape_type,radius,distance,circlePoints,ribbonPoints,curvePoints;
        for (i = 0; i < x.length; i++) {
			
			shape_type = x[i].getAttribute("data-shape");
			weight = parseFloat(x[i].getAttribute("data-weight"));
			distance = parseFloat(x[i].getAttribute("data-distance"));
			radius = widthMapper.transform(weight);
			
			if(shape_type == "ribbonShape"){
				
				circlePoints = circle.generateCircle(radius, getRibbonCircleTurns());
				
				var crossSectionValue = "";
				for (var j = 0; j < circlePoints.length; j++) {
					crossSectionValue +=  circlePoints[j].x+" "+circlePoints[j].y+" ";
				}
				x[i].setAttribute('crossSection',crossSectionValue);
				
				ribbonPoints = ribbon.generateRibbon(radius,distance,getRibbonHelixTurns(),getRibbonStep());
				
				var spineValue = "";
				for (var j = 0; j < ribbonPoints.length; j++) {
					spineValue +=  ribbonPoints[j].x+" "+ribbonPoints[j].y+" "+ribbonPoints[j].z+" ";
				}
				x[i].setAttribute('spine',spineValue);
				
			}
			else if(shape_type == "ellipseShape"){
				circlePoints = circle.generateCircle(radius, getCurveCircleTurns());
				
				var crossSectionValue = "";
				for (var j = 0; j < circlePoints.length; j++) {
					crossSectionValue +=  circlePoints[j].x+" "+circlePoints[j].y+" ";
				}
				x[i].setAttribute('crossSection',crossSectionValue);
				
				var ellipse_x = parseFloat(x[i].getAttribute("data-ellipse_x"));
				var ellipse_y = parseFloat(x[i].getAttribute("data-ellipse_y"));
				curvePoints = ellipse.generateEllipse(ellipse_x ,ellipse_y, getCurveEllipseTurns());
				
				var spineValue = "";
				for (var j = 0; j < curvePoints.length; j++) {
					spineValue +=  curvePoints[j].x+" "+curvePoints[j].y+" "+curvePoints[j].z+" ";
				}
				x[i].setAttribute('spine',spineValue);
				
			}
        }
		
		var y = document.getElementsByTagName("cylinder");
		for (i = 0; i < y.length; i++) {
			weight = parseFloat(y[i].getAttribute("data-weight"));
			radius = widthMapper.transform(weight);
			y[i].setAttribute('radius',radius);
		}

		var z = document.getElementsByTagName("cone");
		var coneRatio = getArrowRatio();
		for (i = 0; i < z.length; i++) {
			weight = parseFloat(z[i].getAttribute("data-weight"));
			radius = widthMapper.transform(weight);
			radius *= coneRatio;
			z[i].setAttribute('bottomRadius',radius);
		}
		
    }
	
    function highlightFeatures(indices){
        for ( var i = 0; i < indices.length; i++ ) {
			var x = "feature" + indices[i];
            var feature = document.getElementById(x);
			feature.setAttribute('diffuseColor', getHighlightColor());
			feature.setAttribute('emissiveColor', getHighlightGlow());
			
			var y = "featureLabel" + indices[i];
			var label = document.getElementById(y);
			label.setAttribute('render', 'true');
		}
        restorePreviousFeatures(indices);
    }

    function restoreFeatures(){
            for ( var i = 0; i < getIndexSize(); i++ ) {
                var feature_id = "feature" + (i+1);
                document.getElementById(feature_id).setAttribute('diffuseColor', getNormalColor());
                document.getElementById(feature_id).setAttribute('emissiveColor', getNormalGlow());
							
				var y = "featureLabel" + (i+1);
				document.getElementById(y).setAttribute('render', 'true');
            }
    }

    function restorePreviousFeatures(indices){
        for (var i=0; i<indices.length; i++){
			indices[i] = parseInt(indices[i], 10);
        }
        var all_indices = Array.range(1, getIndexSize());
        var indices = arrayDifference(all_indices,indices);
        for ( var i = 0; i < indices.length; i++ ) {
			var x = "feature" + indices[i];
			var feature = document.getElementById(x);
			feature.setAttribute('diffuseColor', getNormalColor());
			feature.setAttribute('emissiveColor', getNormalGlow());
			
			var y = "featureLabel" + indices[i];
			var label = document.getElementById(y);
			label.setAttribute('render', 'false');
        }
    }
	
	function restoreViewpoint(){
		var viewpoint = document.getElementById("viewpoint");
		viewpoint.setAttribute('position', getViewpointPosition());
		viewpoint.setAttribute('orientation', getViewpointOrientation());
	}
	
	function hexToRgb(hex) {
		var bigint = parseInt(hex, 16);
		var r = ((bigint >> 16) & 255)/255;
		var g = ((bigint >> 8) & 255)/255;
		var b = (bigint & 255)/255;

		r = Math.round(r * 1000) / 1000
		g = Math.round(g * 1000) / 1000
		b = Math.round(b * 1000) / 1000
		return r + " " + g + " " + b;
	}
	
    $(document).ready(function(){
        $("shape").each(function() {
            $(this).attr("onclick", "handleSingleClick(this)");
        });
    });
