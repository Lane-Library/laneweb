describe('Description Toggle', () => {

    it('should show/hide abstract content and report the clicks to GA', () => {

        cy.visit('/search.html?q=25195623&source=all-all');
        
        // intercept the GA request and count the number of descriptionTrigger click events
        cy.intercept('POST', 'https://www.google-analytics.com/g/collect*', (req) => {
            const descriptionTriggerCount = (req.body.match(/descriptionTrigger/g) || []).length;
            expect(descriptionTriggerCount).to.eq(2);
        });

        cy.get('.descriptionTrigger').as('descriptionTrigger');
        cy.get('@descriptionTrigger').invoke('text').should('contain', 'Abstract');
        cy.get('.description').as('description');
        cy.get('@description').should('not.be.visible');

        cy.get('@descriptionTrigger').click();
        cy.get('@description').should('be.visible');

        cy.get('@descriptionTrigger').click();
        cy.get('@description').should('not.be.visible');

    })

})