/**
 * @author ceyates
 */
//create a global Y object:
var Y = YUI({fetchCSS:false}).use('*');

Y.namespace("lane");

Y.publish('lane:change', {broadcast: 1});
