//= require angular.min
//= require angular-route.min
//= require_self
//= require_full_tree my-app

angular.module('myApp.fooBar', []).directive('testDirective', function() {
    return {
        templateUrl: 'directive.html'
    }
});
angular.module('myApp', ['myApp.fooBar']);
