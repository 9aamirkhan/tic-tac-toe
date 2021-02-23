package com.app.tictactoe;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GameResourceTest {

    private HttpServer server;
    private WebTarget target;
    private NewCookie cookie;

    @Before
    public void setUp() throws Exception {
        // start the server
        server = Main.startServer();
        // create the client
        Client client = ClientBuilder.newClient();
        target = client.target(Main.BASE_URI);

        //New session, get session id in cookie
        Response res = target.path("game/all").request().get();
        Map<String, NewCookie> cookies = res.getCookies();
        cookie  = cookies.get("JSESSIONID");
    }


    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }

    @Test
    public void testGetGameNotFound() {
        Invocation.Builder invocationBuilder = target.path("game/abc").request(MediaType.APPLICATION_JSON);
        Response res = invocationBuilder.cookie(cookie).get();
		assertEquals("Should return status 404", 404, res.getStatus());
    }

    @Test
    public void testDeleteGameNotFound() {
        Invocation.Builder invocationBuilder = target.path("game/abc").request(MediaType.APPLICATION_JSON);
        Response res = invocationBuilder.cookie(cookie).delete();
		assertEquals("Should return status 404", 404, res.getStatus());
    }

}
