package org.cobbzilla.pivotout.web;

import org.apache.commons.io.IOUtils;
import org.cobbzilla.pivotout.config.PivotoutConfiguration;
import org.cobbzilla.pivotout.model.Activity;
import org.cobbzilla.pivotout.service.EventProcessingService;
import org.cobbzilla.pivotout.service.ModelBuilderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet(loadOnStartup=1, urlPatterns=PivotalCallbackServlet.ENDPOINT, initParams = {
        @WebInitParam(name="config", value=PivotalCallbackServlet.PIVOTOUT_CONFIG_XML)
})
public class PivotalCallbackServlet extends HttpServlet {

    private static final boolean debug = false;

    public static final String INITPARAM_CONFIG = "config";
    public static final String ENDPOINT = "/pivotal_callback";

    private static final Logger LOG = LoggerFactory.getLogger(PivotalCallbackServlet.class);
    private static final File DEBUG_FILE = new File("/tmp/pivotal_web_hook.log");

    public static final String PIVOTOUT_CONFIG_XML = "pivotout_config.xml";
    public static final String PIVOTOUT_DEBUG_XML = "pivotout_config_debug.xml";

    private ModelBuilderService modelBuilderService = new ModelBuilderService();
    private EventProcessingService eventProcessingService = new EventProcessingService();

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        PivotoutConfiguration config;
        String configPath = servletConfig.getInitParameter(INITPARAM_CONFIG);
        if (debug) configPath = PIVOTOUT_DEBUG_XML;
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(configPath)) {
            config = PivotoutConfiguration.buildConfig(in);
        } catch (IOException e) {
            throw new ServletException("Error loading configuration: "+e, e);
        }
        try {
            config.configure(eventProcessingService);
        } catch (Exception e) {
            throw new ServletException("Error configuring: "+e, e);
        }
        LOG.info("Successfully configured EventProcessingService with this XML:\n"+config.getConfigurationXml());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ByteArrayOutputStream debug = new ByteArrayOutputStream();
        IOUtils.copy(request.getInputStream(), debug);

        try (FileOutputStream out = new FileOutputStream(DEBUG_FILE, true)) {
            out.write("\nReceived POST from pivotal:\n".getBytes());
            out.write(debug.toByteArray());
            out.write("\n".getBytes());
        }

        Activity activity = modelBuilderService.buildActivity(debug.toString());
        LOG.debug("ModelBuilder: built activity="+activity);

        eventProcessingService.processEvent(activity);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().write(getClass().getName()+" is running here\n");
    }
}
