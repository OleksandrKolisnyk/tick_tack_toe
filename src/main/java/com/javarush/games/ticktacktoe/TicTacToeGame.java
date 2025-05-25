package com.javarush.games.ticktacktoe;

import com.javarush.engine.cell.*;

public class TicTacToeGame extends Game {
    private static final int fieldSizeX = 3;
    private static final int fieldSizeY = 3;

    private static final String symbolSpace = " ";
    private static final String symbolX = "X";
    private static final String symbol0 = "O";

    private static final Color symbolColorSpace = Color.WHITE;
    private static final Color symbolColorX = Color.RED;
    private static final Color symbolColor0 = Color.BLUE;


    private int[][] model = new int[fieldSizeX][fieldSizeY];
    private int currentPlayer;
    private boolean isGameStopped;

    public void initialize(){
        setScreenSize(fieldSizeX, fieldSizeY);
        startGame();
        updateView();
    }

    public void startGame() {
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
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
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                updateCellView(x, y, model[x][y]);
            }
        }
    }

    public void onMouseLeftClick(int x, int y) {
        if (isGameStopped || model[x][y] != 0) {
          return;
        }
        model[x][y] = currentPlayer;
        updateView();
        currentPlayer = 3 - currentPlayer;
    }

    public boolean checkWin(int x, int y, int n) {
        boolean winX = true;
        for (int xi = 0; xi < fieldSizeX; xi++) {
            winX = winX && (model[x][y] == model[xi][y]);
        }
        if (winX ) {
          return true;
        }

        boolean winY = true;
        for (int yi = 0; yi < fieldSizeY; yi++) {
            winY = winY && (model[x][y] == model[x][yi]);
        }
        if (winY ) {
            return true;
        }

        return false;
    }
}
