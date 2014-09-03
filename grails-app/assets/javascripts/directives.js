//= require angular.min
//= require_self
//= require_tree /templates/my-app/directives/

angular.module('myApp.directives', [])
    .directive('testDirective', function() {
    return {
        templateUrl: 'directive.html'
    }
});
