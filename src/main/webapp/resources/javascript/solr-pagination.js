(function() {
        Y.all('.s-pagination form[name=pagination]').on('submit', function (e) {
            var form = e.target, 
            p = form.get('page'), 
            parent = form.get('parentNode'), 
            page = Number(p.get('value').replace(/[^\d]/g,'')), 
            pages = form.get('pages');
            p.set('value',page);
            if (page < 1 || page > Number(pages.get('value'))) {
                e.preventDefault();
                if (!parent.one(".error")) {
                    parent.insert('<div class="error">ERROR: page out of range</div>',0);
                }
                return;
            }
            pages.remove();
          }, this);
})();