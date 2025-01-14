describe('Suggest', () => {

    beforeEach(() => {
        cy.viewport(1200, 3000);
        cy.visit('/cypress-test/search.html?q=skin&source=all-all');
        // Parameters
        cy.get('input[name=start-year]').as('startYear');
        cy.get('input[name=end-year]').as('endYear');
        cy.get("#solr-date-form input[type=submit]").as('filtering')
    })

    it('should limit the search from 2020 t0 2021', () => {

        cy.get('@startYear').type('2020');
        cy.get('@endYear').type('2021');
        cy.get('@filtering').click();
        cy.url().should('include', 'facets=year%3A%5B2020+TO+2021%5D');
        cy.get('.filter-facet div span').should('have.text', '2020\n                            To\n                            2021');
    });

    it('should get an error message because start date bigger than the end date', () => {
        cy.get('@startYear').type('2021');
        cy.get('@endYear').type('2020');
        cy.get('@filtering').click();
        cy.get('#facet-error-message').should('contains.text', 'The start year should be smaller than the end year');
    });

    it('should get an error message because start date smaller than 1400', () => {
        cy.get('.search-button').click();
        cy.get('@startYear').type('1300');
        cy.get('@endYear').type('2020');
        cy.get('@filtering').click();
        cy.get('input[name=start-year]:invalid').should('have.length', 1)
        cy.get('input[name=start-year]').then(($input) => {
            expect($input[0].validationMessage).to.eq('Value must be greater than or equal to 1400.')
        })
    });


    it('should get an error message because end date greater than 2030', () => {
        cy.get('.search-button').click();
        cy.get('@startYear').type('2020');
        cy.get('@endYear').type('2031');
        cy.get('@filtering').click();
        cy.get('input[name=end-year]:invalid').should('have.length', 1)
        cy.get('input[name=end-year]').then(($input) => {
            expect($input[0].validationMessage).to.eq('Value must be less than or equal to 2030.')
        })
    });



    it('should get an error message because end date is empty', () => {
        cy.get('.search-button').click();
        cy.get('@startYear').type('2020');
        cy.get('@filtering').click();
        cy.get('input[name=end-year]:invalid').should('have.length', 1)
        cy.get('input[name=end-year]').then(($input) => {
            expect($input[0].validationMessage).to.eq('Please fill out this field.')
        })
    });






});