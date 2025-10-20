describe('Course Reserves List', () => {

    it('should show/hide results as search input is typed', () => {

        cy.visit('/cypress-test/test/tables-search.html');

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

    it('should show all results after search reset is clicked', () => {

        cy.visit('/cypress-test/test/tables-search.html');

        cy.get('#table-search-input').as('searchInput');

        cy.get('.search-reset').as('searchReset');

        cy.get('.row:visible').should('have.length.gt', 1);

        cy.get('.row:visible').then(($rows) => {
            const initialRowCount = $rows.length;

            cy.get('@searchInput').type('zzzz');
            cy.get('.row:visible').should('have.length', 0);
            cy.get('.row:visible').should('have.length.lt', initialRowCount);

            cy.get('@searchReset').click();
            cy.get('.row:visible').should('have.length', initialRowCount);

        });

    })
})