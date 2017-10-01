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
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.mask/1.14.11/jquery.mask.min.js"></script>
    <script>
        <%@include file="/JS/editAccount.js"%>
    </script>
</head>
<body>
<div class="fixed-centered container">
    <div class="row row-top">
        <form name="main_form" method="post" action="/updateaccount" enctype="multipart/form-data">
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
                            <input type="file" name="avatar" class="file">
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-xs-7">
                <div class="row">
                    <div class="col-xs-12 col-right">
                        <div class="well">
                            <input type="hidden" name="id" value="${account.id}">
                            <table id="main_table" class="table">
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
                                        <tr class="phone_row">
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
                                                <button type="button" onclick="removeButtonClick(this)"
                                                        name="remove_button" class="btn btn-danger">
                                                    <span class="glyphicon glyphicon-minus"></span>
                                                </button>
                                                <button type="button" onclick="addButtonClick()" name="add_button"
                                                        class="btn btn-success" style="display: none">
                                                    <span class="glyphicon glyphicon-plus"></span>
                                                </button>
                                            </td>
                                        </tr>
                                    </c:if>
                                </c:forEach>

                                <tr>
                                    <td>Home address:</td>
                                    <td><input type="text" name="home_address" value="${account.homeAddress}"></td>
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
                                    <td><input type="password" name="password" value=""
                                               required></td>
                                </tr>
                                </tbody>
                            </table>
                            <input type="button" data-toggle="modal" data-target="#confirm_modal"
                                   value="Confirm changes" class="btn btn-block">
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </div>
    <div id="confirm_modal" class="modal fade" role="dialog">
        <div class="modal-dialog modal-sm">
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Confirm changes</h4>
                </div>
                <div class="modal-body">
                    <p>Are you sure?</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-success" data-confirm="modal">Yes</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
