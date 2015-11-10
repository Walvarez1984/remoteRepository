
//////////////////////////////////////////////////////////////////////
// Establece el foco a un control del formulario
//////////////////////////////////////////////////////////////////////
function setFocus(nombreFormulario, nombreControl){
    var focusControl = document.forms[nombreFormulario].elements[nombreControl];
    focusControl.focus();
}

//////////////////////////////////////////////////////////////////////
// Establece el valor true al atributo checked de un radio
//////////////////////////////////////////////////////////////////////
function setChecked(nombreFormulario, nombreRadio, indexRadio){
    var checkedControl = document.forms[nombreFormulario].elements[nombreRadio];
    checkedControl[indexRadio].checked = true;
}

//////////////////////////////////////////////////////////////////////
// Funcion que muestra un mensaje de procesamiento en el boton
//////////////////////////////////////////////////////////////////////
function enProceso(objeto){
	objeto.value="Procesando...";
	return;
}

/**
 *	Realiza un submit cuando se realiza un check, almacenado su valor en la variable ayuda.
 *	@param objeto			El objeto que se esta manejando
 *	@param valor			El valor que sera pasado a la propiedad ayuda
 */
function check(objeto,valor){
	if(valor!=""){
		objeto.form.ayuda.value=valor;
		objeto.blur();
		objeto.form.submit();
	}
	return;
}

/**
 *	Realiza el envio de un formulario, controlando el numero de envios
 *	@param valor			El valor que sera pasado a la propiedad ayuda
 *	@param indiceForm		El indice del formulario que se enviara
 */
var contSubmit=0;
function realizarEnvio(valor, indiceForm){
    if(contSubmit==0){
    	if(indiceForm == undefined)
    		indiceForm = 0;
        contSubmit++;
        document.forms[indiceForm].ayuda.value=valor;
        document.forms[indiceForm].submit();
        if(document.readyState == "loading")
        	popWait('div_wait');
    }
}

/**
 *	Realiza el envio de un formulario, controlando el numero de envios
 *	@param valor			El valor que sera pasado a la propiedad ayuda
 *	@param indiceForm		El indice del formulario que se enviara
 */
var contSubmit=0;
function realizarEnvioSinProcesando(valor, indiceForm){
    if(contSubmit==0){
    	if(indiceForm == undefined)
    		indiceForm = 0;
        contSubmit++;
        document.forms[indiceForm].ayuda.value=valor;
        document.forms[indiceForm].submit();
    }
}

/**
 *	Realiza el envio de un formulario, no utiliza un contador de submits
 *	@param valor			El valor que sera pasado a la propiedad ayuda
 *	@param indiceForm		El indice del formulario que se enviara
 */
var contSubmit=0;
function realizarEnvioSinProcesando2(valor, indiceForm){
    	if(indiceForm == undefined)
    		indiceForm = 0;
        contSubmit++;
        document.forms[indiceForm].ayuda.value=valor;
        document.forms[indiceForm].submit();
 }

/**
 *	Realiza el envio de un formulario, controlando el numero de envios para el pdf
 *	@param valor			El valor que sera pasado a la propiedad ayuda
 *	@param indiceForm		El indice del formulario que se enviara
 */
var contSubmitPDF=0;
function realizarEnvioPDF(valor, indiceForm){
    if(contSubmitPDF==0){
    	if(indiceForm == undefined)
    		indiceForm = 0;
        contSubmitPDF++;
        document.forms[indiceForm].ayuda.value=valor;
        document.forms[indiceForm].submit();
    }
}

/**
 *	Realiza el envio de un formulario, sin controlar el numero de envios
 *	@param valor			El valor que sera pasado a la propiedad ayuda
 *	@param indiceForm		El indice del formulario que se enviara
 */
function enviarFormulario(valor, indiceForm, mostrarWait){
    if(indiceForm == undefined)
    	indiceForm = 0;
    if(mostrarWait == undefined)
    	mostrarWait = true;
    document.forms[indiceForm].ayuda.value=valor;
    document.forms[indiceForm].submit();
    if(mostrarWait && document.readyState == "loading")
      	popWait('div_wait');
}

/**
 *	Realiza el envio de un formulario
 *	@param valor			El valor que sera pasado a la propiedad ayuda
 *	@param indiceForm		El indice del formulario que se enviara
 */
function realizarEnvioEnter(valor, indiceForm){
   	var keycode;
	if (window.event)
		keycode = window.event.keyCode;
	else return true;

	if (keycode == 13){
		realizarEnvio(valor, indiceForm);
   		return false;
  	}else return true;
}

//////////////////////////////////////////////////////////////////////
// Abre una ventana popup
//////////////////////////////////////////////////////////////////////
function mypopup(accion,titulo){
	mywindow = window.open (accion,
		titulo,"menubar=no,directories=no,location=no,toolbar=no,top=50,left=50,scrollbars=yes,titlebar=yes,height=600,width=850,resizable=yes,status=yes");
	mywindow.focus();
}

//////////////////////////////////////////////////////////////////////
//Abre una ventana popup despacho de reservas
//////////////////////////////////////////////////////////////////////
function despachoReservas(accion,titulo,parametro){
	if(parametro==1){
	 mywindow = window.showModalDialog (accion,
		titulo,"menubar=no,directories=no,location=no,toolbar=no,scrollbars=yes,titlebar=yes,height=685,width=1015,resizable=yes,status=yes");
	 mywindow.focus();
	}
}
function openWindow(parametro){
	mywindow = window.open (parametro[0],
		parametro[1],"menubar=no, directories=no, location=no, toolbar=no, scrollbars=no, titlebar=yes, top=0, left=0, height=685, width=1015, resizable=yes, status=yes");
	mywindow.focus();
}

//////////////////////////////////////////////////////////////////////
// Abre una ventana emergente de acuerdo a un parametro
//////////////////////////////////////////////////////////////////////
function verificarAbrirVentana(accion,titulo,parametro){
	if(parametro==1)
		mypopup(accion,titulo);
}

//////////////////////////////////////////////////////////////////////
// Verifica si se presiono la tecla F5, y la bloquea
//////////////////////////////////////////////////////////////////////
function VerificarF5(){
	var tecla= window.event.keyCode;
	if(tecla==116){
		event.keyCode=0;
		event.returnValue=false;
	}
}

/**
 *	Muestra los resultados de una busqueda en una nueva ventana.
 *	@param url					La direccion o accion que se desea ejecutar
 *	@param nombreVentana        El nombre de la nueva ventana a desplegar
 *	@param filtrosFormulario	Los filtros del formulario para la busqueda
 *	@param parametroAccion		El parametro que servira para realizar el proceso en la accion
 *	@param indiceForm			El indice del formulario del cual se toman los datos para los parametros
 */
