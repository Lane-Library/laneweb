describe('Course Reserves List', () => {

    it('should show/hide results as search input is typed', () => {

        cy.visit('/cypress-test/biomed-resources/course-reserves.html');

        cy.get('#course-reserves-search').as('searchInput');

        cy.get('tr:visible').should('have.length.gt', 1);

        cy.get('tr:visible').then(($rows) => {
            const initialRowCount = $rows.length;

            cy.get('@searchInput').type('zzzz');
            cy.get('tr:visible').should('have.length', 1);
            cy.get('tr:visible').should('have.length.lt', initialRowCount);

            cy.get('@searchInput').clear();
            cy.get('tr:visible').should('have.length', initialRowCount);

            cy.get('@searchInput').type('bio');
            cy.get('tr:visible').should('have.length.lt', initialRowCount);
        });

    })

})