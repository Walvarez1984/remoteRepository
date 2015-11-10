<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<tiles:useAttribute id="formName" name="tformName" classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="tamTextoNegro" name="textoNegro" classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="tamTextoAzul" name="textoAzul" classname="java.lang.String" ignore="true"/>

												

<html>
    <head>
        <meta http-equiv="Content-Style-Type" content="text/css"/>
        <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache"/>
        <meta HTTP-EQUIV="Pragma" CONTENT="no-cache"/>
        <meta HTTP-EQUIV="max-age" CONTENT="0"/>
        <meta HTTP-EQUIV="Expires" CONTENT="0"/>       
		<style type="text/css">
			.textoAzul10{
				font-family: Verdana, Arial, Helvetica;
				font-size: 10px;
				color: #0000aa;
			}
			.textoNegro10{
				font-family: Verdana, Arial, Helvetica;
				font-size: 10px;
				color: #000000;
			}
			.textoNegro11{
				font-family: Verdana, Arial, Helvetica;
				font-size: 11px;
				color: #000000;
			}
			.textoAzul11{
				font-family: Verdana, Arial, Helvetica;
				font-size: 11px;
				color: #0000aa;
			}
		</style>
    </head>
    <body>
			<tr align="left">
				<!-- DATOS DE LA EMPRESA -->
				<logic:notEmpty name="${formName}" property="rucEmpresa">
					<td valign="top" width="40%">
					<div id="datosEmpresa">
						<table border="0"  align="left" class="${tamTextoNegro}" >
							<tr>
								<td colspan="2" class="titulo" align="left"><b>Datos de la empresa:</b></td>
							</tr>
							<tr><td height="2"></td></tr>
							<tr>
								<td class="${tamTextoAzul}" align="left">Documento:</td>
								<td align="left" colspan="2">
									<html:hidden name="${formName}" property="tipoDocumento"	write="true" />: 
									<html:hidden name="${formName}" property="rucEmpresa" write="true" />
								</td>
							</tr>
							<tr>
								<td class="${tamTextoAzul}" align="left">Raz&oacute;n social:&nbsp;</td>
								<td align="left" colspan="2"><html:hidden name="${formName}" property="nombreEmpresa" write="true"/></td>
							</tr>
							<tr>															
								<td class="${tamTextoAzul}" align="left">Tel&eacute;fono:&nbsp;</td>
								<td align="left" colspan="2"><html:hidden name="${formName}" property="telefonoEmpresa" write="true" /></td>
							</tr>								
						</table>
						</div>
					</td>
				</logic:notEmpty>								
				
				<!-- DATOS DE LA PERSONA -->		
				<logic:notEmpty name="${formName}" property="numeroDocumentoPersona">
					<td valign="top" width="40%">	
					<div id="datosPersona">					
						<table border="0" align="left" class="${tamTextoNegro}" >
							<tr>
								<td colspan="2" class="titulo" align="left"><b>Datos del cliente:</b></td>
							</tr>
							<tr><td height="2"></td></tr>
							<tr>
								<td class="${tamTextoAzul}" align="left">Documento:</td>												
								<td align="left" colspan="2">
									<html:hidden name="${formName}" property="tipoDocumentoPersona" write="true"/>:																																		
									<html:hidden name="${formName}" property="numeroDocumentoPersona" write="true"/>
								</td>
							</tr>
							<tr>															
								<td class="${tamTextoAzul}" align="left">Nombre:&nbsp;</td>
								<td align="left" colspan="2"><html:hidden name="${formName}" property="nombrePersona" write="true"/></td>
							</tr>	
							<tr>															
								<td class="${tamTextoAzul}" align="left">Tel&eacute;fono:&nbsp;</td>
								<td align="left" colspan="2"><html:hidden name="${formName}" property="telefonoPersona" write="true"/></td>
							</tr>	
							<tr>															
								<td class="${tamTextoAzul}" align="left">Email:&nbsp;</td>
								<logic:notEmpty name="${formName}" property="emailPersona" >
									<td align="left" colspan="2"><html:hidden name="${formName}" property="emailPersona" write="true"/></td>
								</logic:notEmpty>								
							</tr>												
						</table>	
						</div>									
					</td>
				</logic:notEmpty>	
				
					<!-- PARA EL CASO QUE LA PERSONA O EMPRESA NO TIENE CONTACTO -->		
					<logic:empty name="${formName}" property="numeroDocumentoContacto">
						<td valign="top" width="40%">	
						<div id="datosSinContacto">		
							
							<table border="0"  align="left" class="${tamTextoNegro}" >
								<tr>
									<td colspan="2" class="titulo" align="left"><b>Datos del contacto:</b></td>
								</tr>
								<tr><td height="2"></td></tr>
								<tr>
									<td><b><label class="textoRojo11">SIN CONTACTO</label></b></td>																		
								</tr>												
							</table>	
							</div>										
						</td>
					</logic:empty>
				
					<!-- DATOS DEL CONTACTO DE LA PERSONA O EMPRESA -->		
				<logic:notEmpty name="${formName}" property="numeroDocumentoContacto">
					<td valign="top" width="40%">
					<div id="datosContacto">						
						<table border="0" align="left" class="${tamTextoNegro}" >
							<tr>
								<td colspan="2" class="titulo" align="left"><b>Datos del contacto:</b></td>
							</tr>
							<tr><td height="2"></td></tr>
							<tr>
								<td class="${tamTextoAzul}" align="left">Documento:</td>												
								<td align="left" colspan="2">
									<html:hidden name="${formName}" property="tipoDocumentoContacto" write="true"/>:																																		
									<html:hidden name="${formName}" property="numeroDocumentoContacto" write="true"/>
								</td>
							</tr>
							<tr>															
								<td class="${tamTextoAzul}" align="left">Nombre contacto:&nbsp;</td>
								<td align="left" colspan="2"><html:hidden name="${formName}" property="nombreContacto" write="true"/></td>
							</tr>	
							<tr>															
								<td class="${tamTextoAzul}" align="left">Tel&eacute;fono contacto:&nbsp;</td>
								<td align="left" colspan="2"><html:hidden name="${formName}" property="telefonoContacto" write="true"/></td>
							</tr>	
							<tr>															
								<td class="${tamTextoAzul}" align="left">Email contacto:&nbsp;</td>
								<logic:notEmpty name="${formName}" property="emailContacto" >
									<td align="left" colspan="2"><html:hidden name="${formName}" property="emailContacto" write="true"/></td>
								</logic:notEmpty>								
							</tr>												
						</table>	
						</div>									
					</td>					
				</logic:notEmpty>
				<td valign="top" width="20%">
				<div id="numBonos">
					<table border="0" align="left" class="${tamTextoNegro}" >
						<tr>	
							<logic:notEmpty name="${formName}" property="numBonNavEmp">
								<td class="${tamTextoAzul}" align="left">Nro. bonos a recibir: </td>
								<td colspan="2" class="titulo" align="left">
									<b><bean:write name="${formName}" property="numBonNavEmp"/></b>																		
								</td>
							</logic:notEmpty>							
							<logic:empty name="${formName}" property="numBonNavEmp">
								<td class="textoAzul11" align="left" width="20%">&nbsp;</td>
							</logic:empty>
						</tr>
					</table>
					</div>					
				</td>
			</tr>
	</body>
</html>