<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: IZhavoronkov
  Date: 19.07.2017
  Time: 9:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="/WEB-INF/jsp/header.jsp"/>
    <link rel="stylesheet" href="<c:url value="/css/body.css" />">
</head>
<body>
<c:choose>
    <c:when test="${empty account}">
        <% response.sendError(HttpServletResponse.SC_NOT_FOUND); %>
    </c:when>
    <c:otherwise>
        <div class="fixed-centered container">
            <div class="row row-top">
                <div class="col-xs-2">
                    <ul class="list-group">
                        <li>
                            <a class="list-group-item"
                               href="<c:url value="/account?id=${loggedUser.id}"/>"><span
                                    class="glyphicon glyphicon-home" style="margin-right: 10px"></span>
                                My page</a>
                        </li>
                        <li>
                            <a class="list-group-item" href="#"><span
                                    class="glyphicon glyphicon-envelope" style="margin-right: 10px"></span>
                                Messages</a>
                        </li>
                        <li>
                            <a class="list-group-item" href="#"><span
                                    class="glyphicon glyphicon-th-large" style="margin-right: 10px"></span>
                                Groups</a>
                        </li>
                    </ul>
                </div>
                <div class="col-xs-3">
                    <div class="row">
                        <div class="col-xs-12 col-left" align="center">
                            <div class="well">
                                <img width="100%" alt="Avatar"
                                     src="data:image/jpeg;base64,${avatar}"/><br/><br/>
                                <c:if test="${sessionScope.loggedUser.id == account.id}">
                                    <a class="btn btn-block"
                                       href="editaccount?id=${account.id}">Edit profile
                                    </a>
                                </c:if>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-left">
                            <div class="well">
                                <h3> Friends </h3>
                                <ul>
                                    <li>1</li>
                                    <li>2</li>
                                    <li>3</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-left">
                            <div class="well">
                                <h3> Groups </h3>
                                <ul>
                                    <li>1</li>
                                    <li>2</li>
                                    <li>3</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-xs-7">
                    <div class="row">
                        <div class="col-xs-12 col-right">
                            <div class="well">
                                <h3>
                                        ${account.name} ${account.middleName} ${account.surName}
                                </h3>
                                <table class="table">
                                    <tbody>
                                    <tr>
                                        <td>Gender:</td>
                                        <td>${account.gender}</td>
                                    </tr>
                                    <tr>
                                        <td>Birthday:</td>
                                        <td>${account.birthDate}</td>
                                    </tr>
                                    <c:set var="accPhones" value="${account.phones}"/>
                                    <c:if test="${not empty accPhones}">
                                        <c:forEach var="phone" items="${accPhones}">
                                            <tr>
                                                <td>${phone.type}</td>
                                                <td>${phone.number}</td>
                                            </tr>
                                        </c:forEach>
                                    </c:if>

                                    <c:set var="homeAddress" value="${account.homeAddress}"/>
                                    <c:if test="${not empty homeAddress}">
                                        <tr>
                                            <td>Home address:</td>
                                            <td>${homeAddress}</td>
                                        </tr>
                                    </c:if>

                                    <c:set var="workAddress" value="${account.workAddress}"/>
                                    <c:if test="${not empty workAddress}">
                                        <tr>
                                            <td>Work address:</td>
                                            <td>${workAddress}</td>
                                        </tr>
                                    </c:if>

                                    <c:set var="icq" value="${account.icq}"/>
                                    <c:if test="${not empty icq}">
                                        <tr>
                                            <td>icq:</td>
                                            <td>${icq}</td>
                                        </tr>
                                    </c:if>

                                    <c:set var="skype" value="${account.skype}"/>
                                    <c:if test="${not empty skype}">
                                        <tr>
                                            <td>skype:</td>
                                            <td>${skype}</td>
                                        </tr>
                                    </c:if>

                                    <c:set var="additionalInfo" value="${account.additionalInfo}"/>
                                    <c:if test="${not empty additionalInfo}">
                                        <tr>
                                            <td>Additional info:</td>
                                            <td>${additionalInfo}</td>
                                        </tr>
                                    </c:if>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-right">
                            <div class="well">
                                <p>
                                    Lorem ipsum dolor sit amet, <strong>consectetur adipiscing elit</strong>.
                                    Aliquam eget
                                    sapien
                                    sapien. Curabitur in metus urna. In hac habitasse platea dictumst. Phasellus
                                    eu sem sapien,
                                    sed
                                    vestibulum velit. Nam purus nibh, lacinia non faucibus et, pharetra in
                                    dolor. Sed iaculis
                                    posuere diam ut cursus. <em>Morbi commodo sodales nisi id sodales. Proin
                                    consectetur, nisi
                                    id
                                    commodo imperdiet, metus nunc consequat lectus, id bibendum diam velit et
                                    dui.</em> Proin
                                    massa
                                    magna, vulputate nec bibendum nec, posuere nec lacus.
                                    <small>Aliquam mi erat, aliquam vel luctus eu, pharetra quis elit. Nulla
                                        euismod ultrices
                                        massa,
                                        et feugiat ipsum consequat eu.
                                    </small>
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </c:otherwise>
</c:choose>
</body>
</html>
