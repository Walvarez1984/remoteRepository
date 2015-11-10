<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<tiles:insert page="/include/metas.jsp"/>
<script>var $j = jQuery.noConflict();</script>
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
                                        <td class="WhiteSystem">&nbsp;<tiles:insert page="/include/currentSystem.jsp"/></td>
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
                                        <td colspan="2" bgcolor="#D2010D" class="WhiteMiddle" align="right" valign="top">
                                            <tiles:insert page="/include/welcomeUser.jsp"/>&nbsp;
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
                <td width="100%" bgcolor="#F1F3F5" height="27px"></td>
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
            <div id="mensajes" style="font-size:0px;">
	            <logic:empty name="ec.com.smx.sic.sispe.mensajes">
					<tiles:insert page="/include/mensajes.jsp"/>
				</logic:empty>
            </div>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <div id="div_pagina" class="div_pagina">
                <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
                    <tr>
                        <td>