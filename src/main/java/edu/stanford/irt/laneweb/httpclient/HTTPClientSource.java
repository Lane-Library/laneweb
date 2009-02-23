package edu.stanford.irt.laneweb.httpclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.util.DateParseException;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceNotFoundException;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.TimeStampValidity;
import org.apache.log4j.Logger;

public class HTTPClientSource implements Source {

    private Logger logger = Logger.getLogger(HTTPClientSource.class);

    /**
     * Constant used when obtaining the Content-Type from HTTP Headers
     */
    private static final String CONTENT_TYPE = "Content-Type";

    /**
     * Constant used when obtaining the Content-Length from HTTP Headers
     */
    private static final String CONTENT_LENGTH = "Content-Length";

    /**
     * Constant used when obtaining the Last-Modified date from HTTP Headers
     */
    private static final String LAST_MODIFIED = "Last-Modified";

    /**
     * The URI being accessed.
     */
    private final String m_uri;

    /**
     * The {@link HttpClient} object.
     */
    private final HttpClient m_client;

    /**
     * Whether the data held within this instance is currently accurate.
     */
    private boolean m_dataValid;

    /**
     * Whether the resource exists on the server.
     */
    private boolean m_exists;

    /**
     * The mime type of the resource on the server.
     */
    private String m_mimeType;

    /**
     * The content length of the resource on the server.
     */
    private long m_contentLength;

    /**
     * The last modified date of the resource on the server.
     */
    private long m_lastModified;

    /**
     * Stored {@link SourceValidity} object.
     */
    private SourceValidity m_cachedValidity;

    /**
     * Cached last modification date.
     */
    private long m_cachedLastModificationDate;

    /**
     * Constructor, creates a new {@link HTTPClientSource} instance.
     * 
     * @param uri
     *            URI
     * @param parameters
     *            contextual parameters passed to this instance
     * @exception Exception
     *                if an error occurs
     */
    @SuppressWarnings("unchecked")
    public HTTPClientSource(final String uri, final Map parameters, final HttpClient httpClient) throws Exception {
        this.m_uri = uri;
        this.m_client = httpClient;
    }

