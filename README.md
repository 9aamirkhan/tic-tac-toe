# tic-tac-toe1. 

1. Start new session

    If you just start the service, there is no session data in the system. You can use GET ALL api to start a new session.
    HTTP GET Request URL: http://localhost:9090/tictactoe/game/all
    
2. Get all games

	http://localhost:9090/tictactoe/game/all
3. Create a new game

	http://localhost:9090/tictactoe/game
4. Get game

	http://localhost:9090/tictactoe/game/163ee5c9-8e1c-4ef1-b982-cf467354cf9c
5. Update game
  PUT Method
	http://localhost:9090/tictactoe/game/163ee5c9-8e1c-4ef1-b982-cf467354cf9c
  ```json
	{"id":"163ee5c9-8e1c-4ef1-b982-cf467354cf9c","dimension":3,"grid":[[1,0,0],[0,0,0],[0,0,0]]}
	```
  6. Update game and game end

	There are two situations the game will end. The game status value will be END.
	
	* The game board full
	
		If no winner, then the winner value is 0.
	
	* Has winner
	
		The winner value will be 1 (player 1) or 2 (player 2).

7. Delete game
	Delete Method
	http://localhost:9090/tictactoe/game/163ee5c9-8e1c-4ef1-b982-cf467354cf9c
	
