describe('Google Analytics Tracking', () => {

    // this is basic ento-to-end test of GA tracking
    // the rest of google-GA4.js should be tested in unit tests

    it('external click should send tracking event data to GA', () => {

        cy.visit('/index.html');

        // find first visible external link
        cy.get('a[href^="http"]').filter(':visible').first().as('externalLink');

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

    it('internal click should not send tracking event data to GA', () => {

        cy.visit('/index.html');

        // find first visible internal link
        cy.get('a[href^="/about"]').filter(':visible').first().as('internalLink');

        cy.get('@internalLink').then(($link) => {
            const internalLinkHref = $link.attr('href');
            cy.intercept(internalLinkHref, {
                statusCode: 200
            });
        });

        // intercept the GA request and ensure the request body does not contain the string "ONSITE"
        cy.intercept('POST', 'https://www.google-analytics.com/g/collect*').as('gaCollect');

        cy.get('@internalLink').click();

        cy.wait('@gaCollect').then((interception) => {
            expect(interception.request.body).to.not.include('ONSITE');
        });
    })

})