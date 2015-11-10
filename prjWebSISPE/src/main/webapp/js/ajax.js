/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * AJAX
 *
 * Coleccion de scripts que permiten la comunicacion desde la pagina en el browser hasta el servidor (struts).
 *
 * Como usar
 * ==========
 * 1) Llamar al metodo requestAjax desde la pagina (por ej. mediante el evento: onclick), o
	  requestAjaxEnter (por ej. mediante el evento: onkeypress).
 * 2) Pasar la url (ej.: una accion struts), un parametro adicional y el nombre del formulario que se tratara
 * 3) Cuando el servidor responda ...
 *		 - el script iniciara un bucle atravez de la respuesta, tomando en cuenta las estructuras con los ids que 
		   que se van a actualizar (por ej.: <div id="idName">nuevoContenido</div>)
 * 		 - cada tag cuyo <code>id</code> coincida con el nombre enviado en el arreglo de secciones
		   sera remplazado en el documento actual con el nuevo contenido
 *
 * NOTA: El formato de los tags debe estar con minusculas,
		 cualquier cosa despues de la primera marca del tag '>' hasta el cierre </...> es considerada contenido.
 */

//variables globales
 	var request = null;
	var queryString = null;
	var htmlElements = [];
	var propiedades = null;
	
  /**
   * Obtiene el contenido de la URL y envia los datos al servidor mediante el objeto XMLHttpRequest
   * @param	url 				- el contenido que deseamos ejecutar (por ej.: /struts-ajax/sampleajax.do)
   * @param parametros			- parametro adicional enviado a la accion (nombre del control que se ejecuto por ej.: botonGuardar=VALUE)
   * @param secciones			- identificadores de las secciones <span> que se van a actualizar
   * @param nombreFormulario 	- el indice del formulario que se pasara al servidor como parte de la peticion (no es obligatorio)
   *					
   */
  function requestAjax(url,secciones,opciones)
  {
    //Chequeando conexiones existentes
	if (request != null && request.readyState != 0 && request.readyState != 4){
		//hay una conexion activa
	}else{
        initOptions(); //se inicializan las opciones correspondientes
		setOptions(opciones); //se asignan las opciones ingresadas a las propiedades generales
	    //se concatenan los valores del formulario enviados en la peticion
	    queryString = "";
	    queryString = propiedades.parameters + getFormAsString(propiedades.indexForm);
	    if(window.XMLHttpRequest){ // No Internet Explorer
        	request = new XMLHttpRequest();
    	}else if (window.ActiveXObject){ // Internet Explorer
        	request = new ActiveXObject("Msxml2.XMLHTTP");
        	if (!request){
            	request=new ActiveXObject("Microsoft.XMLHTTP");
        	}
     	}
    	if(request){  // el objeto de peticion se creo correctamente
			htmlElements = secciones;
            request.onreadystatechange = processStateChange; //se procesan los cambios de estado
	  		request.open(propiedades.method, url, propiedades.asynchronous); // se inicializa el objeto de la peticion
			var tipoContenido = propiedades.contentType + "; charset=" + propiedades.encoding;
	  		request.setRequestHeader("Content-Type",tipoContenido);
	        request.send(queryString); //se envia la peticion
    	}else{
    		alert('No se pudo realizar la peticion');
    	}
    }
  }
   
  function requestAjaxByNameForm(url,secciones,opciones,nameForm)
  {	
    //Chequeando conexiones existentes
	if (request != null && request.readyState != 0 && request.readyState != 4){
		//hay una conexion activa
	}else{
        initOptions(); //se inicializan las opciones correspondientes
		setOptions(opciones); //se asignan las opciones ingresadas a las propiedades generales	
	    //se concatenan los valores del formulario enviados en la peticion
	    queryString = "";
	    queryString = propiedades.parameters + getFormAsStringByNameForm(nameForm);
	    if(window.XMLHttpRequest){ // No Internet Explorer
        	request = new XMLHttpRequest();
    	}else if (window.ActiveXObject){ // Internet Explorer
        	request = new ActiveXObject("Msxml2.XMLHTTP");
        	if (!request){
            	request=new ActiveXObject("Microsoft.XMLHTTP");
        	}
     	}
    	if(request){  // el objeto de peticion se creo correctamente
			htmlElements = secciones;
            request.onreadystatechange = processStateChange; //se procesan los cambios de estado
	  		request.open(propiedades.method, url, propiedades.asynchronous); // se inicializa el objeto de la peticion
			var tipoContenido = propiedades.contentType + "; charset=" + propiedades.encoding;
	  		request.setRequestHeader("Content-Type",tipoContenido);
	        request.send(queryString); //se envia la peticion
    	}else{
    		alert('No se pudo realizar la peticion');
    	}
    }
  }
  /**
  *	Inicializa las opciones para la peticion
  */
  function initOptions(){
  	propiedades = {
            method:			'post',
            asynchronous: 	true,
            contentType:  	'application/x-www-form-urlencoded',
            encoding:     	'UTF-8',
            parameters:   	'',
            popWait: 		true,
            indexForm:		0,
            evalScripts:	false
	}
  }

  /**
  *	Asigna la opciones ingresadas para la peticion
  *	@param opciones 	- Ojeto que contiene las opciones de conexion
  */
  function setOptions(opciones){
  	Object.extend(propiedades, opciones || {});
  }
  
  /*
   * Usado por el metodo procesarURL 
   * se ejecuta en segundo plano cada vez que el objeto XMLHttpRequest cambia de estado
  */
  function processStateChange() 
  {
  		switch(request.readyState){
			case 1:
				if(propiedades.popWait){
                	popWait('div_wait');
				}break;
			case 2: break;
			case 3: break;
			case 4:	//completa	
				killWait('div_wait');
    			if (request.status == 200){ //respuesta OK
					//llamada a la funcion que actualiza la pagina
    				updateHTML(htmlElements, request.responseText);
	  			}
	    }
  }
 
 /**
  * Obtiene el contenido del formulario como un string URL codificado
  * @param 	indiceFormulario	- indice del formulario
  * @return returnString		- String que contiene los parametros codificados del formulario, inciando con &
  */ 
 function getFormAsString(indiceFormulario)
 {
  	//se inicializa el string de retorno
 	returnString ="";
 	
  	//se obtienen los valores del formulario
 	formElements=document.forms[indiceFormulario].elements;
 	
 	//se inicia un bucle atravez del arreglo de los elementos del formulario, y se va construyendo la url
 	//de la siguiente forma /strutsaction.do&name=value
 	for ( var i=0; i<formElements.length; i++ ){
	 	if(encodeURIComponent(formElements[i].type)!="button" && encodeURIComponent(formElements[i].type)!="submit" && encodeURIComponent(formElements[i].type)!="file"){
 			//se codifica el valor de cada elemento, para que se interprete de forma correcta
			if(encodeURIComponent(formElements[i].type)!="checkbox" && encodeURIComponent(formElements[i].type)!="radio")
				returnString=returnString+"&"+encodeURIComponent(formElements[i].name)+"="+encodeURIComponent(formElements[i].value);
			else if(encodeURIComponent(formElements[i].type)=="checkbox" && encodeURIComponent(formElements[i].checked)=="true")
				returnString=returnString+"&"+encodeURIComponent(formElements[i].name)+"="+encodeURIComponent(formElements[i].value);
			else if(encodeURIComponent(formElements[i].type)=="radio" && encodeURIComponent(formElements[i].checked)=="true")
				returnString=returnString+"&"+encodeURIComponent(formElements[i].name)+"="+encodeURIComponent(formElements[i].value);	
		}
 	}
 	
 	//retorno de los valores
 	return returnString; 
 }
 
  /**
  * Obtiene el contenido del formulario como un string URL codificado - dado el nombre del formulario
  * @param 	nameFormulario		- nombre del formulario
  * @return returnString		- String que contiene los parametros codificados del formulario, inciando con &
  */
 function getFormAsStringByNameForm(nameFormulario)
 {
  	//se inicializa el string de retorno
 	returnString ="";
 	var frm = getFormByFormName( nameFormulario );
  	//se obtienen los valores del formulario
 	formElements = frm.elements;
 	
 	//se inicia un bucle atravez del arreglo de los elementos del formulario, y se va construyendo la url
 	//de la siguiente forma /strutsaction.do&name=value
 	for ( var i=0; i<formElements.length; i++ ){
	 	if(encodeURIComponent(formElements[i].type)!="button" && encodeURIComponent(formElements[i].type)!="submit"){
 			//se codifica el valor de cada elemento, para que se interprete de forma correcta
			if(encodeURIComponent(formElements[i].type)!="checkbox" && encodeURIComponent(formElements[i].type)!="radio")
				returnString=returnString+"&"+encodeURIComponent(formElements[i].name)+"="+encodeURIComponent(formElements[i].value);
			else if(encodeURIComponent(formElements[i].type)=="checkbox" && encodeURIComponent(formElements[i].checked)=="true")
				returnString=returnString+"&"+encodeURIComponent(formElements[i].name)+"="+encodeURIComponent(formElements[i].value);
			else if(encodeURIComponent(formElements[i].type)=="radio" && encodeURIComponent(formElements[i].checked)=="true")
				returnString=returnString+"&"+encodeURIComponent(formElements[i].name)+"="+encodeURIComponent(formElements[i].value);	
		}
 	} 	
 	//retorno de los valores
 	return returnString; 
 }
 
 /**
 *	Retorna el texto HTML contenido en el tag identificado por el parametro <code>
 * 	id</code> dentro de la respuesta del objeto <code>XMLHttpRequest</code>.
 *	
 *	@param	id				el id que identifica al tag
 *	@param	responseText	la respuesta del objeto <code>XMLHttpRequest</code>
 *	@return	un objeto <code>String</code> que representa al texto HTML contenido
 *			en el tag 		
 */
 function getInnerHTMLById(id, responseText){
	var tagName = document.getElementById(id).nodeName.toLowerCase();
        var startTagName = "<" + tagName;
	var finishTagName = "</" + tagName;
	var startPos = responseText.indexOf('>', responseText.indexOf('id="' + id + '"'));
	var startPosTemp = startPos;
	var finishPos = startPos;
	
	do{
	    startPosTemp = responseText.indexOf(startTagName, startPosTemp + 1);
	    finishPos = responseText.indexOf(finishTagName, finishPos + 1);
	} while (startPosTemp != -1 && startPosTemp < finishPos);

	return responseText.substring(startPos + 1, finishPos);
 }
 
 
 /**
 *	Actualiza dinamicamente el HTML contenido en las secciones de la pagina que 
 *	se muestra al usuario con el nuevo HTML obtenido de la respuesta del objeto 
 *	<code>XMLHttpRequest</code>.
 *	
 *	@param	ids				una coleccion que contiene los nombres de los ids que 
 *							identifican a las secciones de las pagina que se 
 *							requiere actualizar.
 *							En Internet Explorer no puede actualizarse dinamicamente 
 *							el contenido de los tags TABLE, TBODY, THEAD y TR 
 *							porque es de solo lectura.
 *	@param	responseText	la respuesta del objeto <code>XMLHttpRequest</code>		
 */
 function updateHTML(ids, responseText){
 	var newHTML = null;
 	for(var i = 0; i < ids.length; i++){
 		if (document.getElementById(ids[i])) {
 			newHTML = getInnerHTMLById(ids[i], responseText);
 			document.getElementById(ids[i]).innerHTML = newHTML;
 			
 			/*Si se requieren actualizar los mensajes se debe cambiar el alto de
 			la seccion que muestra el contenido principal de la pagina*/
 			if (ids[i] == 'mensajes') {
 				setHeightPage();
				if(document.getElementById("div_pagina"))
					//se ubica el scoll de la pagina en la parte superior
					document.getElementById("div_pagina").scrollTop = 0;
 			}
 			
 			//se evaluan los scripts dentro de la respuesta
 			if (propiedades.evalScripts) {
 				newHTML.evalScripts(); 
 			}
 		}	
 	}
	//se debe actualizar el segmento del tiempo restante de sesion
    if (document.getElementById("TimeLeft")) {
    	newHTML = getInnerHTMLById("TimeLeft", responseText);
        document.getElementById("TimeLeft").innerHTML = newHTML;
        newHTML.evalScripts();
	}
 }
 

 /*
  * Muestra un mensaje de espera indicando que la peticion se esta procesando.
  * @param id 	Identificador del objeto que se creara.
  */
