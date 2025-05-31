package com.javarush.games.ticktacktoe;

import com.javarush.engine.cell.*;

import java.util.Random;

/**
 * Game TickTackToe (X-0) on site javarush.com.
 * https://javarush.com/ua/projects/games/com.javarush.games.ticktacktoe
 * Use engine of this site. Don't work without site.
 */
public class TicTacToeGame extends Game {
    private static final int FIELD_SIZE = 3;

    private static final String SYMBOL_SPACE = " ";
    private static final String SYMBOL_X = "X";
    private static final String SYMBOL_O = "O";

    private static final Color COLOR_CELL_BACKGROUND = Color.WHITE;
    private static final Color COLOR_SYMBOL_X = Color.GREEN;
    private static final Color COLOR_SYMBOL_O = Color.RED;
    private static final Color COLOR_MSG_PLAYER_1_WIN = Color.GREEN;
    private static final Color COLOR_MSG_PLAYER_1_LOOSE = Color.RED;
    private static final Color COLOR_MSG_DROW = Color.BLUE;
    private static final Color COLOR_MSG_BACKGROUND = Color.NONE;

    private static final String MESSAGE_PLAYER_1_WIN = "You Win!";
    private static final String MESSAGE_PLAYER_1_LOOSE = "Game Over";
    private static final String MESSAGE_DRAW = "Draw!";
    private static final int MSG_TEXT_SIZE = 55;

    private static final Key KEY_RESTART_GAME = Key.ESCAPE;
    private static final Key KEY_RESTART_GAME_IF_GAME_STOPED = Key.SPACE;

    private int[][] model = new int[FIELD_SIZE][FIELD_SIZE];
    private int currentPlayer;
    private boolean isGameStopped;

    public void initialize(){
        setScreenSize(FIELD_SIZE, FIELD_SIZE);
        startGame();
        updateView();
    }

    public void startGame() {
        for (int x = 0; x < FIELD_SIZE; x++) {
            for (int y = 0; y < FIELD_SIZE; y++) {
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
                symbolForPrint = SYMBOL_X;
                symbolColor = COLOR_SYMBOL_X;
                break;
            case (2) :
                symbolForPrint = SYMBOL_O;
                symbolColor = COLOR_SYMBOL_O;
                break;
            default :
                symbolForPrint = SYMBOL_SPACE;
                symbolColor = COLOR_CELL_BACKGROUND;
        }
        setCellValueEx(x, y, COLOR_CELL_BACKGROUND, symbolForPrint, symbolColor);
    }

    public void updateView() {
        for (int x = 0; x < FIELD_SIZE; x++) {
            for (int y = 0; y < FIELD_SIZE; y++) {
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
            if (currentPlayer == 1) {
                showMessageDialog(COLOR_MSG_BACKGROUND, MESSAGE_PLAYER_1_WIN, COLOR_MSG_PLAYER_1_WIN,
                        MSG_TEXT_SIZE);
            } else {
                showMessageDialog(COLOR_MSG_BACKGROUND, MESSAGE_PLAYER_1_LOOSE, COLOR_MSG_PLAYER_1_LOOSE,
                        MSG_TEXT_SIZE);
            }
            return;
        }

        if (!hasEmptyCell()) {
            isGameStopped = true;
            showMessageDialog(COLOR_MSG_BACKGROUND, MESSAGE_DRAW, COLOR_MSG_DROW, MSG_TEXT_SIZE);
            return;
        }
    }

    public boolean checkWin(int x, int y, int n) {
        boolean winX = true;
        boolean winY = true;
        boolean winMainDiagonal = true;
        boolean winSecondaryDiagonal = true;

        for (int i = 0; i < FIELD_SIZE; i++) {
            winX = winX && (model[i][y] == n);
            winY = winY && (model[x][i] == n);
            winMainDiagonal = winMainDiagonal && (model[i][i] == n);
            winSecondaryDiagonal = winSecondaryDiagonal && (model[i][FIELD_SIZE - i - 1] == n);
        }
        return (winX || winY || winMainDiagonal || winSecondaryDiagonal) ;
    }

    public boolean hasEmptyCell()  {
        for (int x = 0; x < FIELD_SIZE; x++) {
            for (int y = 0; y < FIELD_SIZE; y++) {
                if (model[x][y] == 0) {
                    return true;
                };
            }
        }
        return false;
    }

    public void onKeyPress(Key key) {
        if ((key == KEY_RESTART_GAME) || (isGameStopped && (key == KEY_RESTART_GAME_IF_GAME_STOPED))) {
            startGame();
            updateView();
        }
    }

    public void computerTurn() {
        int centralField = FIELD_SIZE / 2;
        if (model[centralField][centralField] == 0) {
            setSignAndCheck(centralField, centralField);
            return;
        }

        // check win currentPlayer for one turn
        for (int xi = 0; xi < FIELD_SIZE; xi++) {
            for (int yi = 0; yi < FIELD_SIZE; yi++) {
                if (checkFutureWin(xi, yi, currentPlayer) ) {
                    setSignAndCheck(xi, yi);
                    return;
                };
            }
        }

        // check win other player for one turn and d't allow it
        for (int xi = 0; xi < FIELD_SIZE; xi++) {
            for (int yi = 0; yi < FIELD_SIZE; yi++) {
                if (checkFutureWin(xi, yi, 3 - currentPlayer) ) {
                    setSignAndCheck(xi, yi);
                    return;
                };
            }
        }

        // turn in first random free field
        Random random = new Random();
        int xi;
        int yi;
        do {
            xi = random.nextInt(FIELD_SIZE);
            yi = random.nextInt(FIELD_SIZE);
        } while ((model[xi][yi] != 0));
        setSignAndCheck(xi, yi);
        return;

// turn in first free field
//        for (int xi = 0; xi < fieldSize; xi++) {
//            for (int yi = 0; yi < fieldSize; yi++) {
//                if (model[xi][yi] == 0) {
//                    setSignAndCheck(xi, yi);
//                    return;
//                };
//            }
//        }

    }

    public boolean checkFutureWin(int x, int y, int n) {
        if (model[x][y] != 0) {
            return false;
        }
        model[x][y] = n;
        boolean futureWin = checkWin(x, y, n);
        model[x][y] = 0;
        return  futureWin;

    }
}
