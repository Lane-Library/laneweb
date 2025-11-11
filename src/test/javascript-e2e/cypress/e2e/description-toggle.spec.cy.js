describe('Description Toggle', () => {

    it('should show/hide abstract content and report the clicks to GA', () => {

        cy.visit('/cypress-test/search.html?q=25195623&source=all-all');

        cy.intercept('POST', 'https://www.google-analytics.com/g/collect*').as('gaCollect');

        cy.get('.descriptionTrigger').as('descriptionTrigger');
        cy.get('@descriptionTrigger').invoke('text').should('contain', 'Abstract');
        cy.get('.description').as('description');
        cy.get('@description').should('not.be.visible');

        // Perform both clicks to trigger the events
        cy.get('@descriptionTrigger').click();
        cy.get('@descriptionTrigger').click();

        // wait for 'gaCollect'
        // assert that one GA request was made containing two descriptionTrigger events
        cy.waitForInterceptions('@gaCollect', (interception) => {
            return interception.request.body.includes('descriptionTrigger');
        }, 1).then((filteredInterceptions) => {
            expect(filteredInterceptions).to.have.length(1);
            expect(filteredInterceptions[0].request.body.match(/descriptionTrigger/g)).to.have.length(2);
        });

    });

});
