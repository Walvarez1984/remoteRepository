<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <TITLE>Gracias por usar el Sistema</TITLE>
        
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <link href="css/textos.css" rel="stylesheet" type="text/css">
        
        <script language=javascript>
        <!--
          function window_onload(){
            setTimeout("fCerrarVentana()", 0);
          }
          function fCerrarVentana(){
            //window.opener.focus();
            window.parent.close();
          }
        //-->
        </script>
    </head>
    
    <body onload="window_onload()">
        <form action="login.do" method="post">
            <table border="0" height="500" class="textoNegro12" align="center">
                <TR>
                    <TD align="center"><img src="images/SISPE.gif" border="0"></TD>
                    <TD align="center"><b>Gracias por usar el Sistema de Pedidos</b></TD>
                </TR>
            </table>
        </form>
    </body>
</html>
<%session.invalidate();%>