function resultadosBusqueda(url, nombreVentana, filtrosFormulario, parametroAccion, indiceForm)
{
	if(indiceForm == undefined)
		indiceForm = 0;
	url=url+"?";
	var formElements = document.forms[indiceForm].elements;
	var formElement = "";
	for(var i=0;i<filtrosFormulario.length;i++)
	{
		formElement = formElements[filtrosFormulario[i]];
		if(formElement.length == undefined){
			//se codifica el valor de cada elemento (estandar a URL), para que pueda ser interpretado
			if(encodeURIComponent(formElement.type)!="checkbox" && encodeURIComponent(formElement.type)!="radio"){
				url=url+"&"+encodeURIComponent(formElement.name)+"="+encodeURIComponent(formElement.value);
			}else if(encodeURIComponent(formElement.type)=="checkbox" && encodeURIComponent(formElement.checked)=="true"){
				url=url+"&"+encodeURIComponent(formElement.name)+"="+encodeURIComponent(formElement.value);
			}else if(encodeURIComponent(formElement.type)=="radio" && encodeURIComponent(formElement.checked)=="true"){
				url=url+"&"+encodeURIComponent(formElement.name)+"="+encodeURIComponent(formElement.value);
			}
		}else{
			for(var j=0;j<formElement.length;j++){
				//se codifica el valor de cada elemento (estandar a URL), para que pueda ser interpretado
				if(encodeURIComponent(formElement[j].type)!="checkbox" && encodeURIComponent(formElement[j].type)!="radio")
					url=url+"&"+encodeURIComponent(formElement[j].name)+"="+encodeURIComponent(formElement[j].value);
				else if(encodeURIComponent(formElement[j].type)=="checkbox" && encodeURIComponent(formElement[j].checked)=="true")
					url=url+"&"+encodeURIComponent(formElement[j].name)+"="+encodeURIComponent(formElement[j].value);
				else if(encodeURIComponent(formElement[j].type)=="radio" && encodeURIComponent(formElement[j].checked)=="true")
					url=url+"&"+encodeURIComponent(formElement[j].name)+"="+encodeURIComponent(formElement[j].value);
			}
		}	
	}
        //se adjunta el parametro de la accion
        url = url + "&" + parametroAccion + "=ok";
        dialogoWeb(url, nombreVentana, 'dialogHeight:650px;dialogWidth:900px;help:no;scroll:no');
}

/**
 *	Muestra una ventana modal.
 *	@param url				La direccion o accion que se desea ejecutar
 *	@param nombreVentana	El nombre de la nueva ventana a desplegar
 *	@param opciones			Las propiedades para la ventana
 */
function dialogoWeb(url,nombreVentana,propiedades)
{
    popWait('div_wait'); //crea el mensaje procesando...
    window.showModalDialog(url,nombreVentana,propiedades);
    killWait('div_wait'); //elimina el mensaje procesando...
}

/**
 *	Muestra los resultados de una busqueda en una nueva ventana.
 *	@param url					La direccion o accion que se desea ejecutar
 *	@param nombreVentana        El nombre de la nueva ventana a desplegar
 *	@param filtrosFormulario	Los filtros del formulario para la busqueda
 *	@param parametroAccion		El parametro que servira para realizar el proceso en la accion
 *	@param indiceForm			El indice del formulario del cual se toman los datos para los parametros
 */
function resultadosBusquedaENTER(url, nombreVentana, filtrosFormulario, parametroAccion, indiceForm)
{
	var keycode;
	if (window.event)
		keycode = window.event.keyCode;
	else return true;

	if (keycode == 13){
		resultadosBusqueda(url, nombreVentana, filtrosFormulario, parametroAccion, indiceForm)
   		return false;
  	}else return true;
}


var isNav4, isIE4, isNav6 = false; //variables que verifican el navegador
var range = "";
var styleObj = "";

if (navigator.appVersion.charAt(0) >= 4) {
	if (navigator.appName == "Netscape") {
		if (navigator.appVersion.charAt(0) >= 5) {
			isNav6 = true;
		}
		else {
			isNav4 = true;
		}
	}
	else {
		isIE4 = true;
		range = "all.";
		styleObj = ".style";
	}
}


function getObject(obj) {
	var theObj;
	if (typeof obj == "string") {
		if (isNav6) {
			theObj = document.getElementById(obj);
			if (theObj != null) theObj = theObj.style;
		}
		else {
			theObj = eval("document." + range + obj);
			if (theObj != null) theObj = eval("theObj" + styleObj);
		}
	}
	else {
		theObj = obj;
	}
	return theObj;
}


/*function show () {
	var theObj;
	for (var i = 0; i < show.arguments.length; i++) {
		theObj = getObject(show.arguments[i]);
		if (theObj != null) {
			theObj.display = "block";
			theObj.visibility = "visible";
		}
	}
}

function hide () {
	var theObj;
	for (var i = 0; i < hide.arguments.length; i++) {
		theObj = getObject(hide.arguments[i]);
		if (theObj != null) {
			theObj.display = "none";
			theObj.visibility = "hidden";
		}
	}
}*/

/**
 * Muestra un conjunto de secciones.
 * @param secciones		- Arreglo que contiene las secciones.
 */
function show(secciones){
    //recorre las secciones que se van a mostrar
    for(var i=0; i<secciones.length;i++){
        //se obtiene cada objeto
        var theObj = getObject(secciones[i]);
        if (theObj != null) {
            theObj.display = "block";
            theObj.visibility = "visible";
        }
    }
}

/**
 * Oculta un conjunto de secciones.
 * @param secciones		- Arreglo que contiene las secciones.
 */
function hide(secciones){
    //recorre las secciones que se van a ocultar
    for(var i=0; i<secciones.length;i++){
        //se obtiene cada objeto
        var theObj = getObject(secciones[i]);
        if (theObj != null) {
            theObj.display = "none";
            theObj.visibility = "hidden";
        }
    }
}
/**
 * Muestra y oculta secciones con un mismo objeto
 * @param check       -el objeto (check)
 * @param secciones   - Arreglo que contiene las secciones.
 */
function mostraryOcultar(check,secciones){
    //recorre las secciones que se van a mostrar
    for(var i=0; i<secciones.length;i++){
        //se obtiene cada objeto
        var theObj = getObject(secciones[i]);
        if(check.checked==true){
            if (theObj != null) {
                theObj.display = "block";
                theObj.visibility = "visible";
            }
        }
        else{
            theObj.display = "none";
            theObj.visibility = "hidden";
        }
    }
}
///////////////////////////////////////////////////////////////////////////////
// 	Oculta y Muestra Divs en una tabla tipo Arbol de Jerarquia
//////////////////////////////////////////////////////////////////////////////
function mostrar(indice,seccion,muestra,oculta){
	var marco = seccion+ indice;
	var desplegar = muestra + indice;
	var plegar = oculta + indice;
	document.getElementById(plegar).style.visibility = "hidden";
	document.getElementById(desplegar).style.visibility = "visible";
	document.getElementById(marco).style.visibility = "visible";
	document.getElementById(plegar).style.display = "none";
	document.getElementById(desplegar).style.display = "block";
	document.getElementById(marco).style.display = "block";
}

