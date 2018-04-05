var app = angular.module('chart', ['zingchart-angularjs']);


app.service('chartService', function () {


    var chartLineColorList = ["#f18973", "#f7cac9", "#b1cbbb", "#d5f4e6", "#82b74b", "#b5e7a0"];
    var chartBackgroundColorList = ["#bc5a45", "#f7786b", "#deeaee", "#80ced6", "#405d27", "#e3eaa7"];

    this.getChartLineColorList = function () {
        return chartLineColorList;
    }

    this.getChartBackgroundColorList = function () {
        return chartBackgroundColorList;
    }


    var chartMidList = new Array();

    this.getChartMidList = function () {
        return chartMidList;
    }

    this.addObjectToChartMidList = function (xxx) {
        chartMidList.push(xxx)
    }

    this.deleteChartMidList = function () {
        chartMidList.length = 0;
    }

    var chartBidList = new Array();

    this.getChartBidList = function () {
        return chartBidList;
    }

    this.addObjectToChartBidList = function (xxx) {
        chartBidList.push(xxx)
    }

    this.deleteChartBidList = function () {
        chartBidList.length = 0;
    }

    var chartAskList = new Array();

    this.getChartAskList = function () {
        return chartAskList;
    }

    this.addObjectToChartAskList = function (xxx) {
        chartAskList.push(xxx)
    }

    this.deleteChartAskList = function () {
        chartAskList.length = 0;
    }

});


