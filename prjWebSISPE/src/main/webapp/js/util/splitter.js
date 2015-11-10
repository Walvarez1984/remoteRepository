function divisor(idDivisor, idSeccionAOcultar, idSeccionASobreponer, idImgOcultar, idImgMostrar){
	initialize(idDivisor, idSeccionAOcultar, idSeccionASobreponer, idImgOcultar, idImgMostrar,
						{
         					hoverBg             : '#b8bfd3',
         					collapsedBg         : '#c8cddd',
							borderColor         : '#b8bfd3'
						});
}

	
function initialize(divisor, td1, td2, img1, img2, options) {
		
	  	this.spliter        = document.getElementById(divisor);
	 	this.td1            = document.getElementById(td1);	
		this.td2            = document.getElementById(td2);
		this.img1 			= document.getElementById(img1);
		this.img2 			= document.getElementById(img2);
		this.carga 			= false;
		this.anchoPanelIzq = "";
	    this.anchoPanelDer = "";
	   // setOptions(options);
	 	_attachBehaviors();
	    	if(!divisor || !td1 || !td2) return
}
      
function _attachBehaviors() {
	 
		this.spliter.style.border = "1px solid " + "#b8bfd3";
	   	this.spliter.style.borderTopWidth    = "0px";
	  	this.spliter.style.borderBottomWidth = "0px";
	   	this.spliter.style.margin            = "0px";
	 	this.spliter.style.backgroundColor = "#b8bfd3";
	
	   	this.spliter.onclick     = this.titleBarClicked.bindAsEventListener(this);
	   	this.spliter.onmouseover = this.hover.bindAsEventListener(this);
	   	this.spliter.onmouseout  = this.unhover.bindAsEventListener(this);
		this.anchoPanelIzq = this.td1.style.width;
	  	this.anchoPanelDer = this.td2.style.width;
}

function titleBarClicked(e) {
		this.actions();
 }
	
function hover(e) {
		this.spliter.style.backgroundColor = "#b8bfd3";
		this.spliter.style.color           = "#ffffff";
}
	
function unhover(e) {
	    if(this.expanded){
	       this.spliter.style.backgroundColor = "#63699c";
	       this.spliter.style.color           = "#ffffff";
	    }
	    else{
	       this.spliter.style.backgroundColor = "#c8cddd";
	       this.spliter.style.color           = "#ced7ef";
	    }
}
	
function _getDirectChildrenByTag(e, tagName) {
	    var kids = new Array();
	    var allKids = e.childNodes;
	    for( var i = 0 ; i < allKids.length ; i++ )
	       if ( allKids[i] && allKids[i].tagName && allKids[i].tagName == tagName )
	          kids.push(allKids[i]);
	    return kids;
}
	 
function actions(){
	  	if(!this.carga){
			this.td1.style.display = "none";
			this.td1.style.visibility = "hidden";
			this.img1.style.display = "none";
			this.img1.style.visibility = "hidden";
			this.img2.style.display = "block";
			this.img2.style.visibility = "visible";
			//nuevas dimensiones para las secciones
			this.td1.style.width = "0%";
			this.td2.style.width = "100%"
			this.carga = true;
	  	}else{
			this.td1.style.display = "block";
			this.td1.style.visibility = "visible";
			this.img2.style.display = "none";
			this.img2.style.visibility = "hidden";
			this.img1.style.display = "block";
			this.img1.style.visibility = "visible";
			//se restablecen las dimensiones de las secciones
			this.td1.style.width = "100%";
			this.td2.style.width = this.anchoPanelDer;
			this.carga = false;
	  	}
}
