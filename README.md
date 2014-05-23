AngularJs Template Asset-Pipeline
================================
The Grails `angular-template-asset-pipeline` is a plugin that provides angular template precompiler support to the asset-pipeline plugin.

For more information on how to use asset-pipeline, visit [here](http://www.github.com/bertramdev/asset-pipeline).

## Getting started
1. Add the plugin to your **BuildConfig.groovy**:
```groovy
plugins {
		runtime ":angular-template-asset-pipeline:0.9.0"
}
```
1. Make sure your templates all have the file extension **.tpl.htm** or **.tpl.html**


## How it works

This plugin inserts the compressed contents of your template files into AngularJs's $templateCache.
Both the template name and module are determined by the file name and location.

For example a file located at

```
/grails-app/assets/templates/my-app/app-section/index.tpl.htm
```

Will generate the javascript like the following:
```javascript
angular.module('myApp.appSection').run(['$templateCache', function($templateCache) {
	$templateCache.put('index.htm', '<h1>Hello World!</h1>');
}]);
```
This allows you to reference your template by just using the file name (without the .tpl).

**Note:** this requires that the module **myApp.appSection** was previously defined in your JavaScript.

## Example project
Here's an example of how you might use this plugin in a project.
```javascript
//= require angular/angular.min.js
//= require angular/angular-route.min.js
//= require_self
//= require_tree my-app/app-section/

var appSection = angular.module('myApp.appSection', ['ngRoute']);

appSection.config(function($routeProvider) {
      $routeProvider
          .when('/index', {
              templateUrl: 'index.html'
          })
          .otherwise({redirectTo: '/index'});
});
```
Note the use of **require_self** above to make sure that the **myApp.appSection** module is defined before the template files are imported.

## Configuration
You can change the module separator character or disable the compression of your HTML templates in your **Config.groovy** file:
```groovy
grails {
	assets {
		angular {
			compressHtml = false
			moduleSeparator = "*"
		}
	}
}
```
