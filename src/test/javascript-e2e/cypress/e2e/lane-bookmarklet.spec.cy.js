describe('lane bookmarklet selector', () => {


    it('test not ie and not edge', () => {
        cy.visit('/cypress-test/favorites.html');
        cy.get('#bookmarklet').should('be.visible');
        cy.get('.bookmarklet-edge').should('not.be.visible');
    });


    //** The tests below don't work because the headers are not set as the cypress documentation suggest **/

    // it('test ie ', () => {
    //     cy.visit('/cypress-test/favorites.html', {
    //         headers: {
    //             'user-agent': 'MSIE',
    //         }
    //     });
    //     cy.get('#bookmarkletNotIE').should('not.be.visible');
    //     cy.get('#bookmarkletIE').should('not.visible');
    //     cy.get('.bookmarklet-edge').should('not.be.visible');
    // })

    // it('test edge browser', () => {
    //     cy.visit('/cypress-test/favorites.html', {
    //         headers: {
    //             'user-agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Edge/12.246',
    //         }
    //     });
    //     cy.get('#bookmarkletNotIE').should('not.be.visible');
    //     cy.get('#bookmarkletIE').should('not.be.visible');
    //     cy.get('.bookmarklet-edge').should('be.visible');
    //})
});