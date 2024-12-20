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
        cy.window().its('L.search').invoke('setQuery', 'query');
        cy.window().its('L.search').invoke('getQuery').should('equal', 'query');
        cy.get('@queryInput').should('have.value', 'query');
    });

    it('testQueryChangeEvent', () => {
        cy.window().then(win => {
            let value;
            win.L.on("search:queryChange", function (event) {
                win.L.on("search:queryChange", function (event) {
                    value = this.search.getQuery();
                });
                win.L.search.setQuery('query');
                expect(value).to.equal('query');
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
                    win.L.search.setQuery('query');
                    expect(oldVal).to.equal('');
                    expect(newVal).to.equal('query');
                });
            });

            it('testSetQueryNull', () => {
                cy.window().its('L.search').invoke('setQuery', 'query');
                cy.window().its('L.search').invoke('setQuery', null);
                cy.window().its('L.search').invoke('getQuery').should('equal', 'query');
                cy.get('@queryInput').should('have.value', 'query');
            });

            it('testSetQueryUndefined', () => {
                cy.window().its('L.search').invoke('setQuery', 'query');
                cy.window().its('L.search').invoke('setQuery', undefined);
                cy.window().its('L.search').invoke('getQuery').should('equal', 'query');
                cy.get('@queryInput').should('have.value', 'query');
            });

            it('testSetGetSource', () => {
                cy.window().its('L.search').invoke('setSource', 'clinical-all');
                cy.window().its('L.search').invoke('getSource').should('equal', 'clinical-all');
                cy.get('@sourceInput').should('have.value', 'clinical-all');
            });

            it('testSourceChangeEvent', () => {
                cy.window().then(win => {
                    let value;
                    win.L.on("search:sourceChange", function (event) {
                        win.L.on("search:sourceChange", function (event) {
                            value = event.newVal;
                        });
                        win.L.search.setSource('clinical-all');
                        expect(value).to.equal('clinical-all');
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
                            win.L.search.setSource('clinical-all');
                            expect(oldVal).to.equal('all-all');
                            expect(newVal).to.equal('clinical-all');
                        });
                    });

                    it('testSearch', () => {
                        cy.window().then(win => {
                            let searched = false;
                            win.L.search.on('search', function (event) {
                                searched = true;
                            });
                            win.L.search.setQuery('query');
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

                        it('testSearchEventBubble', () => {
                            // this is the same as testSearch ... necessary?
                            cy.window().then(win => {
                                let searched = false;
                                win.L.on('search:search', function (event) {
                                    win.L.on('search:search', function (event) {
                                        searched = true;
                                    });
                                    win.L.search.setQuery('query');
                                    win.L.search.search();
                                    expect(searched).to.be.true;
                                });
                            });

                            // it('testInputFocus', () => {
                            //     cy.get('@queryInput').focus();
                            // });

                            it('testCloseClick', () => {
                                cy.window().its('L.search').invoke('setQuery', 'query');
                                cy.get('@closeButton').click();
                                cy.window().its('L.search').invoke('getQuery').should('equal', '');
                            });

                            it('testSubmit', () => {
                                cy.intercept('/search.html?q=query&source=all-all').as('searched');
                                cy.get('@searchForm').get('input[name=q]').type('query');
                                cy.get('@searchForm').submit();
                                cy.wait('@searched')
                            });


                            it('testSubmitNoQuery', () => {
                                cy.get('@searchForm').submit();
                                cy.url().should('not.include', '/search.html');
                            });

                            it('testInputChange', () => {
                                cy.get('@queryInput').type('query');
                                cy.window().its('L.search').invoke('getQuery').should('equal', 'query');
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
                                    win.L.search.setQuery('query');
                                    let searched = false;
                                    win.L.search.on('search', function (event) {
                                        searched = true;
                                    });
                                    win.L.fire('searchDropdown:change', {
                                        newVal: {
                                            source: 'foo',
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
                                    win.L.search.setQuery('');
                                    let searched = false;
                                    win.L.search.on('search:search', function (event) {
                                        win.L.search.on('search:search', function (event) {
                                            searched = true;
                                        });
                                        win.L.fire('searchDropdown:change', {
                                            newVal: {
                                                source: 'foo',
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
