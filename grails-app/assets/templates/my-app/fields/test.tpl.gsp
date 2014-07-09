<%@ page import="angular.template.asset.pipeline.Foo" %>
<div>
    <h1>Field Test</h1>

    <form>
    <f:with bean="${new Foo()}" >
        <f:field property="name" input-ng-model="foo.name" ></f:field>
        <f:field property="number" input-ng-model="foo.number"></f:field>
    </f:with>
    </form>

    <span id="name-value">{{foo.name}}</span>
    <span id="number-value">{{foo.number}}</span>

</div>