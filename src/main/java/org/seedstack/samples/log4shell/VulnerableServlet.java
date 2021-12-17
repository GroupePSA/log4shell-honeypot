package org.seedstack.samples.log4shell;

import com.google.common.io.BaseEncoding;
import org.seedstack.seed.Logging;
import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class test
 */
@WebServlet("/test")
public class VulnerableServlet extends HttpServlet {
    public static final String BASIC_PREFIX = "Basic ";
    @Logging
    private static Logger LOGGER;
    private static final long serialVersionUID = 1L;

    public VulnerableServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userAgent = request.getHeader("User-agent");
        String auth = request.getHeader("Authorization");

        LOGGER.error("Serving request: " + request.hashCode());

        // These line triggers the RCE by logging the attacker-controlled HTTP User Agent header.
        if (allTests() || System.getProperty("log4shell.userAgent") != null) {
            if (userAgent != null) {
                LOGGER.error("Request User Agent:" + userAgent);
            } else {
                LOGGER.error("No user agent header found");
            }
        }
        if (allTests() || System.getProperty("log4shell.authorization") != null) {
            if (auth != null) {
                LOGGER.error("Authorization: " + auth);
            } else {
                LOGGER.error("No authorization header found");
            }
        }
        if (allTests() || System.getProperty("log4shell.basicAuth") != null) {
            if (auth != null && auth.startsWith("Basic ")) {
                auth = auth.substring(BASIC_PREFIX.length());
                auth = new String(BaseEncoding.base64().decode(auth));
                LOGGER.error("Decoded basic authorization user/password: " + auth);
            } else {
                // still triggers the vulnerability if pattern is unencoded
                LOGGER.error("Cannot decode basic authentication: " + auth);
            }
        }
        if (allTests() || System.getProperty("log4shell.urlPath") != null) {
            LOGGER.error("URL path: " + request.getPathInfo());
        }
        if (allTests() || System.getProperty("log4shell.urlQuery") != null) {
            LOGGER.error("Query string: " + request.getQueryString());
        }
        response.getWriter().append("Done");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private boolean allTests() {
        return System.getProperty("log4shell.all") != null;
    }
}