function popWait(id)
{
	procesando = document.getElementById(id);
	if(procesando == null ){
		showModal();
		var disabledZone = document.createElement('div');
      	disabledZone.setAttribute('id', 'disabledZone');
		disabledZone.id=id;
		disabledZone.className="cargando";      	
		document.body.appendChild(disabledZone);
		var messageZone = document.createElement('div');
      	messageZone.setAttribute('id', 'messageZone');
      	messageZone.style.position = "relative";
     	messageZone.style.top = "100px";
		messageZone.style.backgroundImage = "url(images/cargando.gif)";
		messageZone.style.backgroundRepeat = "no-repeat";
		messageZone.style.backgroundPosition = "center bottom";
		messageZone.style.margin = "0px";
		messageZone.style.padding = "90px";
      	disabledZone.appendChild(messageZone);
	}
}

 /*
  * Elimina el mensaje de espera
  * @param id 	Identificador del objeto que se eliminara.
  */
function killWait(id){
	procesando = document.getElementById(id);
	if(procesando != null){
		hideModal();
		document.body.removeChild(procesando); 
	}
}

  /**
   * Procesa la tecla ENTER en una caja de texto
   * @param	url 				- el contenido que deseamos ejecutar (por ej.: /struts-ajax/sampleajax.do)
   * @param parametros			- parametro adicional enviado a la accion (nombre del control que se ejecuto por ej.: botonGuardar=VALUE)
   * @param secciones			- identificadores de las secciones <span> que se van a actualizar
   * @param nombreFormulario 	- el indice del formulario que se pasara al servidor como parte de la peticion (no es obligatorio)
   *					
   */