    /**
     * Method to make response data available if possible without actually
     * making an actual request (ie. via HTTP HEAD).
     */
    private void updateData() {
        // no request made so far, attempt to get some response data.
        if (!this.m_dataValid) {
            final HttpMethod head = new HeadMethod(this.m_uri);
            try {
                // cy altered so only one head request is made if successful
                if (200 == executeMethod(head)) {
                    this.m_dataValid = true;
                }
                return;
            } catch (final IOException e) {
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Unable to determine response data, using defaults", e);
                }
            } finally {
                head.releaseConnection();
            }

            // default values when response data is not available
            this.m_exists = false;
            this.m_mimeType = null;
            this.m_contentLength = -1;
            this.m_lastModified = 0;
            this.m_dataValid = true;
        }
    }

    /**
     * Executes a particular {@link HttpMethod} and updates internal data
     * storage.
     * 
     * @param method
     *            {@link HttpMethod} to execute
     * @return response code from server
     * @throws IOException
     * @throws HttpException
     * @exception IOException
     *                if an error occurs
     */
    protected int executeMethod(final HttpMethod method) throws HttpException, IOException {

        final int response = this.m_client.executeMethod(method);

        updateExists(method);
        updateMimeType(method);
        updateContentLength(method);
        updateLastModified(method);

        // all finished, return response code to the caller.
        return response;
    }

    /**
     * Method to update whether a referenced resource exists, after executing a
     * particular {@link HttpMethod}.
     * <p>
     * REVISIT: exists() would be better called canRead() or similar, as a
     * resource can exist but not be readable.
     * </p>
     * 
     * @param method
     *            {@link HttpMethod} executed.
     */
    private void updateExists(final HttpMethod method) {
        final int response = method.getStatusCode();

        // The following returns true, if the user can successfully get
        // an InputStream without receiving errors? ie. if we receive a
        // HTTP 200 (OK), 201 (CREATED), 206 (PARTIAL CONTENT)

        // REVISIT(MC): need a special way to handle 304 (NOT MODIFIED)
        // 204 & 205 in the future

        // resource does not exist if HttpClient returns a 404 or a 410
        this.m_exists = ((response == HttpStatus.SC_OK) || (response == HttpStatus.SC_CREATED) || (response == HttpStatus.SC_PARTIAL_CONTENT));
    }

    /**
     * Method to ascertain whether the given resource actually exists.
     * 
     * @return <code>true</code> if the resource pointed to by the URI during
     *         construction exists, <code>false</code> otherwise.
     */
    public boolean exists() {
        updateData();
        return this.m_exists;
    }

    /**
     * Method to obtain an {@link InputStream} to read the response from the
     * server.
     * 
     * @return {@link InputStream} containing data sent from the server.
     * @throws IOException
     *             if some I/O problem occurs.
     * @throws SourceNotFoundException
     *             if the source doesn't exist.
     */
    public InputStream getInputStream() throws IOException, SourceNotFoundException {
        final GetMethod method = new GetMethod(this.m_uri);

        int response = executeMethod(method);
        this.m_dataValid = true;

        // throw SourceNotFoundException - according to Source API we
        // need to throw this if the source doesn't exist.
        if (!exists()) {
        	method.releaseConnection();
            final StringBuffer error = new StringBuffer();
            error.append("Unable to retrieve URI: ");
            error.append(this.m_uri);
            error.append(" (");
            error.append(response);
            error.append(")");

            throw new SourceNotFoundException(error.toString());
        }
        final PipedOutputStream output = new PipedOutputStream();
        PipedInputStream resultStream = new PipedInputStream(output);
        new Thread() {

            @Override
            public void run() {
                try {
                    InputStream in = method.getResponseBodyAsStream();
                    byte[] buffer = new byte[1024];
                    while (true) {
                        int b = in.read(buffer);
                        if (b == -1) {
                            break;
                        }
                        output.write(buffer, 0, b);
                    }
                    in.close();
                    output.close();
                } catch (IOException e) {
                    HTTPClientSource.this.logger.error(e.getMessage(), e);
                } finally {
                    method.releaseConnection();
                }
            }
        }.start();
        return resultStream;
    }

    /**
     * Obtain the absolute URI this {@link Source} object references.
     * 
     * @return the absolute URI this {@link String} object references.
     */
    public String getURI() {
        return this.m_uri;
    }

    /**
     * Return the URI scheme identifier, ie. the part preceding the fist ':' in
     * the URI (see <a href="http://www.ietf.org/rfc/rfc2396.txt">RFC 2396</a>).
     * 
     * @return the URI scheme identifier
     */
    public String getScheme() {
        return "http";
    }

    /**
     * Obtain a {@link SourceValidity} object.
     * 
     * @return a {@link SourceValidity} object, or <code>null</code> if this is
     *         not possible.
     */
    public SourceValidity getValidity() {
        // Implementation taken from URLSource.java, Kudos :)

        final long lm = getLastModified();

        if (lm > 0) {
            if (lm == this.m_cachedLastModificationDate) {
                return this.m_cachedValidity;
            }

            this.m_cachedLastModificationDate = lm;
            this.m_cachedValidity = new TimeStampValidity(lm);
            return this.m_cachedValidity;
        }

        return null;
    }

    /**
     * Refreshes this {@link Source} object.
     */
    public void refresh() {
        this.m_dataValid = false;
    }

    /**
     * Method to update the mime type of a resource after executing a particular
     * {@link HttpMethod}.
     * 
     * @param method
     *            {@link HttpMethod} executed
     */
    private void updateMimeType(final HttpMethod method) {
        // REVISIT: should this be the mime-type, or the content-type ->
        // URLSource
        // returns the Content-Type, so we'll follow that for now.
        final Header header = method.getResponseHeader(CONTENT_TYPE);
        this.m_mimeType = header == null ? null : header.getValue();
    }

    /**
     * Obtain the mime-type for the referenced resource.
     * 
     * @return mime-type for the referenced resource.
     */
    public String getMimeType() {
        updateData();
        return this.m_mimeType;
    }

    /**
     * Method to update the content length of a resource after executing a
     * particular {@link HttpMethod}.
     * 
     * @param method
     *            {@link HttpMethod} executed
     */
    private void updateContentLength(final HttpMethod method) {
        try {
            final Header length = method.getResponseHeader(CONTENT_LENGTH);
            this.m_contentLength = length == null ? -1 : Long.parseLong(length.getValue());
        } catch (final NumberFormatException e) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Unable to determine content length, returning -1", e);
            }

            this.m_contentLength = -1;
        }
    }

    /**
     * Obtain the content length of the referenced resource.
     * 
     * @return content length of the referenced resource, or -1 if
     *         unknown/uncalculatable
     */
    public long getContentLength() {
        updateData();
        return this.m_contentLength;
    }

    /**
     * Method to update the last modified date of a resource after executing a
     * particular {@link HttpMethod}.
     * 
     * @param method
     *            {@link HttpMethod} executed
     */
    private void updateLastModified(final HttpMethod method) {
        final Header lastModified = method.getResponseHeader(LAST_MODIFIED);
        try {
            this.m_lastModified = lastModified == null ? 0 : DateUtil.parseDate(lastModified.getValue()).getTime();
        } catch (DateParseException e) {
            // we ignore this exception and simply set last modified to 0
            this.m_lastModified = 0;
        }
    }

    /**
     * Get the last modification date of this source. This date is measured in
     * milliseconds since the Epoch (00:00:00 GMT, January 1, 1970).
     * 
     * @return the last modification date or <code>0</code> if unknown.
     */
    public long getLastModified() {
        updateData();
        return this.m_lastModified;
    }
}
