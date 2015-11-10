<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<% long id = (new java.util.Date()).getTime(); %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<meta name="GENERATOR" content="IBM Software Development Platform">
	<meta http-equiv="Content-Style-Type" content="text/css">
	<meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
	<meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
	<meta HTTP-EQUIV="max-age" CONTENT="0">
	<meta HTTP-EQUIV="Expires" CONTENT="0">
	<%--<link href="theme/Master.css" rel="stylesheet" type="text/css">--%>
	<script language="JavaScript" src="js/menu/JSCookMenu.js" type="text/javascript"></script>
	<script language="JavaScript" src="js/menu/themeOffice.js" type="text/javascript"></script>
	<script language="JavaScript" src="js/CalendarPopup.js" type="text/javascript"></script>
	<script language="JavaScript" src="js/util/util.js" type="text/javascript"></script>	
	<script language="JavaScript" src="js/prototype.js" type="text/javascript"></script>
	

	<script type="text/javascript" src="js/jquery-1.4/jquery-1.4.2.min.js"></script>
	<script type="text/javascript" src="js/jquery-1.4/jquery.ui.core.min.js"/></script>
	<script type="text/javascript" src="js/jquery-1.4/jquery.ui.widget.min.js"></script>
	<script type="text/javascript" src="js/jquery-1.4/jquery.ui.mouse.min.js"></script>
	<script type="text/javascript" src="js/jquery-1.4/jquery.ui.draggable.min.js"></script>
	<script type="text/javascript" src="js/jquery-1.4/jquery.layout-latest.min.js"></script>
	<script type="text/javascript" src="js/jquery-1.4/jquery.ui.accordion.min.js"></script>
	
	<script language="JavaScript" src="js/ajax.js" type="text/javascript"></script>
	
    
	<link href="css/textos.css" rel="stylesheet" type="text/css">
	<link href="css/componentes.css" rel="stylesheet" type="text/css">
	<link href="css/estilosCalendarizacion.css" rel="stylesheet" type="text/css">
	<link href="css/estilosBotones.css" rel="stylesheet" type="text/css">
	<link href="js/menu/theme.css" rel="stylesheet" type="text/css">
	<link href="js/jquery/css/jquery.cluetip.css" rel="stylesheet" type="text/css">
	
	<script>var $j = jQuery.noConflict();</script>	
	<script type="text/javascript">
			$j(document).keydown(function(e) {
				var element = e.target.nodeName.toLowerCase();
				if (element != 'input' && element != 'textarea' || ($j(e.target).hasClass('rf-cal-inp') || $j(e.target).hasClass('hasDatepicker') )) {
				    if (e.keyCode === 8) {
				        return false;
				    }
				}
			});
			document.oncontextmenu = new Function("return false");
	</script>
	
	<!-- cuando se ingresa para ver el detalle de un pedido en un estado determinado -->
    <logic:notEmpty name="ec.com.smx.sic.resumen.visible">
		<script language="Javascript">
			function imprimeSeleccion(nombreId){
				imprimirReportesTxt(nombreId,'pedidosEspeciales/creacion/rptPedidoTexto.jsp?id=<%=id%>');				
			}
		</script> 
        <!--<link rel=alternate media=print href="pedidosEspeciales/creacion/rptPedidoTexto.jsp?id=<%=id%>">-->
    </logic:notEmpty>
    <logic:empty name="ec.com.smx.sic.resumen.visible">
	    <logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido">
			<script language="Javascript">
				function imprimeSeleccion(nombreId){
					imprimirReportesTxt(nombreId,'pedidosEspeciales/detalleEstadoPedidoEspecial/rptPedidoTexto.jsp?id=<%=id%>');				
				}
			</script> 
	        <!--<link rel=alternate media=print href="pedidosEspeciales/detalleEstadoPedidoEspecial/rptPedidoTexto.jsp?id=<%=id%>">-->
	    </logic:notEmpty>
	</logic:empty>
    <logic:notEmpty name="ec.com.smx.sic.sispe.reporte.pedidoLocal">
    	<link rel=alternate media=print href="pedidosEspeciales/modificacionYDespacho/rptLocalPedido.jsp?id=<%=id%>">
    </logic:notEmpty>
        <logic:notEmpty name="ec.com.smx.sic.sispe.reporte.articulo">
    	<link rel=alternate media=print href="pedidosEspeciales/modificacionYDespacho/rptArticulo.jsp?id=<%=id%>">
    </logic:notEmpty>
	<%-- solamente cuando se realiz&oacute; una cotizaci&oacute;n, recotizaci&oacute;n o reservaci&oacute;n --%>	
	<title>SISPE - Corporaci&oacute;n Favorita C.A.</title>
