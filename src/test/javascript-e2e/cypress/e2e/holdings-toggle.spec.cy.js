describe('Holdings Toggle', () => {

    beforeEach(() => {
        cy.visit('/cypress-test/search.html?q=12&source=all-all&facets=recordType:"bib"');

        cy.intercept('/apps/*', {
            statusCode: 200,
            body: '{}'
        });

        cy.intercept('https://www.google-analytics.com/g/collect*', (req) => {
            if (req.body.match(/hldgsTrigger.*close/g)) {
                req.alias = 'gaCollectClose';
            }
            if (req.body.match(/hldgsTrigger.*open/g)) {
                req.alias = 'gaCollectOpen';
            }
            req.reply('OK');
        });

    })

    it('should hide holdings info and report the clicks to GA', () => {

        cy.get('.hldgsTrigger').first().as('hldgsTrigger');
        cy.get('.table-main').first().as('tableMain');
        expect(cy.get('@tableMain').should('be.visible'));
        cy.get('@hldgsTrigger').click();
        expect(cy.get('@tableMain').should('not.be.visible'));
        cy.wait('@gaCollectClose');
    })

    it('should show and then hide holdings info and report the clicks to GA', () => {

        cy.get('.hldgsTrigger').first().as('hldgsTrigger');
        cy.get('.table-main').first().as('tableMain');
        expect(cy.get('@tableMain').should('be.visible'));
        cy.get('@hldgsTrigger').click();
        cy.get('@hldgsTrigger').click();
        expect(cy.get('@tableMain').should('be.visible'));
        cy.wait('@gaCollectOpen');
    })
})