package com.javarush.games.ticktacktoe;

import com.javarush.engine.cell.*;

/** Game TickTackToe (X-0) on site javarush.com.
https://javarush.com/ua/projects/games/com.javarush.games.ticktacktoe
Use engine of this site. D't work w/o site.
*/
public class TicTacToeGame extends Game {
    private static final int fieldSize = 3;

    private static final String symbolSpace = " ";
    private static final String symbolX = "X";
    private static final String symbol0 = "O";

    private static final Color symbolColorSpace = Color.WHITE;
    private static final Color symbolColorX = Color.RED;
    private static final Color symbolColor0 = Color.BLUE;
    private static final Color symbolColorPlayer1Win = Color.GREEN;
    private static final Color symbolColorPlayer1Loose = Color.RED;

    private static final String player1winMessage = "You Win!";
    private static final String player1looseMessage = "Game Over";


    private static final int msgTextSize = 45;
    private static final Key restartGameKey = Key.ESCAPE;
    private static final Key restartGameKeyIfGameStoped = Key.SPACE;

    private int[][] model = new int[fieldSize][fieldSize];
    private int currentPlayer;
    private boolean isGameStopped;

    public void initialize(){
        setScreenSize(fieldSize, fieldSize);
        startGame();
        updateView();
    }

    public void startGame() {
        for (int x = 0; x < fieldSize; x++) {
            for (int y = 0; y < fieldSize; y++) {
                model[x][y] = 0;
            }
        }
        currentPlayer = 1;
        isGameStopped = false;
    }

    public void updateCellView(int x, int y, int value) {
        String symbolForPrint;
        Color symbolColor;
        switch (value) {
            case (1) :
                symbolForPrint = symbolX;
                symbolColor = symbolColorX;
                break;
            case (2) :
                symbolForPrint = symbol0;
                symbolColor = symbolColor0;
                break;
            default :
                symbolForPrint = symbolSpace;
                symbolColor = symbolColorSpace;
        }
        setCellValueEx(x, y, symbolColorSpace, symbolForPrint, symbolColor);
    }

    public void updateView() {
        for (int x = 0; x < fieldSize; x++) {
            for (int y = 0; y < fieldSize; y++) {
                updateCellView(x, y, model[x][y]);
            }
        }
    }

    public void onMouseLeftClick(int x, int y) {
        if (isGameStopped || model[x][y] != 0) {
            return;
        }
        setSignAndCheck(x, y);
        if (isGameStopped) {
            return;
        }

        currentPlayer = 3 - currentPlayer;

        computerTurn();
        if (isGameStopped) {
            return;
        }

        currentPlayer = 3 - currentPlayer;
    }

    public void setSignAndCheck(int x, int y) {
        model[x][y] = currentPlayer;
        updateView();

        if (checkWin(x, y, currentPlayer)) {
            isGameStopped = true;
//            Color symbolColor = (currentPlayer == 1) ? symbolColorX : symbolColor0;
//            showMessageDialog(Color.NONE, " Player #" + currentPlayer + " win!",
//                    symbolColor, msgTextSize);
            if (currentPlayer == 1) {
                showMessageDialog(Color.NONE, player1winMessage, symbolColorPlayer1Win,
                        msgTextSize);
            } else {
                showMessageDialog(Color.NONE, player1looseMessage, symbolColorPlayer1Loose,
                        msgTextSize);
            }
            return;
        }

        if (!hasEmptyCell()) {
            isGameStopped = true;
            showMessageDialog(Color.NONE, " Draw!",  Color.BLUE, msgTextSize);
            return;
        }
    }

    public boolean checkWin(int x, int y, int n) {
        boolean winX = true;
        boolean winY = true;
        boolean winMainDiagonal = true;
        boolean winSecondaryDiagonal = true;

        for (int i = 0; i < fieldSize; i++) {
            winX = winX && (model[i][y] == n);
            winY = winY && (model[x][i] == n);
            winMainDiagonal = winMainDiagonal && (model[i][i] == n);
            winSecondaryDiagonal = winSecondaryDiagonal && (model[i][fieldSize - i - 1] == n);
        }
        return (winX || winY || winMainDiagonal || winSecondaryDiagonal) ;
    }

    public boolean hasEmptyCell()  {
        for (int x = 0; x < fieldSize; x++) {
            for (int y = 0; y < fieldSize; y++) {
                if (model[x][y] == 0) {
                    return true;
                };
            }
        }
        return false;
    }

    public void onKeyPress(Key key) {
        if ((key == restartGameKey) || (isGameStopped && (key == restartGameKeyIfGameStoped))) {
            startGame();
            updateView();
        }
    }

    public void computerTurn() {
        int centralField = fieldSize / 2;
        if (model[centralField][centralField] == 0) {
            setSignAndCheck(centralField, centralField);
            return;
        }

        // check win currentPlayer for one turn
        for (int xi = 0; xi < fieldSize; xi++) {
            for (int yi = 0; yi < fieldSize; yi++) {
                if (checkFutureWin(xi, yi, currentPlayer) ) {
                    setSignAndCheck(xi, yi);
                    return;
                };
            }
        }

        // check win other player for one turn and d't allow it
        for (int xi = 0; xi < fieldSize; xi++) {
            for (int yi = 0; yi < fieldSize; yi++) {
                if (checkFutureWin(xi, yi, 3 - currentPlayer) ) {
                    setSignAndCheck(xi, yi);
                    return;
                };
            }
        }

        // turn in first free field
        for (int xi = 0; xi < fieldSize; xi++) {
            for (int yi = 0; yi < fieldSize; yi++) {
                if (model[xi][yi] == 0) {
                    setSignAndCheck(xi, yi);
                    return;
                };
            }
        }
    }

    public boolean checkFutureWin(int x, int y, int n) {
        if (model[x][y] != 0) {
            return false;
        }

        boolean winX = true;
        boolean winY = true;
        boolean winMainDiagonal = (x == y) ? true : false;
        boolean winSecondaryDiagonal = (x == (fieldSize - y - 1)) ? true : false;

        for (int i = 0; i < fieldSize; i++) {
            if (i != x) {
                winX = winX && (model[i][y] == n) ;
            }
            if (i != y) {
                winY = winY && (model[x][i] == n);
            }
            // checking fields on main diagonal
            if (winMainDiagonal && (i != x)) {
                winMainDiagonal = winMainDiagonal && (model[i][i] == n);
            }
            // checking fields on Secondary diagonal
            if (winSecondaryDiagonal && (i != x)) {
                winSecondaryDiagonal = winSecondaryDiagonal && (model[i][fieldSize - i - 1] == n);
            }
        }
        return (winX || winY || winMainDiagonal || winSecondaryDiagonal) ;
    }
}
