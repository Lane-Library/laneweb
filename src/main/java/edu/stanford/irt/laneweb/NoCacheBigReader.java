package edu.stanford.irt.laneweb;

import java.io.Serializable;

import org.apache.cocoon.reading.ResourceReader;

/**
 * To prevent caching of large files this Reader returns a null key if the
 * source size is greater than 1000000 bytes (~1.2M).
 * @author ceyates
 *
 */
public class NoCacheBigReader extends ResourceReader {

	@Override
	public Serializable getKey() {
		if (this.inputSource.getContentLength() > 1000000) {
			return null;
		}
		return super.getKey();
	}

}
