<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x" crossorigin="anonymous">

    <title>lab6</title>
</head>
<body>
    <div style="text-align: center;">
        <h1>Countries</h1>
        <h2>
            <a href="${pageContext.request.contextPath}/CountryNew">Add New Country</a>
            &nbsp;&nbsp;&nbsp;
            <a href="${pageContext.request.contextPath}/CountryList">List All Countries</a>
             &nbsp;&nbsp;&nbsp;
            <a href="index.html">Back</a>
        </h2>
    </div>
    <div align="center">
        <table border="1" cellpadding="5" class="table">
            <caption><h2>List of Countries</h2></caption>
            <tr>
                <th scope="col">ID</th>
                <th scope="col">Name</th>
                <th scope="col">Actions</th>
            </tr>
            <c:forEach var="country" items="${listCountry}">
                <tr>
                    <td><c:out value="${country.id}" /></td>
                    <td><c:out value="${country.name}" /></td>
                    <td>
                        <a href="${pageContext.request.contextPath}/CountryEdit?id=<c:out value='${country.id}' />">Edit</a>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <a href="${pageContext.request.contextPath}/CountryDelete?id=<c:out value='${country.id}' />">Delete</a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>   
</body>
</html>