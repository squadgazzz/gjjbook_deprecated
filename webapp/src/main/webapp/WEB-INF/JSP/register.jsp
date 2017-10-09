<%@ page import="com.gjjbook.domain.PhoneType" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: IZhavoronkov
  Date: 27.07.2017
  Time: 11:14
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
        <div class="col-xs-3">
        </div>
        <div class="col-xs-6">
            <div class="well">
                <form method="post" action="/account_registration" enctype="multipart/form-data">
                    <table class="table">
                        <tbody>
                        <tr>
                            <td>Email:</td>
                            <td><input type="email" name="email" placeholder="email@domain.com" required></td>
                        </tr>
                        <tr>
                            <td>Password:</td>
                            <td><input type="password" name="password" placeholder="******"
                                       required></td>
                        </tr>
                        <tr>
                            <td>Name:</td>
                            <td><input type="text" id="name" name="name" placeholder="Name" required></td>
                        </tr>
                        <tr>
                            <td>Middle name:</td>
                            <td><input type="text" id="middleName" name="middleMame" placeholder="Middle name"></td>
                        </tr>
                        <tr>
                            <td>Surname:</td>
                            <td><input type="text" id="surName" name="surName" placeholder="Surname" required></td>
                        </tr>
                        <tr>
                            <td><input type="radio" name="sex" checked value="MALE"> Male
                                <input type="radio" name="sex" value="FEMALE"> Female
                            </td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>Birth date:</td>
                            <td><input type="date" id="birthDate" name="birthDate" required></td>
                        </tr>
                        <tr>
                            <td>
                                <select id="type" name="type">
                                    <c:forEach var="phoneType" items="<%= PhoneType.values() %>">
                                        <option <c:if test="${phoneType=='MOBILE'}"> selected</c:if>
                                                value="${phoneType}"> ${phoneType} </option>
                                    </c:forEach>
                                </select>
                                phone:
                            </td>
                            <td>
                                <input type="text" id="number" name="number" placeholder="X-XXX-XXX-XX-XX">
                            </td>
                        </tr>
                        </tbody>
                    </table>
                    <input type="submit" value="Register" class="btn btn-block">
                </form>
            </div>
        </div>
        <div class="col-xs-3">
        </div>
    </div>

</div>
</body>
</html>
