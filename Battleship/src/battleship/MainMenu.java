package Battleship; 

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *
 * @author Bart Kooijmans
 */
public class MainMenu
{
    
    private JMenuBar mainMenuBar;
    private JMenu mainMenu;
    public JMenuItem startGame = new JMenuItem("Start new game");
    public JMenuItem saveGame = new JMenuItem("Save game");
    public JMenuItem loadGame = new JMenuItem("Load Game");
    public JMenuItem closeGame = new JMenuItem("Exit");
    
    public MainMenu()
    {
                
    }
    
    public JMenuBar createMainMenu()
    {
        mainMenuBar = new JMenuBar();
        mainMenu = new JMenu();
        mainMenu.setText("Main");
        mainMenu.getAccessibleContext().setAccessibleDescription(
                "The only menu in this program that has menu items");
        mainMenuBar.add(mainMenu); 

        mainMenu.add(startGame);
        mainMenu.add(saveGame);
        mainMenu.add(loadGame);
        mainMenu.add(closeGame);
        return mainMenuBar;
    }
}

