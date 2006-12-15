package edu.stanford.laneweb;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cocoon.servlet.ParanoidCocoonServlet;

public class LanewebServlet extends ParanoidCocoonServlet {


	public void service(ServletRequest arg0, ServletResponse arg1) throws ServletException, IOException {
		((HttpServletResponse) arg1).addHeader("X-Laneweb-Version","1.4.7-dev");
		super.service(arg0, arg1);
	}

}
