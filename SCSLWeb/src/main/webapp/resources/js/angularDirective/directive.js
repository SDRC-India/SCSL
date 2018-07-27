/**************samiksha line chart*******************/

myAppConstructor
.directive(
		"samikshaLine",
		function($window) {
			function link(scope, el) {

				var el = el[0];
				var clicks = 0;

				// Render graph based on 'data'
				scope.$watch("dataprovider", function(data) {
					// remove all
//					console.log(data);
					function draw(data) {
						var w = $(window);
						var wnw =d3.select(el)[0][0].parentNode.clientWidth;
						if(wnw == 0){
							wnw = 566;
						}
						var wnh = (w.width()> 940)? 950: 668;
					d3.select("#trendsvg");
//						var margin = {
//							top : 60,
//							right : 45,
//							bottom : 60,
//							left :  30,
//						},
						var margin = {
								top : wnh/10,
								right : wnw/120,
								bottom : wnh/10,
								left : wnw/10
							}, 
//							width = $(document).outerWidth() * (wnw/17) / 140;
							width = wnw - (3 * (margin.left + margin.right)), 
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
						
						if(wnw > 940){
							var svg = d3.select(el).append("svg").attr("id",
							"trendsvg").attr("width",
									wnw).attr(
							"height",
							height + margin.top + margin.bottom)
							.append("g").attr(
									"transform",
									"translate(" + margin.left + ","
											+ margin.top + ")").style(
									"fill", "#000");
						}
						else{
							var svg = d3.select(el).append("svg").attr("id",
							"trendsvg").attr("width",
									wnw).attr(
							"height",
							height + margin.top + margin.bottom)
							.append("g").attr(
									"transform",
									"translate(" + 2 * margin.left + ","
											+ margin.top + ")").style(
									"fill", "#000");
						}

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
						 [ "#f0bf7f", "#d3d3d3", "#7b6888",
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
										.attr("y", 60).style({"fill":"rgb(84, 84, 84)", "font-weight": "bold"});

						svg.selectAll(".tick text").style("text-anchor",
								"end").attr("dx", "-.8em").attr("dy",
								".15em").attr("transform", function(d) {
							return "rotate(-25)";
						});

						

						// Add the Y Axis
						var ya = svg.append("g").attr("class", "y axis").call(yAxis);
						ya.selectAll("text");
						ya.append("text").attr("transform",
								"rotate(-90)").attr("y", -60).attr("x", -height/2).attr(
								"dy", ".71em").style("text-anchor",
								"end").style({"font-weight": "bold"}).text(function(d){return "Score";}).style("fill",
								"rgb(84, 84, 84)");
				
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
							}).style("stroke", "#f0a997").style(
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

/**************samiksha line chart for PDSA*******************/

myAppConstructor
.directive(
		"scslLinePdsa",
		function($window) {
			function link(scope, el) {

				var el = el[0];
				var clicks = 0;

				// Render graph based on 'data'
				scope.$watch("dataprovider", function(data) {
					// remove all
//					console.log(data);
					function draw(data) {
						var w = $(window);
						var wnw =d3.select(el)[0][0].parentNode.clientWidth;
						if(wnw == 0){
							if(w.width()> 464)
								wnw = 566;
							else{
								wnw = 350;
								
							}
						}
						var wnh = (w.width()> 940)? 950: 668;
					d3.select("#trendsvg");
//						var margin = {
//							top : 60,
//							right : 45,
//							bottom : 60,
//							left :  30,
//						},
						var margin = {
								top : wnh/10,
								right : wnw/120,
								bottom : wnh/10,
								left : wnw/10
							}, 
//							width = $(document).outerWidth() * (wnw/17) / 140;
							width = wnw - (3 * (margin.left + margin.right)), 
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
						var lineFunction = d3.svg.line()
						.defined(function(d) { return !isNaN(d.value); })
						.x(function(d) {
							return x(d.axis);
						}).y(function(d) {
							return y(d.value);
						}).interpolate("monotone");

						// Adds the svg canvas
						
						if(wnw > 940){
							var svg = d3.select(el).append("svg").attr("id",
							"trendsvg").attr("width",
									wnw).attr(
							"height",
							height + margin.top + margin.bottom)
							.append("g").attr(
									"transform",
									"translate(" + margin.left + ","
											+ margin.top + ")").style(
									"fill", "#000");
						}
						else{
							var svg = d3.select(el).append("svg").attr("id",
							"trendsvg").attr("width",
									wnw).attr(
							"height",
							height + margin.top + margin.bottom + 20)
							.append("g").attr(
									"transform",
									"translate(" + 2 * margin.left + ","
											+ margin.top + ")").style(
									"fill", "#000");
						}

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
							y.domain([ 0, 100 ]);
							
						}
					

						// Nest the entries by symbol
						var dataNest = d3.nest().key(function(d) {
							return d.source;
						}).entries(data);

						// Loop through each symbol / key
//						var color = d3.scale.category10(); // 
						var color = d3.scale.ordinal().range(
						 [ "#f0bf7f", "#d3d3d3", "#7b6888",
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
										"Date").attr("x", width / 2)// 
										.attr("y", 70).style({"fill":"rgb(84, 84, 84)", "font-weight": "bold"});

						svg.selectAll(".tick text").style("text-anchor",
								"end").attr("dx", "-.8em").attr("dy",
								".15em").attr("transform", function(d) {
							return "rotate(-25)";
						});

						

						// Add the Y Axis
						var ya = svg.append("g").attr("class", "y axis").call(yAxis);
						ya.selectAll("text");
						ya.append("text").attr("transform",
								"rotate(-90)").attr("y", -60).attr("x", -height/2).attr(
								"dy", ".71em").style("text-anchor",
								"end").style({"font-weight": "bold"}).text(function(d){return "Percentage";}).style("fill",
								"rgb(84, 84, 84)");
				
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

							//sarita code
							svg.selectAll(".series").select(".point").data(function() {
								return dataNest[index].values;
							}).enter().append("circle").attr("id",
									"tag" + dataNest[index].key.split(" ")[0]).attr(
									"class","point")
										.attr("cx",
									function(d) {
										return x(d.axis);
									})
										.attr("cy", function(d) {
											return y(d.value);
										}).attr("r",  function(d) {
											if(!isNaN(d.value))
												return "3px";
											else
												return "0px";
										}).style("fill", function(d) {
											if(!isNaN(d.value))
												return color(dataNest[index].key);
										}).style({
									    	"cursor": "pointer"
									    }).style("stroke", "#f0a997").style(
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

							// second render pass for the dashed lines
							var left, right
							for (var j = 0; j < dataNest[index].values.length; j += 1) {
							  var current = dataNest[index].values[j]
							  if (!isNaN(current.value)) {
							    left = current
							  } else {
							    // find the next value which is not nan
							    while (isNaN(dataNest[index].values[j].value) && j < dataNest[index].values.length) j += 1
							    right = dataNest[index].values[j]
							    
							    svg.append("path")
							    .attr("id", "tag" + dataNest[index].key)
							    .attr("d", lineFunction([left, right]))
					    			.style("stroke", color(dataNest[index].key))
					    			.attr('stroke-dasharray', '5, 5').style("incorporate", "ordinal");
					    			

					        j -= 1
							  }
							}
						}
						//sarita code end

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

											return "<span style='color:#333a3b'>" + "Date : "
													+ "</span>" + "<span style='color:#6057bb'>"
													+ d.axis + "</span>" + "<br/>"
													+ "<span style='color:#333a3b'>"
													+ "Percentage : " + "</span>"
													+ "<span style='color:#6057bb'>" + d.value
													+ "</span>";

										}
									});
							$(this).popover('show');
//							$('.popover.fade.top.in').css('top', parseFloat($('.popover.fade.top.in').css('top').slice(0, -2))+$(window).scrollTop());
						}
					};
					draw(data);
					
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

myAppConstructor
.directive(
		"sdrcTableHeaderFix",
		function($window, $compile) {
			function link(scope, el) {
				var tableUniqClass = "";
				
				scope.$watch("tableuniqueclass", function(uniqClass) {
					tableUniqClass = uniqClass;
					function createStaticHeader(uniqClass){
						$("."+uniqClass).before('<div class="static-header-container"><div class="static-header"></div></div>')
					}
					if(uniqClass)
					createStaticHeader(uniqClass);	
				});
				
				scope.$watch("tabledata", function(data) {
					function fixTableHeader(uniqClass) {
						setTimeout(function(){
							
							if($("."+uniqClass)[0].offsetWidth >= $("."+uniqClass)[0].clientWidth){
								var i=0;rowWidthList=[];
								$("."+uniqClass).siblings(".static-header-container").find(".static-header").html($("."+uniqClass).html());
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
								$compile(angular.element(".static-header-container"))(angular.element("body").scope());
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

myAppConstructor.
directive('onlySevenDigits', function () {

  return {
      restrict: 'A',
      require: '?ngModel',
      link: function(scope, element, attrs, ngModelCtrl) {
			if(!ngModelCtrl) {
				return; 
			}
			
			ngModelCtrl.$parsers.push(function(val) {
				if (angular.isUndefined(val)) {
					var val = '';
				}
				
				var clean = val.replace(/[^0-9]/g, '');
				if(!angular.isUndefined(clean)) {
	            	 var num=0;
	            	 if(clean.length>6 ){
	            		 num =clean.slice(0,7);
	            		 clean= num;
	            	 }
	            		 
	             }
				
				if (val !== clean) {
					ngModelCtrl.$setViewValue(clean);
					ngModelCtrl.$render();
				}
				return clean;
			});
			
			element.bind('keypress', function(event) {
				if(typeof InstallTrigger !== 'undefined'){
					if(event.charCode === 101 || event.charCode === 46 || event.charCode === 32) {
						event.preventDefault();
					}
				}
				else{
					if(event.keyCode === 101 || event.keyCode === 46 || event.keyCode === 32) {
						event.preventDefault();
					}
				}
				
			});
		}
  };
});
myAppConstructor.
directive('maxThreeHundred', function () {

  return {
      restrict: 'A',
      require: '?ngModel',
      link: function(scope, element, attrs, ngModelCtrl) {
			if(!ngModelCtrl) {
				return; 
			}
			
			ngModelCtrl.$parsers.push(function(val) {
				if (angular.isUndefined(val)) {
					var val = '';
				}
				var clean = val;
				if(!angular.isUndefined(clean)) {
	            	 var num=0;
	            	 if(clean.length>300 ){
	            		 num =clean.slice(0,300);
	            		 clean= num;
	            	 }
	            		 
	             }
				if (val !== clean) {
					ngModelCtrl.$setViewValue(clean);
					ngModelCtrl.$render();
				}
				return clean;
			});
			
		}
  };
});
myAppConstructor.
directive('onlyDigits', function () {

  return {
      restrict: 'A',
      require: '?ngModel',
      link: function(scope, element, attrs, ngModelCtrl) {
			if(!ngModelCtrl) {
				return; 
			}
			
			ngModelCtrl.$parsers.push(function(val) {
				if (angular.isUndefined(val)) {
					var val = '';
				}
				
				var clean = val.replace(/[^0-9]/g, '');
				/*if(!angular.isUndefined(clean)) {
	            	 var num=0;
	            	 if(clean.length>6 ){
	            		 num =clean.slice(0,7);
	            		 clean= num;
	            	 }
	            		 
	             }*/
				
				if (val !== clean) {
					ngModelCtrl.$setViewValue(clean);
					ngModelCtrl.$render();
				}
				return clean;
			});
			
			element.bind('keypress', function(event) {
				if(typeof InstallTrigger !== 'undefined'){
					if(event.charCode === 101 || event.charCode === 46 || event.charCode === 32) {
						event.preventDefault();
					}
				}
				else{
					if(event.keyCode === 101 || event.keyCode === 46 || event.keyCode === 32) {
						event.preventDefault();
					}
				}
				
			});
		}
  };
});
myAppConstructor.directive("hideHeaderFooter", ["$interval", function($interval) {
    return {
        restrict: "A",
        link: function(scope, elem, attrs) {
            //On click
            if($(window).height() <= 800 && $(window).width() <= 1024){
            	$(elem).focus(function(){
    				$("section.bottomfooter").css("display", "none");
    				$("nav.navbar.nav-menu-container").css("display", "none");
    			});
    			$(elem).blur(function(){
    				$("section.bottomfooter").css("display", "block");
    				$("nav.navbar.nav-menu-container").css("display", "block");
    			});
            }
            $(".inputBox").click(function(){$(this).find("input").focus()});

            
        }
    }
}]);

myAppConstructor.directive('multipleEmails', function () {
    return {
      require: 'ngModel',
      link: function(scope, element, attrs, ctrl) {
        ctrl.$parsers.unshift(function(viewValue) {

          var emails = viewValue.split(',');
          // define single email validator here
          var re = /\S+@\S+\.\S+/; 
            
          // angular.foreach(emails, function() {
            var validityArr = emails.map(function(str){
                return re.test(str.trim());
            }); // sample return is [true, true, true, false, false, false]
            console.log(emails, validityArr); 
            var atLeastOneInvalid = false;
            angular.forEach(validityArr, function(value) {
              if(value === false)
                atLeastOneInvalid = true; 
            }); 
            if(!atLeastOneInvalid) { 
              // ^ all I need is to call the angular email checker here, I think.
              ctrl.$setValidity('multipleEmails', true);
              return viewValue;
            } else {
              ctrl.$setValidity('multipleEmails', false);
              return undefined;
            }
          // })
        });
      }
    };
  });




$(document).ready(function(){
	$(window).scroll(function(){
		if($(window).scrollTop() >= 76){
			if($(window).width()>767){
				$("#slideMenu, #advance-filter-section").css({
					"position": "fixed",
					"top": "62px"
				})
				/*$(".nav-section").css({
					"position": "fixed",
					"top": "0px",
					"width": "100%"
				});*/
				$(".header-section").css("margin-bottom", "50px");
			}
			else{
				$("#slideMenu, #advance-filter-section").css({
					"position": "fixed",
					"top": "0px"
				})
			}
		}
		else{
//			if($(window).width()>767){
				/*$("#slideMenu").css({
					"position": "absolute",
					"top": "62px"
				})*/
				$("#advance-filter-section").css({
				"position": "fixed",
				"top": "124px"
			})
				/*$(".nav-section").css({
					"position": "relative",
					"top": "auto"
				})*/
				$(".header-section").css("margin-bottom", "0px");
//			}
//			else{
//				
//			}
		}
	})
	$(".loginPopBtn button").click(function(e){
		$(".loginPopForm").animate({
			right: 0
		}, 500);
		e.stopPropagation();
	});
	$('body').click(function(evt){    
	       if(evt.target.id == "loginPopForm")
	          return;
	       //For descendants of menu_content being clicked, remove this check if you do not want to put constraint on descendants.
	       else if($(evt.target).closest('#loginPopForm').length)
	          return;             

	      //Do processing of click event here for every element except with id menu_content
	       else{
	    	   $(".loginPopForm").animate({
		   			right: "-251px"
		   		}, 500);
	       }
	});
	$(".slideMenu").css("height", $(window).height());
	$("#advance-filter-section").css("height", $(window).height());
	$(".menuSlideBtn button").click(function(e){
		$(".slideMenu").animate({
			left: 0
		}, 500);
		e.stopPropagation();
	});
	$(".filter-btn").click(function(e){
		$("#advance-filter-section").animate({
			right: 0
		}, 500);
		e.stopPropagation();
	});
	$(".slide-menu-icon").click(function(){
		$(".slideMenu").animate({
			left: "-250px"
		}, 500);
	});
	$('html, .menuSlideBtn, .menuSlideBtn button').click(function(evt){    
	       if(evt.target.id == "advance-filter-section")
	          return;
	       //For descendants of menu_content being clicked, remove this check if you do not want to put constraint on descendants.
	       else if($(evt.target).closest('#advance-filter-section').length)
	          return;             

	      //Do processing of click event here for every element except with id menu_content
	       else{
	    	   $("#advance-filter-section").animate({
		   			right: "-280px"
		   		}, 500);
	       }
	});
	$('html, .filter-btn').click(function(evt){    
	       if(evt.target.id == "slideMenu")
	          return;
	       //For descendants of menu_content being clicked, remove this check if you do not want to put constraint on descendants.
	       else if($(evt.target).closest('#slideMenu').length)
	          return;             

	      //Do processing of click event here for every element except with id menu_content
	       else{
	    	   $("#slideMenu").animate({
		   			left: "-250px"
		   		}, 500);
	       }
	});
});
function minmax(value, min, max) 
{
	if(isNaN(value)){
		return null;
	}
	else if(parseInt(value) < min) 
        return null; 
    else if(parseInt(value) > max) 
        return max; 
    else return value;
}
function maxHundred(value, min, max) 
{
    if(parseInt(value) < min || isNaN(value)) 
        return null; 
    else if(parseInt(value) > max) 
        return max; 
    else return value;
}
$(document).ready(function(){
	$("div#mymain").css("min-height",$(window).height());
//	$("body").css("min-height", $(window).height()-120);
	$("ul.submenu").each(function(){
		if($(this).find("a.active").length){
			$(this).css("display", "block");
			$(this).prev().find("i.fa-chevron-down").css("transform", "rotate(-180deg)");
		}
	});
	$("ul.pageLinks.mainmenu > li a[href='#']").click(function(){
		if($(this).next(".submenu").css("display") == 'none'){                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
			$(this).next(".submenu").slideDown("slow");
			$(this).addClass("opened");
			$(this).find("i.fa-chevron-down").css("transform", "rotate(-180deg)");
		}
		else{
			$(this).next(".submenu").slideUp("slow");
			$(this).removeClass("opened");
			$(this).find("i.fa-chevron-down").css("transform", "rotate(0deg)");
		}
	});
	
	
});

