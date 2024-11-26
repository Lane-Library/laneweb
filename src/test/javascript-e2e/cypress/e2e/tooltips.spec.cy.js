describe('Lane Tooltip Test Case', () => {

    beforeEach(() => {
        cy.visit('/index.html?debug=true');

        cy.get('.tooltip-trigger').first().as('tooltipTrigger');
        cy.get('@tooltipTrigger').invoke('attr', 'id').as('tooltipTriggerId');
        cy.get('@tooltipTriggerId').then((id) => {
            cy.get(`#${id}Tooltip`).as('tooltipContent');
        });
    });


    it('mouseover an element with tooltip content shows the tooltip within the viewport', () => {

        cy.get('.tooltip').should('be.not.exist');

        cy.get('@tooltipTrigger').trigger('mouseenter');
        cy.get('.tooltip').should('be.visible');

        cy.get('.tooltip')
            .then(($tooltip) => {
                cy.get('@tooltipContent').invoke('text').should('eq', $tooltip.text());
                const boundingBox = $tooltip[0].getBoundingClientRect();
                expect(boundingBox.right).to.be.lessThan(Cypress.config('viewportWidth'));
            });

    });

    it('mouseover an element to show a tooltip, mouseout to hide it', () => {

        cy.get('@tooltipTrigger').trigger('mouseenter');

        cy.get('.tooltip').should('be.visible');

        cy.get('@tooltipTrigger').trigger('mouseleave');

        cy.get('.tooltip').should('not.be.exist');
    });

});
