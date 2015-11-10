<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
        
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <TITLE>Usuario No Autorizado</TITLE>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <link href="css/textos.css" rel="stylesheet" type="text/css">
        <link href="css/componentes.css" rel="stylesheet" type="text/css">
        <link href="css/estilosBotones.css" rel="stylesheet" type="text/css">        
    </head>
    <body>
        <table border="0" width="100%" cellpadding="0" cellspacing="0"  class="textoNegro12">
            <tr>
                <td class="titulosAccion" height="55px">
                    <table border="0" width="100%" cellspacing="0" cellpadding="0">
                        <tr>
                            <td width="3%" align="center"><img src="./images/error_48.gif" border="0"></td>
                            <td align="left">Error al ingresar al sistema</td>
                            <td align="right">
                                <div id="botonA">
                                    <a class="cancelarA" href=#" onclick="window.close();">Salir</a>
                                </div>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr><td height="10px"></td></tr>
            <tr>
                <td align="center">
                    <b>&nbsp;Se produjo un error al ingresar al sistema, es posible que este usuario no tenga permisos, por favor comuniquese con el Area de Sistemas.</b>
                </td>
            </tr>
        </table>
    </body>
</html>