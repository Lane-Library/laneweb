describe('Form submited Test Case', () => {

    beforeEach(() => {
        cy.viewport(1101, 1750);
        cy.visit('/cypress-test/contacts/index.html#sfp-book');
        cy.get("#sfp-book form").as('form');
        cy.get("#sfp-book button[type='submit']").as('submit');
    });

    it('Form submited', () => {
        cy.get('@form').should('exist');
        cy.get('@submit').should('exist');
        cy.get('@submit').should('be.visible');
        cy.get('@form').should('not.have.class', 'submitted');
        cy.get('@submit', { force: true }).click();
        cy.get('@form').should('have.class', 'submitted');

    });
});