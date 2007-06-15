/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.stanford.laneweb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.components.source.SourceUtil;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.ServiceableGenerator;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.AbstractSource;
import org.w3c.tidy.Tidy;
import org.xml.sax.SAXException;

/**
 */
public class HTMLGenerator extends ServiceableGenerator implements
		Configurable, CacheableProcessingComponent, Disposable {

	/** The source, if coming from a file */
	private Source inputSource;

	/** JTidy properties */
	private Properties properties;

	private String configUrl;

	public void configure(Configuration config) throws ConfigurationException {

		this.configUrl = config.getChild("jtidy-config").getValue(null);
		doConfig();
	}
	private void doConfig() throws ConfigurationException {

		if (configUrl != null) {
			org.apache.excalibur.source.SourceResolver resolver = null;
			Source configSource = null;
			try {
				resolver = (org.apache.excalibur.source.SourceResolver) this.manager
						.lookup(org.apache.excalibur.source.SourceResolver.ROLE);
				configSource = resolver.resolveURI(configUrl);
				if (getLogger().isDebugEnabled()) {
					getLogger().debug(
							"Loading configuration from "
									+ configSource.getURI());
				}

				this.properties = new Properties();
				this.properties.load(configSource.getInputStream());

			} catch (Exception e) {
				getLogger().warn("Cannot load configuration from " + configUrl);
				throw new ConfigurationException(
						"Cannot load configuration from " + configUrl, e);
			} finally {
				if (null != resolver) {
					this.manager.release(resolver);
					resolver.release(configSource);
				}
			}
		}
	}

	/**
	 * Recycle this component. All instance variables are set to
	 * <code>null</code>.
	 */
	public void recycle() {
		if (this.inputSource != null) {
			this.resolver.release(this.inputSource);
			this.inputSource = null;
		}
		super.recycle();
	}

	/**
	 * Setup the html generator. Try to get the last modification date of the
	 * source for caching.
	 */
	public void setup(SourceResolver resolver, Map objectModel, String src,
			Parameters par) throws ProcessingException, SAXException,
			IOException {
		super.setup(resolver, objectModel, src, par);
		this.inputSource = this.resolver.resolveURI(this.source);
	}

	/**
	 * Generate the unique key. This key must be unique inside the space of this
	 * component. This method must be invoked before the generateValidity()
	 * method.
	 * 
	 * @return The generated key or <code>0</code> if the component is
	 *         currently not cacheable.
	 */
	public java.io.Serializable getKey() {
		if (this.inputSource == null) {
			return null;
		}
		return this.inputSource.getURI();
	}

	/**
	 * Generate the validity object. Before this method can be invoked the
	 * generateKey() method must be invoked.
	 * 
	 * @return The generated validity object or <code>null</code> if the
	 *         component is currently not cacheable.
	 */
	public SourceValidity getValidity() {
		if (this.inputSource == null) {
			return null;
		}
		return this.inputSource.getValidity();
	}

	/**
	 * Generate XML data.
	 */
	public void generate() throws IOException, SAXException,
			ProcessingException {
		try {
			doConfig();
			Tidy tidy = new Tidy();

			tidy.setConfigurationFromProps(this.properties);

			StringWriter stringWriter = new StringWriter();
			PrintWriter errorWriter = new PrintWriter(stringWriter);
			tidy.setErrout(errorWriter);

			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			tidy.parse(this.inputSource.getInputStream(), baos);
			errorWriter.flush();
			errorWriter.close();
			if (getLogger().isWarnEnabled()) {
				getLogger().warn(stringWriter.toString());
			}
			Source foo = new AbstractSource() {
				public boolean exists() {
					return true;
				}

				public InputStream getInputStream() {
					return new ByteArrayInputStream(baos.toByteArray());
				}
			};
			SourceUtil.parse(this.manager, foo, super.xmlConsumer);
		} catch (SAXException e) {
			SourceUtil.handleSAXException(this.inputSource.getURI(), e);
		} catch (ConfigurationException e) {
			throw new ProcessingException(e);
		}
	}

}
