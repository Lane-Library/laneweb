describe('Altmetric', () => {
  it('loads the required altmetric script elements', () => {
    cy.visit('/search.html?q=32764183&source=all-all');
    cy.get('script[src*="https://badge.dimensions.ai/badge.js"]').should('have.length', 1);
    // next script only loads for folks auth'd or on campus
    //cy.get('script[src*="https://d1bxh8uas1mnw7.cloudfront.net/assets/embed.js"]').should('have.length', 1);
  })
})