package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;

import jakarta.servlet.ServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import edu.stanford.irt.laneweb.proxy.ProxyServersService;

@Controller
public class EzproxyServersController {

    private ProxyServersService service;

    public EzproxyServersController(final ProxyServersService service) {
        this.service = service;
    }

    @GetMapping(value = "/eresources/ezproxy-servers.txt")
    public void getEzproxyServers(final ServletResponse response) throws IOException {
        this.service.write(response.getOutputStream());
    }
}
