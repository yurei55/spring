<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>도서 목록</title>
<link href="<c:url value="/resources/css/bootstrap.css"/>" rel="stylesheet">
</head>
<body>
  <nav class="navbar navbar-expand navbar-dark bg-dark">
    <div class="container">
      <div class="navbar-header">
        <a class="navbar-brand" href="./home">Home</a>
      </div>
    </div>
  </nav>
  <div class="jumbotron">
    <div class="container">
      <h1 class="display-3">도서 목록</h1>
    </div>
  </div>
  <div class="container">
    <div class="row" align="center">
      <c:forEach items="${bookList}" var="book">
        <div class="col-md-4">
          <img src="<c:url value="/bookimage/${book.fileName}"/>" style="width: 60%" />
          <h3>${book.name}</h3>
          <p>${book.author}
          <br> ${book.publisher} | ${book.releaseDate}
          <p align=left>${fn:substring(book.description, 0, 100)}...
          <p>${book.unitPrice}원
          <p><a href="<c:url value="/books/book?id=${book.bookId}"/>" class="btn btn-Secondary" role="button">상세정보 &raquo;</a></p>
        </div>
      </c:forEach>
    </div>
    <hr>
    <footer>
      <p>&copy; BookMarket</p>
    </footer>
  </div>
</body>
</html>
