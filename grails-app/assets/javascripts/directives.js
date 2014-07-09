//= require angular.min
//= require_self
//= require_tree my-app/directives/

angular.module('myApp.directives', [])
    .directive('testDirective', function() {
    return {
        templateUrl: 'directive.html'
    }
});
