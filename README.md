AngularJs Template Asset-Pipeline
================================
[ ![Codeship Status for craigburke/angular-template-asset-pipeline](https://codeship.com/projects/5e8740f0-8c5d-0132-b104-6e5f8c02ac8f/status?branch=master)](https://codeship.com/projects/60441)

The `angular-template-asset-pipeline` is an [Asset Pipeline](http://www.github.com/bertramdev/asset-pipeline) module that provides angular template precompiler support for Gradle and Grails projects.

## Getting started
Make sure your templates are contained within a **templates** folder and have the file extension **.tpl.htm**

### Gradle
```groovy
buildscript {
    dependencies {
        classpath 'com.bertramlabs.plugins:asset-pipeline-gradle:2.0.20'
        classpath "com.craigburke.angular:angular-template-asset-pipeline:2.0.4"
        classpath "com.craigburke.angular:angular-annotate-asset-pipeline:2.0.2"
    }
}

dependencies {
  compile "com.craigburke.angular:angular-template-asset-pipeline:2.0.4"
}
```
Make sure the dependency is specified in both the buildscript and dependencies blocks.

### Grails 
Add the plugin to your **BuildConfig.groovy**:
```groovy
	plugins {
		runtime ":angular-template-asset-pipeline:2.0.4"
	}
```
*Note: If you're not yet using Asset Pipeline 2.0+, then you can use version 1.4.2 of the plugin.*

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

You can also change the template folder, disable the compression of your HTML templates, or preserve Html comments.

### Gradle
In gradle these settings can be changed in your **build.config**
```groovy
assets {
	configOptions = [
		angular : [
			templateFolder: "templates",		
			compressHtml: true,
			preserveHtmlComments: false,
			includePathInName: false
		]
    	]
}
```

### Grails
In Grails these settings can be set in your **Config.groovy**

```groovy
grails {
	assets {
		angular {
			// Defaults
			templateFolder = "templates"			
			compressHtml = true
			preserveHtmlComments = false
			includePathInName = false
		}
	}
}
```
