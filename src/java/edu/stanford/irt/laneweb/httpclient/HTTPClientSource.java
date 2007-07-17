/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.stanford.irt.laneweb.httpclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameterizable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.util.DateParseException;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.excalibur.source.ModifiableSource;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceException;
import org.apache.excalibur.source.SourceFactory;
import org.apache.excalibur.source.SourceNotFoundException;
import org.apache.excalibur.source.SourceParameters;
import org.apache.excalibur.source.SourceResolver;
import org.apache.excalibur.source.SourceUtil;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.TimeStampValidity;

/**
 * HTTP URL Source object, based on the Jakarta Commons
 * <a href="http://jakarta.apache.org/commons/httpclient/">HttpClient</a>
 * project.
 *
 * @author <a href="mailto:dev@avalon.apache.org">Avalon Development Team</a>
 * @version CVS $Id: HTTPClientSource.java,v 1.4 2004/02/28 11:47:24 cziegeler Exp $
 */
public class HTTPClientSource extends AbstractLogEnabled
    implements ModifiableSource, Initializable, Parameterizable
{
    /**
     * Constant used for identifying POST requests.
     */
    public static final String POST           = "POST";

    /**
     * Constant used for identifying GET requests.
     */
    public static final String GET            = "GET";

    /**
     * Constant used for configuring the proxy hostname.
     */
    public static final String PROXY_HOST     = "proxy.host";

    /**
     * Constant used for configuring the proxy port number.
     */
    public static final String PROXY_PORT     = "proxy.port";

    /**
     * Constant used when obtaining the Content-Type from HTTP Headers
     */
    public static final String CONTENT_TYPE   = "Content-Type";

    /**
     * Constant used when obtaining the Content-Length from HTTP Headers
     */
    public static final String CONTENT_LENGTH = "Content-Length";

    /**
     * Constant used when obtaining the Last-Modified date from HTTP Headers
     */
    public static final String LAST_MODIFIED  = "Last-Modified";
    
    /**
     * Constant used to get the HttpState in the constructor
     */
    public static final String HTTP_STATE = "http.state";

    /**
     * The URI being accessed.
     */
    private final String m_uri;

    /**
     * Contextual parameters passed via the {@link SourceFactory}.
     */
    private final Map m_parameters;

    /**
     * Optional http state passed from SourceFactory
     */
    private HttpState m_httpState;

    /**
     * The {@link HttpClient} object.
     */
    private final HttpClient m_client;

    /**
     * Proxy port if set via configuration.
     */
    private int m_proxyPort;

    /**
     * Proxy host if set via configuration.
     */
    private String m_proxyHost;

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
     * @param uri URI
     * @param parameters contextual parameters passed to this instance
     * @exception Exception if an error occurs
     */
    public HTTPClientSource( final String uri, final Map parameters, final HttpClient httpClient )
        throws Exception
    {
        this.m_uri = uri;
        this.m_parameters = parameters == null ? Collections.EMPTY_MAP : parameters;
        this.m_client = httpClient;
    }

    /**
     * Parameterizes this {@link HTTPClientSource} instance.
     *
     * @param params a {@link Parameters} instance.
     * @exception ParameterException if an error occurs
     */
    public void parameterize( final Parameters params )
        throws ParameterException
    {
        this.m_proxyHost = params.getParameter( PROXY_HOST, null );
        this.m_proxyPort = params.getParameterAsInteger( PROXY_PORT, -1 );

        if ( this.getLogger().isDebugEnabled() )
        {
            final String message =
                this.m_proxyHost == null || this.m_proxyPort == -1
                ? "No proxy configured"
                : "Configured with proxy host "
                  + this.m_proxyHost + " port " + this.m_proxyPort;

            this.getLogger().debug( message );
        }
    }

    /**
     * Initializes this {@link HTTPClientSource} instance.
     *
     * @exception Exception if an error occurs
     */
    public void initialize() throws Exception
    {
    	if (this.m_parameters.containsKey(HTTP_STATE))
    	{
    		this.m_httpState = (HttpState) this.m_parameters.get(HTTP_STATE);
    		this.m_parameters.remove(HTTP_STATE);
    	}
        if ( this.m_proxyHost != null && this.m_proxyPort != -1 )
        {
            this.m_client.getHostConfiguration().setProxy( this.m_proxyHost, this.m_proxyPort );
        }
        if ( this.m_httpState != null )
        {
        	this.m_client.setState(this.m_httpState);
        }
        
        this.m_dataValid = false;
    }

    /**
     * Method to discover what kind of request is being made from the
     * parameters map passed in to this Source's constructor.
     *
     * @return the method type, or if no method type can be found,
     *         HTTP GET is assumed.
     */
    private String findMethodType()
    {
        final String method =
            (String) this.m_parameters.get( SourceResolver.METHOD );
        return method == null ? GET : method;
    }

    /**
     * Helper method to create the required {@link HttpMethod} object
     * based on parameters passed to this {@link HTTPClientSource} object.
     *
     * @return a {@link HttpMethod} object.
     */
    private HttpMethod getMethod()
    {
        final String method = this.findMethodType();

        // create a POST method if requested
        if ( POST.equals( method ) )
        {
            return this.createPostMethod(
                this.m_uri,
                (SourceParameters) this.m_parameters.get( SourceResolver.URI_PARAMETERS )
            );
        }

        // default method is GET
        return this.createGetMethod( this.m_uri );
    }

    /**
     * Factory method to create a new {@link PostMethod} with the given
     * {@link SourceParameters} object.
     *
     * @param uri URI
     * @param params {@link SourceParameters}
     * @return a {@link PostMethod} instance
     */
    private PostMethod createPostMethod(
        final String uri, final SourceParameters params
    )
    {
        final PostMethod post = new PostMethod( uri );

        if ( params == null )
        {
            return post;
        }

        for ( final Iterator names = params.getParameterNames();
              names.hasNext();
        )
        {
            final String name = (String) names.next();

            for ( final Iterator values = params.getParameterValues( name );
                  values.hasNext();
            )
            {
                final String value = (String) values.next();
                post.addParameter( new NameValuePair( name, value ) );
            }
        }

        return post;
    }

    /**
     * Factory method to create a {@link GetMethod} object.
     *
     * @param uri URI
     * @return a {@link GetMethod} instance
     */
    private GetMethod createGetMethod( final String uri )
    {
        final GetMethod method = new GetMethod( uri );

        // add all parameters as headers
        for ( final Iterator i = this.m_parameters.keySet().iterator(); i.hasNext(); )
        {
            final String key = (String) i.next();
            final String value = (String) this.m_parameters.get( key );

            if ( this.getLogger().isDebugEnabled() )
            {
                this.getLogger().debug(
                    "Adding header '" + key + "', with value '" + value + "'"
                );
            }
           
            method.setRequestHeader( key, value );
        }

        return method;
    }

    /**
     * Factory method to create a {@link HeadMethod} object.
     *
     * @param uri URI
     * @return a {@link HeadMethod} instance
     */
    private HeadMethod createHeadMethod( final String uri )
    {
        return new HeadMethod( uri );
    }

    /**
     * Factory method to create a {@link PutMethod} object.
     *
     * @param uri URI to upload <code>uploadFile</code> to
     * @param uploadFile {@link File} to be uploaded
     * @return a {@link PutMethod} instance
     * @exception IOException if an error occurs
     */
    private PutMethod createPutMethod(
        final String uri, final File uploadFile
    )
        throws IOException
    {
        final PutMethod put = new PutMethod( uri );
        put.setRequestEntity(new InputStreamRequestEntity(
            new FileInputStream( uploadFile.getAbsolutePath() )));
        return put;
    }

    /**
     * Factory method to create a {@link DeleteMethod} object.
     *
     * @param uri URI to delete
     * @return {@link DeleteMethod} instance.
     */
    private DeleteMethod createDeleteMethod( final String uri )
    {
        return new DeleteMethod( uri );
    }

    /**
     * Method to make response data available if possible without
     * actually making an actual request (ie. via HTTP HEAD).
     */
    private void updateData()
    {
        // no request made so far, attempt to get some response data.
        if ( !this.m_dataValid )
        {
            if ( GET.equals( this.findMethodType() ) )
            {
                final HttpMethod head = this.createHeadMethod( this.m_uri );
                try
                {
                    this.executeMethod( head );
                    return;
                }
                catch ( final IOException e )
                {
                    if ( this.getLogger().isDebugEnabled() )
                    {
                        this.getLogger().debug(
                            "Unable to determine response data, using defaults", e
                        );
                    }
                }
                finally {
                    head.releaseConnection();
                }
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
     * Executes a particular {@link HttpMethod} and updates internal
     * data storage.
     *
     * @param method {@link HttpMethod} to execute
     * @return response code from server
     * @exception IOException if an error occurs
     */
    protected int executeMethod( final HttpMethod method )
        throws IOException
    {

        final int response = this.m_client.executeMethod( method );

        this.updateExists( method );
        this.updateMimeType( method );
        this.updateContentLength( method );
        this.updateLastModified( method );

        // all finished, return response code to the caller.
        return response;
    }

    /**
     * Method to update whether a referenced resource exists, after
     * executing a particular {@link HttpMethod}.
     *
     * <p>REVISIT: exists() would be better called canRead()
     * or similar, as a resource can exist but not be readable.</p>
     *
     * @param method {@link HttpMethod} executed.
     */
    private void updateExists( final HttpMethod method )
    {
        final int response = method.getStatusCode();

        // The following returns true, if the user can successfully get
        // an InputStream without receiving errors? ie. if we receive a
        // HTTP 200 (OK), 201 (CREATED), 206 (PARTIAL CONTENT)

        // REVISIT(MC): need a special way to handle 304 (NOT MODIFIED)
        // 204 & 205 in the future

        // resource does not exist if HttpClient returns a 404 or a 410
        this.m_exists = (response == HttpStatus.SC_OK ||
                    response == HttpStatus.SC_CREATED ||
                    response == HttpStatus.SC_PARTIAL_CONTENT);
    }

    /**
     * Method to ascertain whether the given resource actually exists.
     *
     * @return <code>true</code> if the resource pointed to by the
     *         URI during construction exists, <code>false</code>
     *         otherwise.
     */
    public boolean exists()
    {
        this.updateData();
        return this.m_exists;
    }

    /**
     * Method to obtain an {@link InputStream} to read the response
     * from the server.
     *
     * @return {@link InputStream} containing data sent from the server.
     * @throws IOException if some I/O problem occurs.
     * @throws SourceNotFoundException if the source doesn't exist.
     */
    public InputStream getInputStream()
        throws IOException, SourceNotFoundException
    {
        final HttpMethod method = this.getMethod();
        int response = this.executeMethod( method );
        this.m_dataValid = true;

        // throw SourceNotFoundException - according to Source API we
        // need to throw this if the source doesn't exist.
        if ( !this.exists() )
        {
            final StringBuffer error = new StringBuffer();
            error.append( "Unable to retrieve URI: " );
            error.append( this.m_uri );
            error.append( " (" );
            error.append( response );
            error.append( ")" );

            throw new SourceNotFoundException( error.toString() );
        }
        final PipedOutputStream output = new PipedOutputStream();
        PipedInputStream resultStream = new PipedInputStream(output);
        new Thread() {
        	public void run() {
        		try {
        			InputStream in = method.getResponseBodyAsStream();
        			byte[] buffer = new byte[1024];
        			while (true) {
        				int b = in.read(buffer);
        				if (b ==  -1) {
        					break;
        				}
        				output.write(buffer,0,b);
        			}
        		} catch (IOException e) {
					getLogger().error(e.getMessage(),e);
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
    public String getURI()
    {
        return this.m_uri;
    }

    /**
     * Return the URI scheme identifier, ie.  the part preceding the fist ':'
     * in the URI (see <a href="http://www.ietf.org/rfc/rfc2396.txt">RFC 2396</a>).
     *
     * @return the URI scheme identifier
     */
    public String getScheme()
    {
        return SourceUtil.getScheme( this.m_uri );
    }

    /**
     * Obtain a {@link SourceValidity} object.
     *
     * @return a {@link SourceValidity} object, or
     *         <code>null</code> if this is not possible.
     */
    public SourceValidity getValidity()
    {
        // Implementation taken from URLSource.java, Kudos :)

        final long lm = this.getLastModified();

        if ( lm > 0 )
        {
            if ( lm == this.m_cachedLastModificationDate )
            {
                return this.m_cachedValidity;
            }

            this.m_cachedLastModificationDate = lm;
            this.m_cachedValidity = new TimeStampValidity( lm );
            return this.m_cachedValidity;
        }

        return null;
    }

    /**
     * Refreshes this {@link Source} object.
     */
    public void refresh()
    {
        this.recycle();
    }

    /**
     * Method to update the mime type of a resource after
     * executing a particular {@link HttpMethod}.
     *
     * @param method {@link HttpMethod} executed
     */
    private void updateMimeType( final HttpMethod method )
    {
        // REVISIT: should this be the mime-type, or the content-type -> URLSource
        // returns the Content-Type, so we'll follow that for now.
        final Header header = method.getResponseHeader( CONTENT_TYPE );
        this.m_mimeType = header == null ? null : header.getValue();
    }

    /**
     * Obtain the mime-type for the referenced resource.
     *
     * @return mime-type for the referenced resource.
     */
    public String getMimeType()
    {
        this.updateData();
        return this.m_mimeType;
    }

    /**
     * Method to update the content length of a resource after
     * executing a particular {@link HttpMethod}.
     *
     * @param method {@link HttpMethod} executed
     */
    private void updateContentLength( final HttpMethod method )
    {
        try
        {
            final Header length =
                method.getResponseHeader( CONTENT_LENGTH );
            this.m_contentLength =
                length == null ? -1 : Long.parseLong( length.getValue() );
        }
        catch ( final NumberFormatException e )
        {
            if ( this.getLogger().isDebugEnabled() )
            {
                this.getLogger().debug(
                    "Unable to determine content length, returning -1", e
                );
            }

            this.m_contentLength = -1;
        }
    }

    /**
     * Obtain the content length of the referenced resource.
     *
     * @return content length of the referenced resource, or
     *         -1 if unknown/uncalculatable
     */
    public long getContentLength()
    {
        this.updateData();
        return this.m_contentLength;
    }

    /**
     * Method to update the last modified date of a resource after
     * executing a particular {@link HttpMethod}.
     *
     * @param method {@link HttpMethod} executed
     */
    private void updateLastModified( final HttpMethod method )
    {
        final Header lastModified = method.getResponseHeader( LAST_MODIFIED );
        try
        {
            this.m_lastModified =
                lastModified == null ? 0 : DateUtil.parseDate( lastModified.getValue() ).getTime();
        }
        catch (DateParseException e)
        {
            // we ignore this exception and simply set last modified to 0
            this.m_lastModified = 0;
        }
    }

    /**
     * Get the last modification date of this source. This date is
     * measured in milliseconds since the Epoch (00:00:00 GMT, January 1, 1970).
     *
     * @return the last modification date or <code>0</code> if unknown.
     */
    public long getLastModified()
    {
        this.updateData();
        return this.m_lastModified;
    }

    /**
     * Recycles this {@link HTTPClientSource} object so that it may be reused
     * to refresh it's content.
     */
    private void recycle()
    {
        this.m_dataValid = false;
    }

    /////////////////////////// ModifiableSource methods

    /**
     * Obtain an {@link OutputStream} to write to. The {@link OutputStream}
     * returned actually references a temporary local file, which will
     * be written to the server upon closing.
     *
     * The returned stream must be closed or cancelled by the calling code.
     *
     * @return an {@link OutputStream} instance
     * @exception IOException if an error occurs
     */
    public OutputStream getOutputStream() throws IOException
    {
        final File tempFile = File.createTempFile("httpclient", "tmp");
        return new WrappedFileOutputStream( tempFile, this.getLogger() );
    }

    /**
     * Internal class which extends {@link FileOutputStream} to
     * automatically upload the data written to it, upon a {@link #close}
     * operation.
     */
    private class WrappedFileOutputStream extends FileOutputStream
    {
        /**
         * Reference to the File being written itself.
         */
        private File m_file;

        /**
         * Reference to a {@link Logger}.
         */
        private final Logger m_logger;

        /**
         * Constructor, creates a new {@link WrappedFileOutputStream}
         * instance.
         *
         * @param file {@link File} to write to.
         * @param logger {@link Logger} reference.
         * @exception IOException if an error occurs
         */
        public WrappedFileOutputStream( final File file, final Logger logger )
            throws IOException
        {
            super( file );
            this.m_file = file;
            this.m_logger = logger;
        }

        /**
         * Closes the stream, and uploads the file written to the
         * server.
         *
         * @exception IOException if an error occurs
         */
        public void close() throws IOException
        {
            super.close();

            if ( this.m_file != null )
            {
                this.upload();
                this.m_file.delete();
                this.m_file = null;
            }
        }

        /**
         * Method to test whether this stream can be closed.
         *
         * @return <code>true</code> if possible, false otherwise.
         */
        public boolean canCancel()
        {
            return this.m_file != null;
        }

        /**
         * Cancels this stream.
         *
         * @exception IOException if stream is already closed
         */
        public void cancel() throws IOException
        {
            if ( this.m_file == null )
            {
                throw new IOException( "Stream already closed" );
            }

            super.close();
            this.m_file.delete();
            this.m_file = null;
        }

        /**
         * Helper method to attempt uploading of the local data file
         * to the remove server via a HTTP PUT.
         *
         * @exception IOException if an error occurs
         */
        private void upload()
            throws IOException
        {
            final HttpMethod uploader = HTTPClientSource.this.createPutMethod( HTTPClientSource.this.m_uri, this.m_file );

            if ( this.m_logger.isDebugEnabled() )
            {
                this.m_logger.debug( "Stream closed, writing data to " + HTTPClientSource.this.m_uri );
            }

            try
            {
                final int response = HTTPClientSource.this.executeMethod( uploader );

                if ( !this.successfulUpload( response ) )
                {
                    throw new SourceException(
                        "Write to " + HTTPClientSource.this.m_uri + " failed (" + response + ")"
                    );
                }

                if ( this.m_logger.isDebugEnabled() )
                {
                    this.m_logger.debug(
                        "Write to " + HTTPClientSource.this.m_uri + " succeeded (" + response + ")"
                    );
                }
            }
            finally
            {
                if ( uploader != null )
                {
                    uploader.releaseConnection();
                }
            }
        }

        /**
         * According to RFC2616 (HTTP 1.1) valid responses for a HTTP PUT
         * are 201 (Created), 200 (OK), and 204 (No Content).
         *
         * @param response response code from the HTTP PUT
         * @return true if upload was successful, false otherwise.
         */
        private boolean successfulUpload( final int response )
        {
            return response == HttpStatus.SC_OK
                || response == HttpStatus.SC_CREATED
                || response == HttpStatus.SC_NO_CONTENT;
        }
    }

    /**
     * Deletes the referenced resource.
     *
     * @exception SourceException if an error occurs
     */
    public void delete() throws SourceException
    {
        final DeleteMethod delete = this.createDeleteMethod( this.m_uri );
        try
        {
            final int response = this.executeMethod( delete );

            if ( !this.deleteSuccessful( response ) )
            {
                throw new SourceException(
                    "Failed to delete " + this.m_uri + " (" + response + ")"
                );
            }

            if ( this.getLogger().isDebugEnabled() )
            {
                this.getLogger().debug( this.m_uri + " deleted (" + response + ")");
            }
        }
        catch ( final IOException e )
        {
            throw new SourceException(
                "IOException thrown during delete", e
            );
        }
        finally
        {
            delete.releaseConnection();
        }
    }

    /**
     * According to RFC2616 (HTTP 1.1) valid responses for a HTTP DELETE
     * are 200 (OK), 202 (Accepted) and 204 (No Content).
     *
     * @param response response code from the HTTP PUT
     * @return true if upload was successful, false otherwise.
     */
    private boolean deleteSuccessful( final int response )
    {
        return response == HttpStatus.SC_OK
            || response == HttpStatus.SC_ACCEPTED
            || response == HttpStatus.SC_NO_CONTENT;
    }

    /**
     * Method to determine whether writing to the supplied OutputStream
     * (which must be that returned from {@link #getOutputStream()}) can
     * be cancelled
     *
     * @return true if writing to the stream can be cancelled,
     *         false otherwise
     */
    public boolean canCancel( final OutputStream stream )
    {
        // with help from FileSource, dankeschoen lads :)

        if ( stream instanceof WrappedFileOutputStream )
        {
            return ((WrappedFileOutputStream) stream).canCancel();
        }

        throw new IllegalArgumentException(
            "Output stream supplied was not created by this class"
        );
    }

    /**
     * Cancels any data sent to the {@link OutputStream} returned by
     * {@link #getOutputStream()}.
     *
     * After calling this method, the supplied {@link OutputStream}
     * should no longer be used.
     */
    public void cancel( final OutputStream stream ) throws IOException
    {
        if ( stream instanceof WrappedFileOutputStream )
        {
            ((WrappedFileOutputStream) stream).cancel();
        }
        else
        {
            throw new IllegalArgumentException(
                "Output stream supplied was not created by this class"
            );
        }
    }
}
