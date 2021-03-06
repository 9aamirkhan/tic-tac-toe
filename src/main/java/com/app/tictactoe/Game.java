package com.app.tictactoe;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@XmlRootElement
public class Game implements Serializable {
	private String id;
	private int dimension = 3;
	private int[][] grid;
	private Status status;
	private int winner;
	private long lastUpdateTime;
	public enum Status {
		START, PLAYING, END
	}

	public String[] getPlayerNames() {
		return playerNames;
	}

	public void setPlayerNames(String[] playerNames) {
		this.playerNames = playerNames;
	}

	private String [] playerNames=new String[2];

	public Game(){
		this("player_x","player_o");
	}
	public Game(String player0, String player1) {
		this(3,player0,player1);
	}
	public Game(int dimension,String player0, String player1) {
		this.playerNames[0]=player0;
		this.playerNames[1]=player1;
		this.grid = new int[dimension][dimension];
		this.id = java.util.UUID.randomUUID().toString();
		this.status = Status.START;
		this.lastUpdateTime = System.currentTimeMillis();
	}
	
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public int getDimension() {
		return this.dimension;
	}
	public int getWinner() {
		return this.winner;
	}
	public void setWinner(int winner) {
		this.winner = winner;
	}

	public int[][] getGrid() {
		return this.grid;
	}
	
	public void setGrid(int[][] grid) {
		this.grid = grid;
	}
	
	public Status getStatus() {
		return this.status;
	}
	
	public void setStatus(Status s) {
		this.status = s;
	}
	public void updateTime() {
		this.lastUpdateTime = System.currentTimeMillis();
	}

	public long getLastUpdateTime() {
		return this.lastUpdateTime;
	}
	public String toJson() throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(this);
		return jsonString;
	}
    @Override
    public String toString(){
	    try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(this);
            return jsonString;
	    } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }
	public static Game fromJson(String jsonString) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		Game g = mapper.readValue(jsonString, Game.class);
		return g;
	}
	public boolean validate(Game latestGame) {
		if (!this.id.equals(latestGame.getId())) return false;
		if (this.dimension != latestGame.getDimension()) return false;
		if (this.lastUpdateTime > latestGame.getLastUpdateTime()) return false;
		if (this.status == Status.END && latestGame.getStatus() != Status.END) return false;
		if (this.status == Status.PLAYING &&
                !(latestGame.getStatus() == Status.PLAYING || latestGame.getStatus() == Status.END)) return false;
		
		int[][] grid = this.grid;
		int[][] latestGrid = latestGame.getGrid();
		
		int count1 = 0, count2 = 0;
		int latestCount1 = 0, latestCount2 = 0;
		boolean differenceFound = false;
		int difference = 0;
		
		for (int i=0; i<this.dimension; i++) {
			for (int j=0; j<this.dimension; j++) {
				if (latestGrid[i][j] != 0 && latestGrid[i][j] != 1 && latestGrid[i][j] != 2) return false;
				if (grid[i][j] == 1) count1++;
				if (grid[i][j] == 2) count2++;
				if (latestGrid[i][j] == 1) latestCount1++;
				if (latestGrid[i][j] == 2) latestCount2++;
				if (grid[i][j] != latestGrid[i][j]) {
					if (differenceFound) return false; //Only allow one difference
					difference = latestGrid[i][j];
					differenceFound = true;
				}
			}
		}

		if(!differenceFound) return false;

		if (difference == 1) {
			if (!((count1 + 1 == latestCount1) && (count2 == latestCount2) && (latestCount1 -1 == latestCount2))) return false;
		} else {
			if (!((count1 == latestCount1) && (count2 + 1 == latestCount2) && (latestCount1 == latestCount2))) return false;
		}
		
		return true;
	}

	public int checkWinner() {
		int[][] grid = this.grid;
		int d = this.dimension;
		
		//Check Columns
		for (int i=0; i<d; i++) {
			boolean win = true;
			for(int j=0; j<d-1; j++) {
				win = win && grid[i][j] != 0 && grid[i][j] == grid[i][j+1];
				if (!win) break;
			}

			if (win) {
				this.winner = grid[i][0];
				return this.winner;
			}
		}
		
		//Check Rows
		for (int j=0; j<d; j++) {
			boolean win = true;
			for(int i=0; i<d-1; i++) {
				win = win && grid[i][j] != 0 && grid[i][j] == grid[i+1][j];
				if (!win) break;
			}

			if (win) {
				this.winner = grid[0][j];
				return this.winner;
			}
		}
		
		//Check Diagonals
		boolean win = true;
		for (int i=0; i<d-1; i++) {
			win = win && grid[i][i] != 0 && grid[i][i] == grid[i+1][i+1];
			if (!win) break;
		}

		if (win) {
			this.winner = grid[0][0];
			return this.winner;
		}

		//Check Diagonals2
		win = true;
		for (int i=0; i<d-1; i++) {
			int j = d - 1 - i;
			win = win && grid[i][j] != 0 && grid[i][j] == grid[i+1][j-1];
			if (!win) break;
		}

		if (win) {
			this.winner = grid[0][d-1];
			return this.winner;
		}
		
		return 0;
	}
	public boolean checkFull() {
		int d = this.dimension;
		
		for (int i=0; i<d; i++) {
			for(int j=0; j<d; j++) {
				if (this.grid[i][j] == 0) return false;
			}
		}
		
		return true;
	}
}
class Player{
	String player_x;
	String player_o;

	public String getPlayer_o() {
		return player_o;
	}

	public void setPlayer_o(String player_o) {
		this.player_o = player_o;
	}



	public String getPlayer_x() {
		return player_x;
	}

	public void setPlayer_x(String player_x) {
		this.player_x = player_x;
	}
}