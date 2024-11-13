describe('Back to Top', () => {
    it('can selectively see the back to top button', () => {
        cy.viewport(1101, 660);
        cy.visit('/about/about.html');
        cy.get('.back-to-top').as('backToTop');
        cy.get('@backToTop').should('not.be.visible');

        cy.scrollTo('bottom');
        cy.get('@backToTop').should('be.visible');
        cy.window().its('scrollY').should('not.equal', 0);

        cy.get('@backToTop').click();

        cy.window().its('scrollY').should('equal', 0);

    })
})