package org.cobbzilla.pivotout.web;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PivotalCallbackServletTest {

    private static final Logger LOG = LoggerFactory.getLogger(PivotalCallbackServletTest.class);

    private static final int HTTP_PORT = 18080;
    private Server jetty = null;

    protected HttpClient httpClient = new DefaultHttpClient();

    public String getUrlBase () {
        return "http://127.0.0.1:"+HTTP_PORT;
    }

    @Before
    public void setUpJetty() throws Exception {

        if (jetty != null) return;

        WebAppContext context = new WebAppContext();
        context.setResourceBase(System.getProperty("user.dir"));
        context.setContextPath("/");
        context.setParentLoaderPriority(true);

        // manually configure the servlet to use a test driver that will just touch a file
        ServletHolder holder = new ServletHolder(PivotalCallbackServlet.class);
        context.addServlet(holder, PivotalCallbackServlet.ENDPOINT);

        jetty = new Server(HTTP_PORT);
        jetty.setHandler(context);
        jetty.start();
    }

    @After
    public void tearDownJetty () throws Exception {
        jetty.stop();
        jetty = null;
    }

    //@Test
    public void testCallback() throws Exception {

        final HttpPost request = new HttpPost(getUrlBase() + PivotalCallbackServlet.ENDPOINT);
        // request.setEntity(new StringEntity(TEST_POST));
        final HttpResponse response = httpClient.execute(request);

    }

}
