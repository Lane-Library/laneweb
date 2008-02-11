YAHOO.util.Event.addListener(window,'load',initialize);

function initialize(e) {
    var pageTracker;
    if("lane.stanford.edu" == getMetaContent('LW.host')){
        pageTracker = _gat._getTracker("UA-3202241-2");
    }
    else{
        pageTracker = _gat._getTracker("UA-3203486-2");
    }
    pageTracker._initData();
    pageTracker._trackPageview();
    pageTracker._setVar(getMetaContent('WT.seg_1'));
}