function ocultar(indice,seccion,oculta,muestra){	
	var marco = seccion+ indice;
	var desplegar = muestra + indice;
	var plegar = oculta + indice;
	document.getElementById(desplegar).style.visibility = "visible";
	document.getElementById(plegar).style.visibility = "hidden";
	document.getElementById(marco).style.visibility = "hidden";
	document.getElementById(desplegar).style.display = "block";
	document.getElementById(plegar).style.display = "none";
	document.getElementById(marco).style.display = "none";   
}


/**
 * Muestra una seccion indicada por un indice, y oculta el resto de secciones
 * @param indiceMostrar                 - indice de la seccion que se va a mostrar.
 * @param descripcionComunSecciones	- descripcion comun de las secciones que se tratan.
 * @param sizeSecciones			- tamanio de las secciones.
 */
function showExclusivo(indiceMostrar, descripcionComunSecciones, sizeSecciones){
    //recorre las secciones que se van a mostrar
    for(var i=0; i<sizeSecciones;i++){
    	if(indiceMostrar == i)
            show([descripcionComunSecciones+i]);
        else
            hide([descripcionComunSecciones+i]);
    }
}

/**
 *  Cambia el estilo de un determinado objeto
 *  de acuerdo a un color determinado
 */
function cambiarEstilo (myObj, color, grosor) {
	var theObj;
	theObj = getObject(myObj);
	if (theObj != null) {
		theObj.fontWeight = grosor;
		theObj.backgroundColor = color;
	}
}

/**
 * Cambia el color a un objeto HTML cuando se da click en su ckeckbox o radio
 * @param objeto			- El objeto (checkbox o radio) que se verifica
 * @param id				- El id del objeto html al que se cambiara el estilo ej: <tr>,<div>,etc...
 * @param nuevoColor		- El nuevo color que se asigna al objeto
 * @param colorOriginal		- El color original del objeto
 */
function cambiarEstiloChequeado(objeto, id, nuevoColor, colorOriginal) {
    if(objeto.checked == true)
        cambiarEstilo(id, nuevoColor, 'normal');
    else
        cambiarEstilo(id, colorOriginal, 'normal');
}

/**
 * Cambia el color a un objeto HTML cuando se da click en la fila y activa/desactiva su checkbox o radio
 * @param objeto			- El objeto (checkbox o radio) que se activara/desactivara
 * @param id				- El id del objeto html al que se cambiara el estilo ej: <tr>,<div>,etc...
 * @param nuevoColor		- El nuevo color que se asigna a
 * @param colorOriginal		- El color original del objeto
 * @param siempreChequear	- Este campo indica si se debe chequear siempre [1=chequear siempre, 0=normal]
 */

function cambiarEstiloClick(objeto, indiceObjeto, id, nuevoColor, colorOriginal, siempreChequear){
    if(objeto.length)
        objeto = objeto[indiceObjeto];
    //se evalua la condicion
    if(siempreChequear == undefined || siempreChequear == 0){
        if(objeto.checked == false){
            objeto.checked = true;
            cambiarEstilo(id, nuevoColor, 'normal');
        }else{
            objeto.checked = false;
            cambiarEstilo(id, colorOriginal, 'normal');
        }
    }else{
        objeto.checked = true;
        cambiarEstilo(id, nuevoColor, 'normal');
    }
}

/**
 * Activa o desactiva todos los checkboxs de una tabla, de acuerdo al valor del checkbox del encabezado
 * @param checkEncabezado	- El objeto (checkbox) ubicado en el encabezado de una tabla.
 * @param conjuntoChecks	- El conjunto de checkboxs en el detalle de la tabla
 */
function activarDesactivarTodo(checkEncabezado, conjuntoChecks){
	var valorBoleano = false;
	//se verifica el valor del checkbox cabezera
	if(checkEncabezado.checked==true){
		valorBoleano = true;
	}
	
    if(conjuntoChecks!=null){
        if(conjuntoChecks.length != undefined){
            //se recorre el arreglo de checkboxs
            for(var i=0;i<conjuntoChecks.length;i++){
                if(conjuntoChecks[i].disabled == false)
                    conjuntoChecks[i].checked = valorBoleano;
            }
        }else{
            //solo existe un checkbox
            if(conjuntoChecks.disabled == false)
                conjuntoChecks.checked = valorBoleano;
        }
    }
}

/**
 * Desactiva todos los checkboxs de una tabla
 * @param vectorChecks    - El conjunto de checkboxs en el detalle de la tabla
 */
 function desactivarTodo(vectorChecks){
    if(vectorChecks.length == undefined){
        vectorChecks.checked=false;
    }else{
        for(var i=0;i<vectorChecks.length;i++){
            vectorChecks[i].checked=false;
        }
    }
 }

/**
 * Activa/desactiva un checkbox cuando se da click en su texto adjunto
 * en caso de un radio siempre los activa
 *
 * @param objeto        - El objeto (checkbox o radio) que se activara/desactivara
 */
function chequear(objeto)
{
    if(objeto.type == 'radio')
        objeto.checked = true;
    else if(objeto.type == 'checkbox'){
        if(objeto.checked == false)
            objeto.checked = true;
        else
            objeto.checked = false;
    }
}

/**
 * Activa/desactiva un checkbox que esta en un conjunto
 *
 * @param objeto        - El objeto que se activara/desactivara
 * @param indice        - El indice del objeto que se activara/desactivara
 */
function chequear2(objeto, indice)
{
    if(objeto.length != undefined){
    	if(objeto[indice].checked == false)
            objeto[indice].checked = true;
        else
            objeto[indice].checked = false;
    }else{
        if(objeto.checked == false)
            objeto.checked = true;
        else
            objeto.checked = false;
    }
}

/**
 * Activa un checkbox que esta en un conjunto
 *
 * @param objeto        - El objeto que se activara/desactivara
 * @param indice        - El indice del objeto que se activara/desactivara
 */
function activar(objeto, indice){
    if(objeto.length != undefined){
        objeto[indice].checked = true;
    }else{
        objeto.checked = true;
    }
}

/**
 * Activa un objeto checkbox y desactiva los demas ubicados en la misma fila de una matriz
 *
 * @param conjuntoChecks    - El objeto matriz de checkbox
 * @param indiceFila        - El indice de la fila a tratar
 * @param columnas          - El numero de columnas por fila
 * @param indiceActivar     - El indice del objeto que se activara
 */
function activarExclusivoPorFila(conjuntoChecks, indiceFila, columnas, indiceActivar)
{
    var inicio = indiceFila * columnas;
    var tope = inicio + columnas;
    var indiceActivarReal = indiceActivar + inicio;
    if(conjuntoChecks.length != undefined){
        for(var i = inicio; i<tope; i++){
            if(i != indiceActivarReal)
                conjuntoChecks[i].checked = false;
        }
    }
}

/**
 * Activa un objeto checkbox y desactiva los demas de un mismo conjunto
 *
 * @param conjuntoChecks    - El objeto matriz de checkbox
 * @param indiceActivar     - El indice del objeto que se activara
 */
