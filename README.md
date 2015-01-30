AngularJS Template Asset Pipeline
================================
[ ![Codeship Status for craigburke/angular-template-asset-pipeline](https://codeship.com/projects/5551ee00-5167-0132-b713-2673ecafb081/status?branch=master)](https://codeship.com/projects/48337)

## How it works

This plugin inserts the compressed contents of your template files into AngularJs's $templateCache.
Both the template name and module are determined by the file name and location. This plugin expects the module name to be in camel case (ex. myApp not MyApp).

For example a file located at

```
/assets/javascripts/my-app/app-section/templates/index.tpl.htm
```

Will generate javascript like this:
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
//= require /angular/angular
//= require /angular/angular-route
//= require_self
//= require_tree /my-app/app-section/templates

angular.module('myApp.appSection', ['ngRoute'])
	.config(function($routeProvider) {
	      $routeProvider
	          .when('/index', {
	              templateUrl: 'index.htm'
	          })
	          .otherwise({redirectTo: '/index'});
	});
```
Note the use of **require_self** above to make sure that the **myApp.appSection** module is defined before the template files are imported.

## Configuration

If you run into naming collisions with your template names, you can opt to include the full path in the name with the **includePathInName** setting. 

With the setting set to true, a file located at
```
/assets/javascripts/my-app/app-section/templates/index.tpl.htm
```

Will then generate javascript like this:
```javascript
angular.module('myApp.appSection').run(['$templateCache', function($templateCache) {
	$templateCache.put('/my-app/app-section/index.htm', '<h1>Hello World!</h1>');
}]);
```

You can also change the template folder, disable the compression of your HTML templates, or preserve Html comments:

```groovy
assets {
	angular {
		// Defaults
		templateFolder = "templates"			
		compressHtml = true
		preserveHtmlComments = false
		includePathInName = false
	}
}
```

**Note:** If you're looking for documentation for the Grails plugin, it can be found here:

[AngularJS Template Assset Pipeline - Grails Plugin](https://github.com/craigburke/angular-template-grails-asset-pipeline)
