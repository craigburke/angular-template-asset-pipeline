AngularJs Template Asset-Pipeline
================================
[![Build Status](https://drone.io/github.com/craigburke/angular-template-asset-pipeline/status.png)](https://drone.io/github.com/craigburke/angular-template-asset-pipeline/latest)

The Grails `angular-template-asset-pipeline` is a plugin that provides angular template precompiler support to the asset-pipeline plugin.

For more information on how to use asset-pipeline, visit [here](http://www.github.com/bertramdev/asset-pipeline).

## Getting started
Add the plugin to your **BuildConfig.groovy**:
```groovy
plugins {
		runtime ":angular-template-asset-pipeline:1.2.0"
}
```
Make sure your templates are contained within the **assets/templates** folder and have the file extension **.tpl.htm**, **.tpl.html** or **tpl.gsp**

## Templates

Any templates with a **tpl.htm** or **tpl.html** extension will be treated as normal static HTML.

Any templates with the **tpl.gsp** extension are interpreted just like normal Groovy Server Pages and can
be accessed in your Javascript by the **.html** extension. For example, index.tpl.gsp can be accessed as 
simply index.html.

You can use all the typical GSP tags in these templates, meaning that plugins like the excellent [Fields Plugin](https://github.com/gpc/grails-fields) will work here.

One potential gotcha is that that these GSP templates are just used to generate static HTML and won't change on subsequent requests. For example:

```html
<!-- This won't work as expected. The time will never change -->
<p>The current time is: <%= new Date() %></p>
```

## How it works

This plugin inserts the compressed contents of your template files into AngularJs's $templateCache.
Both the template name and module are determined by the file name and location.

For example a file located at

```
/grails-app/assets/templates/my-app/app-section/index.tpl.htm
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
//= require angular/angular.min.js
//= require angular/angular-route.min.js
//= require_self
//= require_tree my-app/app-section/

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
You can change the template root folder, module separator character, disable the compression of your HTML templates, or preserve Html comments in your **Config.groovy**:
```groovy
grails {
	assets {
		angular {
			// Defaults
			templateRoot = "templates"
			moduleSeparator = "."
			compressHtml = true
			preserveHtmlComments = false
		}
	}
}
```
