var app = angular.module('chart', ['zingchart-angularjs']);


app.service('chartService', function(){





    var chartLineColorList = ["#f18973","#f7cac9","#b1cbbb","#d5f4e6","#82b74b","#b5e7a0"];
    var chartBackgroundColorList = ["#bc5a45","#f7786b","#deeaee","#80ced6","#405d27","#e3eaa7"];

    this.getChartLineColorList = function()
    {
        return chartLineColorList;
    }

    this.getChartBackgroundColorList = function()
    {
        return chartBackgroundColorList;
    }


    var chartMidList = new Array();

    this.getChartMidList = function()
    {
        return chartMidList;
    }

    this.addObjectToChartMidList = function(xxx)
    {
        chartMidList.push(xxx)
    }

    this.deleteChartMidList = function()
    {
        chartMidList.length=0;
    }

    var chartBidList = new Array();

    this.getChartBidList = function()
    {
        return chartBidList;
    }

    this.addObjectToChartBidList = function(xxx)
    {
        chartBidList.push(xxx)
    }

    this.deleteChartBidList= function()
    {
        chartBidList.length=0;
    }

    var chartAskList = new Array();

    this.getChartAskList = function()
    {
        return chartAskList;
    }

    this.addObjectToChartAskList = function(xxx)
    {
        chartAskList.push(xxx)
    }

    this.deleteChartAskList= function()
    {
        chartAskList.length=0;
    }

});


