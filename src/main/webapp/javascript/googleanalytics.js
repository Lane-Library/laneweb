var pageTracker;

YAHOO.util.Event.addListener(window,'load',initialize);

function initialize(e) {
    if("lane.stanford.edu" == getMetaContent('LW.host')){
        window.pageTracker = _gat._getTracker("UA-3202241-2");
    }
    else{
        window.pageTracker = _gat._getTracker("UA-3203486-2");
    }
    window.pageTracker._initData();
    window.pageTracker._trackPageview();
    window.pageTracker._setVar(getMetaContent('WT.seg_1'));
}