package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.stanford.irt.laneweb.proxy.ProxyServersService;

@Controller
public class EzproxyServersController {

    private ProxyServersService service;

    public EzproxyServersController(final ProxyServersService service) {
        this.service = service;
    }

    @RequestMapping(value = "/eresources/ezproxy-servers.txt")
    public void getEzproxyServers(final HttpServletResponse response) throws IOException {
        this.service.write(response.getOutputStream());
    }
}
