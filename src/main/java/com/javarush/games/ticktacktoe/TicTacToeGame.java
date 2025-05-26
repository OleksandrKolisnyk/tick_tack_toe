package com.javarush.games.ticktacktoe;

import com.javarush.engine.cell.*;

public class TicTacToeGame extends Game {
    private static final int fieldSize = 3;

    private static final String symbolSpace = " ";
    private static final String symbolX = "X";
    private static final String symbol0 = "O";

    private static final Color symbolColorSpace = Color.WHITE;
    private static final Color symbolColorX = Color.RED;
    private static final Color symbolColor0 = Color.BLUE;

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
        setSignAndCheck(x, y);
    }

    public void setSignAndCheck(int x, int y) {
        if (isGameStopped || model[x][y] != 0) {
            return;
        }

        model[x][y] = currentPlayer;
        updateView();

        if (checkWin(x, y, currentPlayer)) {
            isGameStopped = true;
            Color symbolColor = (currentPlayer == 1) ? symbolColorX : symbolColor0;
            showMessageDialog(Color.NONE, " Player #" + currentPlayer + " win!",
                    symbolColor, msgTextSize);
            return;
        }

        if (!hasEmptyCell()) {
            isGameStopped = true;
            showMessageDialog(Color.NONE, " Draw!",  Color.BLUE, msgTextSize);
            return;
        }

        currentPlayer = 3 - currentPlayer;
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
}
