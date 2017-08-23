<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: IZhavoronkov
  Date: 17.07.2017
  Time: 17:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>GJJbook</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="stylesheet" type="text/css" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro">
    <link rel="stylesheet" type="text/css"
          href="<c:url value="/webjars/bootstrap/3.3.7/css/bootstrap.css"/>"/>
    <style>
        <%@include file="/CSS/header.css" %>
        <%@include file="/CSS/main.css" %>
    </style>

    <script src="<c:url value="/webjars/jquery/3.2.1/jquery.min.js"/>"></script>
    <script src="<c:url value="/webjars/bootstrap/3.3.7/js/bootstrap.min.js"/>"></script>
</head>
<body>
<c:set var="account" value="${sessionScope.loggedUser}"/>
<div class="gjjbook-header">
    <nav class="navbar navbar-inverse">
        <div class="fixed-centered container">
            <div class="row">
                <div class="col-xs-2">
                    <div class="navbar-header" style="padding-left: 15px">
                        <a class="navbar-brand" href="<c:url value="/"/>">GJJbook</a>
                    </div>
                </div>
                <div class="col-xs-3">
                    <c:if test="${not empty account}">
                        <form class="navbar-form navbar-left" action="search" role="search">
                            <div class="input-group">
                                <div class="input-group-btn">
                                    <button class="btn btn-default" type="submit">
                                        <i class="glyphicon glyphicon-search"></i>
                                    </button>
                                </div>
                                <input type="search" class="form-control" placeholder="Search" name="q">
                            </div>
                        </form>
                    </c:if>
                </div>
                <div class="col-xs-7">
                    <c:if test="${not empty account}">
                        <ul class="nav navbar-nav navbar-right">
                            <li class="dropdown">
                                <a class="dropdown-toggle" data-toggle="dropdown" href="">${account.email}
                                    <span class="caret"></span></a>
                                <ul class="dropdown-menu">
                                    <li><a href="${pageContext.request.contextPath}/"><span class="glyphicon glyphicon-user"></span> My page</a></li>
                                    <li><a href="${pageContext.request.contextPath}/logout"><span class="glyphicon glyphicon-log-out"></span>
                                        Logout</a>
                                    </li>
                                </ul>
                            </li>
                        </ul>
                    </c:if>
                </div>
            </div>
        </div>
    </nav>
</div>
</body>
</html>