<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: IZhavoronkov
  Date: 18.07.2017
  Time: 10:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="/WEB-INF/JSP/header.jsp"/>
    <link rel="stylesheet" href="<c:url value="/CSS/body.css" />">
</head>
<body>
<div class="fixed-centered container">
    <div class="row row-top">
        <div class="col-xs-2">
            <ul class="list-group">
                <li>
                    <a class="list-group-item" href="<c:url value="/account?id=${loggedUser.id}"/>"><span
                            class="glyphicon glyphicon-home" style="margin-right: 10px"></span>
                        My page</a>
                </li>
                <li>
                    <a class="list-group-item" href="#"><span
                            class="glyphicon glyphicon-envelope" style="margin-right: 10px"></span> Messages</a>
                </li>
                <li>
                    <a class="list-group-item" href="#"><span
                            class="glyphicon glyphicon-th-large" style="margin-right: 10px"></span> Groups</a>
                </li>
            </ul>
        </div>
        <div class="col-xs-7">
            <div class="well">
                <label>Found accounts:</label>
                <table class="table">
                    <c:forEach var="account" items="${accounts}">
                        <c:if test="${not empty account}">
                            <tr>
                                <td>
                                    <c:set var="image" value="${accountService.getEncodedAvatar(account)}"/>
                                    <img width="100px" alt="Avatar" src="data:image/jpeg;base64,${image}"/>
                                    <a href="<c:url value="/account?id=${account.id}"/>">
                                            ${account.name} ${account.surName}
                                    </a>
                                </td>
                            </tr>
                        </c:if>
                    </c:forEach>
                </table>
            </div>
        </div>
        <div class="col-xs-3">
        </div>
    </div>
</div>
<%--<table>
    <tr>Found accounts:</tr>
    <c:forEach var="account" items="${accounts}">
        <c:if test="${not empty account}">
            <tr>
                <td>
                    <a href="<c:url value="/account?id=${account.id}"/>">
                            ${account.name} ${account.surName}
                    </a>
                </td>
            </tr>
        </c:if>
    </c:forEach>
</table>--%>
</body>
</html>
