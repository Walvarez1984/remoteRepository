<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!-- ventana insertada desde entregaArticuloLocal.jsp -->
<logic:notEmpty name="ventanaCerrar" scope="request">
    <bean:define id="visible" value="visible"/>
    <script language="javascript">
		mostrarModal();
	</script>	
</logic:notEmpty>	
<logic:empty name="ventanaCerrar" scope="request">
    <bean:define id="visible" value="hidden"/>
    <script language="javascript">
		ocultarModal();
	</script>
</logic:empty>	
<div id="popupCerrarForm" class="popup" style="visibility:${visible}"> 
    <div id="center" class="popupcontenido">
        <table border="0" align="center" cellspacing="0" cellpadding="0" bgcolor="#F1F3F5" class="tabla_informacion">
            <tr>
                <td background="images/barralogin.gif" height="22px">
                    <table cellpadding="0" cellspacing="0" width="100%">
                        <tr>
                            <td width="348" class="textoBlanco11"><b>&nbsp;&nbsp;Confirmaci&oacute;n</b></td>
                            <td width="11" align="right">
                                <div id="botonWin">
                                    <a href="#" class="linkBlanco8" onclick="hide(['popupCerrarForm']);ocultarModal();">X</a>
                                </div>
                          </td>
                            <td width="2"></td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
				<table border="0" align="center" cellspacing="0" cellpadding="5" bgcolor="#F4F5EB" class="tabla_informacion" width="100%">
				<tr><td>
                    <table class="fondoBlanco textoNegro11" border="0" width="100%" align="center">
                        <tr>
                            <td align="left" valign="top" colspan="2">
                                <bean:message key="message.confirmacion.cerrarFormulario"/>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top" width="5%"><img src="images/pregunta24.gif" border="0"></td>
                            <td align="left">¿Desea guardar los cambios realizados en la entrega?</td>
                        </tr>
                    </table>
					</td><tr>
					<tr><td>
					<table cellpadding="0" cellspacing="0" align="center" width="50%">
						<td align="center">
							<bean:define id="exit" value="exit"/>
							<div id="botonDmin">
								<html:link styleClass="siDmin" href="#" onclick="cotizarRecotizarReservarForm.botonGuardarCambios.value='ok';cotizarRecotizarReservarForm.submit();">Si</html:link>
							</div>
						</td>
						<td align="center"> 
							<div id="botonDmin">
								<input type="hidden" name="regresar" value="">
								<html:link styleClass="noDmin" href="#" onclick="cotizarRecotizarReservarForm.regresar.value='ok';cotizarRecotizarReservarForm.submit();">No</html:link>
							</div>
						</td>
					</table>
					</td><tr>
				</table>
                </td>
            </tr>
        </table>
    </div>
</div>