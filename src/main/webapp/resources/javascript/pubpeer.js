(function() {

    Y.namespace("lane");

    var lane = Y.lane, 
    model = Y.lane.Model,
    basePath = model.get(model.BASE_PATH) || "",
    searchResults = Y.one('#searchResults'),
    enabled = location.search.indexOf("pp=true") > -1,
    PubPeer = {
        syncUI : function(articles) {
            var node, i;
            for (i = 0; i < articles.length; i++){
                if(articles[i].total_comments > 0){
                    node = Y.one(".pp" + articles[i].id);
                    if(node && !node.get('parentNode').one('.pp')){
                        node.insert('<span class="pp sourceLink"> - PubPeer </span><a href="' + articles[i].url + '">Comments</a>','after');
                    }
                }
            }
        },
        getComments : function() {
            var pmidArray = this.getPmids(), i;
            for (i = 0; i < pmidArray.length; i++) {
                Y.io(basePath + '/apps/pubpeer/json/' + pmidArray[i], {
                    on : {
                        success : function(id, o) {
                            this.syncUI(Y.JSON.parse(o.responseText).feedbacks);
                        }
                    },
                    context : this
                });
            }
        },
        getPmids : function() {
            var pmid, pmidArray = [], pmidString = "", i = 0;
            Y.all('.pmid a').each(function(node) {
                pmid = node.get('text');
                if (i++ == 50) { // pp api only handles 50 ids at a time
                    i = 0;
                    pmidArray.push(pmidString.slice(0, -1));
                    pmidString = "";
                }
                pmidString += pmid + ";";
                node.get('parentNode').addClass("pp" + pmid);
            });
            if (pmidString != ""){
                pmidArray.push(pmidString.slice(0, -1));
            }
            return pmidArray;
        }

    };

    lane.PubPeer = PubPeer;
    
    if(searchResults && enabled){
        lane.PubPeer.getComments();
        Y.lane.SearchHistory.on("facetChange",function(e) {
            lane.PubPeer.getComments();
        });
    }
})();