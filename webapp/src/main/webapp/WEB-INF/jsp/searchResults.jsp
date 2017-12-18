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
    <jsp:include page="/WEB-INF/jsp/header.jsp"/>
    <link rel="stylesheet" href="<c:url value="/css/body.css" />">
    <link rel="stylesheet" href="<c:url value="/plugins/simplePagination/simplePagination.css" />">

    <script src="<c:url value="/plugins/simplePagination/jquery.simplePagination.js"/>"></script>
    <script src="<c:url value="/js/paginator.js"/>"></script>
</head>
<body>
<input id="searchResultCount" type="hidden" value="${searchResultCount}"/>
<input id="pageSize" type="hidden" value="${pageSize}"/>
<input id="query" type="hidden" value="${query}"/>
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
                <ul class="nav nav-tabs">
                    <li class="active"><a href="#global-accounts" data-toggle="tab">Accounts</a></li>
                    <li><a href="#global-groups" data-toggle="tab">Groups</a></li>
                </ul>
                <div class="tab-content clearfix">
                    <div class="tab-pane active" id="global-accounts">
                        <div class="list-group">
                            <c:forEach var="account" items="${accounts}">
                                <c:if test="${not empty account}">
                                    <a class="list-group-item" name="foundAccount"
                                       href="<c:url value="/account?id=${account.id}"/>">
                                        <div class="row">
                                            <div class="col-xs-3">
                                                <img name="accountAvatar" width="100px" alt="Avatar"
                                                     src="data:image/jpeg;base64,${account.stringAvatar}"/>
                                            </div>
                                            <div class="col-xs-9">
                                                <object name="accountName">
                                                        ${account.name} ${account.middleName} ${account.surName}
                                                </object>
                                            </div>
                                        </div>
                                    </a>
                                </c:if>
                            </c:forEach>
                        </div>
                    </div>
                    <div class="tab-pane" id="global-groups">
                        <h3>Development in progress</h3>
                    </div>
                </div>
                <object name="paginator">
                    <div id="compact-pagination" class="pagination"></div>
                </object>
            </div>
        </div>
    </div>
    <div class="col-xs-3">
    </div>
</div>
</body>
</html>


<%--<ul class="list-group">--%>
<%--<li>--%>
<%--<div class="well">--%>
<%--<label>Found accounts:</label>--%>
<%--</div>--%>
<%--</li>--%>
<%--<c:forEach var="account" items="${accounts}">--%>
<%--<c:if test="${not empty account}">--%>
<%--<li name="foundAccount">--%>
<%--<div class="well">--%>
<%--<div class="row">--%>
<%--<div class="col-xs-3">--%>
<%--<img name="accountAvatar" width="100px" alt="Avatar"--%>
<%--src="data:image/jpeg;base64,${account.stringAvatar}"/>--%>
<%--</div>--%>
<%--<div class="col-xs-9">--%>
<%--<a name="accountName" href="<c:url value="/account?id=${account.id}"/>">--%>
<%--${account.name} ${account.middleName} ${account.surName}--%>
<%--</a>--%>
<%--</div>--%>
<%--</div>--%>
<%--</div>--%>
<%--</li>--%>
<%--</c:if>--%>
<%--</c:forEach>--%>
