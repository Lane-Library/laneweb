
describe('hide table columns', () => {

  it('should hide columns without data', () => {

    cy.visit('/search.html?q=206242&source=catalog-all&facets=recordType%3A%22bib%22');

    cy.get('.hide-empty-columns').as('table');

    // the table should have more than one row
    cy.get('@table').find('.table-row').should('have.length.greaterThan', 1);

    // first 3 rows should be visible
    cy.get('.hide-empty-columns .table-head:nth-child(-n+3), .hide-empty-columns .table-cell:nth-child(-n+3)').each(($el) => {
      expect($el).to.not.have.css('display', 'none');
    })

    // last 3 rows should be hidden
    cy.get('.hide-empty-columns .table-head:nth-last-child(-n+2), .hide-empty-columns .table-cell:nth-last-child(-n+2)').each(($el) => {
      expect($el).to.have.css('display', 'none');
    })
  })

})