function activarExclusivo(conjuntoChecks, indiceActivar)
{
    if(conjuntoChecks.length != undefined){
        for(var i = 0; i<conjuntoChecks.length; i++){
            if(i != indiceActivar)
                conjuntoChecks[i].checked = false;
        }
    }
}

/**
 * Activa un radio de un arreglo de radios por su indice siempre y cuando los
 * cualquiera de los otros radios no esta activo
 *
 * @param radios  			el arreglo de radios
 * @param indices 			el arreglo de indices de los radios que se quiere comprobar que
 * 								no estan activos
 * @param indiceSeleccion	el indice del radio que se requiere activar
 **/
 
function chequearRadioPorIndice(radios, indices, indiceSeleccion){
    var noEsChequeado = true;

    if (indices.length <= radios.length){
        for (var i = 0; i < indices.length; i++){
            if (radios[i] != null && radios[i].checked){
                noEsChequeado = false;
                break;
            }
        }
        if (noEsChequeado && radios[indiceSeleccion] != null){
            radios[indiceSeleccion].checked = true;
        }
    }
}
/**
 * Activa y desactiva radios y chequea opciones de acuerdo a ciertas condiciones para las entregas
 *
 * @param radiosResponsable  			el arreglo de radios de entidad responsable
 * @param radiosLugar 			        el arreglo de radios de los lugares de entreg
 * @param radiosStock					el arreglo de radios de stock
 **/

function habilitarOpcionesEntregasAnteriores(radiosLugar, radiosStock){
	
	//Primero habilito todos los radios de stock
	for(k=0;k<radiosStock.length;k++){
		radiosStock[k].disabled=false;
		radiosStock[k].checked=false;
	}	
	//itero los radio de lugar para saber cual esta chequeado
	var j
	for(j=0;j<radiosLugar.length;j++){
		if(radiosLugar[j].checked)
			break;
	}
	//Si el valor del radio es otro local
	if(radiosLugar[j].value == '5'){
		//deshabilito y deschequeo el radio de parcial a bodega del stock y a local
		var h
		for(h=0;h<radiosStock.length;h++){
			if(radiosStock[h].value == '10'){
				radiosStock[h].disabled=true;
			}
			else if(radiosStock[h].value == '9'){
				radiosStock[h].disabled=true;
			}
			else if(radiosStock[h].value == '11'){
				radiosStock[h].checked=true;
			}
		}	
	}
	//Si el valor del radio es mi local o domicilio
	if(radiosLugar[j].value == '4' || radiosLugar[j].value == '6'){
		//chequeo por defaul no pedir cd
		var z
		for(z=0;z<radiosStock.length;z++){
			if(radiosStock[z].value == '9'){
				radiosStock[z].checked=true;
			}			
		}
	}
}

/**
 * Activa y desactiva radios y chequea opciones de acuerdo a ciertas condiciones para las entregas
 *
 * @param radiosResponsable  			el arreglo de radios de entidad responsable
 * @param radiosLugar 			        el arreglo de radios de los lugares de entreg
 * @param radiosStock					el arreglo de radios de stock
 **/

function habilitarOpcionesEntregas(radioElaCanEspeciales,radiosStock,radiosLugar,bloqueoSICMER){
	
	//Primero habilito todos los radios de Lugar
	for(k=0;k<radiosLugar.length;k++){
		radiosLugar[k].disabled=false;
		radiosLugar[k].checked=false;
		if(radiosLugar[k].value == '34' && bloqueoSICMER=='1'){
			radiosLugar[k].disabled=true;	
			radiosLugar[k].checked=false;
		}
	}	
	//itero los radio de stock para saber cual esta chequeado
	var j
	for(j=0;j<radiosStock.length;j++){
		if(radiosStock[j].checked)
			break;
	}
	//Si el valor del radio es tomar mercaderia desde el Local
	if(radiosStock[j].value == '9'){
		//deshabilito los radios de entrega a otro local y entrega a  domicilio desde el CD
		var h
		for(h=0;h<radiosLugar.length;h++){
			if(radiosLugar[h].value == '5'){
				radiosLugar[h].disabled=true;
			}
			else if(radiosLugar[h].value == '6'){
				radiosLugar[h].disabled=true;
			}
		}	
	}
	//Si el valor del radio es Centro Distribucion (SISPE hara la reserva y despacho automaticamente)
	if(radiosStock[j].value == '11'){
		if(radioElaCanEspeciales!=null){
			//itero los radio de quien elabora el canasto para saber cual esta chequeado
			var i 
			for (i=0;i<radioElaCanEspeciales.length;i++){ 
			   if (radioElaCanEspeciales[i].checked) 
				  break; 
			}
			if(radioElaCanEspeciales[i].value == 'LOC'){
				//deshabilito entrega a domicilio desde el LOCAL
				var z
				for(z=0;z<radiosLugar.length;z++){
					if(radiosLugar[z].value == '5'){
						radiosLugar[z].disabled=true;
					}else if(radiosLugar[z].value == '6'){
						radiosLugar[z].disabled=true;
					}			
				}
			}
		}
	}
	
	
}

function habilitarOpcionesEntregasRes(radioElaCanEspeciales, radiosLugar, radiosStock, bloqueoEntregaDomicilio,bloqueoSICMER){
	//Primero habilito todos los radios de stock
	for(k=0;k<radiosStock.length;k++){
		radiosStock[k].disabled=false;
		radiosStock[k].checked=false;
		
	}	
	//Primero habilito todos los radios de lugar de entrega
	for(k=0;k<radiosLugar.length;k++){
		radiosLugar[k].disabled=false;
		radiosLugar[k].checked=false;
		if(radiosLugar[k].value == '34' && bloqueoSICMER=='1'){
			radiosLugar[k].disabled=true;	
			radiosLugar[k].checked=false;
		}
	}
	//itero los radio de responsable para saber cual esta chequeado
	var i 
	for (i=0;i<radioElaCanEspeciales.length;i++){ 
	   if (radioElaCanEspeciales[i].checked) 
		  break; 
	} 
	//Si el valor del radio es entidad responsable el local
	if(radioElaCanEspeciales[i].value == 'LOC'){
		//deshabilito otro lugar y domicilio desde el CD
			var ind
			for(ind=0;ind<radiosLugar.length;ind++){
				if(radiosLugar[ind].value == '5'){
					radiosLugar[ind].disabled=true;
					radiosLugar[ind].checked=false;
				}else if(radiosLugar[ind].value == '6'){
					radiosLugar[ind].disabled=true;
					radiosLugar[ind].checked=false;
				}				
			}
	}
	//Si el valor del radio es entidad responsable la bodega
	else if(radioElaCanEspeciales[i].value == 'BOD'){
		
		//habilito otro lugar
		var ind
		for(ind=0;ind<radiosLugar.length;ind++){
			if(radiosLugar[ind].value == '6' && bloqueoEntregaDomicilio == '1'){
				radiosLugar[ind].disabled=true;
				radiosLugar[ind].checked=false;
			}				
		}
		
		//deshabilito y deschequeo el radio de parcial a bodega del stock y a local
		var h
		for(h=0;h<radiosStock.length;h++){
			if(radiosStock[h].value == '9'){
				radiosStock[h].disabled=true;
			}
			else if(radiosStock[h].value == '11'){
				radiosStock[h].checked=true;
			}
		}	
	}
}


