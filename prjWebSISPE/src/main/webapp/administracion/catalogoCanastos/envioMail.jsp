<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/mensajeria.tld" prefix="mensajeria"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:useAttribute id="vformName"   name="vtformName"  classname="java.lang.String" ignore="true"/>

<div id="popup" class="popup" style="visibility:visible;"> 
    <div id="center" style="position:50px">
        <table border="0" cellpadding="0" cellspacing="0" width="100%">
            <tr>
                <td width="30%">&nbsp;</td>
                <td align="left" width="70%">
                    <div id="center" class="popupcontenido">
                        <table border="0" width="370px" cellspacing="0" cellpadding="0" bgcolor="#F1F3F5" class="tabla_informacion">
                            <tr>
                                <td background="images/barralogin.gif" height="22px" class="textoBlanco11">  
                                    <table border="0" width="100%" align="center" cellspacing="0" cellpadding="0">
                                        <tr>
                                            <td width="150" ><b>Env&iacute;o</b></td>
                                            <td width="100%" align="right">                                                
                                                <div id="botonWin">
                                                    <a href="#" class="linkBlanco8" onclick="requestAjaxByNameForm('catalogoCanastos.do', ['envioMail'], {parameters: 'cerrarVentanaMail=ok', evalScripts: true},'${vformName}');ocultarModal();">X</a>
                                                </div>
                                            </td>
                                        </tr>	
                                    </table>
                                </td>
                            </tr>
                            <tr>
          					<td bgcolor="#F4F5EB" valign="top">                                
                            	<table class="tabla_informacion" border="0" width="100%" align="center" cellpadding="0" cellspacing="5">
                                    <tr>								   		                                                 
                                        <td>
                                        	<table class="tabla_informacion fondoBlanco" border="0" width="100%" align="center" cellpadding="0" cellspacing="5">
                                            	<tr>
                                            <td colspan="2" style="position:relative;">
                                                <tiles:insert page="/include/mensajes.jsp"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td  align="right" class="textoNegro11">Ingrese el email: </td>
                                            <td valign="middle" align="center">
                                            	<input style="display:none;" type="text" name="oculto">
                                                <html:text  property="emailContacto" size="35" value="" onkeyup="requestAjaxEnter('catalogoCanastos.do',['envioMail'],{parameters: 'aceptarEnvioMail=ok'});"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colspan="2" valign="top" align="center">
                                                <div id="botonD"><html:link href="#" onclick="requestAjax('catalogoCanastos.do',['envioMail'],{parameters: 'aceptarEnvioMail=ok', evalScripts: true});" styleClass="enviarMailD">Enviar</html:link></div>
                                            </td>
                                        </tr>                                       
                                    </table>
                                    <logic:notEmpty name="ec.com.smx.sic.sispe.envioMail">
                                        <bean:define id="valoresMail" name="ec.com.smx.sic.sispe.envioMail"/>
                                        <bean:define id="adjuntos" name="imagenesCatalogoArticulos"/>
                                        <bean:define id="contactos" name="contactosEnvioMail"/>
                                        <mensajeria:enviarMail asunto="${valoresMail.asunto}" de="${valoresMail.de}" para="${contactos}" host="${valoresMail.host}" puerto="${valoresMail.puerto}" archivosAdjuntos="${adjuntos}"
											codigoCompania="${valoresMail.eventoDTO.id.companyId}" codigoSistema="${valoresMail.eventoDTO.id.systemId}" 
											codigoEvento="${valoresMail.eventoDTO.id.codigoEvento} ">
											<tiles:insert page="/administracion/catalogoCanastos/enviarMail.jsp" flush="false"/>
										</mensajeria:enviarMail>
                                    </logic:notEmpty>    	
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
</div>