describe('Menu', () => {

    it('test the mobile  menu', () => {
        cy.viewport(801, 600);
        cy.visit('/cypress-test/index.html');

        // Parameters
        cy.get('.menu-container.mobile').as('mobileMenu');
        cy.get('.menu-container.mobile h2 i.fa-angle-down').as('arrowDown');
        cy.get('.menu-container.mobile h2 i.fa-angle-up').as('arrowUp');
        cy.get('.menu-container.mobile ul').as('menuItems');

        // Check if the menu is closed
        cy.get('@arrowDown').should('be.visible');
        cy.get('@arrowUp').should('be.not.visible');
        cy.get('@menuItems').should('be.not.visible');

        // Open menu
        cy.get('@arrowDown').click();
        cy.get('@arrowDown').should('be.not.visible');
        cy.get('@arrowUp').should('be.visible');
        cy.get('@menuItems').should('be.visible');

        // Close menu
        cy.get('@arrowUp').click();
        cy.get('@arrowUp').should('be.not.visible');
        cy.get('@arrowDown').should('be.visible');
        cy.get('@menuItems').should('be.not.visible');

    })
})