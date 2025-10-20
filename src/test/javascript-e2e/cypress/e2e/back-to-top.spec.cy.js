describe('Back to Top', () => {
    it('should display the back to top button and scroll to the top when clicked', () => {
        cy.viewport(1101, 660);
        cy.visit('/cypress-test/about/about.html');
        cy.get('.back-to-top').as('backToTop');

        cy.scrollTo('bottom');
        cy.get('@backToTop').should('be.visible');
        cy.window().its('scrollY').should('not.equal', 0);

        cy.get('@backToTop').click();

        cy.window().its('scrollY').should('equal', 0);

    })
})