/**
 * Activa y desactiva radios de acuerdo a ciertas condiciones para las entregas
 *
 * @param radiosResponsable  			el arreglo de radios de entidad responsable
 * @param radiosLugar 			        el arreglo de radios de los lugares de entreg
 * @param radiosStock					el arreglo de radios de stock
 **/
function mantenerOpcionesEntregasAnteriores(radiosLugar, radiosStock){
	
	//Primero habilito todos los radios de stok
	for(k=0;k<radiosStock.length;k++){
		radiosStock[k].disabled=false;
	}	
	
	//itero los radio de lugar para saber cual esta chequeado
	var j
	for(j=0;j<radiosLugar.length;j++){
		if(radiosLugar[j].checked)
			break;
	}
	
	//Si el valor del radio es otro local	
	if(radiosLugar[j] !=null){
		if(radiosLugar[j].value == '5'){
			//deshabilito y deschequeo el radio de parcial a bodega del stock y a local
			var h
			for(h=0;h<radiosStock.length;h++){
				if(radiosStock[h].value == '10'){
					radiosStock[h].disabled=true;
				}
				else if(radiosStock[h].value == '9'){
					radiosStock[h].disabled=true;
				}
			}	
		}
	}
	
}

/**
 * Activa y desactiva radios de acuerdo a ciertas condiciones para las entregas
 *
 * @param radiosResponsable  			el arreglo de radios de entidad responsable
 * @param radiosLugar 			        el arreglo de radios de los lugares de entreg
 * @param radiosStock					el arreglo de radios de stock
 **/
function mantenerOpcionesEntregas(radiosStock,radiosLugar,bloqueoSICMER){
	
	//Primero habilito todos los radios de stok
	for(k=0;k<radiosLugar.length;k++){
		radiosLugar[k].disabled=false;
		if(radiosLugar[k].value == '34' && bloqueoSICMER=='1'){
			radiosLugar[k].disabled=true;	
		}
		
	}	
	
	//itero los radio de lugar para saber cual esta chequeado
	var j
	for(j=0;j<radiosStock.length;j++){
		if(radiosStock[j].checked)
			break;
	}
	
	//Si el valor del radio es otro local	
	if(radiosStock[j] !=null){
		if(radiosStock[j].value == '9'){
			//deshabilito y deschequeo el radio de parcial a bodega del stock y a local
			var h
			for(h=0;h<radiosLugar.length;h++){
				if(radiosLugar[h].value == '5'){
					radiosLugar[h].disabled=true;
				}
				else if(radiosLugar[h].value == '6'){
					radiosLugar[h].disabled=true;
				}
			}	
		}
		
		//Si el valor del radio es Centro Distribucion (SISPE hara la reserva y despacho automaticamente)
	}
	
}

/**
 * Activa y desactiva radios de acuerdo a ciertas condiciones para las entregas
 *
 * @param radiosResponsable  			el arreglo de radios de entidad responsable
 * @param radiosLugar 			        el arreglo de radios de los lugares de entreg
 * @param radiosStock					el arreglo de radios de stock
 **/
function mantenerOpcionesEntregasRes(radiosResponsable, radiosStock, radiosLugar, bloqueoEntregaDomicilio,bloqueoSICMER){
	//Primero habilito todos los radios de stok
	for(k=0;k<radiosLugar.length;k++){
		radiosLugar[k].disabled=false;
		if(radiosLugar[k].value == '34' && bloqueoSICMER=='1'){
			radiosLugar[k].disabled=true;	
		}
	}	
	//itero los radio de responsable para saber cual esta chequeado
	var i 
	for (i=0;i<radiosResponsable.length;i++){ 
	   if (radiosResponsable[i].checked) 
		  break; 
	} 
	if(radiosResponsable[i] != null){
		//Si el valor del radio es entidad responsable el local
		if(radiosResponsable[i].value == 'LOC'){
			//itero los radio de lugar para saber cual esta chequeado
			var j
			for(j=0;j<radiosStock.length;j++){
				if(radiosStock[j].checked)
					break;
			}
			if(radiosStock[j] != null){
				//Si el valor del radio es direccion
				if(radiosStock[j].value == '9' || radiosStock[j].value == '11'){
					//deshabilito y deschequeo el radio de parcial a bodega del stock
					var h
					for(h=0;h<radiosLugar.length;h++){
						if(radiosLugar[h].value == '5'){
							radiosLugar[h].disabled=true;
						}
						else if(radiosLugar[h].value == '6'){
							radiosLugar[h].disabled=true;
						}
					}		
				}
			}
		}
		//Si el valor del radio es entidad responsable la bodega
		else if(radiosResponsable[i].value == 'BOD'){
			
			//deshabilito y deschequeo el radio de parcial a bodega del stock y a local
			var h
			for(h=0;h<radiosLugar.length;h++){
				if(radiosLugar[h].value == '6' && bloqueoEntregaDomicilio == '1'){
					radiosLugar[h].disabled=true;
					radiosLugar[h].checked=false;
				}
			}	

			//deshabilito donde se entregara la marcaderia - LOCAL
			var t
			for(t=0;t<radiosStock.length;t++){
				if(radiosStock[t].value != '11'){
					radiosStock[t].disabled=true;
					radiosStock[t].checked=false;
				}
			}
		}
	}
}



/**
 * Posiciona el curson en la caja de texto que le corresponde al check
 * 
 *
 * @param objeto        - El objeto (checkbox o radio) que se activara/desactivara
 */
function posicionarCursor(conjuntoTextos,posicionTexto)
{
	 if(conjuntoTextos!=null){
		if(conjuntoTextos.length != undefined){
			conjuntoTextos[posicionTexto].focus(); 
			
        }else{
			conjuntoTextos.focus();       
		}
	 }
}


/**
 *  Cambia el alto de la seccion en la que se muestra la parte principal de la 
 *  aplicacion, en forma manual cuando se especifica un valor o en forma automatica 
 *  cuando se presentan los mensajes de informacion, error y advertencia.
 *
 *  @param  newHeight   el nuevo tamanio de la seccion expresada en pixeles, por 
 *                      ejemplo '400px'. El parametro no es obligatorio, cuando
 *                      se usa la funcion sin especificar un valor, el nuevo alto
 *                      de la seccion se calcula automaticamente segun los mensajes 
 *                      de exito, informacion, advertencia o error que se presenten.
 */
function setHeightPage(newHeight){
		
	var divPage = document.getElementById('div_pagina');
	    
    if (divPage != null) {
        if (newHeight == undefined){
			if(document.getElementById('hiddenMessages') != null){
            	newHeight = document.getElementById('hiddenMessages').value;
			}					
        }
        if(newHeight != undefined)
			divPage.style.height = newHeight;
    } 
}

