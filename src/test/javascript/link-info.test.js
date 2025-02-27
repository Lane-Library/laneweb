require('@/lane.js');
require('@/util.js');
require('@/model.js');
require('@/link-info.js');

test('local link', () => {
    const anchor = document.createElement('a');
    anchor.href = '../index,html';
    const linkInfo = new L.LinkInfo(anchor);
    expect(linkInfo.local).toBe(true);
});

test('link host', () => {
    const anchor = document.createElement('a');
    anchor.href = 'https://www.example.com';
    let linkInfo = new L.LinkInfo(anchor);
    expect(linkInfo.linkHost).toBe('www.example.com');
});

test('proxy login link', () => {
    const anchor = document.createElement('a');
    anchor.href = '/secure/apps/proxy/credential?url=http://www.nejm.org/';
    const linkInfo = new L.LinkInfo(anchor);
    expect(linkInfo.proxyLogin).toBe(true);
    expect(linkInfo.local).toBe(false);
    expect(linkInfo.url).toBe('http://www.nejm.org/');
});

test('proxy links', () => {
    const anchor = document.createElement('a');
    anchor.href = 'http://laneproxy.stanford.edu/login?user=ditenus&amp;ticket=tekcit&amp;url=http://www.nejm.org/';
    let linkInfo = new L.LinkInfo(anchor);
    expect(linkInfo.proxy).toBe(true);
    anchor.href = 'https://login.laneproxy.stanford.edu/login?user=ditenus&amp;ticket=tekcit&amp;url=http://www.nejm.org/';
    linkInfo = new L.LinkInfo(anchor);
    expect(linkInfo.proxy).toBe(true);
});


test('link url', () => {
    const anchor = document.createElement('a');
    anchor.href = 'http://www.example.com/example';
    const linkInfo = new L.LinkInfo(anchor);
    expect(linkInfo.url).toBe('http://www.example.com/example');

});

test('proxy url', () => {
    const anchor = document.createElement('a');
    anchor.href = 'http://laneproxy.stanford.edu/login?user=ditenus&amp;ticket=tekcit&amp;url=http://www.nejm.org/';
    let linkInfo = new L.LinkInfo(anchor);
    expect(linkInfo.url).toBe('http://www.nejm.org/');
    anchor.href = 'https://login.laneproxy.stanford.edu/login?user=ditenus&amp;ticket=tekcit&amp;url=https://www.nejm.org/';
    linkInfo = new L.LinkInfo(anchor);
    expect(linkInfo.url).toBe('https://www.nejm.org/');
});

test('link titles where @rel attributes contain expected titles', () => {
    const div = document.createElement('div');
    div.innerHTML = `
    <a href="https://www.google.com/" rel="Google">Google</a>
    <a href="http://www.google.com:80/" rel="Google">
      <img src="/favicon.ico" alt="Google" />
    </a>
    <a href="https://www.google.com/" title="Google" rel="Google">
        <img src="/favicon.ico" alt="trackable external" />
    </a>
    <a href="https://www.google.com/" rel="Google with nested markup">Google <span>with
        nested markup</span>
    </a>
    <a href="https://www.google.com/" rel="http://www.google.com/intl/en_ALL/images/logo.gif">
        <img alt="" src="http://www.google.com/intl/en_ALL/images/logo.gif" />
    </a>
  `;
    const anchors = div.querySelectorAll('a');
    anchors.forEach(anchor => {
        const linkInfo = new L.LinkInfo(anchor);
        expect(linkInfo.title).toBe(anchor.rel);
    });
});

test('local popup title', () => {
  const anchor = document.createElement('a');
  anchor.rel = 'popup local';
  const linkInfo = new L.LinkInfo(anchor);
  expect(linkInfo.title).toBe('YUI Pop-up [local]: unknown');
});

test('local link query', () => {
  const anchor = document.createElement('a');
  anchor.href = '/index.html?foo=bar';
  let linkInfo = new L.LinkInfo(anchor);
  expect(linkInfo.query).toBe('?foo=bar');
});

test('trackable link', () => {
  const anchor = document.createElement('a');
  anchor.href = '/index.html';
  let linkInfo = new L.LinkInfo(anchor);
  expect(linkInfo.trackable).toBe(false);
});

test('non-trackable link', () => {
  const anchor = document.createElement('a');
  anchor.href = 'https://www.example.com/path.html';
  let linkInfo = new L.LinkInfo(anchor);
  expect(linkInfo.trackable).toBe(true);
});

test('link tracking data', () => {
  const anchor = document.createElement('a');
  anchor.href = '/secure/apps/proxy/credential?url=https://test.com/path.jsp';
  let linkInfo = new L.LinkInfo(anchor);
  expect(linkInfo.trackingData.host).toBe('test.com');
  expect(linkInfo.trackingData.path).toBe('/path.jsp');
  expect(linkInfo.trackingData.query).toBe('');
  expect(linkInfo.trackingData.title).toBe('unknown');
  expect(linkInfo.trackingData.external).toBe(true);
  anchor.href = 'https://test2.com/path2.jsp';
  anchor.title = 'tracking title';
  linkInfo = new L.LinkInfo(anchor);
  expect(linkInfo.trackingData.host).toBe('test2.com');
  expect(linkInfo.trackingData.path).toBe('/path2.jsp');
  expect(linkInfo.trackingData.query).toBe('');
  expect(linkInfo.trackingData.title).toBe('tracking title');
  expect(linkInfo.trackingData.external).toBe(true);
});
