<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:useAttribute id="vformAction" name="vtformAction" classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vformName" name="vtformName" classname="java.lang.String" ignore="true"/>
<bean:define name="sispe.estado.activo" id="estadoActivo"/>
<script language="javascript">mostrarModal();</script>
<div id="redactarMail" class="popup"  style="top:40px;">
<html:form action="envioMail" method="post">
    <table border="0" cellpadding="0" cellspacing="0" align="center" width="100%">
        <tr>
            <td valign="top" align="center">
                <div id="center" style="position:relative;top:30px;">
                    <table border="0" cellspacing="0" cellpadding="0" bgcolor="#F1F3F5" width="35%">
                        <tr>
                            <td background="images/barralogin.gif" align="center">
                                <table cellpadding="0" cellspacing="0" width="100%" border="0">			
                                	                     
                                    <tr>
                                        <td class="textoBlanco11" align="left">
                                        	&nbsp;<b>Redactar mail</b>
                                        </td>
                                        <!-- Boton Cerrar -->
                                        <td align="right">
                                        	<html:link title="Cerrar" href="#" onclick="requestAjax('${vformAction}.do', ['div_pagina','pregunta','mensajes'], {parameters: 'cancelarMail=ok', evalScripts: true});ocultarModal();" >
                                        		<img src="./images/close.gif" border="0" style="padding-top:3px;"/>
                                        	</html:link>&nbsp;
                                        </td>		                           
                                    </tr>
                                </table>
                            </td>
                        </tr> 
                        <tr>
       		 				<td>
	                            <div id="mensajesPopUpMail" style="font-size:0px;position:relative;">
									<tiles:insert  page="/include/mensajes.jsp" />
	 							</div>
		 					</td>
          		 		</tr>   
                        <tr>
          					<td bgcolor="#F4F5EB" valign="top">                                
                            	<table class="tabla_informacion" border="0" width="100%" align="center" cellpadding="0" cellspacing="5">
                                   	<tr>
                                   		<td>
                               		 		<table class="tabla_informacion fondoBlanco" border="0" width="100%" align="center" cellpadding="0" cellspacing="5">
	                               		 		
                                       			<tr>
                                       				<td>
                                       					<table class="tabla_informacion fondoBlanco" border="0" width="100%" align="center" cellpadding="0" cellspacing="5">
                                       				<tr>
                                   						<td  class="textoAzul11">Para: </td>
		                                   				<td>
		                                   				<bean:define id="emailPara" name="ec.com.smx.sic.sispe.paraMail"/>
															<smx:text name="${vformName}" property="emailEnviarCotizacion" styleClass="textObligatorio" styleError="campoError" size="80" value="${emailPara!=null?emailPara:''}"/>                                       
				                                     	</td>
				                                    </tr>
				                                    <tr>
				                                   		<td  class="textoAzul11">CC: </td>
				                                       	<td>
															<smx:text name="${vformName}" property="ccMail" styleClass="textObligatorio" styleError="campoError" size="80"/>                                       
				                                     	</td>
				                                    </tr>
				                                    <tr>
				                                   		<td  class="textoAzul11">Asunto: </td>
				                                       	<td>
				                                       		<bean:define id="asuntoBaseMail" name="ec.com.smx.sic.sispe.asuntoMail"/>
															<smx:text name="${vformName}" property="asuntoMail" styleClass="textObligatorio" styleError="campoError" size="80" value="${asuntoBaseMail}"/>
				                                     	</td>
				                                    </tr>
				                                    <tr>
				                                   		<td colspan="2" class="textoNegro10 tabla_informacion"><b>Nota:</b> Para agregar más de un correo electrónico en los campos para: y cc: coloque una coma(,) para separarlos. Ejemplo: cliente1@correo.com,cliente2@correo.com</td>
				                                    </tr>
				                                    <tr>
				                                    	<table width="100%" cellpadding="1" cellspacing="0" class="tabla_informacion" >
				                                        <tr>
				                                        	<td class="textoCafe10"><input type="checkbox" name="opEntregas" value="${estadoActivo}">&nbsp;<b>Detalle de las entregas</b></td>
				                                       		<td class="textoCafe10"><input type="checkbox" name="opCanastos" value="${estadoActivo}">&nbsp;<b>Detalle de los canastos</b></td>
				                                        
															<logic:empty name="ec.com.smx.sic.sispe.vistaPedidoDTO">
																<logic:empty name="ec.com.smx.sic.sispe.pedido.descuentos">
																	<td class="textoCafe10">
																		<input type="checkbox" name="opDescuentos" value="${estadoActivo}">&nbsp;<b>Detalle de los descuentos</b>
																	</td>
																</logic:empty>
																<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.descuentos">
																	<td class="textoCafe10">
																		<input type="checkbox" name="opDescuentos" value="${estadoActivo}" checked="true">&nbsp;<b>Detalle de los descuentos</b>
																	</td>
																</logic:notEmpty>
															</logic:empty>
															
															<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedidoDTO">
																<logic:empty name="ec.com.smx.sic.sispe.descuentos.aplicadosCol">
																	<td class="textoCafe10">
																		<input type="checkbox" name="opDescuentos" value="${estadoActivo}">&nbsp;<b>Detalle de los descuentos</b>
																	</td>
																</logic:empty>
																<logic:notEmpty name="ec.com.smx.sic.sispe.descuentos.aplicadosCol">
																	<td class="textoCafe10">
																		<input type="checkbox" name="opDescuentos" value="${estadoActivo}" checked="true">&nbsp;<b>Detalle de los descuentos</b>
																	</td>
																</logic:notEmpty>
															</logic:notEmpty>
														
				                                      		<td class="textoCafe10"><input type="checkbox" name="opDiferidos" value="${estadoActivo}">&nbsp;<b>Detalle de los diferidos</b></td>
				                                       </tr>
				                                    </table>
				                                    </tr>
				                                    <tr>
				                                    	<td class="textoAzul11" align="left">Contenido: </td>
				                                    </tr>
				                                    <tr>
				                                       <td colspan="2" width="98%">
				                                       <bean:define id="textoBaseMail" name="ec.com.smx.sic.sispe.textoMail"/>
				                                      	<smx:textarea name="${vformName}" property="textoMail" styleClass="textObligatorio" styleError="campoError" cols="100" rows="20" value="${textoBaseMail}"/>
				                                       </td>
				                                    </tr>
				                            	</table>
			                            	</tr>
			                            	<tr>
		                                        <td align="center" colspan="2">
		                                            <table cellpadding="0" cellspacing="0" border="0">
		                                                <tr>                                                    
		                                                    <td><div id="botonD"><html:link styleClass="enviarMailD" href="#" onclick="requestAjax('${vformAction}.do', ['div_pagina','mensajes'], {parameters: 'ayuda=siEnviarEmail', evalScripts: true});ocultarModal();">Enviar</html:link></div></td>
		                                                    <td>&nbsp;</td>
		                                                    <td><div id="botonD"><html:link styleClass="cancelarD" href="#" onclick="requestAjax('${vformAction}.do', ['div_pagina','pregunta','mensajes'], {parameters: 'cancelarMail=ok', evalScripts: true});ocultarModal();">Cancelar</html:link></div></td>
		                                                </tr>    
		                                            </table>
		                                        </td>
		                                    </tr>
		                            	</table>
                       			   	</tr>
                            	</table>
                            </td>
                        </tr>                        	
                    </table>
                </div>
            </td>
        </tr>
    </table>
    </html:form>
</div>