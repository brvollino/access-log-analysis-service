package com.vollino.log.analyser.server;

import javax.servlet.http.HttpServlet;

/**
 * @author Bruno Vollino
 */
public class HttpEndpoint {

    private final String path;
    private final HttpServlet httpServlet;

    public HttpEndpoint(String path, HttpServlet httpServlet) {
        this.path = path;
        this.httpServlet = httpServlet;
    }

    public String getPath() {
        return path;
    }

    public HttpServlet getHttpServlet() {
        return httpServlet;
    }
}
