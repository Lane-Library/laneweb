describe('Clinical Toggle', () => {




    beforeEach(() => {
        cy.viewport(1101, 1050);
        cy.visit('/cypress-test/search.html?q=skin&source=clinical-all');
    })

    it('displays clinical All Ages  toggle', () => {
        cy.get('.clinical-toggle-label.clinical-toggle-label-active').should('be.visible').and('contain', 'All ages');
    })


    it('displays clinical children toggle', () => {
        cy.get('.clinical-toggle-label').contains('Children').click();
        cy.get('.clinical-toggle-label.clinical-toggle-label-active').should('be.visible').and('contain', 'Children');
    })
})