app.controller('chartCtrl',['$scope','$http','chartService', function($scope,$http,chartService) {

    function addChart(currency,values,line_color,border_color,dataType)
    {
        var chartData = {
            "values":values,
            "text":currency,
            "line-color":border_color,
            "marker":{
                "background-color":border_color,
                "border-color":border_color
            }
        }

        if(dataType=="mid")
        {
            chartService.addObjectToChartMidList(chartData);
        }
        if(dataType=="bid")
        {
            chartService.addObjectToChartBidList(chartData);
        }
        if(dataType=="ask")
        {
            chartService.addObjectToChartAskList(chartData);
        }


    }

    $scope.getAverageRatesFromRestApi = function(codes)
    {
        chartService.deleteChartMidList();
        $scope.jsonAverageRatesMid = JSON.parse(JSON.stringify(myJson)) ;

        var colorNumber=0;

        for (var i = 0; i < codes.length; i++) {

            $http({
                url:'/api/average_rates?code=' + codes[i] + '&startDate=' + $scope.startDate + '&stopDate=' + $scope.stopDate,
                method: 'GET',
                contentType: 'application/json'

            }).then(function success(response) {

                var average_rates = response.data;
                var midValues = new Array();
                for (var j = 0; j < average_rates.length; j++) {
                    midValues.push(parseFloat(average_rates[j].mid).toFixed(4));
                }

                addChart(average_rates[0].currency,midValues,chartService.getChartLineColorList()[colorNumber] ,chartService.getChartBackgroundColorList()[colorNumber],"mid");
                if(colorNumber<chartService.getChartLineColorList().length)
                {
                    colorNumber++;
                }

                $scope.jsonAverageRatesMid["title"]["text"] = "Kursy średnie walut obcych";
                $scope.jsonAverageRatesMid["scale-x"]["min-value"] = Date.parse($scope.startDate);

                console.log('Data saved');

            }, function error(response) {
                console.log('Data not saved ');
            });
        }

        $scope.jsonAverageRatesMid["series"] = chartService.getChartMidList();


    }


    $scope.getTradingRatesFromRestApi = function(codes)
    {
        chartService.deleteChartBidList();
        chartService.deleteChartAskList();

        $scope.jsonTradingRateBids = JSON.parse(JSON.stringify(myJson)) ;
        $scope.jsonTradingRateAsks = JSON.parse(JSON.stringify(myJson)) ;

        var colorNumber=0;
        for (var i = 0; i < codes.length; i++) {
            $http({
                url:'/api/trading_rates?code=' + codes[i] + '&startDate=' + $scope.startDate + '&stopDate=' + $scope.stopDate,
                method: 'GET',
                contentType: 'application/json'
            }).then(function success(response) {

                var traiding_rates = response.data;
                var bidValues = new Array();
                var askValues = new Array();
                for (var i = 0; i < traiding_rates.length; i++) {
                    bidValues.push(parseFloat(traiding_rates[i].bid).toFixed(4));
                    askValues.push(parseFloat(traiding_rates[i].ask).toFixed(4));
                    console.log(parseFloat(traiding_rates[i].bid).toFixed(4))
                }

                addChart(traiding_rates[0].currency,bidValues,chartService.getChartLineColorList()[colorNumber] ,chartService.getChartBackgroundColorList()[colorNumber],"bid");
                addChart(traiding_rates[0].currency,askValues,chartService.getChartLineColorList()[colorNumber] ,chartService.getChartBackgroundColorList()[colorNumber],"ask");
                if(colorNumber<chartService.getChartLineColorList().length)
                {
                    colorNumber++;
                }

                $scope.jsonTradingRateBids["title"]["text"] = "Kursy kupna walut obcych";
                $scope.jsonTradingRateBids["scale-x"]["min-value"] = Date.parse($scope.startDate);;

                $scope.jsonTradingRateAsks["title"]["text"] = "Kursy sprzedaży walut obcych";
                $scope.jsonTradingRateAsks["scale-x"]["min-value"] = Date.parse($scope.startDate);;

                $scope.error2 ="qqqqq";

            }, function error(response) {
                $scope.error2 = "Nie wybrano waluty lub wprowadzono niepoprawnie date."
                console.log('Data not saved ');
            });
            $scope.jsonTradingRateBids["series"] = chartService.getChartBidList();
            $scope.jsonTradingRateAsks["series"] = chartService.getChartAskList();

        }

    }


    myJson = {
        "background-color":"white",
        "type":"line",
        "title":{
            "text":"Yearly Outbreaks by Genus",
            "color":"#333",
            "background-color":"white",
            "width":"60%",
            "text-align":"left",
        },
        "subtitle":{
            "text":"Kilknij na wybrany kurs waluty znajdujący się w legendzie w celu susunięcia go z wykresu.",
            "text-align":"left",
            "width":"60%"
        },
        "legend":{
            "layout":"x1",
            "margin-top":"5%",
            "border-width":"0",
            "shadow":false,
            "marker":{
                "cursor":"hand",
                "border-width":"0"
            },
            "background-color":"white",
            "item":{
                "cursor":"hand"
            },
            "toggle-action":"remove"
        },
        "scale-x":{
            "min-value": 1383292800000,
            "shadow": 0,
            "step": 86400000,
            "line-color": "#f6f7f8",
            "tick": {
                "line-color": "#f6f7f8"
            },
            "guide": {
                "line-color": "#f6f7f8"
            },
            "item": {
                "font-color": "#f6f7f8"
            },
            "transform": {
                "type": "date",
                "all": "%D, %d %M<br />%h:%i %A",
                "guide": {
                    "visible": false
                },
                "item": {
                    "visible": false
                }
            },
            "label": {
                "visible": false
            },
            "minor-ticks": 0
        },
        "scaleY":{
            "line-color":"#333"
        },
        "tooltip":{
            "text":"%t: %v w dniu in %k"
        },
        "plot":{
            "line-width":3,
            "marker":{
                "size":2
            },
            "selection-mode":"multiple",
            "background-mode":"graph",
            "selected-state":{
                "line-width":4
            },
            "background-state":{
                "line-color":"#eee",
                "marker":{
                    "background-color":"none"
                }
            }
        },
        "plotarea":{
            "margin":"15% 25% 10% 7%"
        },
        "series": ""
    };


}]);
