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
    <jsp:include page="/WEB-INF/jsp/header.jsp"/>

    <script src="<c:url value="/webjars/jquery/3.2.1/jquery.min.js"/>"></script>
    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.mask/1.14.11/jquery.mask.min.js"></script>
    <script src="<c:url value="/webjars/bootstrap/3.3.7/js/bootstrap.min.js"/>"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/1000hz-bootstrap-validator/0.11.9/validator.min.js"></script>
    <script src="<c:url value="/js/editAccount.js" />"></script>

    <link rel="stylesheet" href="<c:url value="/css/body.css" />">
</head>
<body>
<div class="fixed-centered container">
    <div class="row row-top">
        <form class="form-horizontal" id="submit" role="form" data-toggle="validator" name="main_form" method="post"
              action="<c:url value="/account_registration"/>">
            <div class="col-xs-2">
            </div>
            <div class="col-xs-7">
                <div class="row">
                    <div class="col-xs-12 col-right">
                        <div class="well">
                            <div class="form-group has-feedback">
                                <label class="control-label col-sm-3" for="email">Email*:</label>
                                <div class="col-sm-9">
                                    <input class="form-control" id="email" type="email" name="email"
                                           placeholder="email@domain.xxx" required="required" data-maxlength="30">
                                    <span class="glyphicon form-control-feedback" aria-hidden="true"></span>
                                    <div class="help-block with-errors"></div>
                                </div>
                            </div>

                            <div class="form-group has-feedback">
                                <label class="control-label col-sm-3" for="password">Password*:</label>
                                <div class="col-sm-9">
                                    <input class="form-control" id="password" type="password" name="password"
                                           placeholder="Minimum 6 symbols" required="required" data-minlength="6"
                                           data-maxlenmgth="40">
                                    <span class="glyphicon form-control-feedback" aria-hidden="true"></span>
                                    <div class="help-block with-errors"></div>
                                </div>
                            </div>

                            <div class="form-group has-feedback">
                                <label class="control-label col-sm-3" for="name">Name*:</label>
                                <div class="col-sm-9">
                                    <input class="form-control" id="name" type="text" name="name"
                                           placeholder="Name" data-error="Name is required" required="required"
                                           pattern="^[_A-z\s-]{1,}$" data-pattern-error="Only A-Z allowed"
                                           data-maxlength="40">
                                    <span class="glyphicon form-control-feedback" aria-hidden="true"></span>
                                    <div class="help-block with-errors"></div>
                                </div>
                            </div>

                            <div class="form-group has-feedback">
                                <label class="control-label col-sm-3" for="middleName">Middle Name:</label>
                                <div class="col-sm-9">
                                    <input class="form-control" id="middleName" type="text" name="middleName"
                                           placeholder="Middle name (optional)" pattern="^[_A-z\s-]{1,}$"
                                           data-pattern-error="Only A-Z allowed" data-maxlength="40">
                                    <span class="glyphicon form-control-feedback" aria-hidden="true"></span>
                                    <div class="help-block with-errors"></div>
                                </div>
                            </div>

                            <div class="form-group has-feedback">
                                <label class="control-label col-sm-3" for="surName">Surname*:</label>
                                <div class="col-sm-9">
                                    <input class="form-control" id="surName" type="text" name="surName"
                                           placeholder="Surname" data-error="Surname is required"
                                           required="required" pattern="^[_A-z\s-]{1,}$"
                                           data-pattern-error="Only A-Z allowed" data-maxlength="40">
                                    <span class="glyphicon form-control-feedback" aria-hidden="true"></span>
                                    <div class="help-block with-errors"></div>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="control-label col-sm-3">Gender*:</label>
                                <div class="col-sm-9">
                                    <input type="radio" name="gender" value="MALE" checked> Male
                                    <input type="radio" name="gender" value="FEMALE"> Female
                                </div>
                            </div>

                            <div class="form-group has-feedback">
                                <label class="control-label col-sm-3" for="birthDate">Birth date*:</label>
                                <div class="col-sm-3">
                                    <input class="form-control" id="birthDate" type="text" name="birthDate"
                                           placeholder="YYYY-mm-DD" required="required"
                                           data-minlength="10" data-maxlength="10">
                                    <span class="glyphicon form-control-feedback" aria-hidden="true"></span>
                                    <div class="help-block with-errors"></div>
                                </div>
                            </div>
                            <div class="form-group phone has-feedback">
                                <label class="col-sm-3">
                                    <select style="padding-right: 0;" class="form-control"
                                            name="phones[0].type">
                                        <c:forEach var="phoneType" items="<%= PhoneType.values() %>">
                                            <option value="${phoneType}">
                                                    ${phoneType}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </label>

                                <div class="col-sm-6">
                                    <input class="form-control" type="text" name="phones[0].number"
                                           placeholder="+7(999)999-99-99" required="required" data-minlength="18"
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

                            <input type="button" onclick="validateForm()" name="confirm-changes" value="Register account"
                                   class="btn btn-block"><br>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-xs-3">
            </div>
        </form>
    </div>
    <div id="confirm_modal" class="modal fade" role="dialog">
        <div class="modal-dialog modal-sm">
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Confirm registration</h4>
                </div>
                <div class="modal-body">
                    <p>Are you sure?</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-success" name="confirm-modal" data-confirm="modal">Yes</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
