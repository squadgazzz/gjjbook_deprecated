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
    <link rel="stylesheet" href="<c:url value="/css/body.css" />">
    <link rel="stylesheet" href="<c:url value="/plugins/simplePagination/simplePagination.css" />">

    <script src="<c:url value="/plugins/simplePagination/jquery.simplePagination.js"/>"></script>
</head>
<body>
<script>
    $(document).ready(function () {
        $('#compact-pagination').pagination({
            items: 100,
            itemsOnPage: 2,
            cssStyle: 'light-theme'
        });
    });
</script>
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
            <ul class="list-group">
                <li>
                    <div class="well">
                        <label>Found accounts:</label>
                    </div>
                </li>
                <c:forEach var="account" items="${accounts}">
                    <c:if test="${not empty account}">
                        <li>
                            <div class="well">
                                <div class="row">
                                    <div class="col-xs-3">
                                        <img width="100px" alt="Avatar"
                                             src="data:image/jpeg;base64,${encodedAvatars.get(account.id)}"/>
                                    </div>
                                    <div class="col-xs-9">
                                        <a href="<c:url value="/account?id=${account.id}"/>">
                                                ${account.name} ${account.middleName} ${account.surName}
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </li>
                    </c:if>
                </c:forEach>
                <li>
                    <div class="well">
                        <div id="compact-pagination" class="pagination"></div>
                    </div>
                </li>
            </ul>
            <%--<label>Found accounts:</label>--%>
            <%--<table class="table">--%>
            <%--<c:forEach var="account" items="${accounts}">--%>
            <%--<c:if test="${not empty account}">--%>
            <%--<tr>--%>
            <%--<td>--%>
            <%--<img width="100px" alt="Avatar"--%>
            <%--src="data:image/jpeg;base64,${encodedAvatars.get(account.id)}"/>--%>
            <%--<a href="<c:url value="/account?id=${account.id}"/>">--%>
            <%--${account.name} ${account.surName}--%>
            <%--</a>--%>
            <%--</td>--%>
            <%--</tr>--%>
            <%--</c:if>--%>
            <%--</c:forEach>--%>
            <%--</table>--%>
        </div>
    </div>
    <div class="col-xs-3">
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
