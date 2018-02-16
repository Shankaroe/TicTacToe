package com.example.gebruiker.tictactoe;

import java.io.Serializable;

/**
 * Created by Shankara on 12-2-2018.
 */

public class Game implements Serializable {
    final private int BOARD_SIZE = 3;
    private Tile[][] board;
    private Boolean playerOneTurn;  // true if player 1's turn, false if player 2's turn
    private int movesPlayed;
    private Boolean gameOver;
    private Boolean firstPlayerWin;
    private Boolean secondPlayerWin;
    private int mode;
    private int rowBlankTileAlmost;
    private int columnBlankTileAlmost;

    /** Constructor. */
    public Game(int newMode) {
        board = new Tile[BOARD_SIZE][BOARD_SIZE];
        for(int i=0; i<BOARD_SIZE; i++)
            for(int j=0; j<BOARD_SIZE; j++)
                board[i][j] = Tile.BLANK;
        playerOneTurn = true;
        gameOver = false;
        firstPlayerWin = false;
        secondPlayerWin = false;
        mode = newMode;
    }

    /** The draw method works as follows:
     *  1) If the clicked tile is blank it get's filled in (else Tile.INVALID is returned).
     *  2) If AI mode is on it lets the AI play.
     *  3) Throughout game status is checked. */
    public Tile draw(int row, int column) {
        checkFinished();
        if (gameOver) {
            return Tile.INVALID;
        }
        Tile tile = board[row][column];
        if (tile == Tile.BLANK) {
            if (playerOneTurn) {
                tile = Tile.CROSS;
            }
            else {
                tile = Tile.CIRCLE;
            }
            board[row][column] = tile;
            checkFinished();
            movesPlayed++;
            playerOneTurn = !playerOneTurn;
            if (movesPlayed == BOARD_SIZE*BOARD_SIZE) {
                gameOver = true;
                return tile;
            }
            if (mode == 1) {
                smartAImove();
                checkFinished();
                if (gameOver) {
                    return Tile.INVALID;
                }
            }
            return tile;
        }
        else {
            return Tile.INVALID;
        }
    }

