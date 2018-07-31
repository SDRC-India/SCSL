/***************************************************Multi line chart****************************************************/
/*
 * @author Laxman(laxman@sdrc.co.in)
*/

app
.directive(
		"sdrcMultiLineChart",
		function($window) {
			function link(scope, el) {

				var el = el[0];
				var clicks = 0;

				// Render graph based on 'data'
				scope.renderDataSeries = function(lineData) {
					// remove all
					d3.select(el).selectAll("*").remove();
					var margin = {
						top : 20,
						right : 15,
						bottom : 60,
						left : 50
					}, width = $(el).parent().width() - margin.left - margin.right; 
					if($(el).parent().width() > 768)
					   var height = 350	- margin.top - margin.bottom;
					else
						var height=250- margin.top - margin.bottom;
					
					// set the ranges
					var x = d3.scale.ordinal().rangeRoundBands(
							[ 0, width ], 1.0);
					var y = d3.scale.linear().range(
							[ height, 0 ]);
					
					// define the axis
					var xAxis = d3.svg.axis().scale(x).orient("bottom")
							.ticks(5);
					var yAxis = d3.svg.axis().scale(y).orient("left")
							.ticks(5);

					// // Define the line
					var lineFunction = d3.svg.line()
					.defined(function(d) {  return d && d.value!= null; })
					.x(function(d) {
						if(d.key == "UCL" || d.key == "LCL")
							lineFunction.interpolate("step-before");
						else
							lineFunction.interpolate("monotone");
						return x(d.date);
					}).y(function(d) {
						return y(d.value);
					});
					y.domain([ 0, 100 ]);
					// Adds the svg canvas
					var svg = d3.select(el).append("svg").attr("id",
							"trendsvg").attr("width",
							width + margin.left + margin.right).attr(
							"height",
							height + margin.top + margin.bottom + 200)
							.append("g").attr(
									"transform",
									"translate(" + margin.left + ","
											+ (margin.top + 50) + ")").style(
									"fill", "#000");

					// Get the data
//					lineData.forEach(function(d) {
//						d.date = d.date;
//						d.value = +d.value;
//					});

					x.domain(lineData.map(function(d) {
						return d.date;
					}));
					y.domain([ 0, d3.max(lineData, function(d) {
						return d.value;
					}) ]);

					// Nest the entries by symbol
					var dataNest = d3.nest().key(function(d) {
						return d.key;
					}).entries(lineData);

					// Loop through each symbol / key
					//var color = d3.scale.category10(); 
					var color = d3.scale.ordinal().range(
							 [ "#386d5c", "#f07258", "#333a3b", "#428ead"]);
					
					//============Text wrap function in x-axis of column chart=====================
					function wrap(text, width) {
						  text.each(function() {
						    var text = d3.select(this),
						        words = text.text().split(/\s+/).reverse(),
						        word,
						        cnt=0,
						        line = [],
						        lineNumber = 0,
						        lineHeight = 1.1, 
						        x = text.attr("x"),
						        y = text.attr("y"),
						        dy = parseFloat(text.attr("dy")),
						        tspan = isNaN(dy) ?  text.text(null).append("tspan").attr("x", x).attr("y", y) : text.text(null).append("tspan").attr("x", x).attr("y", y).attr("dy", dy + "em");
						    while (word = words.pop()) {
						    	cnt++;
						      line.push(word);
						      tspan.text(line.join(" "));
						      if (tspan.node().getComputedTextLength() > width) {
						        line.pop();
						        
						        tspan.text(line.join(" "));	
						        line = [word];
						        if(cnt!=1)
						        tspan = text.append("tspan").attr("x", x).attr("y", y).attr("dy", ++lineNumber * lineHeight + dy + "em").text(word);
						      }
						    }
						  });
						}
					
					// Add the X Axis
					svg.append("g").attr("class", "x axis").attr(
							"transform", "translate(0," + height + ")")
							.call(xAxis).append("text").attr("x",
									"405").attr("y",
									"45").attr("dx", ".71em")												
							.call(wrap, x.rangeBand());
					
					svg.append("g").attr("class", "x axis").attr(
							"transform", "translate(0," + height + ")")
							.call(xAxis).append("text").attr("x",
									width).attr("y",
									"65").attr("dx", ".71em")																			
							.style({"text-anchor": "end",
								"font-weight": "bold",
								"letter-spacing": "1px"
							}).text(
									"Time Period").style({"fill":
									"#000","text-align":"right"});
					d3.selectAll(".x.axis .tick text").style({"text-anchor":
							"end","font-size":"11px","font-weight":"normal"}).attr("dx", "-.6em").attr("dy",
							"0").attr("transform", function(d) {
						return "rotate(-50)";
					});
					
					/*if($(window).width()<900){
					d3.selectAll(".tick text").style({"text-anchor":
							"end","font-size":"11px","font-weight":"normal"}).attr("dx", "-.3em").attr("dy",
							"1em").attr("transform", function(d) {
						return "rotate(-45)";
					});
					}
					else{
						d3.selectAll(".tick text").style({"text-anchor":
							"end","font-size":"12px","font-weight":"normal","fill":"#333a3b"}).attr("dx", "0").attr("dy",
							"1em").attr("transform", function(d) {
						return "rotate(-45)";
					});
						
					}*/

					// xsvg;

					// Add the Y Axis
					
					
						svg.selectAll("text");
						svg.append("g").attr("class", "y axis").call(yAxis)
						.append("text").attr("transform",
								"rotate(-90)").attr("y", -50).attr("x", -height/2).attr(
								"dy", ".71em").style("text-anchor",
								"middle").style({"fill": "#000",
									"font-weight": "bold",
									"letter-spacing": "1px"
								}).text("Value");
					// adding multiple lines in Line chart

					for (var index = 0; index < dataNest.length; index++) {

						var series = svg.append(
								"g").attr("class", "series tag"+ dataNest[index].key.split(" ")[0]).attr("id",
								"tag" + dataNest[index].key.split(" ")[0]);

						var path = svg.selectAll(".series#tag"+dataNest[index].key.split(" ")[0])
								.append("path")
								.attr("class", "line tag"+dataNest[index].key.split(" ")[0])
								.attr("id", "tag" + dataNest[index].key.split(" ")[0])
								.attr(
										"d",
										function(d) {
											return lineFunction(dataNest[index].values);
										}).style("stroke", function(d) {
									return color(dataNest[index].key);
								}).style("stroke-width", function(d) {
									if(dataNest[index].key == "CL")
										return "4px";
									else
										return "2px";
									
							}).style(
										"fill", "none").style("cursor", function(d){
											if(dataNest[index].key == "P-Average")
												return "pointer";
											else
												return "default";
												}).on("mouseover",
														function(d) {
													if($(this).attr("id") == "tagP-Average")
														showPopover.call(this, dataNest[3].values[0]);
												}).on("mouseout", function(d) {
											removePopovers();
										});			;
//						 var totalLength = path.node().getTotalLength();
//
//					        path
//					          .attr("stroke-dasharray", totalLength + " " + totalLength)
//					          .attr("stroke-dashoffset", totalLength)
//					          .transition()
//					            .duration(3000)
//					            .ease("linear")
//					            .attr("stroke-dashoffset", 0);
					        
					        
						svg.selectAll(".series#tag"+dataNest[index].key.split(" ")[0]).select(".point").data(function() {
							return dataNest[index].values;
						}).enter().append("circle").attr("id",
								"tag" + dataNest[index].key.split(" ")[0]).attr(
								"class", function(d){
									if(d.key != "CL" || d.pdsas == "")
										return "point tag"+ dataNest[index].key.split(" ")[0]
									else
										return "point pdsaAvailable tag"+ dataNest[index].key.split(" ")[0]
									}).attr("cx",
								function(d) {
									return x(d.date);
								}).attr("cy", function(d) {
							return y(d.value);
						}).attr("r",  function(d) {
							if(d.value!=null && d.key == "CL")
								return "3px";
							else
								return "0px";}).style("fill", function(d) {
							if(d.key != "CL" || d.pdsas == "")
								return color(dataNest[index].key);
							else
								return "#FFC107";
						}).style("stroke", "none").style(
								"stroke-width", "2px").on("mouseover",
								function(d) {
									showPopover.call(this, d);
								}).on("mouseout", function(d) {
							removePopovers();
						});			
						
						// second render pass for the dashed lines
						var left, right
						for (var j = 0; j < dataNest[index].values.length; j += 1) {
							  var current = dataNest[index].values[j]
							  if (current.value!=null) {
							    left = current
							  } else {
							    // find the next value which is not null
							    while (dataNest[index].values[j]!=undefined && dataNest[index].values[j].value == null && j < dataNest[index].values.length) j += 1
							    right = dataNest[index].values[j]
							    
							    if(left!=undefined && right!=undefined && left.key == right.key){
							    	 svg.append("path")
									    .attr("id", "tag" + dataNest[index].key)
									    .attr("class", "tag" + dataNest[index].key)
									    .attr("d", lineFunction([left, right]))
							    			.style("stroke", color(dataNest[index].key))
							    			.attr('stroke-dasharray', '5, 5').style(
													"fill", "none");
							    }
							   
					    			

					        j -= 1
							  }
							}
						
					}
					/*
					 * switch button for series line chart
					*/
				/*	*/
					if(dataNest.length > 1){
						var switchBtn = svg.append("rect").attr("x", width -30)// author
						.attr("y", -64).attr("rx", 0).attr("ry",
								0).attr("width", 30).attr("height", 30)
								.style("fill", "none").attr("id", 'rext1').attr(
										"key", "UCL")
								.style({
							    	"cursor": "pointer",
							    })
								.on("click", function() { // ************author:kamal***
									// Determine if current line is
									// visible
									
									rectClickHandler('UCL, #tagLCL', $(this).closest("svg"), d3.select(this), false);
								});
						svg.append("svg:image")
					    .attr("xlink:href", 'resources/images/icons/svg_line_Selected.svg')
					    .attr("id",  'line_Selected_img1')
					    .attr("width", 32)
					    .attr("height", 30).attr(
								"key", "UCL")
					    .attr("x", width -30)
					    .style({
					    	"cursor": "pointer"
					    })
					    .attr("y", -64).on("click", function() { // ************author:kamal***
							// Determine if current line is
							// visible
							rectClickHandler('UCL, #tagLCL', $(this).closest("svg"), d3.select(this),false);
						});		
						var switchBtn2 = svg.append("rect").attr("x", width -80)// author
						.attr("y", -64).attr("rx", 0).attr("ry",
								0).attr("width", 30).attr("height", 30)
								.style("fill", "none").attr("id", 'rext1').attr(
										"key", "UCL")
								.style({
							    	"cursor": "pointer",
							    })
								.on("click", function() { // ************author:kamal***
									// Determine if current line is
									// visible
									rectClickHandler('P-Average', $(this).closest("svg"), d3.select(this), true);
								});
						svg.append("svg:image")
					    .attr("xlink:href", 'resources/images/icons/svg_median_Selected.svg')
					    .attr("id",  'median_Selected_img1')
					    .attr("width", 32)
					    .attr("height", 30).attr(
								"key", "UCL")
					    .attr("x", width -80)
					    .style({
					    	"cursor": "pointer"
					    })
					    .attr("y", -64).on("click", function() { // ************author:kamal***
							// Determine if current line is
							// visible
							rectClickHandler('P-Average', $(this).closest("svg"), d3.select(this), true);
						});	
					}	
					svg.append("text").attr("x", width / 2)// author
					.attr("y", height + 90).attr("dy", ".3em")
							.style({
//						    	"cursor": "pointer",
						    	"fill": "rgb(66, 142, 173)",
						    	"font-weight": "bold",
						    	"text-anchor": "middle",
						    		"font-size": "13px"
						    }).text(dataNest[0].values[0].name).call(wrap, width)
					// click handler for hiding series data
					function rectClickHandler(series, selectedSvg, button, median) {

						var disName;
						var fillColor;
						if (selectedSvg.find("#tag"+ series)
								.css("display") == "none") {
							disName = "block";
							fillColor = color(series);
							if(median)
								button.attr("xlink:href", 'resources/images/icons/svg_median_Selected.svg');
							else
								button.attr("xlink:href", 'resources/images/icons/svg_line_Selected.svg');
						} else {
							disName = "none";
							if(median)
								button.attr("xlink:href", 'resources/images/icons/svg_median_not_Selected.svg').attr("id",  'not_median_Selected_img1');
							else
								button.attr("xlink:href", 'resources/images/icons/svg_line_not_Selected.svg').attr("id",  'not_line_Selected_img1');
							fillColor = "#464646";
						}
						/*svg.selectAll("#" + $(this)[0].id + "").style(
								"fill", fillColor);*/
						selectedSvg.find(".tag"+ series)
						.css("display", disName);
					}
					function removePopovers() {
						$('.popover').each(function() {
							$(this).remove();
						});
					}
					function showPopover(d) {
						$(this).popover(
								{
									title : '',
									placement : 'auto top',
									container : 'body',
									trigger : 'manual',
									html : true,
									content : function() {
										return d.key == "P-Average" ? "P-Average: <span class='navy-text'>"+d.value : "</span>Time Period: <span class='navy-text'>" + d.date
												+ "</span><br/>"+ (d.key == "CL" ? "Fractional Index" : d.key)+": <span class='navy-text'>"
												+ d.value +"</span>" + (d.pdsas ? "<br>PDSA:<span class='navy-text'> " + d.pdsas + "</span>":"");
									}
								});
						$(this).popover('show');
					}
					/*//Draw a horizontal line for overall score of latest time period
					svg.append("g").attr("class", "y axis").call(yAxis).append("line")          // attach a line
				    .style("stroke", "#428ead") 		// colour the line
				    .attr("stroke-width", 1)
				    .attr("fill", "none")
				    .attr("x1", 0)     				// x position of the first end of the line
				    .attr("y1", y("30"))      // y position of the first end of the line
				    .attr("x2", width)     				// x position of the second end of the line
				    .attr("y2", y("30"))
				    .style("cursor", "pointer").on("mouseover", function(d) {
						showPopover.call(this, {axis: "", value: Math.round("30")}
									);})    // y position of the second end of the line
					.on("mouseout", function() {
					removePopovers();
					});
*/					d3.selection.prototype.moveToFront = function() {
					  return this.each(function(){
						    this.parentNode.appendChild(this);
						  });
					};
					d3.selectAll(".domain, .y.axis .tick line").style({"fill": "none", "stroke": "#000"});
					d3.selectAll("circle.point.pdsaAvailable").attr("r", "5px").moveToFront();
				};
			
				scope
						.$watch(
								'dataprovider',
								function(newValue, oldValue) {
									
										// console.log("Drawing line
										// series");
										// console.log(scope.selectedArea.properties.utdata.lineSeries);
									if(newValue ){
										/*var sameFlag = false;
										for(var i=0; i<newValue.length; i++){
											if(newValue[i].dataValue == oldValue[i].dataValue){
												sameFlag = true;
											}
										}
										if(!sameFlag)*/
										scope
												.renderDataSeries(newValue);
									}
									
								}, true);
				$(window).resize(function(){
					scope.renderDataSeries(scope.dataprovider);
				});
			}
			return {
				link : link,
				scope : {
					dataprovider : "="
				},
				restrict : "E"
			};
		});

