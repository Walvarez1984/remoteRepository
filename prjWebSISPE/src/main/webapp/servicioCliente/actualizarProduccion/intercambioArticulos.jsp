<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="/include/top.jsp"/>

<logic:notEmpty name="ec.com.smx.sic.sispe.vistaArticuloDTO.Intercambio">
	<bean:define id="articuloDTO" name="ec.com.smx.sic.sispe.vistaArticuloDTO.Intercambio"></bean:define>
</logic:notEmpty>

<table border="0" cellpadding="0" cellspacing="0" width="90%" align="center">
	<tr><td><tiles:insert page="/include/mensajes.jsp"/></td></tr>
	<tr><td><tiles:insert page="/include/mensajes.jsp"/></td></tr>
    <tr>
        <td class="textoNegro11" align="center">	
            <table cellpadding="0" cellspacing="0" border="0" width="100%">
                <tr><td heigh="15px"></td></tr>
                <tr>
                 	<td align="left" colspan="2">Informaci&oacute;n de art&iacute;culo actual:</td>
                </tr>
                 <tr height="2px"></tr>
                <tr>        
                	<td align="left" width="30%" class="textoAzul11">
                		C&oacute;digo barras:
                	</td>        
                    <td align="left" width="70%">
                        
                        
                        <logic:empty name="ec.com.smx.sic.sispe.tipo.objeto.Intercambio">
                        	<bean:write name="articuloDTO" property="codigoBarras"/>
                        </logic:empty>
                        <logic:notEmpty name="ec.com.smx.sic.sispe.tipo.objeto.Intercambio">
                        	<bean:write name="articuloDTO" property="articuloRelacion.codigoBarrasActivo.id.codigoBarras"/>
                        </logic:notEmpty>
                        
                    </td>                    
                </tr>
                <tr>
                	<td align="left" width="30%" class="textoAzul11">
                		Descripci&oacute;n:
                	</td>
                    <td align="left" width="70%">
                    	<logic:empty name="ec.com.smx.sic.sispe.tipo.objeto.Intercambio">
                        	<bean:write name="articuloDTO" property="descripcionArticulo"/>
                        </logic:empty>
                        <logic:notEmpty name="ec.com.smx.sic.sispe.tipo.objeto.Intercambio">
                        	<bean:write name="articuloDTO" property="articuloRelacion.descripcionArticulo"/>
                        </logic:notEmpty>
                    </td>
                </tr>
                <tr height="5px"></tr>
                <tr>                    
                	<td align="left" width="30%">Intercambiar con:</td> 
                	<td align="left" width="70%">
                		<html:text property="codigoArtIntercambio" styleClass="textNormal"></html:text>
                	</td>  
                </tr>                             
            </table>
        </td>
    </tr>
</table>
