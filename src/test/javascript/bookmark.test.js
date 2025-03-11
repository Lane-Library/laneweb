require('@/lane.js');
require('@/util.js');
require('@/bookmark.js');

describe('Bookmark', () => {
    let Bookmark = L.Bookmark, bookmark = null;

    beforeEach(() => {
        bookmark = new Bookmark("label", "url");
    });

    test('should not create a new bookmark with no parameters', () => {
        expect(() => new Bookmark()).toThrow();
    });

    test('should not create a new bookmark with no label', () => {
        expect(() => new Bookmark(null, "url")).toThrow("null or empty newLabel");
    });

    test('should not create a new bookmark with no URL', () => {
        expect(() => new Bookmark("label", null)).toThrow("null or empty newUrl");
    });

    test('should not set null values', () => {
        expect(() => bookmark.setValues(null, null)).toThrow("null or empty newLabel");
        expect(bookmark.getLabel()).toBe("label");
        expect(bookmark.getUrl()).toBe("url");
    });

    test('should get and set label', () => {
        bookmark.setLabel("newlabel");
        expect(bookmark.getLabel()).toBe("newlabel");
    });

    test('should get and set URL', () => {
        bookmark.setUrl("newurl");
        expect(bookmark.getUrl()).toBe("newurl");
    });

    test('should get and set values', () => {
        bookmark.setValues("newlabel", "newurl");
        expect(bookmark.getLabel()).toBe("newlabel");
        expect(bookmark.getUrl()).toBe("newurl");
    });

    test('should trigger change event on set label', () => {
        const mockCallback = jest.fn();
        bookmark.on("valueChange", mockCallback);
        bookmark.setLabel("newlabel");
        expect(mockCallback).toHaveBeenCalledWith(expect.objectContaining({ newLabel: "newlabel" }));
    });

    test('should trigger change event on set URL', () => {
        const mockCallback = jest.fn();
        bookmark.on("valueChange", mockCallback);
        bookmark.setUrl("newurl");
        expect(mockCallback).toHaveBeenCalledWith(expect.objectContaining({ newUrl: "newurl" }));
    });

    test('should trigger change event on set values', () => {
        const mockCallback = jest.fn();
        bookmark.on("valueChange", mockCallback);
        bookmark.setValues("newlabel", "newurl");
        expect(mockCallback).toHaveBeenCalledWith(expect.objectContaining({ newLabel: "newlabel", newUrl: "newurl" }));
    });

    test('should return string Bookmark', () => {
        expect(bookmark.toString()).toBe("Bookmark{label:label,url:url}");
    });
});
