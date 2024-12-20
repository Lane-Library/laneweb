describe('Bassett', () => {

    it('displays and hides the tablet Bassett collection menu', () => {
        // default viewport is 1000px x 660px
        cy.visit('/cypress-test/biomed-resources/bassett/index.html');
        cy.get('h2').as('collectionHome');
        cy.get('#abdomen a.see-all').as('abdomenSeeAll');

        cy.get('@collectionHome').should('be.visible');
        cy.get('@abdomenSeeAll').should('not.be.visible');

        cy.get('@collectionHome').click();
        cy.get('@abdomenSeeAll').should('be.visible');
    })

    it('displays and hides the Abdomen See All menu', () => {
        cy.viewport(1101, 750);
        cy.visit('/cypress-test/biomed-resources/bassett/index.html');

        cy.get('#abdomen a.see-all').as('abdomenSeeAll');

        cy.get('#abdomen li').should('have.length.gt', 10);
        cy.get('#abdomen li:visible').should('have.length.lt', 10);

        cy.get('@abdomenSeeAll').click();
        cy.get('#abdomen li:visible').should('have.length.gt', 10);

        cy.get('@abdomenSeeAll').click();
        cy.get('#abdomen li:visible').should('have.length.lt', 10);
    })

    it('shows diagrams instead of images', () => {
        cy.viewport(1101, 750);
        cy.visit('/cypress-test/biomed-resources/bassett/index.html');

        cy.get('#abdomen a.see-all').click();

        // images should be displayed by default
        cy.get('#thumbnail img').each(($img) => {
            expect($img).to.have.attr('src').match(/.*L.jpg/);
        });

        // waits in place for slower processors, like on lane-dev
        cy.get('#diagram-choice').click();
        cy.wait(750);
        cy.get('#thumbnail img').each(($img) => {
            expect($img).to.have.attr('src').match(/.*D.jpg/);
        });

        cy.get('#photo-choice').click();
        cy.wait(750);
        cy.get('#thumbnail img').each(($img) => {
            expect($img).to.have.attr('src').match(/.*L.jpg/);
        });
    })

    it('pagination form contains appropriate page data', () => {
        cy.viewport(1101, 750);
        cy.visit('/cypress-test/biomed-resources/bassett/index.html');

        cy.get('#abdomen a.see-all').click();

        cy.get('.pagingForm').first().as('paginationForm');

        cy.get('@paginationForm').find('input[name=pages]').as('pagesInput');

        // testSubmitRemovesPages
        cy.get('@pagesInput').should('have.value', '5');
        cy.intercept('GET', '/biomed-resources/bassett/bassettsView.html?r=Abdomen&page=1', (req) => {
            expect(req.body).to.not.have.property('pages');
        }).as('formSubmit');
        cy.get('@paginationForm').submit();
        cy.wait('@formSubmit');

    })

    it('pagination form handles error values', () => {
        cy.viewport(1101, 750);
        cy.visit('/cypress-test/biomed-resources/bassett/index.html');

        cy.get('#abdomen a.see-all').click();

        cy.get('.pagingForm').first().as('paginationForm');

        cy.get('@paginationForm').find('input[name=page]').as('pageInput');

        // set pages to foo and submit
        cy.get('@pageInput').type('foo');
        cy.get('@paginationForm').submit();
        cy.get('.bassett-error').should('be.visible');
        cy.get('.bassett-error').should('contain', 'ERROR');

        // testPageLessThanOne
        cy.get('@pageInput').clear().type('0');
        cy.get('@paginationForm').submit();
        cy.get('.bassett-error').should('be.visible');
        cy.get('.bassett-error').should('contain', 'ERROR');

        // testPageMoreThanPages
        cy.get('@pageInput').clear().type('10');
        cy.get('@paginationForm').submit();
        cy.get('.bassett-error').should('be.visible');
        cy.get('.bassett-error').should('contain', 'ERROR');
    })

    it('pagination form handles valid values', () => {
        cy.viewport(1101, 750);
        cy.visit('/cypress-test/biomed-resources/bassett/index.html');

        cy.get('#abdomen a.see-all').click();

        cy.get('.pagingForm').first().as('paginationForm');

        cy.get('@paginationForm').find('input[name=page]').as('pageInput');

        cy.get('@pageInput').clear().type('3');
        cy.intercept('GET', '/biomed-resources/bassett/bassettsView.html?r=Abdomen&page=3').as('formSubmit');
        cy.get('@paginationForm').submit();
        cy.wait('@formSubmit');
    })

})