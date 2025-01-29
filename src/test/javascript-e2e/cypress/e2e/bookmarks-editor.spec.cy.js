describe('Bookmark editor', () => {




    beforeEach(() => {
        cy.viewport(1101, 1050);
        cy.visit('/cypress-test/test/test-bookmarks.html');

        cy.get('#bookmarks-editor ul li:nth(3) button[value=edit]').as('editorButton');
        cy.get('#bookmarks-editor ul li:nth(3) input[name=label]').first().as('bookmarkLabel');
        cy.get('#bookmarks-editor ul li:nth(3) input[name=url]').first().as('bookmarkUrl');
        cy.get('#bookmarks-editor ul li:nth(3) button[value=save]').first().as('saveButton');
        cy.get('#bookmarks-editor ul li:nth(3) button[value=reset]').first().as('resetButton');
        cy.get('#bookmarks-editor ul li:nth(3) .editContainer').first().as('editorContainer');
    })


    it('displays Dynamed editor', () => {
        cy.get('@editorButton').click();
        cy.get('@editorContainer').should('be.visible');
        cy.get('@bookmarkLabel').should('have.value', 'DynaMed');
        cy.get('@bookmarkUrl').should('have.value', 'http://search.ebscohost.com/login.aspx?authtype=ip,uid&custid=ns260787&profile=dmp&groupid=main');
    })


    it('Undo label updates', () => {
        cy.get('@editorButton').click();
        cy.get('@bookmarkLabel').type('_test');
        cy.get('@bookmarkLabel').should('have.value', 'DynaMed_test');
        cy.get('@resetButton').click();
        cy.get('@bookmarkLabel').should('have.value', 'DynaMed');
    })

    it('test blank label updates', () => {
        cy.get('@editorButton').click();
        cy.get('@bookmarkLabel').clear();
        cy.get('@bookmarkLabel').should('have.attr', 'placeholder', 'Name');

    })

    it('Undo url updates', () => {
        cy.get('@editorButton').click();
        cy.get('@bookmarkUrl').type('_test');
        cy.get('@bookmarkUrl').should('have.value', 'http://search.ebscohost.com/login.aspx?authtype=ip,uid&custid=ns260787&profile=dmp&groupid=main_test');
        cy.get('@resetButton').click();
        cy.get('@bookmarkUrl').should('have.value', 'http://search.ebscohost.com/login.aspx?authtype=ip,uid&custid=ns260787&profile=dmp&groupid=main');
    })

    it('test blank Url updates', () => {
        cy.get('@editorButton').click();
        cy.get('@bookmarkUrl').clear();
        cy.get('@bookmarkUrl').focus();
        cy.get('@bookmarkUrl').should('have.value', 'https://');
    })

    it('test a Fail update save', () => {
        cy.intercept(
            'PUT',
            '/bookmarks',
            {
                statusCode: 404
            }).as('updateBookmark');
        cy.get('@editorButton').click();
        cy.get('@bookmarkLabel').clear();
        cy.get('@bookmarkLabel').type('DynaMed_test');
        cy.get('@bookmarkUrl').clear();
        cy.get('@bookmarkUrl').type('http://search.ebscohost.com');
        cy.get('@saveButton').click();
        cy.wait('@updateBookmark');
        cy.on('window:alert', (str) => {
            expect(str).to.equal(`Sorry, update bookmark failed. Please reload the page and try again later.`)
        })
    })


    it('test update bookmark', () => {
        cy.intercept(
            'PUT',
            '/bookmarks',
            {
                statusCode: 200,
                body: {
                    "label": "DynaMed_test",
                    "url": "http://search.ebscohost.com"
                }
            }).as('updateBookmark');
        cy.get('@editorButton').click();
        cy.get('@bookmarkLabel').clear();
        cy.get('@bookmarkLabel').type('DynaMed_test');
        cy.get('@bookmarkUrl').clear();
        cy.get('@bookmarkUrl').type('http://search.ebscohost.com');
        cy.get('@saveButton').click();
        cy.wait('@updateBookmark');
        cy.get('#bookmarks-editor ul li:nth(3) input[name=label]').should('have.value', 'DynaMed_test');
        cy.get('#bookmarks-editor ul li:nth(3) input[name=url]').should('have.value', 'http://search.ebscohost.com');
        //check bookmark widget
        cy.get('#bookmarks li a:nth(3)').should('have.attr', 'href', 'http://search.ebscohost.com');
        cy.get('#bookmarks li a:nth(3)').should('contain', 'DynaMed_test');
    })

    it('test delete bookmark', () => {
        cy.intercept(
            'DELETE',
            '/bookmarks?indexes=%5B3%5D',
            {
                statusCode: 200,
            }).as('deleteBookmark');
        cy.get('#bookmarks-editor ul li').should('have.length', 7);
        cy.get('#bookmarks li').should('have.length', 7);
        cy.get('#bookmarks-editor ul li:nth(3) button[value=delete]').click();
        cy.wait('@deleteBookmark');
        cy.get('#bookmarks-editor ul li').should('have.length', 6);
        cy.get('@bookmarkLabel').should('have.not.value', 'DynaMed');
        cy.get('@bookmarkLabel').should('have.not.value', 'PubMed');
        //check bookmark widget
        cy.get('#bookmarks li').should('have.length', 6);
        cy.get('#bookmarks li a:nth(3)').should('contain', 'PubMed');
    })

    it('test a fail delete bookmark', () => {
        cy.intercept(
            'DELETE',
            '/bookmarks?indexes=%5B3%5D',
            {
                statusCode: 404,
            }).as('deleteBookmark');
        cy.get('#bookmarks-editor ul li').should('have.length', 7);
        cy.get('#bookmarks li').should('have.length', 7);
        cy.get('#bookmarks-editor ul li:nth(3) button[value=delete]').click();
        cy.wait('@deleteBookmark');
        cy.on('window:alert', (str) => {
            expect(str).to.equal(`Sorry, delete bookmark failed. Please reload the page and try again later.`)
        })
    })

});