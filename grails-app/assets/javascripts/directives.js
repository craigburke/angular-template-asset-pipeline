//= require angular.min
//= require_self
//= require_tree /my-app/directives/templates

angular.module('myApp.directives', [])
    .directive('testDirective', function() {
    return {
        templateUrl: 'directive.html'
    }
});