    /** Most basic AI move, puts a circle in the first blank spot going from top left
     *  to bottom right. */
    private void stupidAImove() {
        for(int i=0; i<BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == Tile.BLANK) {
                    board[i][j] = Tile.CIRCLE;
                    i = BOARD_SIZE;
                    j = BOARD_SIZE;
                }
            }
        }
        playerOneTurn = !playerOneTurn;
    }

    /** Checks if the game is finished */
    private void checkFinished() {
        checkFinishedDiagonal(false,null);
        checkFinishedHorizontal(false,null);
        checkFinishedVertical(false,null);
    }

    /** First checks if there is a spot in which the AI can win the game, if there is none in
     *  horizontal/vertical/diagonal direction it will check if the player has an opportunity to win
     *  the game. If both can't win the game next round it will do the stupid AI move*/
    private void smartAImove() {
        checkFinished();
        if (!gameOver) {
            movesPlayed++;
            if (checkFinishedHorizontal(true, Tile.CIRCLE)) {
                board[rowBlankTileAlmost][columnBlankTileAlmost] = Tile.CIRCLE;
                playerOneTurn = !playerOneTurn;
            } else if (checkFinishedVertical(true, Tile.CIRCLE)) {
                board[rowBlankTileAlmost][columnBlankTileAlmost] = Tile.CIRCLE;
                playerOneTurn = !playerOneTurn;
            } else if (checkFinishedDiagonal(true, Tile.CIRCLE)) {
                board[rowBlankTileAlmost][columnBlankTileAlmost] = Tile.CIRCLE;
                playerOneTurn = !playerOneTurn;
            } else if (checkFinishedHorizontal(true, Tile.CROSS)) {
                board[rowBlankTileAlmost][columnBlankTileAlmost] = Tile.CIRCLE;
                playerOneTurn = !playerOneTurn;
            } else if (checkFinishedVertical(true, Tile.CROSS)) {
                board[rowBlankTileAlmost][columnBlankTileAlmost] = Tile.CIRCLE;
                playerOneTurn = !playerOneTurn;
            } else if (checkFinishedDiagonal(true, Tile.CROSS)) {
                board[rowBlankTileAlmost][columnBlankTileAlmost] = Tile.CIRCLE;
                playerOneTurn = !playerOneTurn;
            } else {
                stupidAImove();
            }
        }
    }

    /** This method has two goals: check if someone won (with almost=false) and check if someone is
     *  about to win (with almost=true and the player you are checking). It does this as follows:
     *  1) For each horizontal line count how many circles and crosses there are.
     *  2) If almost=true it returns true when there are two circles/crosses with one blank space.
     *  3) If almost=false it returns true if someone has won. */
    private boolean checkFinishedHorizontal(boolean almost, Tile tile) {
        int circleTiles = 0;
        int crossTiles = 0;
        boolean blankFound = false;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == Tile.CROSS) {
                    crossTiles++;
                } else if (board[i][j] == Tile.CIRCLE) {
                    circleTiles++;
                }
                else if (board[i][j] == Tile.BLANK) {
                    rowBlankTileAlmost = i;
                    columnBlankTileAlmost = j;
                    blankFound = true;
                }
            }
            if (almost) {
                if (tile == Tile.CROSS) {
                    if (crossTiles == BOARD_SIZE-1 && blankFound) {
                        return true;
                    }
                }
                else if (tile == Tile.CIRCLE ) {
                    if (circleTiles == BOARD_SIZE-1 && blankFound) {
                        return true;
                    }
                }
            }
            else {
                if (crossTiles == BOARD_SIZE) {
                    firstPlayerWin = true;
                    gameOver = true;
                } else if (circleTiles == BOARD_SIZE) {
                    secondPlayerWin = true;
                    gameOver = true;
                }
            }
            crossTiles = 0;
            circleTiles = 0;
            blankFound = false;
        }
        if (almost) {
            return false;
        }
        else {
            return gameOver;
        }
    }

    /** This method has two goals: check if someone won (with almost=false) and check if someone is
     *  about to win (with almost=true and the player you are checking). It does this as follows:
     *  1) For each vertical line count how many circles and crosses there are.
     *  2) If almost=true it returns true when there are two circles/crosses with one blank space.
     *  3) If almost=false it returns true if someone has won. */
    private boolean checkFinishedVertical(boolean almost, Tile tile) {
        int circleTiles = 0;
        int crossTiles = 0;
        boolean blankFound = false;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[j][i] == Tile.CROSS) {
                    crossTiles++;
                } else if (board[j][i] == Tile.CIRCLE) {
                    circleTiles++;
                }
                else if (board[j][i] == Tile.BLANK) {
                    rowBlankTileAlmost = j;
                    columnBlankTileAlmost = i;
                    blankFound = true;
                }
            }
            if (almost) {
                if (tile == Tile.CROSS) {
                    if (crossTiles == BOARD_SIZE-1 && blankFound ) {
                        return true;
                    }
                }
                else if (tile == Tile.CIRCLE) {
                    if (circleTiles == BOARD_SIZE-1 && blankFound ) {
                        return true;
                    }
                }
            }
            else {
                if (crossTiles == BOARD_SIZE) {
                    firstPlayerWin = true;
                    gameOver = true;
                } else if (circleTiles == BOARD_SIZE) {
                    secondPlayerWin = true;
                    gameOver = true;
                }
            }
            crossTiles = 0;
            circleTiles = 0;
            blankFound = false;
        }
        if (almost) {
            return false;
        }
        else {
            return gameOver;
        }
    }

    /** This method has two goals: check if someone won (with almost=false) and check if someone is
     *  about to win (with almost=true and the player you are checking). It does this as follows:
     *  For each possible diagonal combination it checks either:
     *    1) If almost=true it returns true when there are two circles/crosses with one blank space.
     *    2) If almost=false it returns true if someone has won. */
    private boolean checkFinishedDiagonal(boolean almost, Tile tile) {
        int[][] positions = new int[][]{{0,0,2,2,1,1},{2,0,0,2,1,1},{0,0,1,1,2,2},{0,2,1,1,2,0},{2,0,1,1,0,2},{2,2,1,1,0,0}};
        for (int i = 0; i < positions.length; i++) {
            int row1 = positions[i][0];
            int column1 = positions[i][1];
            int row2 = positions[i][2];
            int column2 = positions[i][3];
            int rowBlank = positions[i][4];
            int columnBlank = positions[i][5];
            if (almost) {
                rowBlankTileAlmost = rowBlank;
                columnBlankTileAlmost = columnBlank;
                if (tile == Tile.CROSS) {
                    if (board[row1][column1] == Tile.CROSS && board[row2][column2] == Tile.CROSS && board[rowBlank][columnBlank] == Tile.BLANK) {
                        return true;
                    }
                }
                else if (tile == Tile.CIRCLE) {
                    if (board[row1][column1] == Tile.CIRCLE && board[row2][column2] == Tile.CIRCLE && board[rowBlank][columnBlank] == Tile.BLANK ) {
                        return true;
                    }
                }
            }
            else {
                if (board[row1][column1] == Tile.CROSS && board[row2][column2] == Tile.CROSS && board[rowBlank][columnBlank] == Tile.CROSS) {
                    firstPlayerWin = true;
                    gameOver = true;
                } else if (board[row1][column1] == Tile.CIRCLE && board[row2][column2] == Tile.CIRCLE && board[rowBlank][columnBlank] == Tile.CIRCLE) {
                    secondPlayerWin = true;
                    gameOver = true;
                }
            }

        }
        if (almost) {
            return false;
        }
        else {
            return gameOver;
        }
    }

    /** Returns if the game is over */
    public boolean getGameStatus() {
        return gameOver;
    }

    /** Returns if Player 1 has won the game */
    public boolean getFirstPlayerStatus() {
        return firstPlayerWin;
    }

    /** Returns if Player 2 has won the game */
    public boolean getSecondPlayerStatus() {
        return secondPlayerWin;
    }

    /** Returns the Tile status at the provided index */
    public Tile getTile(int index) {
        int row=index/3;
        int column=index%3;
        return board[row][column];
    }
}
