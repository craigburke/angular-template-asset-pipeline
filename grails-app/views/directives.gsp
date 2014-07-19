<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Directives Test</title>
    <asset:javascript src="directives.js" />
</head>

<body ng-app="myApp.directives" ng-init="template = 'test1.html'">

    <select id="templateSelect" ng-model="template" ng-options="t as t for t in ['test1.html', 'test2.html', 'test3.html']" ></select>

    <ng-include src="template" ></ng-include>

</body>
</html>