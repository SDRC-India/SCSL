function changeAnimEase(element){
    console.log(element);
    animEase = element.value;
    drawChart();
}

function changeAnimDuration(element){
    animDuration = element.value;
    drawChart();
}

function changeChartTheme(element){
    chartTheme = element.value;
    drawChart();
}

function changeReversalType(element){
    reversalType = element.value;
    drawChart();
}

function changeReversalValue(){
    reversalValue = element.value;
    drawChart();
}

function showBreakPoints(element){
    breakPoints = element.checked;
    drawChart();
}

function showLegend(element){
    legend = element.checked;
    drawChart();
}

function showBreakPointTooltip(element){
    breakPointTooltips = element.checked;
    drawChart();
}

function showBreakPoints(element){
    breakPoints = element.checked;
    drawChart();
}

function showBreakPointText(element){
    breakPointText = element.checked;
    drawChart();
}

function showAnimation(element){
    animation = element.checked;
    drawChart();
}

function showRangePointTooltips(element){
    rangeTooltips = element.checked;
    drawChart();
}

function changeCaption(element){
    caption = element.value;
    drawChart();
}

function changeSubCaption(element){
    subCaption = element.value;
    drawChart();
}

function changeRallyColor(element){
    rallyColor = element.value;
    drawChart();
}

function changeRallyThickness(element){
    rallyThickness = element.value;
    drawChart();
}

function changeRallyThicknessOnHover(element){
    rallyThicknessOnHover = element.value;
    drawChart();
}

function changeDeclineColor(element){
    declineColor = element.value;
    drawChart();
}

function changeDeclineThickness(element){
    declineThickness = element.value;
    drawChart();
}

function changeDeclineThicknessOnHover(element){
    declineThicknessOnHover = element.value;
    drawChart();
}

function changeBreakPointRadius(element){
    breakPointRadius = element.value;
    drawChart();
}

function changeBreakPointRadiusOnHover(element){
    breakPointRadiusOnHover = element.value;
    drawChart();
}

function changeBreakPointColor(element){
    breakPointColor = element.value;
    drawChart();
}

function drawChart(){
    var chartElement = document.getElementById("kagiChart");
    chartElement.innerHTML="";

    var chart_options = {
        "width":window.innerWidth,
        "height":500,
        "margin":{top: 75, right: 50, bottom: 100, left: 50},
        "chartTheme":chartTheme,
        "caption": caption,
        "subCaption": subCaption,
        "reversalType": reversalType, // use "diff" for difference in value; use "pct" for percentage change
        "reversalValue": reversalValue,
        "unit": "$",
        "isPrecedingUnit":true,
        "rallyColor": rallyColor,
        "rallyThickness": rallyThickness,
        "rallyThicknessOnHover": rallyThicknessOnHover,
        "declineColor": declineColor,
        "declineThickness": declineThickness,
        "declineThicknessOnHover": declineThicknessOnHover,
        "showBreakPoints":breakPoints,
        "breakPointColor":breakPointColor,
        "breakPointRadius":breakPointRadius,
        "breakPointRadiusOnHover":breakPointRadiusOnHover,
        "showBreakPointText":breakPointText,
        "showBreakPointTooltips":breakPointTooltips,
        "showRangeTooltips":rangeTooltips,
        "showLegend":legend,
        "showAnimation":animation,
        "animationDurationPerTrend":animDuration, // in seconds
        "animationEase":animEase
    }

    KagiChart(data,chart_options); // data is served from data.js
}

var animEase = 'linear';
var animDuration = '500';
var chartTheme = 'light';
var reversalType = 'diff';
var reversalValue = '5';

var caption = "Apple Stock Closing Prices";
var subCaption = "2015-01-02 to 2017-02-24";

var rallyColor = "#2ecc71";
var rallyThickness = "3";
var rallyThicknessOnHover = "5";

var declineColor = "#e74c3c";
var declineThickness = "2";
var declineThicknessOnHover = "4";

var breakPointRadius =  "2";
var breakPointColor = "#3498db";
var breakPointRadiusOnHover = "5";

var legend = true;
var breakPoints = true;
var breakPointText = true;
var breakPointTooltips = true;
var rangeTooltips = true;
var animation = true;

drawChart();