var esActualizarPrecios = null;

function actualizarPrecios(esActualizarPrecios){
	//alert('msg: '+esActualizarPrecios);
	if(esActualizarPrecios!=null){
		requestAjax('crearCotizacion.do',['mensajes','pregunta','seccion_detalle'],{parameters: 'confirmarActualizarPrecios=ok&intercambioPrecios=ok&preciosActuales=ok'});ocultarModal();
	}
}

/**
 *  Oculta la seccion que corresponde a los mensajes  
 *
 *  @param  newHeight   el nombre del tipo de mensaje que se requiere ocultar.
 */
function closeMessages(messageType){
    var divHeightS = document.getElementById('div_pagina').style.height; 
    var newHeight = null;
    
    divHeightS = divHeightS.substring(0, divHeightS.indexOf("p"));
    newHeight = (document.getElementById("hidden" + messageType).value * 1) + (divHeightS * 1);
    document.getElementById(messageType).outerHTML = "";
    if(newHeight == undefined || newHeight != 567 )
    	newHeight = 567;
    setHeightPage(newHeight + "px");
}

//////////////////////////////////////////////////////////////////////
// Oculta y Muestra Divs en una tabla tipo Arbol de Jerarquia
//////////////////////////////////////////////////////////////////////
function desplegar(indice){
	var marco = "div#marco" + indice;
	var desplegar = "div#desplegar" + indice;
	var plegar = "div#plegar" + indice;	
	var capa = $j(marco);
	var des = $j(desplegar);
	var ple = $j(plegar);
    des.hide();	
	ple.show();	
    capa.show().slideDown("slow");
}

function plegar(indice){
	var marco = "div#marco" + indice;
	var desplegar = "div#desplegar" + indice;
	var plegar = "div#plegar" + indice;
	var capa = $j(marco);
	var des = $j(desplegar);
	var ple = $j(plegar);
    des.show();	
	ple.hide();	
    capa.slideUp("normal");
}

//////////////////////////////////////////////////////////////////////
// Oculta y Muestra Divs en una tabla tipo Arbol de Jerarquia abrierta
//////////////////////////////////////////////////////////////////////
function desplegarA(indice){
	var marco = "div#marco" + indice;
	var desplegar = "div#desplegar" + indice;
	var plegar = "div#plegar" + indice;
	var capa = $j(marco);
	var des = $j(desplegar);
	var ple = $j(plegar);
 	des.show();	
	ple.hide();	
    capa.show().slideDown("slow");
}

function plegarA(indice){
	var marco = "div#marco" + indice;
	var desplegar = "div#desplegar" + indice;
	var plegar = "div#plegar" + indice;
	var capa = $j(marco);
	var des = $j(desplegar);
	var ple = $j(plegar);
    des.hide();	
	ple.show();
    capa.slideUp("normal");
}

function desplegarSeccion(nombreSeccion, velocidad){
	var seccion = $j(nombreSeccion);
	if (seccion){
		seccion.show().slideDown(velocidad);	
	}
}

function plegarSeccion(nombreSeccion, velocidad){
	var seccion = $j(nombreSeccion);
	if (seccion){
		seccion.slideUp(velocidad);	
	}
}

function verificarVisibilidad(seccion){
    if(document.getElementById(seccion)){
        if(document.getElementById(seccion).style.display == 'block'){
            return 1;
        }else{
            return 0;
        }
    }
    return -1;
}
//////////////////////////////////////////////////////////////////////
// Muestra el tiempo restante de sesion
//////////////////////////////////////////////////////////////////////
 	var iStart = 0;
 	var iMinute = 0;
 	var temporizador = 0;
    function showTimer(iMinutes) {
    	iMinute= iMinutes;   
        iStart = 60;
        iMinute -= 1
        if(temporizador > 0){
            clearTimeout(temporizador);
            temporizador=0;
        }
        lessMinutes();
    }

    function lessMinutes()
    {
        //Busco mi elemento que uso para mostrar los minutos que le quedan (minutos y segundos)
       
        obj = document.getElementById('TimeLeftLb');
        if(obj){
            if (iStart == 0) {
                iStart = 60;
            iMinute -= 1;
            }
            iStart = iStart - 1;
			
			
            //Si minuto=10 muestra un mensaje para que guarde los datos
            if(iMinute==10 && iStart==0){
                alert("SISPE: Su sesion esta por expirar, por favor verifique los datos");
            }
            
            //Si minuto y segundo = 0 ya expiro la sesion
            if (iMinute==0 && iStart==0) {
                alert("SISPE: Su sesion ha expirado");
            }

            if (iStart < 10)
                obj.innerHTML = iMinute.toString() + ':0' + iStart.toString();
            else
                obj.innerHTML = iMinute.toString() + ':' + iStart.toString();

            //actualizo mi metodo cada segundo
            temporizador = setTimeout("lessMinutes()",1000);
        }
    }
/*Cambia el valor de un combo de acuerdo al valor ingresado en la caja de texto
*/    
function cambiarSeleccion(miText, miSelect)
{
	var vindice;
	if(miText!=null && miText.value!=""){
		vindice = indice(miSelect, miText.value);
		if(vindice != -1){
			miSelect.selectedIndex = vindice;
			requestAjax('entregaLocalCalendario.do', ['entregas','mensajes','confirmarLocalEntrega','opcionesBusqueda','reserva','reservacion','datos','calendario','cambioMes','buscar'], {parameters: 'entregas=ok',popWait:true,evalScripts:true});
		}
	}
}

/*Cambia el valor de un combo de acuerdo al valor ingresado en la caja de texto
*/    
function cambiarSeleccionTexto(miText, miSelect)
{
	var vindice;
	if(miText!=null && miText.value!=""){
		vindice = indice(miSelect, miText.value);
		if(vindice != -1){
			miSelect.selectedIndex = vindice;
			requestAjax('entregaLocalCalendario.do', ['entregas','mensajesPopUp','opcionesBusqueda','reserva','reservacion','datos','calendario','cambioMes','buscar'], {parameters: 'entregas=ok',popWait:true,evalScripts:true});
		}
	}
}

function cambiarSeleccionEntregas(miText, miSelect)
{
	var vindice;
	if(miText!=null && miText.value!=""){
		vindice = indice(miSelect, miText.value);
		if(vindice != -1){
			miSelect.selectedIndex = vindice;
			codigoLocal=miText.value;
			texto=miSelect.options[miSelect.selectedIndex].text.split('-');
			requestAjax('calendarioBodega.do', ['calendario','mensajesEntregas','fDespacho'], {parameters: 'entregas=ok&codigoLocal='+codigoLocal+'&actualizar=ok&textoCombo='+texto[1],popWait:true,evalScripts:true});
		}
	}
}

