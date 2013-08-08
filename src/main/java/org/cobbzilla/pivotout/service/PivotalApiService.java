package org.cobbzilla.pivotout.service;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.cobbzilla.pivotout.model.Story;
import org.cobbzilla.util.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

public class PivotalApiService {

    public static final String UTF_8 = "UTF-8";

    private static final String PROJECT_ID_TOKEN = "@@PROJECT_ID@@";

    private static final String HEADER_TOKEN = "X-TrackerToken";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";

    private static final String CONTENT_TYPE_APPLICATION_XML = "application/xml";

    private static final String STORIES_URL_TEMPLATE = "http://www.pivotaltracker.com/services/v3/projects/" + PROJECT_ID_TOKEN + "/stories";

    private static final Logger LOG = LoggerFactory.getLogger(PivotalApiService.class);

    private String token;
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    private HttpClient httpClient = HttpClientUtil.getOverlyTrustingClient(new DefaultHttpClient());

    private final ModelBuilderService modelBuilderService = new ModelBuilderService();;

    public List<Story> findStoriesByOwnerAndProject(String ownedBy, String projectId) throws IOException {

        String url = STORIES_URL_TEMPLATE.replace(PROJECT_ID_TOKEN, projectId) +
                "?filter=" + URLEncoder.encode("owner:\""+ownedBy+"\"", UTF_8);

        try (InputStream in = makeTokenGetRequest(url)) {
            return modelBuilderService.buildStoryList(in);
        }
    }

    public List<Story> findStoriesByProject(String projectId) throws IOException {

        String url = STORIES_URL_TEMPLATE.replace(PROJECT_ID_TOKEN, projectId);
        try (InputStream in = makeTokenGetRequest(url)) {
            return modelBuilderService.buildStoryList(in);
        }
    }

    private InputStream makeTokenGetRequest(String url) throws IOException {
        HttpGet request = new HttpGet(url);
        request.setHeader(HEADER_TOKEN, token);
        final HttpResponse response = httpClient.execute(request);
        return response.getEntity().getContent();
    }

    private InputStream makeTokenPostRequest(String url, String xml) throws IOException {
        HttpPost request = new HttpPost(url);
        request.setHeader(HEADER_TOKEN, token);
        if (xml != null) {
            request.setHeader(HEADER_CONTENT_TYPE, CONTENT_TYPE_APPLICATION_XML);
            request.setEntity(new StringEntity(xml));
        }
        final HttpResponse response = httpClient.execute(request);
        return response.getEntity().getContent();
    }

//    private static final String NEW_STORY_XML_TEMPLATE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<story><story_type>${story.type}</story_type><name>${story.name}</name><requested_by>${story.requestedBy}</requested_by></story>\n";
//    public Activity newStory(String projectId, StoryType storyType, String name, String requestedBy) throws IOException {
//
//        Story story = new Story();
//        story.setProjectId(projectId);
//        story.setStoryType(storyType.name());
//        story.setName(name);
//        story.setRequestedBy(requestedBy);
//
//        String xml = VelocityUtil.merge(story, NEW_STORY_XML_TEMPLATE);
//        try (final InputStream in = makeTokenPostRequest(STORIES_URL_TEMPLATE.replace(PROJECT_ID_TOKEN, projectId), xml)) {
//            return modelBuilderService.buildActivity(in);
//        }
//    }
}
