describe('Bookmark editor', () => {

    beforeEach(() => {
        cy.viewport(1101, 750);
        cy.visit('/cypress-test/test/test-bookmarks.html');
        cy.get('#bookmarks-editor [value=add]').first().as('editorButton');
        cy.get('#bookmarks-editor .editContainer').first().as('editorContainer');
        cy.get('#bookmarks-editor input[name=label]').first().as('bookmarkLabel');
        cy.get('#bookmarks-editor input[name=url]').first().as('bookmarkUrl');
        cy.get('#bookmarks-editor [value=save]').first().as('saveButton');
    })



    it('displays bookmark editor', () => {
        cy.get('@editorButton').should('be.visible');
        cy.get('@editorButton').click();
        cy.get('@editorContainer').should('be.visible');
    })

    it('close bookmark editor', () => {
        cy.get('@editorButton').click();
        cy.get('@bookmarkLabel').should('have.attr', 'placeholder', 'Name');
        cy.get('@bookmarkUrl').should('have.attr', 'placeholder', 'Location');
        cy.get('@editorContainer').should('be.visible');
        cy.get('[value=cancel]').first().click();
        cy.get('@editorContainer').should('be.not.visible');
    })

    it('save empty value', () => {
        cy.get('@editorButton').click();
        cy.get('@saveButton').click();
        cy.get('@bookmarkUrl').should('have.attr', 'placeholder', 'required');
        cy.get('@bookmarkLabel').should('have.attr', 'placeholder', 'required');
    })

    it('test url input value after focus and type', () => {
        cy.get('@editorButton').click();
        cy.get('@bookmarkUrl').focus();
        cy.get('@bookmarkUrl').should('have.value', 'https://');
        cy.get('@bookmarkUrl').type('google.com');
        cy.get('@bookmarkUrl').should('have.value', 'https://google.com');
    })


    it('test a save empty label', () => {
        cy.get('.bookmark-editor-content .actions button[value=edit]').first().click();
        cy.get('@bookmarkLabel').type('{backspace}').type('{backspace}').type('{backspace}').type('{backspace}').type('{backspace}').type('{backspace}');
        cy.get('#bookmarks-editor [value=save]').first().as('saveButton');
        cy.get('@saveButton').click();
        cy.get('@bookmarkLabel').should('have.attr', 'placeholder', 'required');
    })

    it('test clear button', () => {
        cy.get('@editorButton').click();
        cy.get('@bookmarkLabel').type('test bookmark');
        cy.get('@bookmarkUrl').type('google.com');
        cy.get('#bookmarks-editor [value=save]').first().as('resetButton');
        cy.get('@bookmarkLabel').should('have.attr', 'placeholder', 'Name');
        cy.get('@bookmarkUrl').should('have.attr', 'placeholder', 'Location');
    })

    it('test fail saved', () => {
        cy.intercept(
            'POST',
            '/bookmarks',
            {
                statusCode: 404,
                body: { id: 8, label: 'Test bookmark', url: 'google.com' }
            }
        ).as('addBookmark');
        cy.get('#bookmarks-editor ul li').should('have.length', 7);
        cy.get('@editorButton').click();
        cy.get('@bookmarkLabel').type('Test bookmark');
        cy.get('@bookmarkUrl').type('google.com');
        cy.get('@saveButton').click();
        cy.wait('@addBookmark');
        cy.on('window:alert', (str) => {
            expect(str).to.equal(`Sorry, add bookmark failed. Please reload the page and try again later.`)
        })
    })



    it('test saved', () => {
        cy.intercept(
            'POST',
            '/bookmarks',
            {
                statusCode: 200,
                body: { id: 8, label: 'Test bookmark', url: 'google.com' }
            }
        ).as('addBookmark');
        cy.intercept('POST', 'https://www.google-analytics.com/g/collect*en=lane%3AbookmarkAdd*', (req) => {
            if (!(req.url.includes('ep.event_label=Test%20bookmark') && !req.url.includes('en=lane%3AbookmarkAdd'))) {
                req.reply('OK');
                req.alias = 'gaCollect';
            }
        });
        cy.get('#bookmarks-editor ul li').should('have.length', 7);
        cy.get('@editorButton').click();
        cy.get('@bookmarkLabel').type('Test bookmark');
        cy.get('@bookmarkUrl').type('google.com');
        cy.get('@saveButton').click();
        cy.wait('@addBookmark');
        cy.wait('@gaCollect');
        cy.get('#bookmarks-editor ul li').should('have.length', 8);
        cy.get('#bookmarks li').first().should('contain', 'Test bookmark');
        //check bookmark widget
        cy.get('#bookmarks li a').should('have.attr', 'href', '/apps/proxy/credential?url=https://google.com');
    })

    it('test long label', () => {
        cy.intercept(
            'POST',
            '/bookmarks',
            { statusCode: 200 }
        ).as('addBookmark');
        cy.get('@editorButton').click();
        //135 characters label 
        cy.get('@bookmarkLabel').type('To find out the word and character count of your writing, simply copy and paste text into the tool or write directly into the text area');
        cy.get('@bookmarkUrl').type('google.com');
        cy.get('@saveButton').click();
        cy.wait('@addBookmark');
        cy.get('#bookmarks-editor ul li').first().should('contain', 'To find out the word and character count of your writing, simply copy and paste text into the tool or write directly into the text...');
    })


})