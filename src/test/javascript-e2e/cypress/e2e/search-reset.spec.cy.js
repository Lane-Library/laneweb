describe('Search Reset', () => {

    beforeEach(() => {
        cy.viewport(1200, 3000);
        cy.visit('/cypress-test/index.html');

        // Parameters
        cy.get('input[name=q]').as('input');

    })

    it('should be hide when input is empty', () => {
        cy.get('.search-reset').should('not.be.visible');
    });

    it('should clear the search input when clicking the reset button', () => {
        cy.get('@input').type('test');
        cy.get('.search-reset').should('be.visible');
        cy.get('.search-reset.search-reset-active').click();
        cy.get('@input').should('have.value', '');
        cy.get('.search-reset').should('not.be.visible');
    });

    it("should tracker:trackableEvent fired when clicking the reset button", () => {
        cy.window().then((win) => {
            cy.spy(win.L, 'fire').as('lanewebSpy');
        });

        cy.get('@input').type('test');
        cy.get('.search-reset.search-reset-active').click();

        cy.get('@lanewebSpy').should('have.been.calledWith', 'tracker:trackableEvent', {
            category: "lane:searchFormReset",
            action: "/cypress-test/index.html"
        });
    });

});