function cambiarTextoEntregas(miText, miSelect)
{
	if(miSelect.options[miSelect.selectedIndex].value!='ciudad'){
		miText.value = miSelect.options[miSelect.selectedIndex].value;
		texto=miSelect.options[miSelect.selectedIndex].text.split('-');
		codigoLocal=miText.value;
		requestAjax('calendarioBodega.do', ['calendario','mensajesEntregas','fDespacho'], {parameters: 'entregas=ok&codigoLocal='+codigoLocal+'&actualizar=ok&textoCombo='+texto[1],popWait:true,evalScripts:true});
	}
}
//////////////////////////////////////////////////////////////////////
//
// Cambia el valor de la caja de texto segun la seleccion del combo
//
//////////////////////////////////////////////////////////////////////

function cambiarTexto(miText, miSelect)
{
	if(miSelect.options[miSelect.selectedIndex].value!='ciudad'){
		miText.value = miSelect.options[miSelect.selectedIndex].value;
		requestAjax('entregaLocalCalendario.do', ['entregas','mensajes','confirmarLocalEntrega','opcionesBusqueda','reserva','reservacion','datos','calendario','cambioMes','buscar'], {parameters: 'entregas=ok',popWait:true,evalScripts:true});
	}
}

//////////////////////////////////////////////////////////////////////
//
// Cambia el valor de la caja de texto del local de entrega segun la seleccion del combo
//
//////////////////////////////////////////////////////////////////////

function cambiarTextoLocal(miText, miSelect)
{
	if(miSelect.options[miSelect.selectedIndex].value!='ciudad'){
		miText.value = miSelect.options[miSelect.selectedIndex].value;
		requestAjax('entregaLocalCalendario.do', ['entregas','mensajesPopUp','opcionesBusqueda','reserva','reservacion','datos','calendario','cambioMes','buscar'], {parameters: 'entregas=ok',popWait:true,evalScripts:true});
	}
}

//////////////////////////////////////////////////////////////////////
// Cambia el valor del combo segun el codigo ingresado en una caja de texto
//////////////////////////////////////////////////////////////////////

function indice(miSelect, valor)
{
	var rindice = -1;
	for(var i = 0; i < miSelect.length; i++){
		if(miSelect.options[i].value == valor){
			rindice = i;
			i = miSelect.length;
		}	
	}
	return rindice;
}

//////////////////////////////////////////////////////////////////////
// Estas funciones son especificas para verificar el documento del cliente 
// cuando fue cambiado en la caja de texto, si el documento se encuentra guardado 
// en la base de datos se retorna los datos del cliente mediante la funcion "requestAjax"
//////////////////////////////////////////////////////////////////////

var textoCambiado = 0;
function documentoCambiado(){
    textoCambiado = 1;
}

function limpiarTextoCambio(){
    textoCambiado = 0;
}

function verificarCambioDocumento(accion, segmentos){
    if(textoCambiado == 1){
        requestAjax(accion,segmentos,{parameters: 'consultarCliente=ok', evalScripts: true});
        textoCambiado = 0;
    }
}

////////////////////////////////////////////////////////////////////////
// Funciones que permiten truncar el contenido de una cadena 
////////////////////////////////////////////////////////////////////////

/*function truncar(v, id, numeroRegistro , tamanioMax , titulo){
		
	var campo = "." + id + numeroRegistro;													
	
	v = $.trim(v);			           
	if (v.length >= tamanioMax){		
		if(titulo==null)
		   titulo = "Detalle";
		d = v.substring(0,tamanioMax) + "...";				
		var spanTool = "span.tooltip" + numeroRegistro;
		$j(campo).html("<span class='tooltip"+numeroRegistro+"' title='"+titulo+"|"+ v +"' id='"+v+"'>" + d + "</span>");				
		$j(document).ready(function() {
			$j(spanTool).cluetip({splitTitle:'|',arrows:true,dropShadow:true,positionBy:'mouse',cluetipClass:'jtip'});
		});
	}
}*/

function truncar(id, texto, tamanioMax){
	//verificacion del tamanio del texto con el tamanio maximo
	if (texto.length >= tamanioMax){
		var objDiv = document.getElementById(id);
		var textoAux = texto.substring(0, tamanioMax) + '...';
		//se reemplaza el contenido del elemento
		objDiv.innerHTML = textoAux;
		//se aniade el title
		objDiv.title = texto;
		
	}
}

function truncarNormal(v, id, numeroRegistro , tamanioMax ){
		
	var campo = "." + id + numeroRegistro;													
	
	v = $.trim(v);			           
	if (v.length >= tamanioMax){				
		d = v.substring(0,tamanioMax) + "...";				
		var spanTool = "span.tooltip" + numeroRegistro;
		$j(document).ready(function() {
			$j(campo).html("<span title='"+ v +"' id='"+v+"'>" + d + "</span>");	
		});
	}			
}

function ocultarSeccion(seccion, estado){
        //se obtiene cada objeto
        if (estado == 1) {
           var objDiv = document.getElementById(seccion);
            alert("ok");
            //theObj.display = "none";
            objDiv.visibility = "hidden";
       }  
}

function validarInputNumeric(evento){
	   var tecla = evento.keyCode;
	   if ( ((tecla >= 48 && tecla <= 57)) ){
	   		//tecla-48 equivale al valor que se desea ingresar ej: 48-48=0  48:0 -> 57:9
	   		return true;
	   }else if(tecla == 13){		
			return true;
	   }else {
	   	 return(false);
	   }   
}

function validarInputNumericDecimal(evento){
	   var tecla = evento.keyCode;
	   if ( ((tecla >= 48 && tecla <= 57)) ){
	   		//tecla-48 equivale al valor que se desea ingresar ej: 48-48=0  48:0 -> 57:9
	   		return true;
	   }else if(tecla == 13 || tecla == 46){		
			return true;
	   }else {
	   	 return(false);
	   }   
}

function validarInputFecha(evento){
	   var tecla = evento.keyCode;
	   if ( ((tecla >= 48 && tecla <= 57)) ){
	   		//tecla-48 equivale al valor que se desea ingresar ej: 48-48=0  48:0 -> 57:9
	   		return true;
	   }else if(tecla == 13 || tecla == 45){		
			return true;
	   }else {
	   	 return(false);
	   }   
}

//Funcion para validar al dar enter y desactivar el bloqueo de la ventana principal
function mantenerModalOnkeyEnter(e)
{  
   if (window.event.keyCode  == 13){
     ocultarModal();
   }
}

//Funcion que va a Activar un Acordeon
function acordeon(idAcordeon, height){
	new Accordion(idAcordeon,
                                   {
                                    	panelHeight	:height,
                                        expandedBg          : '#CCD0A4',
                                        hoverBg             : '#DBDEC0',
                                        collapsedBg         : '#E9EBD8',
                                        expandedTextColor   : '#990000',
                                        expandedFontWeight  : 'bold',
                                        hoverTextColor      : '#990000',
                                        collapsedTextColor  : '#003366',
                                        collapsedFontWeight : 'bold',
                                        hoverTextColor      : '#990000',
                                        borderColor         : '#D2D6AD'
                                    });
}


