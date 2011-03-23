/**
 * @author ceyates
 */
YUI({
    logInclude: {
        TestRunner: true
    }
}).use('node-event-simulate', 'console', 'test', function(T) {
	
	var response = '<?xml version="1.0" encoding="UTF-8"?>' +
		'<html xmlns="http://www.w3.org/1999/xhtml"><body>' +
		'<a href="/biomed-resources/bassett/bassettsView.html?r=Abdomen--Central Nervous System&amp;pageNumber=0" id="photo-choice">' +
		'<a href="/biomed-resources/bassett/bassettsView.html?r=Abdomen--Central Nervous System&amp;t=diagram&amp;pageNumber=0" id="diagram-choice">' +
		'<span>Diagrams</span></a> </span><div id="thumbnail" class="bassett-images">' +
		'<div class="image-container"><div class="hr"><a href="/biomed-resources/bassett/bassettView.html?bn=151-3"><img title="Kidneys, suprarenal glands and posterior abdominal vessels, nerves and muscles. Aortic plexus, superior hypogastric plexus, testicular plexuses; lymphatic structures along lower part of abdominal aorta" src="http://elane.stanford.edu/public/L254573/small/AD.B.0151.003.L.jpg" alt="bassett Number 151-3"/></a></div><div class="image-text">#151-3<br/><a title="Kidneys, suprarenal glands and posterior abdominal vessels, nerves and muscles. Aortic plexus, superior hypogastric plexus, testicular plexuses; lymphatic structures along lower part of abdominal aorta" href="/biomed-resources/bassett/bassettView.html?bn=151-3"> View Larger</a></div></div><div class="image-container"><div class="hr"><a href="/biomed-resources/bassett/bassettView.html?bn=152-1"><img title="Kidneys, suprarenal glands and posterior abdominal vessels, nerves and muscles. Nerves, blood vessels, and muscle attachments in relation to lumbar vertebrae, close-up lateral view" src="http://elane.stanford.edu/public/L254573/small/AD.B.0152.001.L.jpg" alt="bassett Number 152-1"/></a></div><div class="image-text">#152-1<br/><a title="Kidneys, suprarenal glands and posterior abdominal vessels, nerves and muscles. Nerves, blood vessels, and muscle attachments in relation to lumbar vertebrae, close-up lateral view" href="/biomed-resources/bassett/bassettView.html?bn=152-1"> View Larger</a></div></div></div></body></html>';
	
	Y.lane.Bassett.set("io", function(id, o) {
	    o.on.success(0, {responseText:response}, o.arguments);
	});
	
	var eventOccurred = false;
	
	//FIXME: lane:change is no longer fired so this is broken
	Y.on("lane:change", function() {
		eventOccurred = true;
	});

	var bassettTestCase = new T.Test.Case({
		name : 'Lane Basset Test Case',
		
		setUp : function() {
			eventOccurred = false;
		},

		testAccordeonLink : function() {
			T.all('a').item(0).simulate('click');
			T.Assert.isTrue(eventOccurred);
		},

		testImageLink : function() {
			T.one('#bassettContent').all('a').item(2).simulate('click');
			T.Assert.isTrue(eventOccurred);
		},

		testDiagramLink : function() {
			T.one('#bassettContent').all('a').item(1).simulate('click');
			T.Assert.isTrue(eventOccurred);
		},

		testPhotoLink : function() {
			T.one('#bassettContent').all('a').item(0).simulate('click');
			T.Assert.isTrue(eventOccurred);
		}

	});

    
    T.one('body').addClass('yui3-skin-sam');
    new T.Console({
        newestOnTop: false
    }).render('#log');
    
    T.Test.Runner.add(bassettTestCase);
    T.Test.Runner.run();
});
