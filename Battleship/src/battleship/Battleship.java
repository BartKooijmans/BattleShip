package Battleship;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.*;
import java.math.RoundingMode;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
 * Main Class of project Battleship
 * @author Bart Kooijmans
 */
public class Battleship
{
    private boolean close = false; //The boolen to determine if the program should close (set to false).
    private JFrame mainFrame; // The main frame containg the different game elements.
    private MainMenu mainMenu; //Main menu bar.
    private int option; //Int the choice to representing the output to close the menu.
    private boolean player1Turn; //Boolean storing whose turn it is, if player 1's turn it is true player 2's or AI's turn is represented by false.
    private boolean gameType; //Boolean reflecting the choice of game, versus another player is represented by false, versus IA by true .
    private TreeMap<Integer, Map> map1 = new TreeMap<Integer, Map>(); //Map refelecting the gamestate of the field on which player 1 placed their boats and player 2 is shooting on
    private TreeMap<Integer, Map> map2 = new TreeMap<Integer, Map>(); //Map refelecting the gamestate of the field on which player 2 or IA placed their boats and player 1 is shooting on
    private CardLayout cl = new CardLayout(); //Card layout manager that manages the switching between the 2 fields
    private JPanel activeScreen = new JPanel(cl); //The JPanel containing the 2 fields
    private JPanel field1; //Field of buttons refelecting the gamestate of the map on which player 1 placed their boats and player 2 is shooting on.
    private JPanel field2; //Field of buttons refelecting the gamestate of the map on which player 2 placed their boats and player 1 is shooting on.
    private Random rd = new Random(); //Random number generator used for placing the boats at random positions
    private CPUAI opponentIA; //The computer opponent AI
    private int hitCounterP1 = 0; //Counter for the amount of hits Player 1 scored
    private int hitCounterP2 = 0;//Counter for the amount of hits Player 2 or AI scored
    private int[] currentShot = {-1, -1}; //x and y coordinates (in that order [0] = x [1] = y) of the currentshot of the IA 
    public FileNameExtensionFilter textFilter; //Filter for the load and save menu
    JFileChooser saveFileChooser = new JFileChooser(); //Filechooser to select the save and load files.

    /**
     * Main method
     */
    public static void main(String[] args)
    {
        Battleship bs = new Battleship();
    }

    /**
     * Constructor for the class battleship - initialises variables and starts
     * the game console
     */
    public Battleship()
    {
        opponentIA = new CPUAI();
        textFilter = new FileNameExtensionFilter("Text file", "txt"); //Set the filter to only select .txt files
        saveFileChooser.addChoosableFileFilter(textFilter); //adds the filter to the filechooser 
        saveFileChooser.setAcceptAllFileFilterUsed(false); //disables the select all files option leaving on the .txt option
        mainFrame = new JFrame();
        mainMenu = new MainMenu();
        mainFrame.setSize(320, 60); //Set the initial size of the menu
        mainFrame.setLocationByPlatform(true); //Sets the standard location of the mainFrame relative to the platform
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Sets the default Close operationn
        mainFrame.setJMenuBar(mainMenu.createMainMenu());
        mainMenu.saveGame.setEnabled(false); //Disables the save button (until a game has been loaded or new game has been started as there is nothing to save)
        activeScreen.setVisible(true);
        mainFrame.add(activeScreen);
        mainFrame.setVisible(true);
        mainFrame.setTitle("Battleship");
        startMenu();
    }

    /**
     * Keeps the main frame running and awaiting input
     */
    private void startMenu()
    {
        setMenuInput();
        while (close == false)
        {
            option = 0;
            Scanner kb = new Scanner(System.in);
            while (option != 1 && option == 1)
            {
                try
                {
                    option = Integer.parseInt(kb.nextLine());
                    if (option == 1)
                    {

                    }
                }
                catch (NumberFormatException e)
                {
                    System.out.println("Error");
                }
            }
            close = true;
        }
    }
    