function mostrarOcultarTodos(mostrar, prefijoChecks){
	
	var list =  jQuery('div[id*='+prefijoChecks+']');
	if (mostrar == 'none') {

		for(var i=0;i<list.length;i++){
		hide([list[i].id]);
	
		}
		
	} else {
		for(var i=0;i<list.length;i++){
			show([list[i].id]);
	
		}
		
	}
	
	//for(var i=0;i<list.length;i++){
		//list[i].style.display=mostrar;
		//jQuery('div[id=list[i].id]').css("display","none");
		//jQuery(list[i].css("display","none"));
	
	//}
}
function ocultarScroll(){	
	var d = document.getElementById('div_pagina');
	d.style.overflow="hidden";
}


//Coloca la accion del boton agregar al seleccionar algun check en el listado de articulos de pedidos anteriores, verifica si algun check esta en true
function ocultarModales(conjuntoChecks){ 

	var bandera=true;
	var modal=document.getElementById('agregar');
		
	if(conjuntoChecks!=null)
	{
		if(conjuntoChecks.length != undefined)
		{
			//se recorre el arreglo de checkboxs
			for(var i=0;i<conjuntoChecks.length;i++)
			{
				if(conjuntoChecks[i].disabled == false)
				{
					if(conjuntoChecks[i].checked == true)
					{
						bandera=false;
						break;
					}
				}
			}
		}else{
            //solo existe un checkbox
            if(conjuntoChecks.disabled == false){
                if(conjuntoChecks.checked == true){
					bandera=false;
				}
			}
        }
		
	}
	
	if(bandera==true)
	{
		modal.onclick=	function()
		{
		 requestAjax('crearCotizacion.do',['mensajesPopup'], {parameters: 'pedidosAnteriores=ok&accionesPedAnt=agregarArticulos',popWait:true});
		}
	}
	else
	{
		modal.onclick=	function()
		{
		 requestAjax('crearCotizacion.do',['pregunta','seccion_detalle','mensajes','divTabs'], {parameters: 'pedidosAnteriores=ok&accionesPedAnt=agregarArticulos&actualizarDetalle=ok',popWait:true,evalScripts:true});
		 ocultarModalID('frameModal2');
		 ocultarModal();
		}
		
	}
	
	
	
}

function mascaraWebInputNumeric(item,evento){
	   var nom = navigator.appName;
	   if (nom == "Microsoft Internet Explorer"){
		   //asignacion de tecla en IE
		   var tecla = evento.keyCode;}	   
	   else{
		   //asignacion de tecla en Firefox
		   var tecla = evento.which;
	   }
	   //Validacion ingreso numeros
	   if ( ((tecla >= 48 && tecla <= 57)) ){
		   return true;
	   }else if (	(tecla == 9) 
			|| 	(tecla == 8) 
			|| 	(tecla == 46) 
			|| 	(tecla == 0)){
		   return(true);}
	   else {
		   return(false);
	  }   

	}
/* Convierte a mayusculas los valores ingresados */
function validarMayuscula(field,mensaje){	  
	try {
		
		if (typeof(field) == 'NodeList') {
			for(var i=0; i < field.length; i++) {
				validarMayuscula(field[i], mensaje);
         }
		} else {		
	    	field.value = field.value.toUpperCase();	    
		}
		
		return true;
 } catch (e) {
 	return true;
 }
}

function validateValueCopyPage(pasteValue, element,limitText, numeric, onlyInteger, maxInt, maxDec){
	if(element != null && numeric != null && limitText != null && onlyInteger != null && maxInt != null &&  maxDec !=null){
		var valueSpace = $j(element).val();
		var valueNewReplace = deleteSpaceInitial($j.trim(valueSpace));
		var valueLength = validateValueLength(valueNewReplace, limitText);					
		if(numeric == true){
			if(validateValueDecimal(valueLength, maxInt, maxDec)){
				$j(element).val(valueLength);
				if(onlyInteger == true){
					if(validateValueInteger(valueLength)){
						$j(element).val(valueLength);
						clearTimeout(pasteValue);
					}else{
						$j(element).val("");
						clearTimeout(pasteValue);
					}
				}
			}else{
				$j(element).val("");
				clearTimeout(pasteValue);
			}
	}else{
		$j(element).val(valueLength);
		clearTimeout(pasteValue);
	}
}
}

function deleteSpaceInitial(value){
	var ls_value = value;
	var spaceIndex = 0;	
	var temp = ls_value[0];
	if(temp == ' '){
		var finalValue = ls_value.substr(spaceIndex+1);
		return finalValue;
	}else{
		return value;
	}
}

function validateValueLength(valueElement,limitText){
	if(valueElement != null || limitText != null){								
		if(valueElement.length > limitText){
			var textValueNew = valueElement.substring(0,limitText);
		    return textValueNew;
		}else{
			return valueElement;
		}
	}
}

function validateValueDecimal(valueNumeric, maxInt, maxDec){
	if(valueNumeric != null){
		var valNumber =new RegExp("^[0-9]{1,"+maxInt+"}(\.[0-9]{0,"+maxDec+"})?$");
		if (valNumber.test(valueNumeric)) {				
			return true;
		}else{
			return false;
		}
	}
}

function validateValueInteger(valueNumeric){
	var valNumber =/^[0-9]+$/;
	if (valNumber.test(valueNumeric)) {
		return true;
	}else{
		return false;
	}
}

function setPasteTime(element, maxLength, numeric, onlyInt, maxInt, maxDec){
	pasteValue = setTimeout(function(){
		validateValueCopyPage(pasteValue, element, maxLength, numeric, onlyInt, maxInt, maxDec)
	 }, 100);
}


function checkKeysCombination(ev,teclasSplit) {
	var teclasArray = teclasSplit.split(',');
	var isAlt=false,isCtrl=false,isShift=false,keycodeParameter;
	
	for(var i=0;i<teclasArray.length;i++) {
		//valido los casos de las teclas de control
		switch(teclasArray[i]) {
			case 'alt':
				isAlt=true;
				break;
			case 'ctrl':
				isCtrl=true;
				break;
			case 'shift':
				isShift=true;
				break;
			default :
				keycodeParameter=teclasArray[i];
		}
	}
	//valido el caso de las combinaciones que solicitan
	if((ev.ctrlKey==isCtrl)&&(ev.altKey==isAlt)&&(ev.shiftKey==isShift)&&(ev.keyCode==keycodeParameter)){
		return true;
	}else{
		return false;
	}
	return false;
}

function imprimirReportesTxt(idDivReporte,url){	
	var ventimp = window.open(url, 'popimpr', '_parent');
	ventimp.print( );
	setTimeout(function(){ ventimp.self.close(); }, 500);
}

function imprimirReporteEntregas(url){		
	var ventimp = window.open(url, 'popimpr', '_parent');
	ventimp.print();
	setTimeout(function(){ ventimp.self.close(); }, 600);
}