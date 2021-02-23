package com.app.tictactoe;

import java.util.Map;
import java.util.HashMap;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.inject.Inject;
import javax.inject.Provider;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Session;
import org.json.JSONException;
import org.json.JSONObject;

@Path("/game")
public class GameResource {


	@Inject
    private Provider<Request> grizzlyRequestProvider;
    private Map<String, Game> games;

    @Path("{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getGame(@PathParam("id") String id) {
        //return all games in current session
        if (id.toLowerCase().equals("all")) {
            return Response.status(Response.Status.OK).entity(this.getGames()).build();
        }

    	Game g = this.getGameById(id);
    	if (g == null) {
    		return Response.status(Response.Status.NOT_FOUND).entity("Game not found for ID: " + id).build();
    	} else {
	        return Response.ok(g).build();
	    }
    }

    @POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
    public Response createGame(Player input) throws JSONException {
    	Map<String, Game> games = this.getGames();
		String p0=(String) input.getPlayer_x();
		String p1=(String) input.getPlayer_o();
    	Game g = new Game(p0,p1);
    	games.put(g.getId(), g);
    	return Response.ok(g).build();
    }

    @Path("{id}")
    @PUT
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
    public Response updateGame(@PathParam("id") String id, Game currentGame) {
    	Game g = this.getGameById(id);
    	if (g == null) {
    		return Response.status(Response.Status.NOT_FOUND).entity("Game not found for ID: " + id).build();
    	}

		currentGame.updateTime();
		
    	if (currentGame.checkFull()) {
	    	currentGame.setStatus(Game.Status.END);
	    } else {
	    	currentGame.setStatus(Game.Status.PLAYING);
	    }
	    
	    int winner = currentGame.checkWinner();
	    if (winner != 0) {
	    	currentGame.setStatus(Game.Status.END);
	    }

		boolean validate = g.validate(currentGame);
		if (validate) {
			Map<String, Game> games = this.getGames();
			games.put(id, currentGame);
			return Response.ok(currentGame).build();
		} else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Game status is invalidate").build();
		}
    }


    @Path("{id}")
    @DELETE
	@Produces({MediaType.APPLICATION_JSON})
    public Response deleteGame(@PathParam("id") String id) {
    	Game g = this.getGameById(id);
    	if (g == null) {
    		return Response.status(Response.Status.NOT_FOUND).entity("Game not found for ID: " + id).build();
    	}
    	
    	Map<String, Game> games = this.getGames();
    	games.remove(id);
    	
    	return Response.ok().build();
    }

    private Session getSession() {
    	Request httpRequest = grizzlyRequestProvider.get();
    	Session session = httpRequest.getSession();
    	return session;
    }

    private Map<String, Game> getGames() {
    	Session session = this.getSession();
    	Map<String, Game> games = (Map<String, Game>)session.getAttribute("games");
        if (games == null) {
            games = new HashMap<>();
            session.setAttribute("games", games);
        }
    	return games;
    }

    private Game getGameById(String id) {
    	Map<String, Game> games = this.getGames();
    	if (games == null) {
    		return null;
    	} else {
	    	return games.get(id);
	    }
    }
}
