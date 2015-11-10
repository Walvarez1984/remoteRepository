<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

	<bean:define id="valorImpresion"
		name="ec.com.smx.sic.sispe.imprimirConvenios" /> 
		
	<c:if test="${valorImpresion == 'imprimirConvenioL'}">
		<link rel="alternate" media="print" href="/reportes/estadoDetallePedido/imprimirConvenio.jsp?id=<%=(new java.util.Date()).getTime()%>">
	</c:if>
	
	<c:if test="${valorImpresion == 'imprimirConvenioM'}">
		<link rel="alternate" media="print" href="/reportes/estadoDetallePedido/imprimirConvenioMatricial.jsp?id=<%=(new java.util.Date()).getTime()%>">
	</c:if>
	
	<c:remove scope="session" var="ec.com.smx.sic.sispe.imprimirConvenios"/>	
	
