describe('Google Analytics Tracking', () => {

    beforeEach(() => {
        cy.viewport(1101, 1500);
        cy.visit('/index.html');
    });
    // this is basic ento-to-end test of GA tracking
    // the rest of google-GA4.js should be tested in unit tests

    it('external click should send tracking event data to GA', () => {

        //     // find first visible external link
        cy.get('.content a[href^="http"]').filter(':visible').first().as('externalLink');

        // intercept external link request
        // this allows the test to continue without actually navigating to the external link
        cy.get('@externalLink').then(($link) => {
            const externalLinkHref = $link.attr('href');
            cy.intercept(externalLinkHref, {
                statusCode: 200
            });
        });

        cy.intercept('POST', 'https://www.google-analytics.com/g/collect*', (req) => {
            if (req.body.includes('OFFSITE-CLICK-EVENT')) {
                req.alias = 'gaCollect';
            }
        });

        cy.get('@externalLink').click();

        cy.wait('@gaCollect');
    })

    it('internal click should  send tracking event data to GA', () => {
        // find first visible internal link
        cy.visit('/help/searchtools.html')
        cy.get('.btn.alt').first().as('popup');
        cy.intercept('POST', 'https://www.google-analytics.com/g/collect*', (req) => {
            if (req.body.includes('dl=%2FONSITE')) {
                req.alias = 'gaCollect';
            }
        });

        cy.get('@popup').click();

        cy.wait('@gaCollect');
    });




    it('internal click should not send tracking event data to GA', () => {
        // find first visible internal link
        cy.get('.search-help a[href^="/help"]').filter(':visible').first().as('internalLink');

        cy.intercept('POST', 'https://www.google-analytics.com/g/collect*', (req) => {
            if (!req.body.includes('dl=%2FONSITE')) {
                req.alias = 'gaCollect';
            }
        });

        cy.get('@internalLink').click();

        cy.wait('@gaCollect');
    });


})