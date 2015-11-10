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
		
		<script language="JavaScript" src="js/jquery1.3.2/jquery.js" type="text/javascript"></script>
		<script language="JavaScript" src="js/jquery1.3.2/jquery/jquery-ui.js" type="text/javascript"></script>   
		<script language="JavaScript" src="js/jquery/jquery.iutil.js" type="text/javascript"></script>
		<script language="JavaScript">var $j = jQuery.noConflict();</script>
<!-- 		<script language="JavaScript" src="js/prototype.js" type="text/javascript"></script> -->
		<script language="JavaScript" src="js/ajax.js" type="text/javascript"></script>
		<script language="JavaScript" src="js/util/util.js" type="text/javascript"></script>
		<script language="JavaScript" src="js/jquery1.3.2/calendarioBodega.js" type="text/javascript"></script>
	    
		<link href="css/textos.css" rel="stylesheet" type="text/css">
		<link href="css/componentes.css" rel="stylesheet" type="text/css">
		<link href="css/estilosBotones.css" rel="stylesheet" type="text/css">
		<link href="css/estilosCalendarioBodega.css" rel="stylesheet" type="text/css">
		<link href="js/jquery1.3.2/css/demos.css" rel="stylesheet" type="text/css">
		<link href="js/jquery1.3.2/css/ui.all.css" rel="stylesheet" type="text/css">
		
	    <logic:notEmpty name="ec.com.smx.sic.sispe.reporte.diaCalendario">
	    	<link rel=alternate media=print href="calendarioBodega/imprimirDia.jsp?id=<%=id%>">
	    </logic:notEmpty>
		
		<title>SISPE - Corporaci&oacute;n Favorita C.A.</title>
	</head>
	<body onLoad="ajustarAltoContenido(true);">
		<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
		    <tr>
		        <td id="encabezado_pagina" colspan="2">
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
			                                                    	&nbsp;<tiles:insert page="/include/fecha.jsp"/>
			                                                    </td>
			                                                    <%--td valign="middle"  class="textoBlanco10" align="right" width="57%">
			                                                        Tiempo restante de sesión:&nbsp;
			                                                    </td--%>
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
			                                                       <%--div id="TimeLeft">
			                                                            <label id="TimeLeftLb"></label>
			                                                             &nbsp(mm/ss)
			                                                            <bean:define id="tiempoSesion">
			                                                                <%= request.getSession().getMaxInactiveInterval()/60%>
			                                                            </bean:define>
			                                                            <script language="javascript">
			                                                                showTimer(${tiempoSesion});
			                                                            </script>
			                                                        </div--%>
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
			            <%--tr>
			                <td width="100%" height="10px" bgcolor="#F1F3F5">&nbsp;</td>
			            </tr--%>
		            </table>
		        </td>
		    </tr>
		    <%--tr height="2%" class="tablaMenuBG">
		        <td id="fecha_pagina" colspan="2">
		        	<table border="0" cellpadding="0" cellspacing="0" width="100%">
		        		<tr>
		        			<td align="left">&nbsp;<tiles:insert page="/include/fecha.jsp"/></td>
		        			<td align="right" width="65%"><tiles:insert page="/include/currentLocalizacion.jsp"/>&nbsp;</td>
		        		</tr>
		        	</table>
		        </td>
		    </tr--%>
		    <tr>
		        <td colspan="2">
		            