function requestAjaxEnter(url,secciones,opciones)
{
	var keycode;
	if (window.event)
		keycode = window.event.keyCode;
	else return true;

	if (keycode == 13){
		requestAjax(url,secciones,opciones);
   		return false;
  	}else return true;
}

 /**
  * Obtiene el formulario mediante el nombre, verifica el explorador
  * @param nombreFormulario 	- el nombre del formulario que se pasara al servidor como parte de la peticion
  *	@return frm					- el formulario				
  */
function getFormByFormName( formName ){
	var frm = null;
	if(window.XMLHttpRequest){ // No Internet Explorer
		for( index=0;index<document.forms.length;index++){
			var frmAux = document.forms[index];
			if(frmAux.name == formName ){
				frm = frmAux;
				break;
			}
		}        	
    }else if (window.ActiveXObject){ // Internet Explorer
		frm = document.forms( formName );
    }
    return frm;
}

/**
  * Muestra un frame opaco sobre la pantalla
  */
var temporizador = 0;
function mostrarModal(){
    if(!document.getElementById('frameModal')){
		if(temporizador != 0){
	        clearTimeout(temporizador);
	        temporizador = 0;
	    }
	    if(document.readyState == 'complete'){
			modal = document.createElement('iframe');     
			modal.setAttribute('id', 'frameModal');
			modal.scrolling = "no"; 
			modal.frameborder = "1"; 
			modal.className = "frameModal";	
			document.body.appendChild(modal);	
	    }else{
	        temporizador = setTimeout('mostrarModal()', 200);
	    }
    }
}

