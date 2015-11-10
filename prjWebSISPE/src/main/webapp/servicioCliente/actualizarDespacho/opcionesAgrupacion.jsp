<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<bean:define id="opFechaArtLocal" name="ec.com.smx.sic.sispe.articulo.rptFechaArtLocal"></bean:define>
<bean:define id="opArtFechaLocal" name="ec.com.smx.sic.sispe.articulo.rptArtFechaLocal"></bean:define>
<bean:define id="opLocalFechaArt" name="ec.com.smx.sic.sispe.articulo.rptLocalFechaArt"></bean:define>
<bean:define id="opLocalArtFecha" name="ec.com.smx.sic.sispe.articulo.rptLocalArtFecha"></bean:define>

<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
    <tr>
        <td class="textoNegro11" align="center">	
            <table cellpadding="0" cellspacing="0" border="0" width="75%">
                <tr><td heigh="15px"></td></tr>
                <tr>
                    <td align="left">
                        <html:radio  property="opTipoAgrupacion" value="${opFechaArtLocal}">Fecha Despacho, Artículo, Local</html:radio>
                    </td>
                </tr>
                 <tr>
                    <td align="left">
                        <html:radio property="opTipoAgrupacion" value="${opArtFechaLocal}">Artículo, Fecha Despacho, Local</html:radio>
                    </td>
                </tr>
                <tr>
                    <td align="left">
                        <html:radio property="opTipoAgrupacion" value="${opLocalFechaArt}">Local, Fecha Despacho, Artículo</html:radio>
                    </td>
                </tr>
                 <tr>
                    <td align="left">
                        <html:radio property="opTipoAgrupacion" value="${opLocalArtFecha}">Local, Artículo, Fecha Despacho</html:radio>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>