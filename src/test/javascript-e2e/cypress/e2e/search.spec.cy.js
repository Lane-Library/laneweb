describe('Lane Search Test Case', () => {
    beforeEach(() => {
        cy.visit('/cypress-test/index.html');
        cy.get('.search-form').as('searchForm');
        cy.get('input[name=q]').as('queryInput');
        cy.get('input[name=source]').as('sourceInput');
        cy.get('.search-reset').as('closeButton');
    });

    it('testExists', () => {
        cy.window().its('L.search').should('exist');
    });

    it('testSetGetQuery', () => {
        cy.window().its('L.search').then(search => {
            search.query = 'query';
            expect(search.query).to.equal('query');
        });
        cy.get('@queryInput').should('have.value', 'query');
    });

    it('testQueryChangeEvent', () => {
        cy.window().then(win => {
            let value;
            win.L.on("search:queryChange", function (event) {
                win.L.on("search:queryChange", function (event) {
                    value = this.search.query;
                });
                win.L.search.query = 'query';
                expect(value).to.equal('query');
            });
        });
    });

    it('testQueryChangeEventBubble', () => {
        cy.window().then(win => {
            let newVal, oldVal;
            win.L.on('search:queryChange', function (event) {
                win.L.on('search:queryChange', function (event) {
                    newVal = event.newVal;
                    oldVal = event.oldVal;
                });
                win.L.search.query = 'query';
                expect(oldVal).to.equal('');
                expect(newVal).to.equal('query');
            });
        });
    });

    it('testSetQueryNull', () => {
        cy.window().its('L.search').then(search => {
            search.query = 'query';
            search.query = null;
            expect(search.query).to.equal('query');
        });
        cy.get('@queryInput').should('have.value', 'query');
    });

    it('testSetQueryUndefined', () => {
        cy.window().its('L.search').then(search => {
            search.query = 'query';
            search.query = undefined;
            expect(search.query).to.equal('query');
        });
        cy.get('@queryInput').should('have.value', 'query');
    });

    it('testSetGetSource', () => {
        cy.window().its('L.search').then(search => {
            search.source = 'clinical-all';
            expect(search.source).to.equal('clinical-all');
        });
        cy.get('@sourceInput').should('have.value', 'clinical-all');
    });

    it('testSourceChangeEvent', () => {
        cy.window().then(win => {
            let value;
            win.L.on("search:sourceChange", function (event) {
                win.L.on("search:sourceChange", function (event) {
                    value = event.newVal;
                });
                win.L.search.source = 'clinical-all';
                expect(value).to.equal('clinical-all');
            });
        });
    });

    it('testSourceChangeEventBubble', () => {
        cy.window().then(win => {
            let newVal, oldVal;
            win.L.on("search:sourceChange", function (event) {
                win.L.on("search:sourceChange", function (event) {
                    newVal = event.newVal;
                    oldVal = event.oldVal;
                });
                win.L.search.source = 'clinical-all';
                expect(oldVal).to.equal('all-all');
                expect(newVal).to.equal('clinical-all');
            });
        });
    });

    it('testSearch', () => {
        cy.window().then(win => {
            let searched = false;
            win.L.search.on('search', function (event) {
                searched = true;
            });
            win.L.search.query = 'query';
            win.L.search.search();
            expect(searched).to.be.true;
        });
    });

    it('testSearchNoQuery', () => {
        cy.window().then(win => {
            let searched = false;
            win.L.search.on('search:search', function (event) {
                win.L.search.on('search:search', function (event) {
                    searched = true;
                });
                win.L.search.search();
                expect(searched).to.be.false;
            });
        });
    });

    it('testSearchEventBubble', () => {
        // this is the same as testSearch ... necessary?
        cy.window().then(win => {
            let searched = false;
            win.L.on('search:search', function (event) {
                win.L.on('search:search', function (event) {
                    searched = true;
                });
                win.L.search.query = 'query';
                win.L.search.search();
                expect(searched).to.be.true;
            });
        });
    });
    // it('testInputFocus', () => {
    //     cy.get('@queryInput').focus();
    // });

    it('testCloseClick', () => {
        cy.get('@queryInput').type('query');
        cy.get('@closeButton').click();
        cy.get('@queryInput').should('have.value', '');
    });

    it('testSubmit', () => {
        cy.intercept('/search.html?q=query&source=all-all').as('searched');
        cy.get('@searchForm').get('input[name=q]').type('query');
        cy.get('@searchForm').submit();
        cy.wait('@searched');
    });


    it('testSubmitNoQuery', () => {
        cy.get('@searchForm').submit();
        cy.url().should('not.include', '/search.html');
    });

    it('testInputChange', () => {
        cy.get('@queryInput').type('query');
        cy.window().its('L.search.query').should('equal', 'query');
    });

    it('test reset clears query', () => {
        cy.get('@queryInput').type('query');
        cy.window().then(win => {
            win.L.fire('searchReset:reset');
        });
        cy.get('@searchForm').get('input[name=q]').should('have.value', '');
    });

    it('searchDropdown:change submits if query', () => {
        cy.window().then(win => {
            win.L.search.query = 'query';
            let searched = false;
            win.L.search.on('search', function (event) {
                searched = true;
            });
            win.L.fire('searchDropdown:change', {
                newVal: {
                    source: 'foo',
                    "foo": {
                        "placeholder": "foo"
                    }
                }
            });
            expect(searched).to.be.true;
        });
    });

    it('searchDropdown:change doesn\'t submit if no query', () => {
        cy.window().then(win => {
            win.L.search.query = '';
            let searched = false;
            win.L.search.on('search:search', function (event) {
                searched = true;
            });
            win.L.fire('searchDropdown:change', {
                newVal: {
                    source: 'foo',
                    "foo": {
                        "placeholder": "foo"
                    }
                }
            });
            expect(searched).to.be.false;
        });
    });
});
