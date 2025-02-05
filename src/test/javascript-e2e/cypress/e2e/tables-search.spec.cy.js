describe('Course Reserves List', () => {

    it('should show/hide results as search input is typed', () => {

        cy.visit('/biomed-resources/course-reserves.html');

        cy.get('#table-search-input').as('searchInput');

        cy.get('.row:visible').should('have.length.gt', 1);

        cy.get('.row:visible').then(($rows) => {
            const initialRowCount = $rows.length;

            cy.get('@searchInput').type('zzzz');
            cy.get('.row:visible').should('have.length', 0);
            cy.get('.row:visible').should('have.length.lt', initialRowCount);

            cy.get('@searchInput').clear();
            cy.get('.row:visible').should('have.length', initialRowCount);

            cy.get('@searchInput').type('bio');
            cy.get('.row:visible').should('have.length.lt', initialRowCount);
        });

    })

})