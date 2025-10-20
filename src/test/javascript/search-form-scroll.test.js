require('@/lane.js');
require('@/util.js');

beforeEach(() => {
    // reset modules to ensure a clean state for Model, document, etc.
    jest.resetModules();

    const div = document.createElement('div');
    div.innerHTML = `
      <form id="search">form element required by search-form-scroll.js</form>
    `;
    document.body.appendChild(div);

    // mock window.location.pathname
    delete window.location;
    window.location = {
        pathname: "/search.html"
    };

    // Mock form offsetTop ... jsdom doesn't support element offsets
    // https://github.com/jsdom/jsdom/issues/135
    Object.defineProperty(document.forms[0], 'offsetTop', {
        value: 200,
        writable: true
    });

    // Mock scroll function
    window.scrollTo = jest.fn();

});

test('search form scrolling', () => {

    require('@/search-form-scroll.js');

    expect(window.scrollY).toBe(0);

    expect(window.scrollTo).toHaveBeenCalledWith({ "top": 70, "behavior": "smooth" });

});
