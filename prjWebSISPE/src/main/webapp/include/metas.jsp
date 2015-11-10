<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<% long id = (new java.util.Date()).getTime(); %>

<head>
	<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <meta http-equiv="Content-Style-Type" content="text/css">
    <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
    <meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
    <meta HTTP-EQUIV="max-age" CONTENT="0">
    <meta HTTP-EQUIV="Expires" CONTENT="0">
    <script language="JavaScript" src="js/menu/JSCookMenu.js" type="text/javascript"></script>
    <script language="JavaScript" src="js/menu/themeOffice.js" type="text/javascript"></script> 
    <script language="JavaScript" src="js/CalendarPopup.js" type="text/javascript"></script>
    <script language="JavaScript" src="js/util/util.js" type="text/javascript"></script>
	
	
 	<script language="JavaScript" src="js/prototype.js" type="text/javascript"></script> 
	
<!--    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script> -->
    
<!--    <script src="https://ajax.googleapis.com/ajax/libs/prototype/1.7.2.0/prototype.js"></script> -->
    <script language="JavaScript" src="js/util/effects.js" type="text/javascript"></script>
    <script language="JavaScript" src="js/util/splitter.js" type="text/javascript"></script>
    <script language="JavaScript" src="js/util/accordion.js" type="text/javascript"></script>
    
 
	<script type="text/javascript" src="js/jquery-1.4/jquery-1.4.2.min.js"></script>
	<script type="text/javascript" src="js/jquery-1.4/jquery.ui.core.min.js"/></script>
	<script type="text/javascript" src="js/jquery-1.4/jquery.ui.widget.min.js"></script>
	<script type="text/javascript" src="js/jquery-1.4/jquery.ui.mouse.min.js"></script>
	<script type="text/javascript" src="js/jquery-1.4/jquery.ui.draggable.min.js"></script>
	<script type="text/javascript" src="js/jquery-1.4/jquery.layout-latest.min.js"></script>
	<script type="text/javascript" src="js/jquery-1.4/jquery.ui.accordion.min.js"></script>
    
    <script language="JavaScript" src="js/ajax.js" type="text/javascript"></script>
    <script language="JavaScript" src="js/calendarizacion.js" type="text/javascript"></script>
    
    <link href="css/textos.css" rel="stylesheet" type="text/css">
    <link href="css/componentes.css" rel="stylesheet" type="text/css">
    <link href="css/estilosCalendarizacion.css" rel="stylesheet" type="text/css">
    <link href="css/estilosBotones.css" rel="stylesheet" type="text/css">
    <link href="js/menu/theme.css" rel="stylesheet" type="text/css">          
    <!-- solamente cuando se realiz&oacute; una cotizaci&oacute;n, recotizaci&oacute;n o reservaci&oacute;n -->
    <logic:notEmpty name="ec.com.smx.sic.sispe.transaccionRealizada">
        <logic:empty name="ec.com.smx.sic.sispe.imprimirConvenios">
        	<script language="Javascript">
				function imprimeSeleccion(nombreId){
				 imprimirReportesTxt(nombreId,'reportes/cotizacion_reservacion/rptPedidoTexto.jsp?id=<%=id%>');
				}
			</script> 
        	<!--  <link rel=alternate media=print href="reportes/cotizacion_reservacion/rptPedidoTexto.jsp?id=<%=id%>"> -->
        </logic:empty>
   	 	<logic:notEmpty name="ec.com.smx.sic.sispe.imprimirConvenios">
			<bean:define id="valorImpresion" name="ec.com.smx.sic.sispe.imprimirConvenios" /> 
				
			<c:if test="${valorImpresion == 'imprimirConvenioL'}">
				<script language="Javascript">
					function imprimeSeleccion(nombreId){
						imprimirReportesTxt(nombreId,'reportes/estadoDetallePedido/imprimirConvenio.jsp?id=<%=id%>');
					}
				</script>
				<!--<link rel="alternate" media="print" href="reportes/estadoDetallePedido/imprimirConvenio.jsp?id=<%=id%>">-->
			</c:if>
			
			<c:if test="${valorImpresion == 'imprimirConvenioM'}">
				<script language="Javascript">
					function imprimeSeleccion(nombreId){
						imprimirReportesTxt(nombreId,'reportes/estadoDetallePedido/imprimirConvenioMatricial.jsp?id=<%=id%>');
					}
				</script>
				<!--<link rel="alternate" media="print" href="reportes/estadoDetallePedido/imprimirConvenioMatricial.jsp?id=<%=id%>">-->
			</c:if>
			
			<c:remove scope="session" var="ec.com.smx.sic.sispe.imprimirConvenios"/>	
   	 	</logic:notEmpty>
    </logic:notEmpty>
    <!-- cuando se ingresa para ver el detalle de un pedido en un estado determinado -->
    <logic:notEmpty name="ec.com.smx.sic.sispe.enVistaDetallePedidoDTO">
   	 	<logic:empty name="ec.com.smx.sic.sispe.imprimirConvenios">
        	<script language="Javascript">
				function imprimeSeleccion(nombreId){
					imprimirReportesTxt(nombreId,'reportes/estadoDetallePedido/rptEstadoDetallePedidoTexto.jsp?id=<%=id%>');
				}
			</script> 
        	<!--<link rel=alternate media=print href="reportes/estadoDetallePedido/rptEstadoDetallePedidoTexto.jsp?id=<%=id%>"> -->
        </logic:empty>
   	 	<logic:notEmpty name="ec.com.smx.sic.sispe.imprimirConvenios">
			<bean:define id="valorImpresion"
				name="ec.com.smx.sic.sispe.imprimirConvenios" /> 
				
			<c:if test="${valorImpresion == 'imprimirConvenioL'}">
				<script language="Javascript">
					function imprimeSeleccion(nombreId){
						imprimirReportesTxt(nombreId,'reportes/estadoDetallePedido/imprimirConvenio.jsp?id=<%=id%>');
					}
				</script>
				<!--<link rel="alternate" media="print" href="reportes/estadoDetallePedido/imprimirConvenio.jsp?id=<%=id%>">-->
			</c:if>
			
			<c:if test="${valorImpresion == 'imprimirConvenioM'}">
				<script language="Javascript">
					function imprimeSeleccion(nombreId){
						imprimirReportesTxt(nombreId,'reportes/estadoDetallePedido/imprimirConvenioMatricial.jsp?id=<%=id%>');
					}
				</script>
				<!--<link rel="alternate" media="print" href="reportes/estadoDetallePedido/imprimirConvenioMatricial.jsp?id=<%=id%>">-->
			</c:if>
			
			<c:remove scope="session" var="ec.com.smx.sic.sispe.imprimirConvenios"/>	
   	 	</logic:notEmpty>
    </logic:notEmpty>
    <title>SISPE - Corporaci&oacute;n Favorita C.A.</title>
</head>