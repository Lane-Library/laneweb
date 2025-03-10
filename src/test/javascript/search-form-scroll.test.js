require('@/lane.js');
require('@/util.js');

beforeEach(() => {
  // reset modules to ensure a clean state for Model, document, etc.
  jest.resetModules();

  const div = document.createElement('div');
  div.innerHTML = `
      <form>form element required by search-form-scroll.js</form>
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
  window.scroll = jest.fn();

});

test('search form scrolling', () => {

  require('@/search-form-scroll.js');

  expect(window.scrollY).toBe(0);

  expect(window.scroll).toHaveBeenCalledWith(0, 70);

});

test('search form will not immediately scroll w/ Edge browser', () => {

  // override the getUserAgent method
  L.getUserAgent = function () {
    return "fake Edge user agent";
  }

  expect(window.scrollY).toBe(0);

  require('@/search-form-scroll.js');

  expect(window.scroll).not.toHaveBeenCalledWith();

});

test('search form scrolling w/ Edge browser and a little time', async () => {

  // override the getUserAgent method
  L.getUserAgent = function () {
    return "fake Edge user agent";
  }

  expect(window.scrollY).toBe(0);

  require('@/search-form-scroll.js');

  // sleep to allow edgeDelay to run
  await new Promise((r) => setTimeout(r, 1100));

  expect(window.scroll).toHaveBeenCalledWith(0, 70);

});
