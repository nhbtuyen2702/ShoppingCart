<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Product List</title>
<style type="text/css">
  <%@include file="css/styles.css" %> 
</style>

</head>
<body>
	<jsp:include page="_header.jsp" />
	<jsp:include page="_menu.jsp" />

	<fmt:setLocale value="en_US" scope="session" />

	<div class="page-title">Product List</div>

	<c:forEach items="${paginationProductInfos.list}" var="productInfo">
		<div class="product-preview-container">
			<ul>
				<li>
					<img class="product-image" src="${contextPath}/productImage?code=${productInfo.code}" />
				</li>
				<li>Code: ${productInfo.code}</li>
				<li>Name: ${productInfo.name}</li>
				<li>Price: 
					<fmt:formatNumber value="${productInfo.price}"	type="currency" /><!-- $100 -->
				</li>
				<li>
					<a href="${contextPath}/buyProduct?code=${productInfo.code}">Buy Now</a>
				</li>
				<!-- For Manager edit Product -->
				<security:authorize access="hasRole('ROLE_MANAGER')">
					<li>
						<a style="color: red;"	href="${contextPath}/product?code=${productInfo.code}">Edit Product</a>
					</li>
				</security:authorize>
			</ul>
		</div>

	</c:forEach>
	<br />

	<c:if test="${paginationProductInfos.totalPages > 1}">
		<div class="page-navigator">
		
			<c:forEach items="${paginationProductInfos.navigationPages}" var="page"><!-- 1 2 3 -->
				<c:if test="${page != -1 }">
					<a href="productList?page=${page}" class="nav-item">${page}</a><!-- productList?page=1 --><!-- productList?page=2 --> <!-- productList?page=3 -->
				</c:if>
				<c:if test="${page == -1 }">
					<span class="nav-item"> ... </span>
				</c:if>
			</c:forEach>

		</div>
	</c:if>

	<jsp:include page="_footer.jsp" />
	
</body>
</html>