/*
 * @author Laxman(laxman@sdrc.co.in)
 * 
 */

myAppConstructor
.directive(
		"trendLine",
		function($window) {
			function link(scope, el) {

				var el = el[0];
				var clicks = 0;

				// Render graph based on 'data'
				scope.$watch("dataprovider", function(data) {
					// remove all
//					console.log(data);
					function draw(data) {
						d3.select(el).selectAll("*").remove();
						var wnw =d3.select(el)[0][0].parentNode.clientWidth;
						if(wnw < 260){
							wnw = 260;
						}
						var wnh = wnw * 2;
					d3.select("#trendsvg");
						var margin = {
							top : 60,
							right : 45,
							bottom : 60,
							left :  30,
						},
//						var margin = {
//								top : wnh/10,
//								right : wnw/120,
//								bottom : wnh/10,
//								left : wnw/10
//							}, 
//							width = $(document).outerWidth() * (wnw/17) / 140;
							width = wnw - 45 , 
							height = wnw > 940 ? (wnh/2.8) : (wnh/2.8)- margin.top - margin.bottom;

						// set the ranges
						var x = d3.scale.ordinal().rangeRoundBands(
								[ 0, width ], 1.0);
						var y = d3.scale.linear().rangeRound([ height, 0 ]);

						// define the axis
						var xAxis = d3.svg.axis().scale(x).orient("bottom")
								.ticks(5);
						var yAxis = d3.svg.axis().scale(y).orient("left")
								.ticks(5);

						// // Define the line
						var lineFunction = d3.svg.line().x(function(d) {
							return x(d.axis);
						}).y(function(d) {
							return y(d.value);
						}).interpolate("monotone");

						// Adds the svg canvas
						
//						if(wnw > 940){
							var svg = d3.select(el).append("svg").attr("id",
							"trendsvg").attr("width",
									wnw).attr(
							"height",
							height + margin.top + margin.bottom)
							.append("g").attr(
									"transform",
									"translate(" + (margin.left + 10) + ","
											+ (margin.top - 20) + ")").style(
									"fill", "#F2F2F2");
						/*}
						else{
							var svg = d3.select(el).append("svg").attr("id",
							"trendsvg").attr("width",
									wnw).attr(
							"height",
							height + margin.top + margin.bottom)
							.append("g").attr(
									"transform",
									"translate(" + 2 * margin.left + ","
											+ (margin.top -50) + ")").style(
									"fill", "#F2F2F2");
						}*/

						// Get the data
						data.forEach(function(d) {
//							console.log(d);
							d.axis = d.axis;
							d.value = +d.value;
						});

						x.domain(data.map(function(d) {
							return d.axis;
						}));
						// Y domain set using loop
						var flag = false;
						data.forEach(function (d){
							if(d.value  > 100){
								y.domain([ 0, d3.max(data, function(d) {
									return d.value;
								}) ]);
								flag = true;
						}
					});
						
						if(!flag){
							y.domain([ 0, 5 ]);
							
						}
					

						// Nest the entries by symbol
						var dataNest = d3.nest().key(function(d) {
							return d.source;
						}).entries(data);

						// Loop through each symbol / key
//						var color = d3.scale.category10(); // 
						var color = d3.scale.ordinal().range(
						 [ "#1f77b4", "#d3d3d3", "#7b6888",
						 "#66CCFF", "#9c8305" ,"#101b4d" , "#17becf"]);
						// Add the X Axis
						//============Text wrap function in x-axis of column chart=====================
						//************ end ************
						
						 svg.append("g").attr("class", "x axis").attr(
								"transform", "translate(0," + height + ")")
								.call(xAxis).append("text").attr("x",
										width - margin.right).attr("y",
										margin.bottom).attr("dx", ".71em")
								.style("text-anchor", "middle").text(
										"Time Period").attr("x", width / 2)// 
										.attr("y", 80).style({"fill":"#f2f2f2", "font-weight": "bold"});

						svg.selectAll(".tick text").style("text-anchor",
								"end").attr("dx", "-.8em").attr("dy",
								".15em").attr("transform", function(d) {
							return "rotate(-25)";
						});

						

						// Add the Y Axis
						var ya = svg.append("g").attr("class", "y axis").call(yAxis);
						ya.selectAll("text");
						ya.append("text").attr("transform",
								"rotate(-90)").attr("y", -40).attr("x", -height/2).attr(
								"dy", ".71em").style("text-anchor",
								"end").style({"font-weight": "bold"}).text(function(d){return "Score";}).style("fill",
								"#f2f2f2");
				
						// adding multiple line chart

						for (var index = 0; index < dataNest.length; index++) {

							var series = svg.selectAll(".series").data(
									dataNest[index].values).enter().append(
									"g").attr("class", "series").attr("id",
									"tag" + dataNest[index].key);

							series.select(".line").data(function() {
								return dataNest[index].values;
							}).enter().append("path")
									.attr("id", "tag" + dataNest[index].key)
									.attr("d",function(d) {
												return lineFunction(dataNest[index].values);
											}).style("stroke", function(d) {
										return color(dataNest[index].key);
									}).style("stroke-width", "2px").style(
											"fill", "none").style("incorporate", "ordinal");

							series.select(".point").data(function() {
								return dataNest[index].values;
							}).enter().append("circle").attr("id",
									"tag" + dataNest[index].key).attr(
									"class", "point").attr("cx",
									function(d) {
										return x(d.axis);
									}).attr("cy", function(d) {
								return y(d.value);
							}).attr("r", "3px").style("fill", function(d) {
								return color(dataNest[index].key);
							}).style("stroke", "rgb(111, 152, 181)").style(
									"stroke-width", "2px").on("mouseover",
									function(d) {
										showPopover.call(this, d);
									}).on("mouseout", function(d) {
								removePopovers();
							});

							/*svg.append("text").attr("x", 15)// 
							.attr("y", (index * 30) -20).style("font-size","15px")// ("y", height
							// +
							// (margin.right
							// / 2) + 5)

							.attr("class", "legend").style("fill",
									function() {
										return color(dataNest[index].key);
									}).text("cases registered");*/

							svg.append("rect").attr("x", 0)// author
							.attr("y", (index * 30)-30).attr("rx", 2).attr("ry",
									2).attr("width", (wnw/60)).attr("height", (wnw/60))
									.style("fill", function(d) {
										return color(dataNest[index].key);
									}).attr("id", 'rext' + index).attr(
											"key", dataNest[index].key)
									.style("stroke", "grey")
									.on("click", function() { // ************author:kamal***
										// Determine if current line is
										// visible
										rectClickHandler.call(this);
									});

						}


						// End adding multiple line chart

						// click handler for hiding series data
						function rectClickHandler() {

							var disName;
							var fillColor;
							if (d3
									.select(
											"#tag"
													+ dataNest[parseInt($(this)[0].id
															.substr($(this)[0].id.length - 1))].key)
									.style("display") == "none") {
								disName = "block";
								fillColor = color(dataNest[parseInt($(this)[0].id
										.substr($(this)[0].id.length - 1))].key);
							} else {
								disName = "none";
								fillColor = "#fff";
							}
							svg.selectAll("#" + $(this)[0].id + "").style(
									"fill", fillColor);
							svg
									.selectAll(
											"#tag"
													+ dataNest[parseInt($(this)[0].id
															.substr($(this)[0].id.length - 1))].key)
									.style("display", disName);
						}

						function removePopovers() {
							$('.popover').each(function() {
								$(this).remove();
							});
						}
						function showPopover(d) {
							$(this).popover(
									{
										title : '',
										placement : 'auto top',
										container : 'body',
										trigger : 'manual',
										html : true,
										content : function() {

											return "<span style='color:#333a3b'>" + "Time Period : "
													+ "</span>" + "<span style='color:#6057bb'>"
													+ d.axis + "</span>" + "<br/>"
													+ "<span style='color:#333a3b'>"
													+ "Score : " + "</span>"
													+ "<span style='color:#6057bb'>" + d.value
													+ "</span>";

										}
									});
							$(this).popover('show');
//							$('.popover.fade.top.in').css('top', parseFloat($('.popover.fade.top.in').css('top').slice(0, -2))+$(window).scrollTop());
						}
					};
					draw(data);
					$(window).resize(function(){
						if(data)
							draw(data);
					});
					
		});
	}
	return {
		restrict : "E",
		scope : {
			dataprovider : "="
		},
		link : link
	};

});
app.directive('windowSize', function ($window) {
	  return function (scope, element) {
	    var w = angular.element($window);
	    scope.getWindowDimensions = function () {
	        return {
	            'h': w.height(),
	            'w': w.width()
	        };
	    };
	    scope.$watch(scope.getWindowDimensions, function (newValue, oldValue) {
	      scope.windowHeight = newValue.h;
	      scope.windowWidth = newValue.w;
	      if(scope.windowWidth > 1200)
	    	  scope.oneRowGrid = 4;
	      else if(scope.windowWidth > 991)
	    	  scope.oneRowGrid = 3;
	      else if(scope.windowWidth > 665)
	    	  scope.oneRowGrid = 2;
	      else
	    	  scope.oneRowGrid = 1;
	      $("head").append("<style>.indicatorGrid:last-child {border-right: 0;}</style>")
	      
//	      $(".indicatorGrid:nth-child("+ (scope.oneRowGrid + 1) +")").css({"border-right": 0})
	      scope.style = function () {
	          return {
	              'height': (newValue.h - 100) + 'px',
	              'width': (newValue.w - 100) + 'px'
	          };
	      };
	    }, true);

	    w.bind('resize', function () {
	        scope.$apply();
	    });
	  };
	});

$(window).scroll(function(){
	if($(window).width() > 991 && $(window).scrollTop() >= 120){
		$(".fixedChart").css({
			'position': 'fixed',
		    'top': '45%',
		    'transform': 'translate(0,-30%)'
		});
	}
	else if($(window).width() > 991){
		$(".fixedChart").css({
			'position': 'absolute',
		    'top': '0',
		    'transform': 'translate(0)'
		});
	}
	else{
		$(".fixedChart").css({
			'position': 'relative'
		});
	}
});