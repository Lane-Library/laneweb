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
        expect(() => new Bookmark(null, "url")).toThrow("Bookmark label cannot be null or empty.");
    });

    test('should not create a new bookmark with no URL', () => {
        expect(() => new Bookmark("label", null)).toThrow("Bookmark URL cannot be null or empty.");
    });

    test('should not set null values', () => {
        expect(() => bookmark.setValues(null, null)).toThrow("Bookmark label cannot be null or empty.");
        expect(bookmark.label).toBe("label");
        expect(bookmark.url).toBe("url");
    });

    test('should get and set label', () => {
        bookmark.label = "newlabel";
        expect(bookmark.label).toBe("newlabel");
    });

    test('should get and set URL', () => {
        bookmark.url = "newurl";
        expect(bookmark.url).toBe("newurl");
    });

    test('should get and set values', () => {
        bookmark.setValues("newlabel", "newurl");
        expect(bookmark.label).toBe("newlabel");
        expect(bookmark.url).toBe("newurl");
    });

    test('should trigger change event on set label', () => {
        const mockCallback = jest.fn();
        bookmark.on("valueChange", mockCallback);
        bookmark.label = "newlabel";
        expect(mockCallback).toHaveBeenCalledWith(expect.objectContaining({ newLabel: "newlabel" }));
    });

    test('should trigger change event on set URL', () => {
        const mockCallback = jest.fn();
        bookmark.on("valueChange", mockCallback);
        bookmark.url = "newurl";
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