/**
 * Muestra un frame opaco sobre la pantalla
 */
var temporizador = 0;
function mostrarModalAux(){
	if(temporizador != 0){
        clearTimeout(temporizador);
        temporizador = 0;
    }
    if(document.readyState == 'complete'){
		modal = document.createElement('iframe');     
		modal.setAttribute('id', 'frameModal');
		modal.scrolling = "no"; 
		modal.frameborder = "1"; 
		modal.className = "frameModal";	
		document.body.appendChild(modal);	
    }else{
        temporizador = setTimeout('mostrarModal()', 200);
    }
}

/**
  * Ocultar un frame opaco sobre la pantalla
  */
function ocultarModal(){	
	var vmodal = document.getElementById('frameModal'); 
	if (vmodal != null){
		document.body.removeChild(vmodal);
	}
}
/**
  * Muestra un frame opaco sobre la pantalla
  */
function showModal(){

		modal = document.createElement('iframe');     
		modal.setAttribute('id', 'frameModal1');
		modal.scrolling = "no"; 
		modal.frameborder = "1"; 
		modal.className = "frameModal";	
		document.body.appendChild(modal);	
}

/**
  * Ocultar un frame opaco sobre la pantalla
  */
function hideModal(){	
	var vmodal = document.getElementById('frameModal1'); 
	if (vmodal != null){
		document.body.removeChild(vmodal);
	}
}

/**
 * Muestra un frame opaco sobre la pantalla con un nivel en el eje z definido
 */
function mostrarModalZ(id, zindex){
	
	if(document.readyState == 'complete'){
		var vmodal = document.getElementById(id);
		if(vmodal == null){
			modal = document.createElement('iframe');     
			modal.setAttribute('id', id);
			modal.scrolling = "no"; 
			modal.frameborder = "1"; 
			modal.className = "frameModal";
			modal.style.zIndex = zindex;	
			modal.src = "ssl.html";
			document.body.appendChild(modal);
		}
	}else{
       setTimeout('mostrarModalZ('+id+','+zindex+')', 200);
   }	
}
/**
 * Ocultar un frame opaco sobre la pantalla
 */
function ocultarModalID(id){	
	var vmodal = document.getElementById(id); 
	if (vmodal != null){
   	document.body.removeChild(vmodal);
	}
}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////