    /**
     * Set up the menu item actions and adds the actions to the menu buttons
     */
    private void setMenuInput()
    {
        mainMenu.startGame.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                startNewGame();
            }

        });
        mainMenu.saveGame.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                saveGame();
            }

        });
        mainMenu.loadGame.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                loadGame();
            }

        });
        mainMenu.closeGame.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
            }

        });
    }

    /**
     * Start a new game
     */
    private void startNewGame()
    {
        // Resets the hitcounters
        hitCounterP1 = 0;
        hitCounterP2 = 0;
        // Asks and set the type of game
        Object[] options =
        {
            "versus IA", "Hot Seat"
        };
        int n = JOptionPane.showOptionDialog(mainFrame, "Computer or Human opponent?", "Choose opponent", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
        try
        {
            mainMenu.saveGame.setEnabled(false);
            //Allows player 1 to place their boats and sets up their field
            JOptionPane.showMessageDialog(mainFrame, "Player1 place your boats");
            map1.putAll(fillMap());
            placeBoat(map1, 5);
            placeBoat(map1, 4);
            placeBoat(map1, 3);
            placeBoat(map1, 3);
            placeBoat(map1, 2);
            field1 = updateField(map1, 2);

            if (n == 0)
            {
                //sets up the IA and randomly places the boats of the IA on their field                
                gameType = true;
                map2.putAll(fillMap());
                placeRandom(map2, 5);
                placeRandom(map2, 4);
                placeRandom(map2, 3);
                placeRandom(map2, 3);
                placeRandom(map2, 2);
                field2 = updateField(map2, 1);
                mainMenu.saveGame.setEnabled(true);

            }

            else if (n == 1)
            {
                //Allows player 2 to place their boats and sets up their field
                gameType = false;
                JOptionPane.showMessageDialog(mainFrame, "Player2 place your boats");
                map2.putAll(fillMap());
                placeBoat(map2, 5);
                placeBoat(map2, 4);
                placeBoat(map2, 3);
                placeBoat(map2, 3);
                placeBoat(map2, 2);
                field2 = updateField(map2, 1);
            }
            //Removes any previous playfield from the active screen and loads the new ones
            activeScreen.removeAll();
            activeScreen.add(field2, BorderLayout.CENTER);
            activeScreen.add(field1, BorderLayout.CENTER);
            player1Turn = true;
            mainMenu.saveGame.setEnabled(true);
            runGame();
        }
        catch (NullPointerException e)
        {

        }
    }

    /**
     * Creates a map of 10 entries that contain 10 maps containing integers
     * which are set to 0, reflecting an empty playing field
     *
     * @return
     */
    private TreeMap<Integer, Map> fillMap()
    {
        TreeMap<Integer, Map> rows = new TreeMap<Integer, Map>();
        TreeMap<Integer, Integer> columns;
        for (int y = 0; y < 10; y++)
        {
            columns = new TreeMap<Integer, Integer>();
            for (int x = 0; x < 10; x++)
            {
                columns.put(x, 0);
            }
            rows.put(y, columns);
        }
        return rows;
    }
    
    /**
     * The method that deals with what the game should do when a button is
     * pressed.
     *
     * @param b the button that is pressed.
     */
    public void buttonPress(BButton b)
    {
        b.pressButton(); //Method that tells the button to update it own status based on what it should do when pressed 
        //Checks which players turn it is and updates initially the map accordingly based on the new values of the button and then updates the playingfield on the new map
        if (player1Turn == true)
        {
            Map<Integer, Integer> temp = map2.get(b.getY());
            temp.put(b.getX(), b.getState());
            map2.put(b.getY(), temp);
            //Checks if it was a hit or miss and updates the player accordingly plus updates their score/hitcounter if required
            if (b.getState() == 3)
            {
                hitCounterP1++;
                JOptionPane.showMessageDialog(mainFrame, "You hit");
            }
            else
            {
                JOptionPane.showMessageDialog(mainFrame, "You missed");
            }
            //Updates the players turn and runs the turn accordingly
            player1Turn = false;
            runGame();
            //If it is a game versus the IA the IA's turn is handled
            if (gameType == true)
            {
                currentShot = opponentIA.shoot(map1);
                temp = map1.get(currentShot[1]);
                int currentState = temp.get(currentShot[0]) + 1;
                if(currentState == 3)
                {
                    hitCounterP2++;
                }
                temp.put(currentShot[0], currentState);
                map1.put(currentShot[1], temp);
                activeScreen.remove(field1);
                field1 = updateField(map1, 2);
                activeScreen.add(field1);
                cl.last(activeScreen);
                JOptionPane.showMessageDialog(mainFrame, "Computer's turn");
                player1Turn = true;
                runGame();
                JOptionPane.showMessageDialog(mainFrame, "Player 1's turn");
            }
            //Otherwise runs the turn of player 2 similair to player 1's turn. 
            else
            {
                player1Turn = false;
                JOptionPane.showMessageDialog(mainFrame, "Player 2's turn");
            }
        }
        else if (player1Turn == false)
        {
            Map<Integer, Integer> temp = map1.get(b.getY());
            temp.put(b.getX(), b.getState());
            map1.put(b.getY(), temp);
            if (b.getState() == 3)
            {
                hitCounterP2++;
                JOptionPane.showMessageDialog(mainFrame, "You hit");
            }
            else
            {
                JOptionPane.showMessageDialog(mainFrame, "You missed");
            }
            b.setEnabled(false);
            player1Turn = true;
            runGame();
            JOptionPane.showMessageDialog(mainFrame, "Player 1's turn");
        }
    }
    
    /**
     * Places a boat of the given size in the given map.
     *
     * @param field is the map on which the boat is getting placed.
     * @param boatSize reflects the size of the boat that needs to be placed
     */
    private void placeBoat(TreeMap<Integer, Map> field, int boatSize)
    {
        //Determines by the size of the boat the indicator of which boat type the user is going to place, reflect by the String boattype
        String boattype;
        if (boatSize == 5)
        {
            boattype = boatSize + " Battleship.";
        }
        else if (boatSize == 4)
        {
            boattype = boatSize + " Cruiser.";
        }
        else if (boatSize == 3)
        {
            boattype = boatSize + " Destroyer.";
        }
        else
        {
            boattype = boatSize + " Submarine.";
        }
        //String reflecting the questions which coordinate the boat should be placed
        String questionx = "Choose on which column (1-10) you want to place your length " + boattype;
        String questiony = "Choose on which row (1-10) you want to place your length " + boattype;

        int y = -1;
        String yco = null;
        // Asks the user which Y coordinate (row on the grid) they want to place their boat on untill they enter a value between 1 and 10.
        while (y < 0 || y > 10)
        {
            try
            {
                yco = JOptionPane.showInputDialog(mainFrame, questiony, "");
                y = (Integer.parseInt(yco) - 1);
            }
            catch (NumberFormatException e)
            {
                yco = JOptionPane.showInputDialog(mainFrame, questiony, "");
            }
        }

        int x = -1;
        String xco = null;
        // Asks the user which X coordinate (column on the grid) they want to place their boat on untill they enter a value between 1 and 10.
        while (x < 0 || x > 10)
        {
            try
            {
                xco = JOptionPane.showInputDialog(mainFrame, questionx, "");
                x = (Integer.parseInt(xco) - 1);
            }
            catch (NumberFormatException e)
            {
                xco = JOptionPane.showInputDialog(mainFrame, questionx, "");
            }
        }

        //Asks for the direct they want to place the boat in standard option is downwards if the user doesn't select any value and closes the dialog box
        Object[] directionOptions =
        {
            "Upwards", "Downwards", "Left", "Right"
        };
        int n = JOptionPane.showOptionDialog(mainFrame, "Left or Down ?", "Choose Direction", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, directionOptions, directionOptions[1]);
        boolean collision = false;
        //Checks per direction if the boat is placed entirely within bounds of of the field and place the boat, otherwise will recall the mehtod to force the user to choose again
        if (n == 0)
        {
            if ((y - boatSize) >= 0)
            {
                for (int i = 0; i < boatSize; i++)
                {
                    if (checkBoatCollision(x, (y - i), field) == true)
                    {
                        collision = true;
                    }
                }
                if (collision == false)
                {
                    for (int i = 0; i < boatSize; i++)
                    {
                        field.get(y - i).put(x, 2);
                    }
                }
                else
                {
                    placeBoat(field, boatSize);
                }
            }
            else
            {
                placeBoat(field, boatSize);
            }
        }
        else if (n == 1)
        {
            if ((y + boatSize) <= 9)
            {
                for (int i = 0; i < boatSize; i++)
                {
                    if (checkBoatCollision(x, (y + i), field) == true)
                    {
                        collision = true;
                    }
                }

                if (collision == false)
                {
                    for (int i = 0; i < boatSize; i++)
                    {
                        field.get(y + i).put(x, 2);
                    }
                }
                else
                {
                    placeBoat(field, boatSize);
                }
            }
            else
            {
                placeBoat(field, boatSize);
            }
        }
        else if (n == 2)
        {
            if ((x - boatSize) >= 0)
            {
                for (int i = 0; i < boatSize; i++)
                {
                    if (checkBoatCollision((x - i), y, field) == true)
                    {
                        collision = true;
                    }

                }
                if (collision == false)
                {
                    for (int i = 0; i < boatSize; i++)
                    {
                        field.get(y).put((x - i), 2);
                    }
                }
                else
                {
                    placeBoat(field, boatSize);
                }

            }
            else
            {
                placeBoat(field, boatSize);
            }
        }
        else if (n == 3)

        {
            if ((x + boatSize) <= 9)
            {
                for (int i = 0; i < boatSize; i++)
                {
                    if (checkBoatCollision((x + i), y, field) == true)
                    {
                        collision = true;
                    }

                }
                if (collision == false)
                {
                    for (int i = 0; i < boatSize; i++)
                    {
                        field.get(y).put((x + i), 2);
                    }
                }
                else
                {
                    placeBoat(field, boatSize);
                }

            }
            else
            {
                placeBoat(field, boatSize);
            }
        }

    }

    /**
    * Attempts to place the boat of size boatSize randomly until it manages to do so without overlapping other boats
    * @param field is the map on which the boat is getting placed.
    * @param boatSize reflects the size of the boat that needs to be placed
    */
    public void placeRandom(TreeMap<Integer, Map> field, int boatSize)
    {
        int x = rd.nextInt(10);
        int y = rd.nextInt(10);
        int n = rd.nextInt(4);
        boolean collision = false;
        if (n == 0)
        {
            if ((y - boatSize) >= 0)
            {
                for (int i = 0; i < boatSize; i++)
                {
                    if (checkBoatCollision(x, (y - i), field) == true)
                    {
                        collision = true;
                    }
                }
                if (collision == false)
                {
                    for (int i = 0; i < boatSize; i++)
                    {
                        field.get(y - i).put(x, 2);
                    }
                }
                else
                {
                    placeRandom(field, boatSize);
                }
            }
            else
            {
                placeRandom(field, boatSize);
            }
        }
        else if (n == 1)
        {
            if ((y + boatSize) <= 9)
            {
                for (int i = 0; i < boatSize; i++)
                {
                    if (checkBoatCollision(x, (y + i), field) == true)
                    {
                        collision = true;
                    }
                }
                if (collision == false)
                {
                    for (int i = 0; i < boatSize; i++)
                    {
                        field.get(y + i).put(x, 2);
                    }
                }
                else
                {
                    placeRandom(field, boatSize);
                }
            }
            else
            {
                placeRandom(field, boatSize);
            }
        }
        else if (n == 2)
        {
            if ((x - boatSize) >= 0)
            {
                for (int i = 0; i < boatSize; i++)
                {
                    if (checkBoatCollision((x - i), y, field) == true)
                    {
                        collision = true;
                    }
                }
                if (collision == false)
                {
                    for (int i = 0; i < boatSize; i++)
                    {
                        field.get(y).put((x - i), 2);
                    }
                }
                else
                {
                    placeRandom(field, boatSize);
                }
            }
            else
            {
                placeRandom(field, boatSize);
            }
        }
        else if (n == 3)
        {
            if ((x + boatSize) <= 9)
            {
                for (int i = 0; i < boatSize; i++)
                {
                    if (checkBoatCollision((x + i), y, field) == true)
                    {
                        collision = true;
                    }
                }
                if (collision == false)
                {
                    for (int i = 0; i < boatSize; i++)
                    {
                        field.get(y).put((x + i), 2);
                    }
                }
                else
                {
                    placeRandom(field, boatSize);
                }

            }
            else
            {
                placeRandom(field, boatSize);
            }
        }
    }
    
    /**
     *
     * @param x coordinate of the cell on the field to check
     * @param y coordinate of the cell on the field to check
     * @param field actual field to check the cells on
     * @return returns a boolean that reflects if a cell has a already had part
     * of a boat placed (false equals no collision, true means that there has
     * been a boat placed on it already )
     */
    private boolean checkBoatCollision(int x, int y, TreeMap<Integer, Map> field)
    {
        Map<Integer, Integer> tempMap = field.get(y);
        int temp = tempMap.get(x);
        return temp == 2;
    }
    
    /**
     * Returns an updated field of buttons and appropriate labels on based the
     * map that got entered.
     *
     * @param map the map that reflects the playing field.
     * @param player the indicator of the player which will be shooting on the
     * field, please note that this is the opposite player than the players
     * whose field get updated.
     * @return a JPanel containing a field of buttons in a even grid-layout that
     * reflect the playing field, with appropriate labels indicating the fields
     * and player who is shooting on that field.
     */
    private JPanel updateField(TreeMap<Integer, Map> map, int player)
    {
        // Creates the field with a gridlayout of 11 by 11 to enable the labels and the buttons and sets the dimensions
        JPanel field = new JPanel(new GridLayout(11, 11));
        field.setLayout(new GridLayout(11, 11));
        field.setPreferredSize(new Dimension(550, 550));
        //Adds the player indicator label of the player who is shooting on that field
        JLabel pLabel = new JLabel("P" + player + ": ");
        field.add(pLabel);
        //Add the initial column indicator labels 
        for (int i = 0; i < 10; i++)
        {
            JLabel xLabel = new JLabel();
            xLabel.setText((i + 1) + ":");
            field.add(xLabel);
        }
        for (int y = 0; y < 10; y++)
        {
            //adds the row indicator label
            JLabel yLabel = new JLabel();
            yLabel.setText((y + 1) + ":");
            field.add(yLabel);
            //adds the buttons in that row based on the values in the map
            for (int x = 0; x < 10; x++)
            {
                Map<Integer, Integer> tempMap = map.get(y);
                int state = tempMap.get(x);
                BButton b = new BButton(x, y, state);
                b.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        buttonPress(b);
                    }
                });
                field.add(b);
            }
        }

        return field;
    }
    
    /**
     * Runs a turn of the game
     */
    private void runGame()
    {
        mainFrame.pack();
        mainFrame.setResizable(false);
        // checks if one of the players won and if so gives the choice to start a new game, load a game or exit.
        if (hitCounterP1 >= 17 || hitCounterP2 >= 17)
        {
            Object[] options =
            {
                "Start New Game", "Load Game", "Exit"
            };
            int n = JOptionPane.showOptionDialog(mainFrame, "Game finished", "What do you want to do next?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
            if (n == 0)
            {
                startNewGame();
            }
            else if (n == 1)
            {
                loadGame();
            }
            else
            {
                mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
            }
        }
        // Switches between the active playing fields
        else if (player1Turn == true)
        {
            SwingUtilities.updateComponentTreeUI(activeScreen);
            cl.first(activeScreen);
        }
        else
        {
            SwingUtilities.updateComponentTreeUI(activeScreen);
            cl.last(activeScreen);
        }
    }

    /**
     * Loads a game from a .txt file
     */
    private void loadGame()
    {
        // sets it to only accept files
        saveFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        //opens the file selection pane and sets the file path
        int returnValue = saveFileChooser.showOpenDialog(null);
        File selectedFile = null;
        if (returnValue == JFileChooser.APPROVE_OPTION)
        {
            selectedFile = saveFileChooser.getSelectedFile();
        }
        if (selectedFile != null)
        {
            String path = selectedFile.getAbsolutePath();
            File eFile = new File(path);
            Scanner fileScanner = null;
            //tries to open and read the file
            try
            {
                String line;
                fileScanner = new Scanner(new BufferedReader(new FileReader(eFile)));
                Scanner lineScanner;
                line = fileScanner.nextLine();
                lineScanner = new Scanner(line);
                //delimiters used in the file is the comma. 
                lineScanner.useDelimiter(",");
                //reads the initial game setup variables, see description behind variables
                gameType = lineScanner.nextBoolean();
                player1Turn = lineScanner.nextBoolean();
                hitCounterP1 = lineScanner.nextInt();
                hitCounterP2 = lineScanner.nextInt();
                //Creates a map to read the state of player 1's playfield and adds it to map1
                TreeMap<Integer, Map> rows = new TreeMap<Integer, Map>();
                for (int y = 0; y < 10; y++)
                {
                    TreeMap<Integer, Integer> columns = new TreeMap<Integer, Integer>();
                    for (int x = 0; x < 10; x++)
                    {
                        columns.put(x, lineScanner.nextInt());
                    }
                    rows.put(y, columns);
                }
                map1.putAll(rows);
                //Reads the next line and creates a map to read the state of player 2's playfield and adds it to map2
                line = fileScanner.nextLine();
                lineScanner = new Scanner(line);
                lineScanner.useDelimiter(",");
                rows = new TreeMap<Integer, Map>();
                for (int y = 0; y < 10; y++)
                {
                    TreeMap<Integer, Integer> columns = new TreeMap<Integer, Integer>();
                    for (int x = 0; x < 10; x++)
                    {
                        columns.put(x, lineScanner.nextInt());
                    }
                    rows.put(y, columns);
                }
                map2.putAll(rows);
            }
            catch (Exception aException)
            {
                System.out.println("Error while trying to read file: " + aException);
            }
            finally
            {
                try
                {
                    fileScanner.close();
                }
                catch (Exception aException)
                {
                    System.out.println("Error while closing file: " + aException);
                }
            }
            //loads the maps into the playing fields and empties the current content and loads in the loaded state
            field1 = updateField(map1, 2);
            field2 = updateField(map2, 1);
            activeScreen.removeAll();
            activeScreen.add(field1, BorderLayout.CENTER);
            activeScreen.add(field2, BorderLayout.CENTER);
            mainMenu.saveGame.setEnabled(true);
            runGame();
        }
    }

    /**
     * Save the game to a .txt file
     */
    private void saveGame()
    {
        saveFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnValue = saveFileChooser.showSaveDialog(null);
        File selectedFile = null;
        if (returnValue == JFileChooser.APPROVE_OPTION)
        {
            selectedFile = saveFileChooser.getSelectedFile();
        }
        if (selectedFile != null)
        {
            String path = selectedFile.getAbsolutePath();
            File eFile = new File(path + "batleShipSave.txt");
            try
            {
                eFile.createNewFile();
            }
            catch (IOException ex)
            {

            }

            Writer fileWriter = null;

            try
            {
                fileWriter = new BufferedWriter(new FileWriter(path));
                String dl = ",";
                String lineSeperator = System.getProperty("line.separator");
                fileWriter.write(gameType + dl);
                fileWriter.write(player1Turn + dl);
                fileWriter.write(hitCounterP1 + dl);
                fileWriter.write(hitCounterP2 + dl);
                for (int y = 0; y < 10; y++)
                {
                    Map<Integer, Integer> tempMap = map1.get(y);
                    for (int x = 0; x < 10; x++)
                    {
                        fileWriter.write(tempMap.get(x) + dl);
                    }
                }
                fileWriter.write(lineSeperator);
                for (int y = 0; y < 10; y++)
                {
                    Map<Integer, Integer> tempMap = map2.get(y);
                    for (int x = 0; x < 10; x++)
                    {
                        fileWriter.write(tempMap.get(x) + dl);
                    }
                }
            }
            catch (IOException ex)
            {

            }
            finally
            {
                try
                {
                    fileWriter.close();
                }
                catch (Exception aException)
                {
                    System.out.println("Error while closing file: " + aException);
                }
            }
        }
    }
}
