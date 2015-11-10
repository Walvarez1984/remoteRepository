<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="../include/top.jsp"/>
	<div id="migrarAut">
	<html:form action="migrarAutorizaciones" method="post">
    <TABLE border="0" cellspacing="0" cellpadding="0" width="100%" class="textos">
        <%--Titulo de los datos--%>
        <tr>
            <td align="left" valign="top" width="100%">                
                <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
                    <tr>
                        <td  width="3%" align="center"><img src="images/lineaComercial.gif" border="0"></img></td>
                        <td height="35" valign="middle">Migrar Autorizaciones de Descuento Variable a Linea Comercial</td>
                        <td align="right" valign="top">
                            <table border="0">
                                <tr>
                                    <td>
                                        <div id="botonA">                                     
                                        	<html:link  href="#" styleClass="guardarA" title="Migrar Autorizaciones" onclick="javascript:requestAjax('migrarAutorizaciones.do',['mensajes','migrarAut'],{parameters: 'migrarAutorizaciones=ok', evalScripts: true});">
												Migrar
											</html:link>
                                        </div>                                     
                                    </td>
                                    <td>
                                        <bean:define id="exit" value="exit"/>
                                        <div id="botonA">
                                            <html:link action="menuPrincipal" paramId="salir" paramName="exit" styleClass="cancelarA">
                                                Cancelar
                                            </html:link>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
        	<td>
        		<table align="center">
        			<tr>
        				<td align="left" >&nbsp;&nbsp;C&oacute;digo pedido:&nbsp;</td>
						<td>
							&nbsp;<smx:text property="codigoPedido" styleClass="textObligatorio" styleError="campoError" size="19" onkeypress="return validarInputNumeric(event);"/>	
						</td>
        			</tr>
        		</table>
        	</td>
        </tr> 
        
        
		<tr>
			<td></td>
		</tr>
		<tr>
			<td align="center">
				<a>Corregir las llaves de los descuentos y autorizaciones</a>
			</td>
		</tr>
        <tr>
        	<td>
        		<table align="center">
       
        			<tr>
        				<td align="left" >&nbsp;&nbsp;C&oacute;digo pedido:&nbsp;</td>
						<td>
							&nbsp;<smx:text property="codigoPedidoCorregir" styleClass="textObligatorio" size="19" onkeypress="return validarInputNumeric(event);"/>	
						</td>
						<td>
		                   <div id="botonA">                                     
                                <html:link  href="#" styleClass="guardarA" title="Corregir llaves" onclick="javascript:requestAjax('migrarAutorizaciones.do',['mensajes','migrarAut'],{parameters: 'corregirAutorizaciones=ok', evalScripts: true});">
								Corregir
								</html:link>
                           </div>  
						</td>
        			</tr>
        		</table>
        	</td>
        </tr>  
                    
    </TABLE>
    </html:form>
  </div>

<tiles:insert page="../include/bottom.jsp"/>