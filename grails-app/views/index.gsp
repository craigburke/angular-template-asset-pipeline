<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Angular Test Page</title>
    <asset:javascript src="app.js" />
</head>

<body ng-app="myApp" ng-init="template = 'test1.html'">

    <select id="templateSelect" ng-model="template" ng-options="t as t for t in ['test1.html', 'test2.html', 'test3.html', 'test4.html']" ></select>

    <ng-include src="template" ></ng-include>

</body>
</html>