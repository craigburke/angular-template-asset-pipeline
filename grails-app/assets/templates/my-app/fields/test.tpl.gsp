<%@ page import="com.craigburke.angular.Foo" %>
<div>
    <h1>Field Test</h1>

    <form>
    <f:with bean="${new Foo()}" >
        <f:field property="name" input-ng-model="foo.name" />
        <f:field property="number" input-ng-model="foo.number"/>
        <f:field property="fooDate" input-ng-model="foo.fooDate"/>
    </f:with>
    </form>

    <f:with bean="${new Foo()}" >
        <f:display property="name" />
        <f:display property="number" />
        <f:display property="fooDate" />
    </f:with>

    <span id="name-value">{{foo.name}}</span>
    <span id="number-value">{{foo.number}}</span>

</div>