<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<tiles:useAttribute id="vformName" name="vtformName" classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="estadoActivo" name="estadoActivo" classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="token" name="token" classname="java.lang.String" ignore="true"/>

<script language="javascript">mostrarModalZ('frameModal2',120);</script>

<div id="descuentos" class="popup" style="top:150px;z-index:121;">
	<table border="0" cellpadding="0" cellspacing="0" align="center" width="50%">
		<tr>
			<td valign="top" align="center">
			    <div id="center" class="popupcontenido">
					<%-- Contenido de la ventana de confirmacion --%>					
			        <table border="0" cellspacing="0" cellpadding="0" bgcolor="#F1F3F5" width="62%">
			            <tr>
			                <td background="images/barralogin.gif" align="center">
			                    <table cellpadding="0" cellspacing="0" width="100%" border="0">			                        
                                    <tr>
			                            <td class="textoBlanco11" align="left" height="20px">&nbsp;
			                                <b>Autorizaci&oacute;n requerida</b>
			                            </td>
			                            <td align="right">											
                                            <html:link title="Cerrar" href="#" onclick="requestAjax('crearCotizacion.do',['pregunta'], {parameters: 'cancelarAutorizacion=ok', evalScripts: true});ocultarModalID('frameModal2');">
                                                <img src="./images/close.gif" border="0" style="padding-top:3px;"/>
                                            </html:link>&nbsp;                                            
			                            </td>		                           
			                        </tr>
			                    </table>
			                </td>
			            </tr>
			            <tr>
			                <td bgcolor="#F4F5EB" valign="top">	                            
			                    <table class="tabla_informacion textoNegro11" border="0" width="100%" align="center" cellpadding="0" cellspacing="5">			                       								
	                                <tr>								   		                                                 
                                        <td>
                                        	<table class="tabla_informacion fondoBlanco" border="0" width="500px" align="center" cellpadding="0" cellspacing="5">
                                            	<tr>
                                                	<td valign="top"><img src="./images/advertencia.gif" border="0" style="padding-top:3px;"/></td>
                                                	<td class="textoAzul12" align="left">
                                                    	El descuento seleccionado necesita autorizaci&oacute;n de <b>Gerencia Comercial</b>	para ser aplicado en el pedido.
                                                    </td>
                                                </tr>
                                                <tr>                                                    
                                                    <td class="textoNegro11" align="left"><b>Nota:</b></td>    
                                                    <td class="textoNegro11" align="left">La autorizaci&oacute;n se la solicitar&aacute; al guardar o reservar el pedido.</td>
                                                </tr>
                                            </table>                                            
                                        </td>
                                    </tr>
                                    <tr>
                                    	<td align="center">
                                            <table cellpadding="0" cellspacing="0" border="0">
                                                <tr>                                                                                                   
                                                    <td><div id="botonD"><html:link href="#" styleClass="aceptarD" onclick="requestAjax('crearCotizacion.do',['mensajes','seccion_detalle','pregunta','div_datosCliente'],{parameters: 'agregarAutorizacion=ok', evalScripts: true});ocultarModalID('frameModal2');ocultarModal();">Aceptar</html:link></div></td>
													<td>&nbsp;</td>
                                                    <td><div id="botonD"><html:link href="#" styleClass="cancelarD" onclick="requestAjax('crearCotizacion.do',['pregunta'], {parameters: 'cancelarAutorizacion=ok', evalScripts: true});ocultarModalID('frameModal2');ocultarModal();">Cancelar</html:link></div></td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>									
			                    </table>
			                </td>
			            </tr>
			        </table>
			    </div>
			</td>
		</tr>
	</table>
</div>