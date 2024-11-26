describe('Browzine', () => {

    it('browzine PDF, fulltext and access links display after a search', () => {
        cy.viewport(1101, 750);

        cy.intercept('/apps/browzine/doi/10.1152/ajplung.00077.2021', { fixture: 'browzine/1.json' });
        cy.intercept('/apps/browzine/doi/10.21037/apm-20-2050', { fixture: 'browzine/2.json' });
        cy.intercept('/apps/browzine/doi/10.1089/sur.2021.084', { statusCode: 404 });
        cy.intercept('/apps/browzine/doi/10.1177/08850666231157286', { fixture: 'browzine/3.json' });

        cy.visit('/search.html?q=id%3Apubmed-33949201+OR+id%3Apubmed-33752427+OR+id%3Apubmed-33983849+OR+id%3Apubmed-36803290&source=all-all&sort=title_sort+asc,year+desc');

        cy.get('li[data-index=1] .hldgsContainer a').should('have.attr', 'href').and('match', /.*sfx.stanford.edu.*/);
        cy.get('li[data-index=1] .hldgsContainer a').invoke('text').should('contain', 'Access Options');

        cy.get('li[data-index=2] .hldgsContainer a').should('have.attr', 'href').and('match', /.*libkey.io.*/);
        cy.get('li[data-index=2] .hldgsContainer a').invoke('text').should('contain', 'Direct to PDF');

        cy.get('li[data-index=3] .hldgsContainer a').should('have.attr', 'href').and('match', /.*sfx.stanford.edu.*/);
        cy.get('li[data-index=3] .hldgsContainer a').invoke('text').should('contain', 'Access Options');

        cy.get('li[data-index=4] .hldgsContainer a').should('have.attr', 'href').and('match', /.*libkey.io.*/);
        cy.get('li[data-index=4] .hldgsContainer a').invoke('text').should('contain', 'Direct to Full Text');
    })

})