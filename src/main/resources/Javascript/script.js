	function handleSingleClick(shape){
        var data_class = $(shape).attr("data-class"); 
            if(data_class == "feature"){
                var data_index = $(shape).attr("data-index"); 
                document.getElementById("lastClickedObject").innerHTML = data_class + " " + data_index;
                showRelationsForNode(data_index);
            }
			if(data_class == "relation"){
				var shape_type = $(shape).attr("def");
				var first = shape.getAttribute("data-firstId");
				var second = shape.getAttribute("data-secondId");
                document.getElementById("lastClickedObject").innerHTML = data_class + " " +shape_type +" "+first + " "+ second;
				changeRelation(shape);
            }
            else{
				document.getElementById("lastClickedObject").innerHTML = data_class + " false";
            }
    }

    function showRelationsForNode(nodeId) {
        var x = document.getElementsByTagName("shape");
        var i;
        for (i = 0; i < x.length; i++) {
            var data_class = x[i].getAttribute("data-class");
            if(data_class == "relation"){
                var firstId = x[i].getAttribute("data-firstId");
                var secondId = x[i].getAttribute("data-secondId");
                if(firstId == nodeId || secondId == nodeId){
                    x[i].render = "true";
                }
                else{
                    x[i].render = "false";
                }
            }
        }
    }

    function showAllRelations() {
        var x = document.getElementsByTagName("shape");
        var i;
        for (i = 0; i < x.length; i++) {
            var data_class = x[i].getAttribute("data-class");
            if(data_class == "relation"){
                x[i].render = "true";
            }
        }
    }

	function addNewColor(){
		var table = document.getElementById("colorTable");
		var done = false;

		var newInputColor = document.getElementById("NewInputColor").value;
		var newOutputColor = document.getElementById("NewOutputColor").value;

		if((newInputColor.split(" ")).length == 1){
			newInputColor = parseFloat(newInputColor);
		}

		newOutputColor = newOutputColor.split(" ");

		if(newOutputColor.length == 3){
			var red = parseFloat(newOutputColor[0]);
			var green = parseFloat(newOutputColor[1]);
			var blue = parseFloat(newOutputColor[2]);

			if( !isNaN(newInputColor) && red>=0 && red<=1 && green>=0 && green<=1 && blue>=0 && blue<=1 ){
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
					outputColor.innerHTML = red + " "+green +" "+blue;
				}
				else{
					table.rows[i].cells[1].innerHTML = red + " "+green +" "+blue;
				}
				done = true;
			}
			
		}
		
		if(done){
			document.getElementById("NewInputColor").value = "";
			document.getElementById("NewOutputColor").value = "";
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
		var rows = document.getElementById("colorTable").rows.length;
		table.deleteRow(rows-1);
	}
	
	function deleteNewWidth(){
		var table = document.getElementById("widthTable");
		var rows = document.getElementById("widthTable").rows.length;
		table.deleteRow(rows-1);
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
			var inputColorValue = table.rows[i].cells[0].innerHTML;
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
			var inputWidthValue = table.rows[i].cells[0].innerHTML;
			inputWidths.push(inputWidthValue);
		}
		return inputWidths;
	};

	function getOutputWidths(){ 
		var outputWidths = []
		var table = document.getElementById("widthTable");
		var table_length = table.rows.length;
			
		for(var i = 1;i<table_length;i++){
			var outputWidthValue = table.rows[i].cells[1].innerHTML;
			outputWidths.push(outputWidthValue);
		}
		return outputWidths;
	};
	
	function getColorMapper(){
		sortColorFunction();
		var inputColors = getInputColors();
		var outputColors = getOutputColors();
		
		var colorMapper = new MpSimpleHypsometricColor();
		colorMapper.setPalette(inputColors, outputColors, true);
		return colorMapper;
	}
	
	function getWidthMapper(){
		sortWidthFunction();
		var inputWidths = getInputWidths();
		var outputWidths = getOutputWidths();
		
		var widthMapper = new MpValue2NumericExtent();
		widthMapper.setPalette(inputWidths, outputWidths, true);
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
				var color = red + ' '+ blue + ' ' + green;
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
	
	
    $(document).ready(function(){
        $("shape").each(function() {
            $(this).attr("onclick", "handleSingleClick(this)");
        });
    });