app.controller('chartCtrl', ['$scope', '$http', 'chartService', function ($scope, $http, chartService) {


    function addChart(currency, values, line_color, border_color, dataType) {
        var chartData = {
            "values": values,
            "text": currency,
            "line-color": line_color,
            "legend-marker": {
                "type": "circle",
                "size": 5,
                "background-color": line_color,
                "border-width": 1,
                "shadow": 0,
                "border-color": border_color
            },
            "marker": {
                "background-color": line_color,
                "border-width": 1,
                "shadow": 0,
                "border-color": border_color
            }
        }

        if (dataType == "mid") {
            chartService.addObjectToChartMidList(chartData);
        }
        if (dataType == "bid") {
            chartService.addObjectToChartBidList(chartData);
        }
        if (dataType == "ask") {
            chartService.addObjectToChartAskList(chartData);
        }


    }

    $scope.getAverageRatesFromRestApi = function (codes) {
        chartService.deleteChartMidList();
        $scope.jsonAverageRatesMid = JSON.parse(JSON.stringify(myJson));

        var colorNumber = 0;

        for (var i = 0; i < codes.length; i++) {

            $http({
                url: '/api/average_rates?code=' + codes[i] + '&startDate=' + $scope.startDate + '&stopDate=' + $scope.stopDate,
                method: 'GET',
                contentType: 'application/json'

            }).then(function success(response) {

                var average_rates = response.data;
                var midValues = new Array();
                for (var j = 0; j < average_rates.length; j++) {
                    midValues.push(parseFloat(average_rates[j].mid).toFixed(5));
                }

                addChart(average_rates[0].currency, midValues, chartService.getChartLineColorList()[colorNumber], chartService.getChartBackgroundColorList()[colorNumber], "mid");
                if (colorNumber < 5) {
                    colorNumber++;
                }

                $scope.jsonAverageRatesMid["title"]["text"] = "mid";
                $scope.jsonAverageRatesMid["scale-x"]["min-value"] = Date.parse($scope.startDate);
                $scope.jsonAverageRatesMid["series"] = chartService.getChartMidList();

                console.log('Data saved');

            }, function error(response) {
                console.log('Data not saved ');
            });
        }

    }


    $scope.getTradingRatesFromRestApi = function (codes) {
        chartService.deleteChartBidList();
        chartService.deleteChartAskList();

        $scope.jsonTradingRateBids = JSON.parse(JSON.stringify(myJson));
        $scope.jsonTradingRateAsks = JSON.parse(JSON.stringify(myJson));

        var colorNumber = 0;
        for (var i = 0; i < codes.length; i++) {
            $http({
                url: '/api/trading_rates?code=' + codes[i] + '&startDate=' + $scope.startDate + '&stopDate=' + $scope.stopDate,
                method: 'GET',
                contentType: 'application/json'
            }).then(function success(response) {

                var traiding_rates = response.data;
                var bidValues = new Array();
                var askValues = new Array();
                for (var i = 0; i < traiding_rates.length; i++) {
                    bidValues.push(traiding_rates[i].bid);
                    askValues.push(traiding_rates[i].ask);
                }

                addChart(traiding_rates[0].currency, bidValues, chartService.getChartLineColorList()[colorNumber], chartService.getChartBackgroundColorList()[colorNumber], "bid");
                addChart(traiding_rates[0].currency, askValues, chartService.getChartLineColorList()[colorNumber], chartService.getChartBackgroundColorList()[colorNumber], "ask");
                if (colorNumber < 10) {
                    colorNumber++;
                }

                $scope.jsonTradingRateBids["title"]["text"] = "bid";
                $scope.jsonTradingRateBids["scale-x"]["min-value"] = Date.parse($scope.startDate);
                ;
                $scope.jsonTradingRateBids["series"] = chartService.getChartBidList();

                $scope.jsonTradingRateAsks["title"]["text"] = "ask";
                $scope.jsonTradingRateAsks["scale-x"]["min-value"] = Date.parse($scope.startDate);
                ;
                $scope.jsonTradingRateAsks["series"] = chartService.getChartAskList();

            }, function error(response) {
                console.log('Data not saved ');
            });
        }

    }


    myJson = {
        "type": "line",
        "background-color": "#003849",
        "utc": true,
        "title": {
            "y": "7px",
            "text": "Webpage Analytics",
            "background-color": "#003849",
            "font-size": "24px",
            "font-color": "white",
            "height": "25px"
        },
        "plotarea": {
            "margin": "20% 8% 14% 12%",
            "background-color": "#003849"
        },
        "legend": {
            "layout": "float",
            "background-color": "none",
            "border-width": 0,
            "shadow": 0,
            "width": "75%",
            "text-align": "middle",
            "x": "25%",
            "y": "10%",
            "item": {
                "font-color": "#f6f7f8",
                "font-size": "14px"
            }
        },
        "scale-x": {
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
        "scale-y": {
            "values": "0.000:5.000:1.000",     //"0:1000:250",
            "line-color": "#f6f7f8",
            "shadow": 5,
            "tick": {
                "line-color": "#f6f7f8"
            },
            "guide": {
                "line-color": "#f6f7f8",
                "line-style": "dashed"
            },
            "item": {
                "font-color": "#f6f7f8"
            },
            "label": {
                "text": "Page Views",
                "font-color": "#f6f7f8"
            },
            "minor-ticks": 5,
            "thousands-separator": ","
        },
        "crosshair-x": {
            "line-color": "#f6f7f8",
            "plot-label": {
                "border-radius": "5px",
                "border-width": "1px",
                "border-color": "#f6f7f8",
                "padding": "10px",
                "font-weight": "bold"
            },
            "scale-label": {
                "font-color": "#00baf0",
                "background-color": "#f6f7f8",
                "border-radius": "5px"
            }
        },
        "tooltip": {
            "visible": false
        },
        "plot": {
            "tooltip-text": "%t views: %v<br>%k",
            "shadow": 0,
            "line-width": "3px",
            "marker": {
                "type": "circle",
                "size": 3
            },
            "hover-marker": {
                "type": "circle",
                "size": 4,
                "border-width": "1px"
            }
        },
        "series": ""
    };


}]);


function SingleChart(currency, values) {
    this.currency = currency;
    this.values = values;

    this.setSingleChartParams = function (currency, values) {
        this.currency = currency;
        this.values = values;
    }

    this.showSingleChartParams = function () {
        return 'SingleChart: [currency: ' + currency + ', values: ' + values + ']';
    }
}