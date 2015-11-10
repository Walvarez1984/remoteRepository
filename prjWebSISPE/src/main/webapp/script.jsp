<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<% String mainId = "WIN_MAIN_"+(new java.util.Date()).getTime(); %>
<script>
	principal = window.open('menuPrincipal.do?id=<%=mainId%>','<%=mainId%>','menubar=no,directories=no,location=no,toolbar=no,scrollbars=yes,titlebar=yes,top=0,left=0,height=685,width=1024,resizable=yes,status=yes');
</script>
<tiles:insert page="login.jsp"/>