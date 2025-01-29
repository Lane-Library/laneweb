describe('Zotero Metadata Test', () => {
    it('page should contain DOIs from search results in metadata element', () => {
        cy.visit('/cypress-test/search.html?q=id%3Apubmed-39183222+OR+id%3Apubmed-28971919&source=all-all');

        cy.get('.zotero-metadata').then(metadata => {
            cy.get('li[data-doi]').each(node => {
                cy.wrap(metadata).should('contain.text', 'doi:' + node.attr('data-doi'));
            });
        });
    });
});