describe('Google Analytics Tracking', () => {

    beforeEach(() => {
        cy.viewport(1101, 1500);
        cy.visit('/cypress-test/index.html');
        cy.intercept('POST', 'https://www.google-analytics.com/g/collect*').as('gaCollect');
    });
    // this is basic ento-to-end test of GA tracking
    //the rest of google - GA4.js should be tested in unit tests

    it('external click should send tracking event data to GA', () => {

        // find first visible external link
        cy.get('.content a[href^="http"]').filter(':visible').first().as('externalLink');

        // intercept external link request
        // this allows the test to continue without actually navigating to the external link
        cy.get('@externalLink').then(($link) => {
            const externalLinkHref = $link.attr('href');
            cy.intercept(externalLinkHref, {
                statusCode: 200,
                body: 'OK'
            });
        });

        cy.get('@externalLink').click();

        cy.waitForInterceptions('@gaCollect', (interception) => {
            return interception.request.body.includes('OFFSITE-CLICK-EVENT');
        }, 1).then((filteredInterceptions) => {
            expect(filteredInterceptions).to.have.length(1);
        });
    })

    it('internal click should send tracking event data to GA', () => {
        // find first visible internal link
        cy.visit('/cypress-test/help/searchtools.html')
        cy.get('.btn.alt').first().as('popup');

        cy.get('@popup').click();

        cy.waitForInterceptions('@gaCollect', (interception) => {
            return interception.request.body.includes('ONSITE');
        }, 1).then((filteredInterceptions) => {
            expect(filteredInterceptions).to.have.length(1);
        });
    });

    it('internal click should not send tracking event data to GA', () => {
        // find first visible internal link
        cy.get('.search-help a[href^="/help"]').filter(':visible').first().as('internalLink');

        cy.get('@internalLink').click();

        cy.waitForInterceptions('@gaCollect', (interception) => {
            const body = interception.request.body;
            console.log('GA Body:', body);
            return body && !body.includes('dl=%2FONSITE');
        }, 1).then((filteredInterceptions) => {
            // should only be one interception with a body 
            // and that body should not include 'dl=%2FONSITE'
            expect(filteredInterceptions).to.have.length(1);
        });
    });

    it('test metasearch and google analytics', () => {
        cy.visit('/cypress-test/search.html?q=skin&source=clinical-all&facet=uptodate');
        cy.intercept('GET', 'https://www.uptodate.com/**', {
            statusCode: 200,
            body: '<html><body>test</body></html>'
        }).as('uptodateResult');
        cy.get('.lwSearchResults a.primaryLink.bookmarking').first().click();
        cy.wait('@uptodateResult');
        cy.waitForInterceptions('@gaCollect', (interception) => {
            return interception.request.body.includes('en=lane%3AsearchResultClick');
        }, 1).then((filteredInterceptions) => {
            expect(filteredInterceptions).to.have.length(1);
        });
    });

    it('test image src tracking analytics', () => {
        cy.intercept('GET', 'https://laneblog.stanford.edu/**', {
            statusCode: 200,
            body: '<html><body>test</body></html>'
        }).as('blogImage');
        cy.get('.newsfeed img').first().click();
        cy.wait('@blogImage');
        cy.waitForInterceptions('@gaCollect', (interception) => {
            return interception.request.body.includes('ep.event_label=laneblog.stanford.edu');
        }, 1).then((filteredInterceptions) => {
            expect(filteredInterceptions).to.have.length(1);
        });
    });

    it('test image alt tracking analytics', () => {
        cy.intercept('GET', 'https://profiles.stanford.edu/**', {
            statusCode: 200,
            body: 'OK'
        }).as('profile');
        cy.wait(1000);
        cy.get('.slide-container .slide img').first().click();
        cy.wait('@profile');
        cy.waitForInterceptions('@gaCollect', (interception) => {
            return interception.request.url.includes('photo');
        }, 1).then((filteredInterceptions) => {
            expect(filteredInterceptions).to.have.length(1);
        });
    });

})