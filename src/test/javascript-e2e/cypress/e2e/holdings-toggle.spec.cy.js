describe('Holdings Toggle', () => {

    it('should show and then hide holdings info and report the clicks to GA', () => {

        cy.visit('/cypress-test/search.html?q=12&source=all-all&facets=recordType:"bib"');

        cy.intercept('/apps/*', {
            statusCode: 200,
            body: '{}'
        });

        cy.intercept('https://www.google-analytics.com/g/collect*').as('gaCollect');

        cy.get('.hldgsTrigger').first().as('hldgsTrigger');
        cy.get('.table-main').first().as('tableMain');
        cy.get('@tableMain').should('be.visible');
        cy.get('@hldgsTrigger').click();
        cy.get('@tableMain').should('not.be.visible');

        // wait for 'gaCollect'
        // assert that one GA request was made containing a hldgsTrigger close event
        cy.waitForInterceptions('@gaCollect', (interception) => {
            const urlAndBody = interception.request.url + interception.request.body;
            return urlAndBody.match(/hldgsTrigger.*close/g);
        }, 1).then((filteredInterceptions) => {
            expect(filteredInterceptions).to.have.length(1);
        });

        cy.get('@hldgsTrigger').click();
        cy.get('@tableMain').should('be.visible');

        // wait for 'gaCollect'
        // assert that one GA request was made containing a hldgsTrigger open event
        cy.waitForInterceptions('@gaCollect', (interception) => {
            const urlAndBody = interception.request.url + interception.request.body;
            return urlAndBody.match(/hldgsTrigger.*open/g);
        }, 1).then((filteredInterceptions) => {
            expect(filteredInterceptions).to.have.length(1);
        });
    })
})