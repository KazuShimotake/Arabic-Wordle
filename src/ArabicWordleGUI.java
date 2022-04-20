import java.awt.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;


public class ArabicWordleGUI extends JFrame{// implements ActionListener {
    private JFrame window;//Displays the whole GUI
    private JPanel mainPanel;//Holds the parts for the GUI
    private JMenuBar menuBar;
    private JMenuItem menu;
    private JMenuItem gameStats;
    private JMenuItem quit;

    private JPanel pane;//Holds the section for guessing words
    private JPanel firstWord;//Holds the boxes in the first line from the top
    private JPanel secondWord;//Holds the boxes in the second line from the top
    private JPanel thirdWord;//Holds the boxes in the third line from the top
    private JPanel fourthWord;//Holds the boxes in the fourth line from the top
    private JPanel fifthWord;//Holds the boxes in the fifth line from the top
    private JPanel sixthWord;//Holds the boxes in the sixth line from the top

    private JPanel keyboard;//Holds the keyboard
    private JPanel keyboardTop;//Holds the top row of the keyboard
    private JPanel keyboardTopMid;//Holds the top middle row of the keyboard
    private JPanel keyboardBotMid;//Holds the bottom middle row of the keyboard
    private JPanel keyboardBot;//Holds the bottom row of the keyboard
    private int currentRow;//What row the user is currently guessing on
    private int currentCol;//What column the user is currently guessing on
    private int letterMode;

    /**
     * MASTER CONTROL CENTER
     * this is where all the final variables that allow adjustment
     * of the sizes of various parts of the GUI are held
     */
    private int scale = 2;
    //window dimensions
    private final int WINDOW_WIDTH = 220*scale;//width of the GUI as a whole
    private final int WINDOW_HEIGHT = 400*scale;//height of the GUI as a whole

    //guess section dimensions
    private final int GUESS_WIDTH = 200*scale;//width of the section for guesses as a whole
    private final int GUESS_HEIGHT = 240*scale;//height of the section for guesses as a whole
    private final int LETTER_SIDE = 40*scale;//side length of one box holding a guessed letter
    private final int LETTER_SIZE = 15*scale;//font size of the guessed letters

    //keyboard section dimensions
    private final int KEYBOARD_WIDTH = 220*scale;//width of the keyboard as a whole
    private final int KEYBOARD_HEIGHT = 160*scale;//height of the keyboard as a whole
    private final int BUTTON_WIDTH = 17*scale;//width of one key on the keyboard
    private final int BUTTON_HEIGHT = 30*scale;//height of one key on the keyboard
    private final int BUTTON_SIZE = 10*scale;//font size of the letters on the keyboard
    private final int ENTER_WIDTH = 27*scale;//width of the enter key on the keyboard
    private final int ENTER_HEIGHT = 30*scale;//height of the enter key on the keyboard
    private final int DELETE_WIDTH = 27*scale;//width of the delete key on the keyboard
    private final int DELETE_HEIGHT = 30*scale;//height of the delete key on the keyboard

    private int gameMode = 2;
    private int wordIndex=7;
    private int gameCount=0;
    private int oneWinCount=0;
    private int twoWinCount=0;
    private int threeWinCount=0;
    private int fourWinCount=0;
    private int fiveWinCount=0;
    private int sixWinCount=0;
    private int loseCount=0;

    private int wordsRemaining=45;
    private boolean checker = false;
    private int fourLineCount = 6;
    private int fiveLineCount = 7;

    private JTextField[][] board;//2D array holding all the boxes in the guessing section
    private JButton[][] keys;//2D array holding all the keys in the keyboard
    private String[][] letters;//2D array holding all the letters in the keyboard

    private ArrayList<String> answerTrans = new ArrayList<String>();
    private ArrayList<String> answer = new ArrayList<String>();
    private ArrayList<String> guess = new ArrayList<String>();
    private ArrayList<Integer> randomIndex = new ArrayList<Integer>();

    public int counter=0;

