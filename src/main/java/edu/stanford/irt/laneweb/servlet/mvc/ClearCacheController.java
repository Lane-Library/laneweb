package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

import javax.cache.Cache;
import javax.cache.Cache.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.cocoon.cache.CachedResponse;

@Controller
public class ClearCacheController {

	private static final Logger log = LoggerFactory.getLogger(ClearCacheController.class);

	private Cache<Serializable, CachedResponse> cache;

	public ClearCacheController(final Cache<Serializable, CachedResponse> cache) {
		this.cache = cache;
	}

	@GetMapping(value = {"/secure/admin/clearcache","/admin/clearcache"})
	@ResponseBody
	public String clearCache() {
		log.error("I'm in clearcache");

		log.error("size cache before clear " + getSizeCache());

		this.cache.clear();
		log.error("size cache after clear " + getSizeCache());
		return "OK";
	}

	private int getSizeCache() {
		Iterator<Entry<Serializable, CachedResponse>> c = this.cache.iterator();
		int i = 0;
		while (c.hasNext()) {
			c.next();
			i++;
		}
		return i;
	}
}
