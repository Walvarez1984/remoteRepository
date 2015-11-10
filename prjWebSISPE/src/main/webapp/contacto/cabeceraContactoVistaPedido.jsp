<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/kreport.tld" prefix="kreport"%>
<%@page contentType="application/ms-excel" language="java"%>
<c:set var="cedulaCto" value="SN"> </c:set>
<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="cedulaContacto">
<c:set var="cedulaCto"><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="cedulaContacto" /></c:set>
</logic:notEmpty>
<html>
    <head>
        <meta http-equiv="Content-Style-Type" content="text/css"/>
        <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache"/>
        <meta HTTP-EQUIV="Pragma" CONTENT="no-cache"/>
        <meta HTTP-EQUIV="max-age" CONTENT="0"/>
        <meta HTTP-EQUIV="Expires" CONTENT="0"/>         
    </head>
    <body>
	<tr>
		<td>
			<table cellpadding="0" cellspacing="0" width="100%">
					<tr>
					  <!-- PARA EL CASO DE DATOS DE EMPRESA -->   
					<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="nombreEmpresa">
						<td valign="top" width="50%" align="left">
							<table border="0" width="100%" cellspacing="0">
								<tr>
									<td colspan="2" >Datos de la empresa:</td>
								</tr>
								<tr>
									<td height="6px" colspan="2"></td>
								</tr>
								<tr>
									<td width="17%" >RUC:</td>
									<td>
										<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="rucEmpresa" />
									</td>
								</tr>
								<tr>
									<td height="5px" colspan="2"></td>
								</tr>
								<tr>
									<td>Raz&oacute;n social:</td>
									<td><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="nombreEmpresa" /></td>
								</tr>
								<tr>
									<td height="5px" colspan="2"></td>
								</tr>
								<tr>
									<td>Tel&eacute;fono:</td>
									<td><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="telefonoEmpresa" /></td>
								</tr>
							</table>
						</td>
					</logic:notEmpty>
					
					  <!-- PARA EL CASO DE DATOS DE PERSONA -->   
					<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="nombrePersona">
						<td valign="top" width="50%" align="left">
							<table border="0" width="100%" cellspacing="0" cellpadding="0">
								<tr>
									<td colspan="2" >Datos del cliente:</td>
								</tr>
								<tr>
									<td height="6px" colspan="2"></td>
								</tr>
								<tr>
									<td  width="17%">Documento:</td>
									<td>
										<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="numeroDocumentoPersona" />
									</td>
								</tr>
								<tr>
									<td height="5px" ></td>
								</tr>
								<tr>
									<td>Nombre:</td>
									<td><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="nombrePersona" /></td>
								</tr>
								<tr>
									<td height="5px" ></td>
								</tr>
								<tr>
									<td>Tel&eacute;fono:</td>
									<td><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="telefonoPersona" /></td>
								</tr>
								<tr>
									<td height="5px" ></td>
								</tr>
								<tr>
									<td>Email:</td>
									<td><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="emailPersona" /></td>
								</tr>
								<tr>
									<td height="5px" ></td>
								</tr>
							</table>
						</td>
					</logic:notEmpty>
					
					<c:if test="${cedulaCto == 'SN'}">
						<td valign="top" width="50%" align="left">
							<table border="0" width="100%" cellspacing="0" cellpadding="0">
								<tr>
									<td colspan="2" >Datos del contacto:</td>
								</tr>
								<tr>
									<td height="6px" colspan="2"></td>
								</tr>
								<tr>
									<td height="5px">SIN CONTACTO</td>
								</tr>
							</table>
						</td>
					</c:if>
					<c:if test="${cedulaCto != 'SN'}">
						<td valign="top" width="50%" align="left">
							<table border="0" width="100%" cellspacing="0" cellpadding="0">
								<tr>
									<td colspan="2" >Datos del contacto:</td>
								</tr>
								<tr>
									<td height="6px" colspan="2"></td>
								</tr>
								<tr>
									<td  width="17%">Documento:</td>
									<td>
										<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="cedulaContacto" />
									</td>
								</tr>
								<tr>
									<td height="5px" ></td>
								</tr>
								<tr>
									<td>Nombre:</td>
									<td><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="nombreContacto" /></td>
								</tr>
								<tr>
									<td height="5px" ></td>
								</tr>
								<tr>
									<td>Tel&eacute;fono:</td>
									<td><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="telefonoContacto" /></td>
								</tr>
								<tr>
									<td height="5px" ></td>
								</tr>
								<tr>
									<td>Email:</td>
									<td><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="emailContacto" /></td>
								</tr>
								<tr>
									<td height="5px" ></td>
								</tr>
							</table>
						</td>
					</c:if>					
				</tr>
			</table>
		</td>
	</tr>
	</body>
</html>