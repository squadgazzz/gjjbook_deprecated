<%@ page import="com.gjjbook.domain.PhoneType" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: IZhavoronkov
  Date: 19.07.2017
  Time: 17:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="/WEB-INF/JSP/header.jsp"/>
    <style>
        <%@include file="/CSS/body.css" %>
    </style>
</head>
<body>
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
                        <form method="post" action="/updateavatar" enctype="multipart/form-data">
                            Select a file: <input type="file" name="avatar">
                            <input type="submit" class="btn btn-block" value="Upload">
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xs-7">
            <div class="row">
                <div class="col-xs-12 col-right">
                    <div class="well">
                        <form method="post" action="/updateaccount">
                            <input type="hidden" name="id" value="${account.id}">
                            <table class="table">
                                <tbody>
                                <tr>
                                    <td>Name:</td>
                                    <td><input type="text" name="name" value="${account.name}" required></td>
                                </tr>
                                <tr>
                                    <td>Middle name:</td>
                                    <td><input type="text" name="middle_name" value="${account.middleName}"></td>
                                </tr>
                                <tr>
                                    <td>Surname:</td>
                                    <td><input type="text" name="surname" value="${account.surName}" required></td>
                                </tr>
                                <tr>
                                    <td>Work address:</td>
                                    <td><input type="text" name="work_address" value="${account.workAddress}"></td>
                                </tr>
                                <tr>
                                    <td><input type="radio" name="gender"
                                               <c:if test="${account.sex=='MALE'}">checked</c:if> value="MALE"> Male
                                        <input type="radio" name="gender"
                                               <c:if test="${account.sex=='FEMALE'}">checked</c:if> value="FEMALE">
                                        Female
                                    </td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td>Birth date:</td>
                                    <td><input type="date" name="birth_date" value="${account.birthDate}" required></td>
                                </tr>
                                <c:forEach var="phone" items="${account.phones}">
                                    <c:if test="${not empty phone}">
                                        <tr>
                                            <td>
                                                <select name="phone_type">
                                                    <c:forEach var="type" items="<%= PhoneType.values() %>">
                                                        <option id="${phone.id}"
                                                                <c:if test="${phone.type == type}">
                                                                    selected
                                                                </c:if>
                                                                value="${type}">
                                                                ${type}
                                                        </option>
                                                    </c:forEach>
                                                </select>
                                                phone:
                                            </td>
                                            <td>
                                                <input type="text" name="phone" id="${phone.id}"
                                                       value="${phone.number}">
                                            </td>
                                        </tr>
                                    </c:if>
                                </c:forEach>
                                <tr>
                                    <td>Home address:</td>
                                    <td>${account.additionalInfo}</td>
                                </tr>
                                <tr>
                                    <td>Work address:</td>
                                    <td><input type="text" name="work_address" value="${account.workAddress}"></td>
                                </tr>
                                <tr>
                                    <td>ICQ:</td>
                                    <td><input type="number" name="icq" value="${account.icq}"></td>
                                </tr>
                                <tr>
                                    <td>Skype:</td>
                                    <td><input type="text" name="skype" value="${account.skype}"></td>
                                </tr>
                                <tr>
                                    <td>Additional info:</td>
                                    <td><textarea name="additional_info" rows="10"
                                                  cols="30">${account.additionalInfo}</textarea></td>
                                </tr>
                                <tr>
                                    <td>Email:</td>
                                    <td><input type="email" name="email" value="${account.email}" required></td>
                                </tr>
                                <tr>
                                    <td>Password:</td>
                                    <td><input type="password" name="password" value="${password}"
                                               required></td>
                                </tr>
                                </tbody>
                            </table>
                            <input type="submit" value="Confirm changes" class="btn btn-block">
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
