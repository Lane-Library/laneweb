// register onmousemove event to set XY
var X,Y,
    faqCallback = {
        success: faqSuccessHandler,
        failure: faqFailureHandler
    };
function setXY(e) {
    // IE and Safari don't have pageX/pageY so we need to do a little more calculation
    if(window.event){
        X = event.clientX;
        Y = event.clientY;
            if( document.body && ( document.body.scrollLeft || document.body.scrollTop ) ) { // Safari
                X += document.body.scrollLeft;
                Y += document.body.scrollTop;
            } else if( document.documentElement && ( document.documentElement.scrollLeft || document.documentElement.scrollTop ) ) { // IE
                X += document.documentElement.scrollLeft;
                Y += document.documentElement.scrollTop;
            }
    }
    else{
        X = e.pageX;
        Y = e.pageY;
    }
}
document.onmousemove = setXY;

YAHOO.namespace("popup");
function showPopup(elm,type,id){
    // create popupContainer if not already present
    var body, popupContainer, popupUrl, popupRequest;
    if((type == 'local' && !document.getElementById(id)) || !document.getElementById('popupContainer')){
        body = document.getElementsByTagName("body").item(0);
        popupContainer = document.createElement('div');
        popupContainer.setAttribute('id','popupContainer');
        body.appendChild(popupContainer);
    }

    YAHOO.popup.panel = 
        new YAHOO.widget.Panel("popupContainer", 
            {
            effect:{effect:YAHOO.widget.ContainerEffect.FADE,duration:0.25}, 
            underlay:"none", 
            close:true, 
            visible:false, 
            draggable:true, 
            constraintoviewport: true, 
            modal:false
            }
        );
    YAHOO.popup.panel.hideEvent.subscribe(panelHideCallback);

    if(type == 'faq'){
        popupUrl = '/././content/howto/index.html?mode=dl&id=_'+id;
        popupRequest = YAHOO.util.Connect.asyncRequest('GET', popupUrl, faqCallback);
    }
    if(type == 'local'){
        localPopup(id);
    }


}
function panelHideCallback(type){
    YAHOO.popup.panel.destroy();
}

function Popup(){
}
Popup.prototype.render = function(){
    // constrain popup placement to viewport
    var clientWidth = document.body.clientWidth;
    if(this.locX + this.width > clientWidth){
        this.locX = (this.locX - this.width) > 0 ? (this.locX - this.width) - 5 : this.locX;
    }
    YAHOO.popup.panel.setHeader(this.title);
    YAHOO.popup.panel.setBody(this.body);
    YAHOO.popup.panel.cfg.setProperty('height',this.height+'px');
    YAHOO.popup.panel.cfg.setProperty('width',this.width+'px');
    YAHOO.popup.panel.cfg.setProperty('X',this.locX+5);
    YAHOO.popup.panel.cfg.setProperty('Y',this.locY-140);
    YAHOO.popup.panel.render();
    YAHOO.popup.panel.show();

    // webtrends call
    dcsMultiTrack('WT.ti','YUI Pop-up [' + this.type + ':'+ this.id + '] -- ' + this.title);
};

function faqSuccessHandler(o){
    var popup, f, m, faqid, faqUrl;
    popup = new Popup();
    f = o.responseXML.documentElement;
    m = f.getElementsByTagName('meta')[0];
    faqid = f.getElementsByTagName('a')[0].getAttribute('id');
   faqUrl = '/././howto/index.html?id=' + faqid;
    popup.type = 'faq';
    popup.id = faqid;
    popup.title = f.getElementsByTagName('a')[0].firstChild.data;
    popup.body = f.getElementsByTagName('dd')[0].firstChild.data+'&nbsp;<a href="'+faqUrl+'">More</a>';
    popup.height = '135';
    popup.width =  (popup.title.length*6.5 > 250 ) ? popup.title.length*6.5 : 250;
    popup.locX = X;
    popup.locY = Y;
    popup.render();
}
function faqFailureHandler(o){
    YAHOO.popup.panel.setHeader("Error");
    YAHOO.popup.panel.setBody("Can't load FAQ");
    YAHOO.popup.panel.render(document.body);
    YAHOO.popup.panel.show();
}

function localPopup(elmId){
    var popup = new Popup(),
    	elm = (document.getElementById(elmId)) ? document.getElementById(elmId) : 0;
    popup.type = 'local';
    popup.id = elmId;
    popup.title = (elm.getAttribute('title')) ? elm.getAttribute('title') : 0;
    popup.body = (document.getElementById(elmId)) ? document.getElementById(elmId).innerHTML : 0;
    popup.height = '135';
    popup.width =  ( popup.title.length*6.5 > 250 ) ? popup.title.length*6.5 : 250;
    popup.locX = X;
    popup.locY = Y;
    popup.render();
}
