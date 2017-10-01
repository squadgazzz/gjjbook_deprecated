<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: IZhavoronkov
  Date: 20.07.2017
  Time: 14:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="/WEB-INF/JSP/header.jsp"/>
    <style>
        <%@include file="/CSS/body.css" %>
        img.back {
            position: absolute;
            margin: auto;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
        }
    </style>
</head>
<body>
<img class="back" src="<c:url value="/IMG/background.png"/>"/>
<div class="fixed-centered container">
    <div class="row row-top">
        <div class="col-xs-3">
        </div>
        <div class="col-xs-6" style="opacity: 0.8">
            <div class="well">
                <form method="post" action="login_authenticate">
                    <label>Email:</label>
                    <input class="form-control" type="email" name="email" placeholder="example@domain.com"/><br/>
                    <label>Password:</label>
                    <input class="form-control" type="password" name="password" placeholder="Password"/><br/>
                    <input type="submit" value="Login" class="btn btn-block"/> Remember me? <input type="checkbox"
                                                                                                   name="rememberMe"
                                                                                                   value="remember">
                    <span style="color: red">
                        <c:if test="${not empty requestScope.errMsg}">
                            <c:out value="${requestScope.errMsg}"/>
                        </c:if>
                    </span>
                </form>
                <label>Not registered?</label>
                <form method="post" action="register">
                    <input type="submit" value="Register" class="btn btn-block"/>
                </form>
            </div>
        </div>
        <div class="col-xs-3">
        </div>
    </div>
</div>
</body>
</html>