</head>
<body onLoad="setHeightPage();">
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr height="5%">
        <td colspan="2">
            <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
	            <tr>
	                <td colspan="3" bgcolor="#D2010D">
	                    <table width="100%" border="0" cellpadding="0" cellspacing="0">
	                        <tr>
	                            <td width="50%" colspan="2" valign="top">
	                                <!-- TEXTO EMPRESA Y SISTEMA -->
	                                <table width="100%" border="0" cellpadding="0" cellspacing="0">
	                                    <tr>
	                                        <td height="35px" style="background:url(./images/rojos.gif)"><img src="./images/super.gif" ></td>
	                                    </tr>
	                                    <tr>
	                                        <td width="50%" class="WhiteSystem">
	                                            <%--&nbsp;<tiles:insert page="/include/currentSystem.jsp"/>--%>
	                                            <table width="100%" border="0" cellpadding="0"cellspacing="0">
	                                                <tr>
	                                                    <td width="58%">
	                                                        &nbsp; <tiles:insert page="/include/currentSystem.jsp"/>
	                                                    </td>
	                                                    <td valign="middle"  class="textoBlanco10" align="right" width="57%">
	                                                        Tiempo restante de sesión:&nbsp;
	                                                    </td>
	                                                </tr>
	                                            </table>
	                                        </td>
	                                    </tr>
	                                </table>
	                            </td>
	                            <td width="50%" height="100%" valign="top">
	                                <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" >
	                                    <tr>
	                                        <td rowspan="2" align="right" style="background:url(./images/rojos.gif)"><img src="./images/esquina.gif"></td>
	                                        <td width="42%" bgcolor="#F1F3F5" height="24">
	                                            <!-- BOTONES DE LA BARRA -->
	                                            <table width="100%" border="0" cellpadding="0" cellspacing="0">
	                                                <tr>
	                                                    <td width="60%"></td>
	                                                    <td width="20%" align="right">
	                                                        <div id="botonBg">
	                                                            <a class="ayudaBg" href="ayuda/ManualUsuarioSISPE.pdf" target="blank">Ayuda</a>
	                                                        </div>
	                                                    </td>
	                                                    <td width="20%" align="right" >
	                                                        <div id="botonB">
	                                                            <a class="salirB" href="logout.do">Cerrar sesion</a>
	                                                        </div>
	                                                    </td>
	                                                </tr>
	                                            </table>
	                                        </td>
	                                    </tr>
	                                    <tr>
	                                        <td style="background:url(./images/filobrillo.gif)" height="13"></td>
	                                    </tr>
	                                     <tr>
	                                        <td colspan="2" bgcolor="#D2010D" align="right" valign="top">
	                                            <table width="100%" border="0" cellpadding="0" cellspacing="0">
	                                                <tr>
	                                                     <td  width="16%" class="textoBlanco10" bgcolor="#D2010D"  align="left" valign="top">
	                                                       <div id="TimeLeft">
	                                                            <label id="TimeLeftLb"></label>
	                                                             &nbsp(mm/ss)
	                                                            <bean:define id="tiempoSesion">
	                                                                <%= request.getSession().getMaxInactiveInterval()/60%>
	                                                            </bean:define>
	                                                            <script language="javascript">
	                                                                showTimer(${tiempoSesion});
	                                                            </script>
	                                                        </div>
	                                                    </td>
	                                                    <td  class="WhiteMiddle" width="50%">
	                                                      <tiles:insert page="/include/welcomeUser.jsp"/>&nbsp;
	                                                    </td>
	                                                </tr> 
	                                            </table>
	                                        </td>
	                                    </tr>
	                                </table>				
	                            </td>
	                        </tr>
	                    </table>
	                </td>
	            </tr>
	            <tr>
	                <td bgcolor="#fe7279" height="3"></td>
	            </tr>
	            <tr>
	                <td width="100%" bgcolor="#F1F3F5"><tiles:insert page="/include/mainMenu.jsp"/></td>
	            </tr>
            </table>
        </td>
    </tr>
    <tr height="2%" class="tablaMenuBG">
        <td align="left">&nbsp;<tiles:insert page="/include/fecha.jsp"/></td>
        <td align="right" width="65%"><tiles:insert page="/include/currentLocalizacion.jsp"/>&nbsp;</td>
    </tr>
    <tr>
        <td colspan="2">
            <div id="div_pagina">
                <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
                    <tr>
                        <td>