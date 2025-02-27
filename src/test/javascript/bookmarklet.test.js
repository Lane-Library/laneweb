require('@/lane.js');
require('@/util.js');

beforeEach(() => {
  // clear the document body before each test
  document.body.innerHTML = '';

  const div = document.createElement('div');
  div.innerHTML = `
      <div id="bookmarklet" class="bookmarklet">bookmarklet</div>
      <div class="bookmarklet-edge" style="display:none">bookmarkletIE</div>
    `;
  document.body.appendChild(div);

});

test('default bookmarklet instructions hidden, Edge instructions shown', () => {
  // override the getUserAgent method
  L.getUserAgent = function () {
    return "fake Edge user agent";
  }
  require('@/lane-bookmarklet.js');
  const nonEdge = document.querySelector('#bookmarklet');
  expect(nonEdge.style.display).toBe('none');
  const edge = document.querySelector('.bookmarklet-edge');
  expect(edge.style.display).toBe('block');
});

test('default bookmarklet instructions shown, Edge instructions hidden', () => {
  require('@/lane-bookmarklet.js');
  const nonEdge = document.querySelector('#bookmarklet');
  expect(nonEdge.style.display).toBe('');
  const edge = document.querySelector('.bookmarklet-edge');
  expect(edge.style.display).toBe('none');
});
