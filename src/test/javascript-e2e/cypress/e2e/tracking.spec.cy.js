describe('Tracking', () => {

    beforeEach(() => {
        cy.viewport(1101, 1500);
    });

    it('click on search result should fire lane:searchResultClick event', () => {

        cy.visit('/cypress-test/search.html?q=12&source=catalog-all&facets=recordType%3A"bib"');

        // find first primaryLink
        cy.get('.primaryLink').filter(':visible').as('primaryLink');

        // intercept link request
        // this allows the test to continue without actually navigating to the external link
        cy.get('@primaryLink').then(($link) => {
            const primaryLinkHref = $link.attr('href');
            cy.intercept(primaryLinkHref, {
                statusCode: 200
            });
        });

        cy.intercept('POST', 'https://www.google-analytics.com/g/collect*', (req) => {
            if (req.body.includes('searchResultClick') && req.body.includes('ep.event_label=bib-12%20-%3E%20Journal%20-%3E%20Lancet')) {
                req.alias = 'gaCollect';
            }
        });

        cy.get('@primaryLink').click();

        cy.wait('@gaCollect');
    })

    it('click on CME link reports correct host to GA', () => {

        // set emrid to disable HTTPS redirect
        // Cypress dies with a ERR_EMPTY_RESPONSE (-324) error id redirects to HTTPS on localhost
        cy.visit('/cypress-test/index.html?emrid=disablesHTTPSredirect');

        // find UpToDate link
        cy.get('#uptodate').as('cmeLink');

        // change the href to a CME link and open in new window to avoid need to intercept
        cy.get('@cmeLink').invoke('attr', 'target', '_blank');
        cy.get('@cmeLink').invoke('attr', 'href', '/redirect/cme?url=https://reportable-host4GA.com/contents/');

        cy.intercept('POST', 'https://www.google-analytics.com/g/collect*', (req) => {

            if (req.body.includes('event_label=reportable-host4GA.com')) {
                req.alias = 'gaCollect';
            }
        });

        cy.get('@cmeLink').click();

        cy.wait('@gaCollect');
    })

    it('click on proxy link reports correct host to GA', () => {

        cy.visit('/cypress-test/index.html');

        // find UpToDate link
        cy.get('#uptodate').as('proxyLink');

        // change the href to a proxy link and open in new window to avoid need to intercept
        cy.get('@proxyLink').invoke('attr', 'target', '_blank');
        cy.get('@proxyLink').invoke('attr', 'href', 'https://login.laneproxy.stanford.edu/login?url=http://www.ncbi.nlm.nih.gov/sites/entrez?otool=stanford&holding=F1000%2CF1000M');

        cy.intercept('POST', 'https://www.google-analytics.com/g/collect*', (req) => {

            if (req.body.includes('event_label=www.ncbi.nlm.nih.gov')) {
                req.alias = 'gaCollect';
            }
        });

        cy.get('@proxyLink').click();

        cy.wait('@gaCollect');
    })

})