describe('Authors Toggle', () => {
  it('displays the authors toggle and shows/hides lots of authors accordingly', () => {
    cy.visit('/search.html?q=18188003&source=all-all');

    cy.get('.authors-hide').as('hiddenAuthors');
    cy.get('.authorsTrigger').as('authorsTrigger');

    cy.get('@authorsTrigger').should('be.visible');
    cy.get('@hiddenAuthors').should('not.be.visible');

    cy.get('@authorsTrigger')
      .invoke('text')
      .should('contain', 'Show More');

    cy.get('@authorsTrigger').click();
    cy.get('@authorsTrigger')
      .invoke('text')
      .should('contain', 'Show Less');
    cy.get('@hiddenAuthors').should('be.visible');

    cy.get('@authorsTrigger').click();
    cy.get('@hiddenAuthors').should('not.be.visible');
  })

})