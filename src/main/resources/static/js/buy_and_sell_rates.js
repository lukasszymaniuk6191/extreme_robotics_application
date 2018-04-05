/* global angular */
/*eslint no-mixed-spaces-and-tabs: ["error", "smart-tabs"]*/
/*eslint no-console: ["error", { allow: ["warn", "error"] }] */

var  mainPage2 = angular.module('buyAndSellRates', []);

mainPage2.service('BuyAndSellRates', function(){
	
	var buyAndSellRates = new Array();
	
	this.getBuyAndSellRates  = function()
	{
		return buyAndSellRates ;
	}
	
	this.addBuyAndSellRate = function(buyAndSellRate)
	{
		buyAndSellRates.push(buyAndSellRate);
	}
    
    var codes = new Array();
    
    this.getCodes = function()
    {
        return codes;
    }
    
    this.addCode = function(code)
    {
        codes.push(code);
    }
    
    
	
});


mainPage2.controller('buyAndSellRatesCtrl', ['$scope','$http','BuyAndSellRates', function($scope,$http,BuyAndSellRates) {

    
    $scope.getCheckedObjectData = function(buyAndSellRates)
    {
        var checked = false;

        var codes = BuyAndSellRates.getCodes();
        for (var i=0; i < codes.length; i++) {
            if (codes[i] === buyAndSellRates.code) {
                checked = true;
            }
        }

        if(checked) {
            var index = BuyAndSellRates.getCodes().indexOf(buyAndSellRates.code);
            BuyAndSellRates.getCodes().splice(index, 1);
        }
        else {
            BuyAndSellRates.addCode(buyAndSellRates.code);
        }

    }

    $scope.codes=BuyAndSellRates.getCodes();

    $scope.getDataBuyAndSellRatesFromRestApi = function()
    {
        $http({
            url : '/api/current/trading_rates/all',
            method : 'GET',
            contentType: 'application/json'

        }).then(function success(response) {

            var traiding_rates_all = response.data;

            for(var i=0; i<traiding_rates_all.length; i++)
            {
                var buyAndSellRate = new BuyAndSellRate(traiding_rates_all[i].currency
                    ,traiding_rates_all[i].code, traiding_rates_all[i].bid,traiding_rates_all[i].ask);
                BuyAndSellRates.addBuyAndSellRate(buyAndSellRate);
            }

            $scope.buyAndSellRates=BuyAndSellRates.getBuyAndSellRates();

            console.log('Data saved');

        }, function error(response) {
            console.log('Data not saved ');
        });

    }

}]);  


function BuyAndSellRate(currency,code,bid,ask)
{
    this.currency = currency;
    this.code = code;
    this.bid = bid;
    this.ask = ask;
    
    this.setBuyAndSellRateParams = function(currency,code, bid, ask)
    {
        this.currency = currency;
        this.code = code;
        this.bid = bid;
        this.ask = ask;
    }
    
    this.showBuyAndSellRateParams = function()
    {
        return 'AverageRate: [currency: '+currency+', code: '+code+', bid: '+bid+ ', ask: '+ ask+']';
    }
}


