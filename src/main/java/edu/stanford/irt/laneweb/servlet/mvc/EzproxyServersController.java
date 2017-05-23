package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.stanford.irt.laneweb.proxy.ProxyServersService;

@Controller
public class EzproxyServersController {

    @Autowired
    private ProxyServersService writer;

    @RequestMapping(value = "/eresources/ezproxy-servers.txt")
    public void getEzproxyServers(final HttpServletResponse response) throws IOException {
        this.writer.write(response.getOutputStream());
    }
}
