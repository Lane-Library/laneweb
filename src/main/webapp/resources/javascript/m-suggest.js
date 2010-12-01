(function() {
    LANE.Suggest = function(input,propsObj) {
        propsObj = propsObj || {};
        this.element = (typeof input === "string") ? iui.$(input) : input;
        this.initialize();
        this.setProps(propsObj);
    };
    LANE.Suggest.prototype = {
        _keyupPid : null,
        //TODO: is this too long? 50 ms?
        idleDelay : 800,
        requestLimit : "er-mesh",
        minChars : 3,
        suggestUrl : null,
        initialize : function() {
            this.element.suggest = this;
            this.element.addEventListener("keyup", this.onKeyup,false);
            //this.element.addEventListener("focus", this.position,false);
            this.element.addEventListener("blur", this.cancel,false);
        },
        setProps : function(a) {
            this.idleDelay = (typeof a.idleDelay !== "undefined") ? a.idleDelay
                    : this.idleDelay;
            this.requestLimit = (typeof a.requestLimit !== "undefined") ? a.requestLimit
                    : this.requestLimit;
            this.minChars = (typeof a.minChars !== "undefined") ? a.minChars
                    : this.minChars;
            this.suggestUrl = (typeof a.suggestUrl !== "undefined") ? a.suggestUrl
                    : this.suggestUrl;
        },
        onKeyup : function(e) {
            //console.log("keyupPid->"+this.suggest._keyupPid);
            //console.log("onKeyUp event->");
            //console.log(e);
            if (this.suggest._keyupPid) {
                clearTimeout(this.suggest._keyupPid);
            }
            if (this.value.length >= this.suggest.minChars) {
                this.suggest._keyupPid = setTimeout(function(){
                            e.target.suggest.update(e);
                    },this.suggest.idleDelay);
            } else {
                this.suggest.cancel;
            }
        },
        syncUI : function(data) {
            //console.log("sync UI: this -> ");
            //console.log(this);
            var inputElement = this.element,
                ul = iui.$(this.element.id+"Sgst"),
                li, i;
            //console.log(data.suggest.toString());
            if(data && data.suggest.length){
                ul.parentNode.style.display = 'block';
                while (ul.hasChildNodes()){
                    ul.removeChild(ul.childNodes.item(0));
                }
                for(i = 0; i < data.suggest.length && i < 5; i++){
                    li = document.createElement("li");
                    li.addEventListener("click",function(e){
                        e.inputElement = inputElement;
                        LANE.suggestSelect(e);
                        LANE.track(e);
                        //console.log("inner click");
                        //console.log(inputElement.id);
                        //console.log(e.target.innerHTML);
                    },false);
    //                if(data.suggest[i].length > 37){
    //                    li.innerHTML = data.suggest[i].substring(0,34)+'...';
    //                }
    //                else{
                        li.textContent = data.suggest[i];
    //                }
                    ul.appendChild(li);
                }
                // positioning here makes for bounce
                /*
                setTimeout(function(){
                    scrollTo(0,inputElement.offsetTop+30);
                }, 50);
                */
            }
            else {
                ul.parentNode.style.display = 'none';
            }
        },
        update : function(e) {
            //console.log("update event:->");
            //console.log(e);
            var srElm = document.getElementById("sgstRes"),
                scriptUrl = e.target.suggest.suggestUrl + "?l="+e.target.suggest.requestLimit+"&callback=iui.$('"+e.target.id+"').suggest.syncUI&q=" + encodeURIComponent(e.target.value);
            if (srElm) {
                srElm.parentNode.removeChild(srElm);
            }
            LANE.suggestScriptTag(scriptUrl);
        },
        cancel : function(e) {
            //console.log('cancel and hide');
            var ul = iui.$(e.target.suggest.element.id+"Sgst"),
                f = function(){
                    ul.parentNode.style.display = 'none';
                };
            setTimeout(f,100);
        },
        position : function(e) {
            //console.log('positioning page Y->'+window.pageYOffset);
            //console.log('positioning input->'+e.target.offsetTop);
            setTimeout(function(){
                scrollTo(0,e.target.offsetTop+30);
                //console.log('post positioning page Y->'+window.pageYOffset);
                //console.log('post positioning input->'+e.target.offsetTop);
            }, 50);
        }
    };;
    
    
    LANE.suggestScriptTag = function(b) {
        var a = document.createElement("script");
        a.id = "sgstRes";
        a.type = "text/javascript";
        a.src = b;
        document.body.appendChild(a)
    }
    
    LANE.suggestSelect = function(e) {
        //console.log("LANE suggest select");
        //console.log(e.inputElement.id);
        //console.log(e.target.innerHTML);
        //if this is the laneSearch form then submit, otherwise assume pico and just update input
        e.inputElement.value = e.target.textContent;
        if(e.inputElement.form.id == 'laneSearch'){
            LANE.submitForm(e.inputElement.form);
        }else{
            var ul = iui.$(e.inputElement.id+"Sgst");
            ul.parentNode.style.display = 'none';
        }
        e.preventDefault();
    };
})();
