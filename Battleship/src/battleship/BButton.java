package Battleship; 

import java.awt.*;


/**
 * Class for the button that extends the normal button to be able to assign some values to it
 * @author Bart Kooijmans
 */
public class BButton extends Button
{
    private int x; //Column the button is on in the playingfield (x-coordinate).
    private int y; //Row the button is on in the playingfield (y-coordinate).
    private int state; //State of the button 0: Not shot and no boat present, 1: Shot and no boat present, 2: Not shot and boat present, 3: Shot and boatpresent.

    
    /**
     * Constructor for the button that sets the variables and sets the colour and label accordingly to reflect, not shot, hit or miss.
     * @param xCo given x-coordinate to assign to the button
     * @param yCo given y-coordinate to assign to the button
     * @param bState given state to assign to the button
     */
    public BButton(int xCo, int yCo,int bState)
    {
        super();
        x = xCo;
        y = yCo;  
        state = bState;
        this.setBackground(new Color(0, 153, 204));
        if(state == 1 || state == 3)
        {
            if(state == 1)
            {
                this.setLabel("X");
                this.setBackground(new Color(51, 204, 204));
            }
            if(state == 3)
            {
                this.setLabel("H");
                this.setBackground(new Color(255, 102, 0));
            }
            this.setEnabled(false);
        }
        this.setVisible(true);
    }
    
    /**
     * Getter for state
     * @return the state of the button
     */
    public int getState()
    {
        return state;
    }

    /**
     * Getter for x
     * @return the x of the button
     */
    public int getX()
    {
        return x;
    }

    /**
     * Getter for y
     * @return the y of the button
     */
    public int getY()
    {
        return y;
    }
    
    /**
     * Updates the state of the button when pressed and sets the colour and label accordingly to reflect, not shot, hit or miss.
     * @return 
     */
    public boolean pressButton()
    {
        state = state + 1;
        if(state == 3)
        {
            this.setEnabled(false);
            this.setLabel("H");
            this.setBackground(new Color(255, 102, 0));
            return true;
        }
        else
        {
            this.setEnabled(false);
            this.setLabel("X");
            this.setBackground(new Color(51, 204, 204));
            return false;
        }
    }    
}
