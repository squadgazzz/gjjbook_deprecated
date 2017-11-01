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
    <script src="<c:url value="/js/editAccount.js" />"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.mask/1.14.11/jquery.mask.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/1000hz-bootstrap-validator/0.11.9/validator.min.js"></script>
    <link rel="stylesheet" href="<c:url value="/css/body.css" />">
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
        <form class="form-inline" id="submit" role="form" data-toggle="validator" name="main_form" method="post"
              action="<c:url value="/updateaccount"/>" enctype="multipart/form-data">
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


                            <div class="row">
                                <input type="hidden" name="id" value="${account.id}">
                                <div class="input-group">
                                    <span class="input-group-addon">Name</span>
                                    <input class="form-control" id="name" type="text" name="name"
                                           value="${account.name}" data-error="Name is required" required="required"
                                           pattern="^[_A-z]{1,}$" data-pattern-error="Only A-Z allowed"
                                           data-maxlength="40"/>
                                    <span class="input-group-btn">
                                            <button class="btn btn-link" type="button">GO</button>
                                        </span>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>

            </div>



            <%--<div class="row">--%>
            <%--<div class="form-group col-sm-3">--%>
            <%--<div class="input-group"> --%>
            <%--<span class="input-group-addon"><i class="fa fa-envelope-o fa-fw"></i></span>--%>

            <%--<input type="email" class="form-control" placeholder="Enter email" />--%>
            <%--</div>--%>
            <%--</div>--%>
            <%--<div class="form-group col-sm-3">--%>
            <%--<div class="input-group"> <span class="input-group-addon"><i class="fa fa-key fa-fw"></i></span>--%>

            <%--<input type="password" class="form-control" placeholder="Choose password" />--%>
            <%--</div>--%>
            <%--</div>--%>
            <%--<button type="submit" class="btn btn-default">Create account</button>--%>
            <%--</div>--%>
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