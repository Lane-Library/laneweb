describe('Holdings Toggle', () => {

    it('should show/hide holdings info and report the clicks to GA', () => {

        cy.visit('/search.html?q=12&source=all-all&facets=recordType:"bib"');

        // intercept the GA request and count the number of click events
        cy.intercept('POST', 'https://www.google-analytics.com/g/collect*', (req) => {
            const holdingsTriggerCount = (req.body.match(/hldgsTrigger/g) || []).length;
            expect(holdingsTriggerCount).to.eq(2);
        });

        cy.get('.hldgsTrigger').first().as('hldgsTrigger');
        cy.get('.table-main').first().as('tableMain');
        expect(cy.get('@tableMain').should('be.visible'));
        cy.get('@hldgsTrigger').click();
        expect(cy.get('@tableMain').should('not.be.visible'));
        cy.get('@hldgsTrigger').click();
        expect(cy.get('@tableMain').should('be.visible'));
    })

})