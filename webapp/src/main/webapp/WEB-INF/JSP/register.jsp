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
                            <td><input type="text" name="name" placeholder="Name" required></td>
                        </tr>
                        <tr>
                            <td>Middle name:</td>
                            <td><input type="text" name="middle_name" placeholder="Middle name"></td>
                        </tr>
                        <tr>
                            <td>Surname:</td>
                            <td><input type="text" name="surname" placeholder="Surname" required></td>
                        </tr>
                        <tr>
                            <td><input type="radio" name="gender" checked value="MALE"> Male
                                <input type="radio" name="gender" value="FEMALE"> Female
                            </td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>Birth date:</td>
                            <td><input type="date" name="birth_date" required></td>
                        </tr>
                        <tr>
                            <td>
                                <select name="phone_type">
                                    <c:forEach var="type" items="<%= PhoneType.values() %>">
                                        <option <c:if test="${type=='MOBILE'}"> selected</c:if>
                                                value="${type}"> ${type} </option>
                                    </c:forEach>
                                </select>
                                phone:
                            </td>
                            <td>
                                <input type="text" name="phone" placeholder="X-XXX-XXX-XX-XX">
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
<%--<div class="column-left"></div>
<div class="column-center">
    <h2>Register form</h2>
    <form action="/account_registration" method="post">
        Name: <input type="text" name="name" required>
        <br/>
        Middle name: <input type="text" name="middle_name">
        <br/>
        Surname: <input type="text" name="surname" required>
        <br/>
        <input type="radio" checked name="gender" value="MALE"> Male
        <input type="radio" checked name="gender" value="FEMALE"> Female
        <br/>
        Birth date: <input type="date" name="birth_date" required>
        <br/>
        <select name="phone_type">
            <c:forEach var="type" items="<%= PhoneType.values()%>">
                <option value="${type}">${type}</option>
            </c:forEach>
        </select> phone: <input type="number" name="phone">
        <br/>
        <select name="phone_type">
            <c:forEach var="type" items="<%= PhoneType.values()%>">
                <option value="${type}">${type}</option>
            </c:forEach>
        </select> phone: <input type="number" name="phone">
        <br/>
        <select name="phone_type">
            <c:forEach var="type" items="<%= PhoneType.values()%>">
                <option value="${type}">${type}</option>
            </c:forEach>
        </select> phone: <input type="number" name="phone">
        <br/>
        Home address: <input type="text" name="home_address">
        <br/>
        Work address: <input type="text" name="work_address">
        <br/>
        email: <input type="email" name="email" required>
        <br/>
        icq: <input type="number" name="icq">
        <br/>
        skype: <input type="text" name="skype">
        <br/>
        Additional info: <textarea name="additional_info" rows="10" cols="30"></textarea>
        <br/>
        Password: <input type="password" name="password" required>
        <br/>
        <input type="submit" value="Confirm">
    </form>
</div>
<div class="column-right"></div>--%>
</body>
</html>
