package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;

import javax.servlet.ServletResponse;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.stanford.irt.laneweb.proxy.ProxyServersService;

@Controller
public class EzproxyServersController {

    private ProxyServersService service;

    public EzproxyServersController(
            @Qualifier("edu.stanford.irt.laneweb.proxy.ProxyServersService/HTTP")
            final ProxyServersService service) {
        this.service = service;
    }

    @RequestMapping(value = "/eresources/ezproxy-servers.txt", method = RequestMethod.GET)
    public void getEzproxyServers(final ServletResponse response) throws IOException {
        this.service.write(response.getOutputStream());
    }
}
