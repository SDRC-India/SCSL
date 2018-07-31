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
					.defined(function(d) { return d && d.value!= null; })
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
							height + margin.top + margin.bottom + 50 + 70)
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
							 [ "#386d5c", "#f07258", "#333a3b"]);
					
					//============Text wrap function in x-axis of column chart=====================
					function wrap(text, width) {/*
						  text.each(function() {
						    var text = d3.select(this),
						        words = text.text().split(/\s+/).reverse(),
						        word,
						        cnt=0,
						        line = [],
						        lineNumber = 0,
						        lineHeight = 1.1, 
						        y = text.attr("y"),
						        dy = parseFloat(text.attr("dy")),
						        tspan = text.text(null).append("tspan").attr("x", 0).attr("y", y).attr("dy", dy + "em");
						    while (word = words.pop()) {
						    	cnt++;
						      line.push(word);
						      tspan.text(line.join(" "));
						      if (tspan.node().getComputedTextLength() > width) {
						        line.pop();
						        
						        tspan.text(line.join(" "));	
						        line = [word];
						        if(cnt!=1)
						        tspan = text.append("tspan").attr("x", 0).attr("y", y).attr("dy", ++lineNumber * lineHeight + dy + "em").text(word);
						      }
						    }
						  });
						*/}
					
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
								}).style("stroke-width", "2px").style(
										"fill", "none");
						 var totalLength = path.node().getTotalLength();

					        path
					          .attr("stroke-dasharray", totalLength + " " + totalLength)
					          .attr("stroke-dashoffset", totalLength)
					          .transition()
					            .duration(3000)
					            .ease("linear")
					            .attr("stroke-dashoffset", 0);
					        
					        
						svg.selectAll(".series#tag"+dataNest[index].key.split(" ")[0]).select(".point").data(function() {
							return dataNest[index].values;
						}).enter().append("circle").attr("id",
								"tag" + dataNest[index].key.split(" ")[0]).attr(
								"class", "point tag"+ dataNest[index].key.split(" ")[0]).attr("cx",
								function(d) {
									return x(d.date);
								}).attr("cy", function(d) {
							return y(d.value);
						}).attr("r",  function(d) {
							if(d.value!=null && d.key == "CL")
								return "3px";
							else
								return "0px";}).style("fill", function(d) {
							return color(dataNest[index].key);
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
						    // find the next value which is not nan
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
//									rectClickHandler('P-Average', $(this).closest("svg"), d3.select(this), true);
									removeMedian($(this).closest("svg"), d3.select(this), true);
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
//							rectClickHandler('P-Average', $(this).closest("svg"), d3.select(this), true);
					    	removeMedian($(this).closest("svg"), d3.select(this));
						});	
					}	
					svg.append("text").attr("x", width / 2)// author
					.attr("y", height + 90)
							.style({
//						    	"cursor": "pointer",
						    	"fill": "rgb(66, 142, 173)",
						    	"font-weight": "bold",
						    	"text-anchor": "middle",
						    		"font-size": "18px"
						    }).text(dataNest[0].values[0].name);
					
					function removeMedian(selectedSvg, button){
						var disName;
						var fillColor;
						if (selectedSvg.find("#medianScore")
								.css("display") == "none") {
							disName = "block";
							button.attr("xlink:href", 'resources/images/icons/svg_median_Selected.svg');
						} else {
							disName = "none";
							button.attr("xlink:href", 'resources/images/icons/svg_median_not_Selected.svg');
						}
						/*svg.selectAll("#" + $(this)[0].id + "").style(
								"fill", fillColor);*/
						selectedSvg.find("#medianScore")
						.css("display", disName);
					}
					
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
								button.attr("xlink:href", 'resources/images/icons/svg_median_not_Selected.svg').attr("id", 'not_median_Selected_img1');
							else
								button.attr("xlink:href", 'resources/images/icons/svg_line_not_Selected.svg').attr("id", 'not_line_Selected_img1');
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
									content : function() { //d.key is undefined in case of median
										return d.key == undefined ? "P-Average: "+d.value : "Time Period: " + d.date
												+ "<br/>"+ (d.key == "CL" ? "Fractional Index" : d.key )+": "
												+ d.value;
									}
								});
						$(this).popover('show');
					}
					//Draw a horizontal line for overall score of latest time period
					svg.append("g").attr("class", "y axis").call(yAxis).append("line")          // attach a line
				    .style("stroke", "#428ead") 		// colour the line
				    .attr("stroke-width", 1)
				    .attr("fill", "none").attr("id", "medianScore")
				    .attr("x1", function() {
						return x(dataNest[0].date);
					})     				// x position of the first end of the line
				    .attr("y1", y(angular.element('body').scope().pdsaDetails.median))      // y position of the first end of the line
				    .attr("x2", width)     				// x position of the second end of the line
				    .attr("y2", y(angular.element('body').scope().pdsaDetails.median))
				    .style("cursor", "pointer").on("mouseover", function(d) {
						showPopover.call(this, {axis: "", value: angular.element('body').scope().pdsaDetails.median}
									);})    // y position of the second end of the line
					.on("mouseout", function() {
					removePopovers();
					});
					d3.selectAll(".domain, .y.axis .tick line").style({"fill": "none", "stroke": "#000"});

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
myAppConstructor
.directive(
		"sdrcPdsaTableHeaderFix",
		function($window, $compile) {
			function link(scope, el) {
				var tableUniqClass = "";
				
				scope.$watch("tableuniqueclass", function(uniqClass) {
					tableUniqClass = uniqClass;
					function createStaticHeader(uniqClass){
						$("."+uniqClass).before('<div class="static-header-container"><div class="static-header ' + uniqClass +'-static-head"></div></div>')
					}
					if(uniqClass)
					createStaticHeader(uniqClass);	
				});
				
				scope.$watch("tabledata", function(data) {
					function fixTableHeader(uniqClass) {
						setTimeout(function(){
							
							if($("."+uniqClass)[0].offsetWidth >= $("."+uniqClass)[0].clientWidth){
								var i=0;rowWidthList=[];
								angular.element("."+uniqClass+"-static-head").html($("."+uniqClass).html());
								$("."+uniqClass).siblings(".static-header-container").find(".static-header").find("table thead").css("visibility", "visible");
								$("."+uniqClass).scrollLeft(0);
								$("."+uniqClass).siblings(".static-header-container").height($("."+uniqClass).siblings(".static-header-container").find(".static-header table thead").height());
								$("."+uniqClass).css("margin-top", -$("."+uniqClass).siblings(".static-header-container").find(".static-header table thead").height()+"px");
//								$("."+uniqClass).parent()
								$("."+uniqClass).siblings(".static-header-container").css({
									 'overflow-x': 'hidden',
									 'overflow-y': 'hidden',
									 'background-color': '#F1F1F1',
									 'position': 'relative',
									 'clear': 'both'	 
								});
								$("."+uniqClass).siblings(".static-header-container").find(".static-header").css({
									 'overflow-x': 'auto',
									 'overflow-y': 'hidden',
									 'background-color': '#FFF'
								})
								$("."+uniqClass).find("table").css({"margin-bottom": "0px"});
								$("."+uniqClass).find("table thead").css("visibility", "hidden");
								if($(window).width() > 845){
									if($("."+uniqClass)[0].offsetWidth == $("."+uniqClass)[0].clientWidth){
										if($("."+uniqClass).find("table").height() > $("."+uniqClass).height())
											$("."+uniqClass).siblings(".static-header-container").css({'margin-right': "17px"});
										else
											$("."+uniqClass).siblings(".static-header-container").css({'margin-right': "0px"});

									}
									else{
										if($("."+uniqClass).find("table").height() + 17 > $("."+uniqClass).height())
											$("."+uniqClass).siblings(".static-header-container").css({'margin-right': "17px"});
										else
											$("."+uniqClass).siblings(".static-header-container").css({'margin-right': "0px"});

									}
																	}
								
								var ignoreTableBodyScroll = false;
								var ignoreTableHeadScroll = false;
								$(".header-fixed-table").scroll(function(){
										if(!ignoreTableBodyScroll){
											ignoreTableHeadScroll = true;
										$(this).prev().find(".static-header").scrollLeft($(this).scrollLeft());
										
										}
									
										ignoreTableBodyScroll = false;
										
								}); 
								$(".static-header").scroll(function(){
									if(!ignoreTableHeadScroll){
										ignoreTableBodyScroll = true;
									$(this).parent().siblings(".table-header-fixed, .header-fixed-table ").scrollLeft($(this).scrollLeft());
									
									}
									ignoreTableHeadScroll = false;
								})

							}
						}, 200);
					};
					if(tableUniqClass && data)
						fixTableHeader(tableUniqClass);
				});
				
			}
			return {
				restrict : "A",
				scope : {
					tableuniqueclass : "=",
					tabledata : "="
				},
				link : link
			};
});
