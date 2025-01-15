describe('Google Analytics Tracking', () => {

    beforeEach(() => {
        cy.viewport(1101, 1500);
        cy.visit('/cypress-test/index.html');
    });
    // this is basic ento-to-end test of GA tracking
    //the rest of google - GA4.js should be tested in unit tests

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
        cy.visit('/cypress-test/help/searchtools.html')
        cy.get('.btn.alt').first().as('popup');
        cy.intercept('POST', 'https://www.google-analytics.com/g/collect*', (req) => {
            if (req.body.includes('ONSITE')) {
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
            if (!(req.body.includes('dl=%2FONSITE') && !req.url.includes('en='))) {
                req.alias = 'gaCollect';
            }
        });

        cy.get('@internalLink').click();

        cy.wait('@gaCollect');
    });

    it('test metasearch and google analytics', () => {
        cy.visit('/cypress-test/search.html?q=skin&source=clinical-all');
        cy.intercept('POST', 'https://www.google-analytics.com/g/collect*', (req) => {
            if (req.body.includes('en=lane%3AsearchResultClick')) {
                req.alias = 'gaCollect';
            }
        });
        cy.get('.lwSearchResults a.primaryLink.bookmarking').first().click();
        cy.wait('@gaCollect');
    });

    it('test browse and google analytics', () => {
        cy.visit('/cypress-test/biomed-resources/ej.html?a=a');
        cy.intercept('POST', 'https://www.google-analytics.com/g/collect*', (req) => {
            if (req.body.includes('en=lane%3AbrowseResultClick')) {
                req.alias = 'gaCollect';
            }
        });
        cy.get('.lwSearchResults a.primaryLink.bookmarking').first().click();
        cy.wait('@gaCollect');
    });

    it('test image src tracking analytics', () => {
        cy.visit('/cypress-test/index.html');
        cy.intercept('POST', 'https://www.google-analytics.com/g/collect*', (req) => {
            if (req.body.includes('ep.event_label=laneblog.stanford.edu')) {
                req.alias = 'gaCollect';
            }
        });
        cy.get('.newsfeed img').first().click();
        cy.wait('@gaCollect');
    });


    it('test image alt tracking analytics', () => {
        cy.visit('/cypress-test/index.html');
        cy.intercept('POST', 'https://www.google-analytics.com/g/collect*', (req) => {
            if (req.body.includes('photo')) {
                req.alias = 'gaCollect';
            }
        });
        cy.get('.slide-container .slide img').first().click();
        cy.wait('@gaCollect');
    });


})