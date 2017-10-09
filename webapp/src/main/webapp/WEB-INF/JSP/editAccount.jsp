<%@ page import="com.gjjbook.domain.PhoneType" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
    <%--сделать кеширование css\js, проверить, что это будет работать через томкат из cmd--%>
    <jsp:include page="/WEB-INF/JSP/header.jsp"/>
    <script src="<c:url value="/JS/editAccount.js" />"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.mask/1.14.11/jquery.mask.min.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/1000hz-bootstrap-validator/0.11.9/validator.min.js"></script>
    <link rel="stylesheet" href="<c:url value="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css"/>">
    <link rel="stylesheet" href="<c:url value="/CSS/body.css" />">
</head>
<body>
<div class="fixed-centered container">
    <div class="row row-top">
        <form class="form-horizontal" id="submit" role="form" data-toggle="validator" name="main_form" method="post"
              action="/updateaccount" enctype="multipart/form-data">
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
                            <input type="file" id="avatar" name="avatar" class="file">
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-xs-7">
                <div class="row">
                    <div class="col-xs-12 col-right">
                        <div class="well">
                            <input type="hidden" id="id" name="id" value="${account.id}">
                            <div class="form-group has-feedback">
                                <label class="control-label col-sm-3" for="name">Name*:</label>
                                <div class="col-sm-9">
                                    <input class="form-control" id="name" type="text" name="name"
                                           value="${account.name}" data-error="Name is required" required="required"
                                           pattern="^[_A-z]{1,}$" data-pattern-error="Only A-Z allowed"
                                           data-maxlength="40">
                                    <span class="glyphicon form-control-feedback" aria-hidden="true"></span>
                                    <div class="help-block with-errors"></div>
                                </div>
                            </div>

                            <div class="form-group has-feedback">
                                <label class="control-label col-sm-3" for="middleName">Middle Name:</label>
                                <div class="col-sm-9">
                                    <input class="form-control" id="middleName" type="text" name="middleName"
                                           value="${account.middleName}" pattern="^[_A-z]{1,}$"
                                           data-pattern-error="Only A-Z allowed" data-maxlength="40">
                                    <span class="glyphicon form-control-feedback" aria-hidden="true"></span>
                                    <div class="help-block with-errors"></div>
                                </div>
                            </div>

                            <div class="form-group has-feedback">
                                <label class="control-label col-sm-3" for="surName">Surname*:</label>
                                <div class="col-sm-9">
                                    <input class="form-control" id="surName" type="text" name="surName"
                                           value="${account.surName}" data-error="Surname is required"
                                           required="required" pattern="^[_A-z]{1,}$"
                                           data-pattern-error="Only A-Z allowed" data-maxlength="40">
                                    <span class="glyphicon form-control-feedback" aria-hidden="true"></span>
                                    <div class="help-block with-errors"></div>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="control-label col-sm-3" for="surname">Gender*:</label>
                                <div class="col-sm-9">
                                    <input type="radio" name="sex"
                                           <c:if test="${account.sex=='MALE'}">checked</c:if> value="MALE"> Male
                                    <input type="radio" name="sex"
                                           <c:if test="${account.sex=='FEMALE'}">checked</c:if> value="FEMALE">
                                    Female
                                </div>
                            </div>

                            <div class="form-group has-feedback">
                                <label class="control-label col-sm-3" for="birthDate">Birth date*:</label>
                                <div class="col-sm-3">
                                    <input class="form-control" id="birthDate" type="text" name="birthDate"
                                           value="${account.birthDate}" required="required"
                                           data-minlength="10" data-maxlength="10">
                                    <span class="glyphicon form-control-feedback" aria-hidden="true"></span>
                                    <div class="help-block with-errors"></div>
                                </div>
                            </div>

                            <c:forEach var="phone" items="${account.phones}">
                                <c:if test="${not empty phone}">
                                    <div class="form-group phone has-feedback">
                                        <label class="col-sm-3">
                                            <select style="padding-right: 0;" id="type" class="form-control"
                                                    name="type">
                                                <c:forEach var="phoneType" items="<%= PhoneType.values() %>">
                                                    <option id="${phone.id}"
                                                            <c:if test="${phone.type == phoneType}">
                                                                selected
                                                            </c:if>
                                                            value="${phoneType}">
                                                            ${phoneType}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </label>

                                        <div class="col-sm-6">
                                            <input class="form-control" type="text" name="number" id="${phone.id}"
                                                   value="${phone.number}" required="required" data-minlength="18"
                                                   data-maxlength="18" data-pattern-error="Only digits. Length 10.">
                                            <span class="glyphicon form-control-feedback" aria-hidden="true"></span>
                                            <div class="help-block with-errors"></div>
                                        </div>
                                        <div class="col-sm-1">
                                            <button type="button" onclick="removeButtonClick(this)"
                                                    name="remove_button" class="btn btn-link">
                                                <span class="glyphicon glyphicon-minus"></span>
                                            </button>
                                        </div>
                                        <div class="col-sm-1">
                                            <button type="button" onclick="addButtonClick()" name="add_button"
                                                    class="btn btn-link" style="display: none">
                                                <span class="glyphicon glyphicon-plus"></span>
                                            </button>
                                        </div>
                                    </div>
                                </c:if>
                            </c:forEach>

                            <div class="form-group has-feedback">
                                <label class="control-label col-sm-3" for="homeAddress">Home address:</label>
                                <div class="col-sm-9">
                                    <input class="form-control" id="homeAddress" type="text" name="homeAddress"
                                           value="${account.homeAddress}" data-maxlength="100">
                                    <span class="glyphicon form-control-feedback" aria-hidden="true"></span>
                                    <div class="help-block with-errors"></div>
                                </div>
                            </div>

                            <div class="form-group has-feedback">
                                <label class="control-label col-sm-3" for="workAddress">Work address:</label>
                                <div class="col-sm-9">
                                    <input class="form-control" id="workAddress" type="text" name="workAddress"
                                           value="${account.workAddress}" data-maxlength="100">
                                    <span class="glyphicon form-control-feedback" aria-hidden="true"></span>
                                    <div class="help-block with-errors"></div>
                                </div>
                            </div>

                            <div class="form-group has-feedback">
                                <label class="control-label col-sm-3" for="icq">ICQ:</label>
                                <div class="col-sm-9">
                                    <input class="form-control" id="icq" type="number" name="icq"
                                           value="${account.icq}" data-maxlength="10">
                                    <span class="glyphicon form-control-feedback" aria-hidden="true"></span>
                                    <div class="help-block with-errors"></div>
                                </div>
                            </div>

                            <div class="form-group has-feedback">
                                <label class="control-label col-sm-3" for="skype">Skype:</label>
                                <div class="col-sm-9">
                                    <input class="form-control" id="skype" type="text" name="skype"
                                           value="${account.skype}" data-maxlength="30">
                                    <span class="glyphicon form-control-feedback" aria-hidden="true"></span>
                                    <div class="help-block with-errors"></div>
                                </div>
                            </div>

                            <div class="form-group has-feedback">
                                <label class="control-label col-sm-3" for="additionalInfo">Additional info:</label>
                                <div class="col-sm-9">
                                    <textarea rows="10" class="form-control" id="additionalInfo" name="additionalInfo"
                                              data-maxlength="300">${account.additionalInfo}</textarea>
                                    <span class="glyphicon form-control-feedback" aria-hidden="true"></span>
                                    <div class="help-block with-errors"></div>
                                </div>
                            </div>

                            <div class="form-group has-feedback">
                                <label class="control-label col-sm-3" for="email">Email*:</label>
                                <div class="col-sm-9">
                                    <input class="form-control" id="email" type="email" name="email"
                                           value="${account.email}" required="required" data-maxlength="30">
                                    <span class="glyphicon form-control-feedback" aria-hidden="true"></span>
                                    <div class="help-block with-errors"></div>
                                </div>
                            </div>

                            <div class="form-group has-feedback">
                                <label class="control-label col-sm-3" for="password">Password*:</label>
                                <div class="col-sm-9">
                                    <input class="form-control" id="password" type="password" name="password"
                                           value="${account.password}" required="required" data-minlength="6"
                                           data-maxlenmgth="40">
                                    <span class="glyphicon form-control-feedback" aria-hidden="true"></span>
                                    <div class="help-block with-errors"></div>
                                </div>
                            </div>

                            <input type="button" onclick="validateForm()" value="Confirm changes" class="btn btn-block">

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