/**
 * @author ceyates
 */
//create a global Y object:
var Y = YUI({fetchCSS:false}).use('*');

Y.namespace("lane");

Y.publish('lane:change', {broadcast: 2});
    
    // add .indexOf functionality to Array if native not present (IE)
    if(!Array.indexOf){
        Array.prototype.indexOf = function(obj){
            for(var i=0; i<this.length; i++){
                if(this[i]==obj){
                    return i;
                }
            }
            return -1;
        };
    }