    /**
     * Constructor for the Arabic Wordle's GUI. It takes in the answer read from the file in
     * the main function in ArabicWordleMain and then creates the GUI(the visible parts to a user)
     *
     */
    public ArabicWordleGUI(){
        super();
        String[] options = {"Three Letter Mode", "Four Letter Mode", "Five Letter Mode"};
        int input = JOptionPane.showOptionDialog(null, "Please only click Five Letter Mode for now",
                "Click a button", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (input == 0) {
            gameMode=0;
            System.exit(0);
        }else if (input == 1) {
            gameMode=1;
            System.exit(0);
        }else if(input == 2){
            gameMode=2;
            letterMode = 5;

            for(int i = 0; i<45;i++){
                randomIndex.add(i);
            }
            Collections.shuffle(randomIndex);
        }
        reset();
    }

    private void reset(){
        window = new JFrame("Arabic Wordle");
        window.setTitle("Arabic Wordle");
        window.setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
        window.setResizable(false);
        window.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        letters = new String[][]{{"آ", "أ", "إ", "ئ", "ء"},
                {"ض", "ص", "ث", "ق", "ف", "غ", "ع", "ه", "خ", "ح", "ج"},
                {"ش", "س", "ي", "ب", "ل", "ا", "ت", "ن", "م", "ك", "ة"},
                {"ذ", "د", "ر", "ز", "ط", "ظ", "ى", "و"}};
        mainPanel = new JPanel();
        mainPanel.removeAll();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        pane = new JPanel();
        pane.setLayout(new GridLayout(6,1));
        //pane.setLayout(new BoxLayout(pane,BoxLayout.Y_AXIS));
        pane.setMaximumSize(new Dimension(GUESS_WIDTH,GUESS_HEIGHT));
        firstWord = new JPanel();
        firstWord.setLayout(new GridLayout(1,letterMode));
        secondWord = new JPanel();
        secondWord.setLayout(new GridLayout(1,letterMode));
        thirdWord = new JPanel();
        thirdWord.setLayout(new GridLayout(1,letterMode));
        fourthWord = new JPanel();
        fourthWord.setLayout(new GridLayout(1,letterMode));
        fifthWord = new JPanel();
        fifthWord.setLayout(new GridLayout(1,letterMode));
        sixthWord = new JPanel();
        sixthWord.setLayout(new GridLayout(1,letterMode));

        keyboard = new JPanel();
        keyboard.setLayout(new BoxLayout(keyboard,BoxLayout.Y_AXIS));
        keyboard.setMaximumSize(new Dimension(KEYBOARD_WIDTH,KEYBOARD_HEIGHT));

        keyboardTop = new JPanel();
        keyboardTop.invalidate();
        keyboardTop.validate();
        keyboardTop.repaint();
        keyboardTop.setLayout(new FlowLayout(FlowLayout.CENTER,3,1));//GridLayout(1,10));

        keyboardTopMid = new JPanel();
        keyboardTopMid.setLayout(new FlowLayout(FlowLayout.CENTER,3,1));//GridLayout(1,8));

        keyboardBotMid = new JPanel();
        keyboardBotMid.setLayout(new FlowLayout(FlowLayout.CENTER,3,1));

        keyboardBot = new JPanel();
        keyboardBot.setLayout(new FlowLayout(FlowLayout.CENTER,3,1));//GridLayout(1,10));

        currentRow = 0;
        currentCol = letterMode-1;
        letterMode = 5;

        answerTrans.clear();
        answer.clear();
        String answerHolder = "";
        if (gameMode == 0) {
            System.exit(0);
        }else if (gameMode == 1) {
            System.exit(0);
        }else if(gameMode == 2){
            letterMode = 5;
            answerHolder = getAnswer(fiveLineCount);
        }

        for(int i = 0; i<letterMode-1;i++){
            answer.add(answerHolder.substring(0,1));
            answerHolder = answerHolder.substring(2);
        }
        answer.add(answerHolder);
        board = new JTextField[6][letterMode];
        keys = new JButton[4][11];



        initializeMenuBar();
        initializeBoard();
        initializeKeyboard();

        mainPanel.add(menuBar);
        mainPanel.add(pane);
        mainPanel.add(keyboard);
        window.add(mainPanel);
        window.getContentPane().invalidate();
        window.getContentPane().validate();
        window.getContentPane().repaint();
        window.setVisible(true);
    }

    private String getAnswer(int lineCount){
        wordIndex = lineCount;
        File answerList = new File("FiveLetterAnswers.txt");;
        if(gameMode == 1) {
            answerList = new File("FourLetterAnswers.txt");
        }else if(gameMode ==2){
            answerList = new File("FiveLetterAnswers.txt");
        }
        String answerLine = "";
        try {
            BufferedReader linereader = new BufferedReader(new FileReader(answerList));
            int wordNumber = 0;
            if(randomIndex.size()==1){
                wordNumber = randomIndex.get(0);
                randomIndex.clear();
            }else{
                wordNumber = randomIndex.get(randomIndex.size()-1);
                randomIndex.remove(randomIndex.size()-1);
            }
            int mather = wordNumber+1;
            // Read the line containing the word for this round
            for(int i = 0; i<wordNumber;i++){
                answerLine = linereader.readLine();
            }
            answerLine = linereader.readLine();
        } catch (FileNotFoundException e) {
            // Return a non-zero exit code to the OS
            System.exit(1);
        } catch (IOException e) {
            System.exit(1);
        }
        return answerLine;
    }
    private void initializeMenuBar(){
        menuBar = new JMenuBar();
        menu = new JMenu("Menu");
        gameStats = new JMenuItem("Game Stats");
        gameStats.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                JOptionPane.showMessageDialog(null,
                        "Games Played: " + gameCount + "\n" +
                        "Words Left: " + wordsRemaining + "\n" +
                        "Wins in one guess: " + oneWinCount + "\n" +
                        "Wins in two guesses: " + twoWinCount + "\n" +
                        "Wins in three guesses: " + threeWinCount + "\n" +
                        "Wins in four guesses: " + fourWinCount + "\n" +
                        "Wins in five guesses: " + fiveWinCount + "\n" +
                        "Wins in six guesses: " + sixWinCount + "\n" +
                        "Losses: " + loseCount + "\n" +
                        "If letters remain, click a button and they'll go away");
            }
        });

        quit = new JMenuItem("Quit");
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, "Thanks for Playing!");
                System.exit(0);
            }
        });
        menu.add(gameStats);
        menu.add(quit);

        menuBar.add(menu);
    }

    /**
     * Creates all the boxes for the upper half of the GUI where the user's
     * guesses go. It creates the grid of boxes one row at a time from top
     * to bottom then adds each to the window holding the boxes for guesses
     */
    private void initializeBoard(){
        for(int i = 0; i < letterMode; i++){
            JTextField letter = new JTextField("");
            letter.setPreferredSize(new Dimension(LETTER_SIDE, LETTER_SIDE));
            letter.setBackground(Color.WHITE);
            letter.setHorizontalAlignment(JTextField.CENTER);
            letter.setEditable(false);
            letter.setFont(new Font(Font.SANS_SERIF,Font.BOLD,LETTER_SIZE));
            board[0][i] = letter;
            firstWord.add(letter);
        }
        for(int j = 0; j < letterMode; j++){
            JTextField letter = new JTextField("");
            letter.setPreferredSize(new Dimension(LETTER_SIDE, LETTER_SIDE));
            letter.setBackground(Color.WHITE);
            letter.setHorizontalAlignment(JTextField.CENTER);
            letter.setEditable(false);
            letter.setFont(new Font(Font.SANS_SERIF,Font.BOLD,LETTER_SIZE));
            board[1][j] = letter;
            secondWord.add(letter);
        }
        for(int k = 0; k < letterMode; k++){
            JTextField letter = new JTextField("");
            letter.setPreferredSize(new Dimension(LETTER_SIDE, LETTER_SIDE));
            letter.setBackground(Color.WHITE);
            letter.setHorizontalAlignment(JTextField.CENTER);
            letter.setEditable(false);
            letter.setFont(new Font(Font.SANS_SERIF,Font.BOLD,LETTER_SIZE));
            board[2][k] = letter;
            thirdWord.add(letter);
        }
        for(int l = 0; l < letterMode; l++){
            JTextField letter = new JTextField("");
            letter.setPreferredSize(new Dimension(LETTER_SIDE, LETTER_SIDE));
            letter.setBackground(Color.WHITE);
            letter.setHorizontalAlignment(JTextField.CENTER);
            letter.setEditable(false);
            letter.setFont(new Font(Font.SANS_SERIF,Font.BOLD,LETTER_SIZE));
            board[3][l] = letter;
            fourthWord.add(letter);
        }
        for(int m = 0; m < letterMode; m++){
            JTextField letter = new JTextField("");
            letter.setPreferredSize(new Dimension(LETTER_SIDE, LETTER_SIDE));
            letter.setBackground(Color.WHITE);
            letter.setHorizontalAlignment(JTextField.CENTER);
            letter.setEditable(false);
            letter.setFont(new Font(Font.SANS_SERIF,Font.BOLD,LETTER_SIZE));
            board[4][m] = letter;
            fifthWord.add(letter);
        }
        for(int n = 0; n < letterMode; n++){
            JTextField letter = new JTextField("");
            letter.setPreferredSize(new Dimension(LETTER_SIDE, LETTER_SIDE));
            letter.setBackground(Color.WHITE);
            letter.setHorizontalAlignment(JTextField.CENTER);
            letter.setEditable(false);
            letter.setFont(new Font(Font.SANS_SERIF,Font.BOLD,LETTER_SIZE));
            board[5][n] = letter;
            sixthWord.add(letter);
        }
        pane.add(firstWord);
        pane.add(secondWord);
        pane.add(thirdWord);
        pane.add(fourthWord);
        pane.add(fifthWord);
        pane.add(sixthWord);
    }

    /**
     * Creates all the boxes for the lower half of the GUI where the keyboard
     * keys go. It creates the grid of boxes one row at a time from top
     * to bottom then adds each to the window holding the boxes for keys
     */
    private void initializeKeyboard(){
        initializeTopKeyboard();
        initializeTopMidKeyboard();
        initializeBotMidKeyboard();
        initializeBotKeyboard();

        keyboard.add(keyboardTop);
        keyboard.add(keyboardTopMid);
        keyboard.add(keyboardBotMid);
        keyboard.add(keyboardBot);
    }

    /*
    آ
    أ
    إ
    ئ
    ء
     */
    private void initializeTopKeyboard() {
        for(int i = 0; i<5;i++){
            JButton btn = new JButton(letters[0][i]);
            btn.setVisible(true);
            final int currentLetterIndex = i;
            btn.setContentAreaFilled(false);
            btn.setOpaque(true);
            btn.setBorder(new LineBorder(Color.BLACK,1));

            btn.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
            btn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, BUTTON_SIZE));
            btn.setBackground(Color.WHITE);
            btn.invalidate();
            btn.validate();
            btn.repaint();
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (currentCol > -1) {
                        board[currentRow][currentCol].setText(btn.getText());
                        currentCol--;
                    }
                }
            });
            keys[0][i] = btn;
            keyboardTop.add(btn);
        }
    }

    /*
    ض
    ص
    ث
    ق
    ف
    غ
    ع
    ه
    خ
    ح
    ج
     */
    private void initializeTopMidKeyboard(){
        for(int i = 0; i<11;i++){
            JButton btn = new JButton(letters[1][i]);
            btn.setVisible(true);
            final int currentLetterIndex = i;
            btn.setContentAreaFilled(false);
            btn.setOpaque(true);
            btn.setBorder(new LineBorder(Color.BLACK,1));

            btn.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
            btn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, BUTTON_SIZE));
            btn.setBackground(Color.WHITE);
            btn.invalidate();
            btn.validate();
            btn.repaint();
            keys[1][i] = btn;
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (currentCol > -1) {
                        board[currentRow][currentCol].setText(btn.getText());
                        currentCol--;
                    }
                }
            });
            keyboardTopMid.add(btn);
        }
    }

    /*
    ش
    س
    ي
    ب
    ل
    ا
    ت
    ن
    م
    ك
    ؛
     */
    private void initializeBotMidKeyboard(){
        for(int i = 0; i<11;i++){
            JButton btn = new JButton(letters[2][i]);
            btn.setVisible(true);
            final int currentLetterIndex = i;
            btn.setContentAreaFilled(false);
            btn.setOpaque(true);
            btn.setBorder(new LineBorder(Color.BLACK,1));

            btn.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
            btn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, BUTTON_SIZE));
            btn.setBackground(Color.WHITE);
            btn.invalidate();
            btn.validate();
            btn.repaint();
            keys[2][i] = btn;
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (currentCol > -1) {
                        board[currentRow][currentCol].setText(btn.getText());
                        currentCol--;
                    }
                }
            });
            keyboardBotMid.add(btn);
        }
    }

    /*
    ذ
    د
    ر
    ز
    ة
    ظ
    ى
    و
    */
    private void initializeBotKeyboard(){
        JButton enter = new JButton("↲");
        enter.setVisible(true);
        enter.setContentAreaFilled(false);
        enter.setOpaque(true);
        enter.setBorder(new LineBorder(Color.BLACK,1));

        enter.setPreferredSize(new Dimension(ENTER_WIDTH, ENTER_HEIGHT));
        enter.setFont(new Font(Font.SANS_SERIF, Font.BOLD, BUTTON_SIZE));
        enter.setBackground(Color.WHITE);
        enter.invalidate();
        enter.validate();
        enter.repaint();
        enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(currentCol>-1) {
                    JOptionPane.showMessageDialog(null, "Not enough letters");
                }else{
                    for (int i = letterMode-1; i > -1; i--) {
                        for (int j = 0; j < letterMode; j++) {
                            if (letterMode-1-i != j) {
                                if (board[currentRow][i].getText().equals(answer.get(j))) {
                                    board[currentRow][i].setBackground(Color.YELLOW);
                                    for(int k=0;k<5;k++){
                                        boolean isGreen = keys[0][k].getBackground()!=Color.GREEN;
                                        if(isGreen&&keys[0][k].getText().equals(board[currentRow][i].getText())){
                                            keys[0][k].setBackground(Color.YELLOW);
                                        }
                                    }
                                    for(int k=0;k<11;k++){
                                        boolean isGreen = keys[1][k].getBackground()!=Color.GREEN;
                                        if(isGreen&&keys[1][k].getText().equals(board[currentRow][i].getText())){
                                            keys[1][k].setBackground(Color.YELLOW);
                                        }
                                    }
                                    for(int k=0;k<11;k++){
                                        boolean isGreen = keys[2][k].getBackground()!=Color.GREEN;
                                        if(isGreen&&keys[2][k].getText().equals(board[currentRow][i].getText())){
                                            keys[2][k].setBackground(Color.YELLOW);
                                        }
                                    }
                                    for(int k=0;k<8;k++){
                                        boolean isGreen = keys[3][k].getBackground()!=Color.GREEN;
                                        if(isGreen&&keys[3][k].getText().equals(board[currentRow][i].getText())){
                                            keys[3][k].setBackground(Color.YELLOW);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    for (int i = letterMode - 1; i > -1; i--) {
                        if (board[currentRow][i].getText().equals(answer.get(letterMode-1-i))) {
                            board[currentRow][i].setBackground(Color.GREEN);
                            for(int j = 4; j > -1; j--){
                                if(keys[0][4-j].getText().equals(board[currentRow][i].getText())){
                                    keys[0][4-j].setBackground(Color.GREEN);
                                }
                            }
                            for(int j = 10; j > -1; j--){
                                if(keys[1][10-j].getText().equals(board[currentRow][i].getText())){
                                    keys[1][10-j].setBackground(Color.GREEN);
                                }
                            }
                            for(int j = 10; j > -1; j--){
                                if(keys[2][10-j].getText().equals(board[currentRow][i].getText())){
                                    keys[2][10-j].setBackground(Color.GREEN);
                                }
                            }
                            for(int j = 7; j > -1; j--){
                                if(keys[3][7-j].getText().equals(board[currentRow][i].getText())){
                                    keys[3][7-j].setBackground(Color.GREEN);
                                }
                            }
                        }
                    }

                    for (int i = letterMode-1; i > -1; i--) {
                        for(int j = 0; j<letterMode;j++){
                            if (!board[currentRow][i].getText().equals(answer.get(j)) && !(board[currentRow][i].getBackground() == Color.YELLOW || board[currentRow][i].getBackground() == Color.GREEN)) {
                                board[currentRow][i].setBackground(Color.GRAY);
                                for(int k=0;k<5;k++){
                                    boolean isNotColored = keys[0][k].getBackground()!=Color.GREEN&&keys[0][k].getBackground()!=Color.GRAY;
                                    if(isNotColored&&board[currentRow][i].getText().equals(keys[0][k].getText())){
                                        keys[0][k].setBackground(Color.GRAY);
                                    }
                                }
                                for(int k=0;k<11;k++){
                                    boolean isNotColored = keys[1][k].getBackground()!=Color.GREEN&&keys[1][k].getBackground()!=Color.GRAY;
                                    if(isNotColored&&board[currentRow][i].getText().equals(keys[1][k].getText())){
                                        keys[1][k].setBackground(Color.GRAY);
                                    }
                                }
                                for(int k=0;k<11;k++){
                                    boolean isNotColored = keys[2][k].getBackground()!=Color.GREEN&&keys[2][k].getBackground()!=Color.GRAY;
                                    if(isNotColored&&board[currentRow][i].getText().equals(keys[2][k].getText())){
                                        keys[2][k].setBackground(Color.GRAY);
                                    }
                                }
                                for(int k=0;k<8;k++){
                                    boolean isNotColored = keys[3][k].getBackground()!=Color.GREEN&&keys[3][k].getBackground()!=Color.GRAY;
                                    if(isNotColored&&board[currentRow][i].getText().equals(keys[3][k].getText())){
                                        keys[3][k].setBackground(Color.GRAY);
                                    }
                                }
                            }
                        }
                    }
//                    for(int i=0;i<5;i++){
//                        if (!keys[0][i].getText().equals(answer.get(i)) && !(keys[0][i].getBackground() == Color.YELLOW || keys[0][i].getBackground() == Color.GREEN)) {
//                            keys[0][i].setBackground(Color.GRAY);
//                        }
//                    }

                    int correctCheck = 0;
                    for (int i = 0; i < letterMode; i++) {
                        if (board[currentRow][i].getBackground() == Color.GREEN) {
                            correctCheck++;
                        }
                    }
                    if (correctCheck == 5) {
                        int input = JOptionPane.showOptionDialog(null, "Well Done!", "Congratulatory Message before a New Game", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);

                        if (input == JOptionPane.OK_OPTION) {
                            gameCount++;
                            wordsRemaining--;
                            switch(currentRow){
                                case 0:
                                    oneWinCount++;
                                    break;
                                case 1:
                                    twoWinCount++;
                                    break;
                                case 2:
                                    threeWinCount++;
                                    break;
                                case 3:
                                    fourWinCount++;
                                    break;
                                case 4:
                                    fiveWinCount++;
                                    break;
                                case 5:
                                    sixWinCount++;
                                    break;
                                default:
                                    break;
                            }
                            window.removeAll();
                            window.setVisible(false);
                            reset();
                        }
                        if (input == JOptionPane.OK_CANCEL_OPTION) {
                            JOptionPane.showMessageDialog(null, "Thank you for playing!");
                            System.exit(0);
                        }
                    }

                    if (currentCol == -1 && currentRow < 5) {
                        currentRow++;
                        currentCol = 4;
                    }else if(currentCol == -1 && currentRow==5){
                        if(correctCheck<5){
                            int input = JOptionPane.showOptionDialog(null, "Better Luck Next Time", "New Game?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);

                            if (input == JOptionPane.OK_OPTION) {
                                gameCount++;
                                wordsRemaining--;
                                loseCount++;
                                window.removeAll();
                                window.setVisible(false);
                                reset();
                            }
                            if (input == JOptionPane.OK_CANCEL_OPTION) {
                                JOptionPane.showMessageDialog(null, "Thank you for playing!");
                                System.exit(0);
                            }
                        }
                    }


                }
            }
        });
        keyboardBot.add(enter);

        for(int i = 0; i<8;i++){
            JButton btn = new JButton(letters[3][i]);
            btn.setVisible(true);
            final int currentLetterIndex = i;
            btn.setContentAreaFilled(false);
            btn.setOpaque(true);
            btn.setBorder(new LineBorder(Color.BLACK,1));

            btn.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
            btn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, BUTTON_SIZE));
            btn.setBackground(Color.WHITE);
            btn.invalidate();
            btn.validate();
            btn.repaint();
            keys[3][i] = btn;
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (currentCol > -1) {
                        board[currentRow][currentCol].setText(btn.getText());
                        currentCol--;
                    }
                }
            });
            keyboardBot.add(btn);
        }

        JButton delete = new JButton("⌫");
        delete.setVisible(true);
        delete.setContentAreaFilled(false);
        delete.setOpaque(true);
        delete.setBorder(new LineBorder(Color.BLACK,1));

        delete.setPreferredSize(new Dimension(DELETE_WIDTH, DELETE_HEIGHT));
        delete.setFont(new Font(Font.SANS_SERIF, Font.BOLD, BUTTON_SIZE));
        delete.setBackground(Color.WHITE);
        delete.invalidate();
        delete.validate();
        delete.repaint();
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(currentCol<4){
                    if(currentCol>=-1){
                        board[currentRow][currentCol+1].setText("");
                        currentCol++;
                    }
                }
            }
        });
        keyboardBot.add(delete);
    }
    //"Not in the book"
}