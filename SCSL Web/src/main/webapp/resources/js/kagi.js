   // Set the dimensions of the canvas
    width = width - margin.left - margin.right,
    height = height - margin.top - margin.bottom;

    // Set the ranges
    var	x = d3.scale.linear().range([0, width]);
    var	y = d3.scale.linear().range([height, 0]);

    // Define the axes
    var	xAxis = d3.svg.axis().scale(x)
        .orient("bottom").ticks(0);

    var	yAxis = d3.svg.axis().scale(y)
        .orient("left").ticks(10);

    // Define the line function.
    var	valueline = d3.svg.line()
        .x(function(d) { return x(d.x); })
        .y(function(d) { return y(d.close); });

    // Adds the svg canvas
    var	svg = d3.select("#kagiChart")
        .append("svg")
        .style("background",chartTheme=='light' ? "#ffffff" : "#000000")
        .attr("fill",chartTheme=='light' ? "#000000" : "#ffffff")
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom)
        .append("g")
        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

   // Get the maxima and minima of Y axis
    var yMax = d3.max(data, function(row) { return d3.max(row.p, function(d) { return d.close; }); });
    var yMin = d3.min(data, function(row) { return d3.min(row.p, function(d) { return d.close; }); });

    // Get the maxima and minima of X axis
    var xMax = d3.max(data, function(row) { return d3.max(row.p, function(d) { return d.x; }); });
    var xMin = d3.min(data, function(row) { return d3.min(row.p, function(d) { return d.x; }); });

    // Setting the Domain of the chart with a padding of 2 so the lines are not drawn at the edges.
    x.domain([xMin-2,xMax+2]);
    y.domain([yMin-2,yMax+2]);

    // Add the valueline path.
    var path  = svg.selectAll('path')
        .data(data)
        .enter()
        .append("path")
        .attr("class", "line")
        .attr("d", function(d){return valueline(d.p);}) // line group
        .style('stroke-width', function(d) { return d.w; }) //stroke-widths
        .style('stroke', function(d) { return d.c; }) // stroke-colors

    if(showAnimation){

      for(var i=0; i<path[0].length; i++){
          var totalLength = path[0][i].getTotalLength();
          d3.select(path[0][i])
          .attr("stroke-dasharray", totalLength + " " + totalLength )
          .attr("stroke-dashoffset", totalLength)
          .transition()
            .delay(animationDurationPerTrend*i)
            .duration(animationDurationPerTrend)
            .ease(animationEase)
            .attr("stroke-dashoffset", 0);

      }

    }

    if(showBreakPoints){
        var data_for_ticks = [];
        data.forEach(function(lines){
            data_for_ticks.push({x:lines.p[0].x,close:lines.p[0].close,date:lines.p[0].date});
            data_for_ticks.push({x:lines.p[lines.p.length-1].x,close:lines.p[lines.p.length-1].close,date:lines.p[lines.p.length-1].date});
        });

        if (showBreakPointText){

            var ticks_text = svg.selectAll('text')
                .data(data_for_ticks)
                .enter()
                .append("text")
                .attr("transform", function(d){return "translate(" + x(d.x) + "," + (height+15) + ") rotate(-45)";})
                .attr("text-anchor", "end")
                .attr("dy", 3)
                .attr("dx", 5)
                .text(function(d){return formatDateToString(new Date(d.date));})
        }
        var ticks = svg.selectAll('circle')
            .data(data_for_ticks)
            .enter()
            .append("circle")
            .attr("class","break_points")
            .attr("cx", function(d){return x(d.x);})
            .attr("cy", function(d){return y(d.close) ;})
            .attr("r", breakPointRadius)
            .style("fill", breakPointColor)

        if(showBreakPointTooltips){
            add_breakpoint_tooltips();
        }
        function add_breakpoint_tooltips(){

            // Adding a tooltip which on mouseover shows the date range and the last_close points range.
            var tooltip = d3.select("body")
                .append('div')
                .attr('class', 'tooltip');

                tooltip.append('div')
                .attr('class', 'tabular_div');

                svg.selectAll(".break_points")
                .on('mouseover', function(d) {

                    if(isPrecedingUnit){
                        var html = "<table><thead><tr><th>Close</th><th>Date</th></tr></thead><tbody><tr><td>" + unit + d.close + "</td><td>" + formatDateToString(new Date(d.date)) + "</td></tr></tbody></table>"
                        tooltip.select('.tabular_div').html(html);
                    }
                    else{
                        var html = "<table><thead><tr><th>Close</th><th>Date</th></tr></thead><tbody><tr><td>"  + d.close + unit + "</td><td>" + formatDateToString(new Date(d.date)) + "</td></tr></tbody></table>"
                        tooltip.select('.tabular_div').html(html);
                    }

                    d3.select(this).style("r", breakPointRadiusOnHover);
                    tooltip.style('display', 'block');
                    tooltip.style('opacity',2);

                })
                .on('mousemove', function(d) {
                    d3.select(this).style("r", breakPointRadiusOnHover);
                    tooltip.style('top', (d3.event.layerY + 10) + 'px')
                    .style('left', (d3.event.layerX - 25) + 'px');
                })
                .on('mouseout', function(d) {
                    d3.selectAll(".break_points").style("r", breakPointRadius);
                    tooltip.style('display', 'none');
                    tooltip.style('opacity',0);
                });

        }
    }

    // Add a caption to the chart.
    svg
      .append("text")
      .attr("x", width/2)
      .attr("y", -20)
      .attr("text-anchor", "middle")
      .style("font-size", "12px")
      .text(subCaption);

    // Add a subCaption to the chart.
    svg
      .append("text")
      .attr("x", width/2)
      .attr("text-anchor", "middle")
      .attr("y", -40)
      .style("font-size", "16px")
      .text(caption);

    //  Add the X Axis
    svg.append("g")
        .attr("class", "x axis")
        .attr("transform", "translate(0," + height + ")")
        .call(xAxis);

    // Add the Y Axis
    svg.append("g")
        .attr("class", "y axis")
        .call(yAxis);

    if(showRangeTooltips){
        add_range_tooltips();
    }
    function add_range_tooltips(){

        // Adding a tooltip which on mouseover shows the date range and the last_close points range.
        var tooltip = d3.select("body")
            .append('div')
            .attr('class', 'tooltip');

            tooltip.append('div')
            .attr('class', 'tabular_div');

            svg.selectAll(".line")
            .on('mouseover', function(d) {

                if(isPrecedingUnit){
                    var html = "<table><thead><tr><th>Close</th><th>Date</th></tr></thead><tbody><tr><td>" + unit + d.p[0].close + "</td><td>" + formatDateToString(new Date(d.p[0].date)) + "</td></tr><tr><td>" + unit + d.p[d.p.length -1].close + "</td><td>" + formatDateToString(new Date(d.p[d.p.length -1].date)) + "</td></tr></tbody></table>"
                    tooltip.select('.tabular_div').html(html);
                }
                else{
                    var html = "<table><thead><tr><th>Close</th><th>Date</th></tr></thead><tbody><tr><td>" + d.p[0].close + unit + "</td><td>" + formatDateToString(new Date(d.p[0].date)) + "</td></tr><tr><td>" + d.p[d.p.length -1].close + unit + "</td><td>" + formatDateToString(new Date(d.p[d.p.length -1].date)) + "</td></tr></tbody></table>"
                    tooltip.select('.tabular_div').html(html);
                }

                d3.select(this).style("stroke-width", d.uptrend ? rallyThicknessOnHover : declineThicknessOnHover);
                tooltip.style('display', 'block');
                tooltip.style('opacity',2);

            })
            .on('mousemove', function(d) {
                d3.select(this).style("stroke-width", d.uptrend ? rallyThicknessOnHover : declineThicknessOnHover);
                tooltip.style('top', (d3.event.layerY + 10) + 'px')
                .style('left', (d3.event.layerX - 25) + 'px');
            })
            .on('mouseout', function(d) {
                d3.select(this).style("stroke-width", function(d){return d.w;});
                tooltip.style('display', 'none');
                tooltip.style('opacity',0);
            });

    }

    if(showLegend){

        var legendData = [];
        legendData.push({category:'Yang',color:rallyColor});
        legendData.push({category:'Ying',color:declineColor});
        legendData.push({category:'Breakout',color:breakPointColor});

        // add legend
        var legend = svg
        .selectAll(".legend")
        .data(legendData)
        .enter()
        .append("g")
        .attr("class", "legend")
        .attr("transform", "translate(0,0)");

        legend
          .append("rect")
          .attr("x", width - margin.right)
          .attr("y", function(d,i){return i*20 - margin.top + 15;})
          .attr("width", 15)
          .attr("height", 10)
          .style("fill", function(d) {
            return d.color;
          })

        legend
          .append("text")
          .attr("x", width-margin.right+20)
          .attr("y", function(d,i){return i*20 + 8 - margin.top + 15;})
          .text(function(d){return d.category});
    }

    function formatDateToString(date){
       // 01, 02, 03, ... 29, 30, 31
       var dd = (date.getDate() < 10 ? '0' : '') + date.getDate();
       // 01, 02, 03, ... 10, 11, 12
       var MM = ((date.getMonth() + 1) < 10 ? '0' : '') + (date.getMonth() + 1);
       // 1970, 1971, ... 2015, 2016, ...
       var yy = (date.getFullYear()).toString();
       // create the format you want
       return (dd + "-" + MM + "-" + yy.substr(-2,2));
    }

