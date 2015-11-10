package ec.com.smx.sic.sispe.web.redireccionarCalendarioBodegaHoras.sispev2.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.sic.sispe.common.constantes.ConstantesGlobales;

public class CalendarioBodegaHoraAction  extends BaseAction{


	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, 
			HttpServletResponse response) throws Exception 
			{
	
		String forward = ConstantesGlobales.FORWARD_REDIRECT;
		return mapping.findForward(forward);

	}	
}
