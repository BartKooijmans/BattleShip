package Battleship;

import java.util.*;
import java.util.Random;

/**
 * The class representing the computer IA for project Battleship
 *
 * @author Bart Kooijmans
 */
class CPUAI
{

    private boolean found; //boolean indicating if the IA has found a ship
    public int[] foundOrigin; //X and Y Coordinates of the shot where a hit ship was found, [0] reflects x-coordinate and [1] reflect y-coordinate.
    public int[] lastHitShot; //X and Y Coordinates of the last shot that hit a found ship, [0] reflects x-coordinate and [1] reflect y-coordinate.

    /**
     * Constructor for the class CPUAI Initialises the 3 class variables.
     */
    public CPUAI()
    {
        boolean found = false;
        foundOrigin = new int[2];
        lastHitShot = new int[2];
    }

    /**
     * Determines the next location for the IA to shoot and returns that value
     *
     * @param field the map of the field on which the IA is shooting
     * @return the coordinates, [0] representing x and [1] representing y, of
     * the next shot
     */
    public int[] shoot(Map<Integer, Map> field)
    {
        //Initialises the nextShot return variable
        int[] nextShot =
        {
            0, 0
        };
        //Check if there is a direction to next shoot in from the hit shot
        Map<Integer, Integer> temp;
        int direction = checkDirectionShot(field, lastHitShot[1], lastHitShot[0]);
        //If no possible direction was found or no ship was found shoot at random coordinates
        if (found == false || direction == 0)
        {
            found = false;
            Random rd = new Random();
            int x = rd.nextInt(10);
            int y = rd.nextInt(10);
            temp = field.get(y);
            direction = checkDirectionShot(field, y, x);
            //Check that the next random shot is not in a field already shot and that it is not next to a surounded field
            while (temp.get(x) == 1 || temp.get(x) == 3)
            {
                x = rd.nextInt(10);
                y = rd.nextInt(10);
                temp = field.get(y);
                direction = checkDirectionShot(field, y, x);
            }
            nextShot[0] = x;
            nextShot[1] = y;
        }
        //Checks if the AI can shoot in a horizontal direction from the found shipwithout going out of bounds of the playing field
        else if (direction != 0)
        {
            if (direction == 1 || direction == 2)
            {
                temp = field.get(lastHitShot[1]);
                if (direction == 1 && (lastHitShot[0] + 1) < 10 && (temp.get(lastHitShot[0] + 1) == 0 || temp.get(lastHitShot[0] + 1) == 2))
                {
                    nextShot[0] = (lastHitShot[0] + 1);
                    nextShot[1] = lastHitShot[1];
                }
                else if (direction == 1 && temp.get(foundOrigin[0] - 1) == 0 || temp.get(foundOrigin[0] - 1) == 2)
                {
                    nextShot[0] = (foundOrigin[0] - 1);
                    nextShot[1] = lastHitShot[1];
                }
                else if (direction == 2 && (lastHitShot[0] - 1) >= 0 && (temp.get(lastHitShot[0] - 1) == 0 || temp.get(lastHitShot[0] - 1) == 2))
                {
                    nextShot[0] = (lastHitShot[0] - 1);
                    nextShot[1] = lastHitShot[1];
                }
                else if (direction == 2 && temp.get(foundOrigin[0] + 1) == 0 || temp.get(foundOrigin[0] + 1) == 2)
                {
                    nextShot[0] = (foundOrigin[0] + 1);
                    nextShot[1] = lastHitShot[1];
                }
            }
            //Checks if the AI can shoot in a vertical direction from the found ship without going out of bounds of the playing field
            else if (direction == 3)
            {
                if ((lastHitShot[1] + 1) < 10)
                {
                    temp = field.get(lastHitShot[1] + 1);
                    if (temp.get(lastHitShot[0]) == 0 || temp.get(lastHitShot[0]) == 2)
                    {
                        nextShot[0] = (lastHitShot[0]);
                        nextShot[1] = (lastHitShot[1] + 1);
                    }
                }
                else
                {
                    temp = field.get(foundOrigin[1] - 1);
                    if (temp.get(lastHitShot[0]) == 0 || temp.get(lastHitShot[0]) == 2)
                    {
                        nextShot[0] = (lastHitShot[0]);
                        nextShot[1] = (foundOrigin[1] - 1);
                    }
                }
            }
            else if (direction == 4)
            {
                if ((lastHitShot[1] - 1) >= 0)
                {
                    temp = field.get(lastHitShot[1] - 1);
                    if (temp.get(lastHitShot[0]) == 0 || temp.get(lastHitShot[0]) == 2)
                    {
                        nextShot[0] = (lastHitShot[0]);
                        nextShot[1] = (lastHitShot[1] - 1);
                    }
                }
                else
                {
                    temp = field.get(foundOrigin[1] + 1);
                    if (temp.get(lastHitShot[0]) == 0 || temp.get(lastHitShot[0]) == 2)
                    {
                        nextShot[0] = (lastHitShot[0]);
                        nextShot[1] = (foundOrigin[1] + 1);
                    }

                }
            }
        }
        else
        {
            found = false;
            nextShot = shoot(field);
        }
        temp = field.get(nextShot[1]);
        //Lets the IA know if it hit or not with it's shot.
        if (temp.get(nextShot[0]) == 2)
        {
            setHit(true, nextShot[0], nextShot[1]);
        }
        else
        {
            setHit(false, nextShot[0], nextShot[1]);
        }
        return nextShot;
    }

    /**
     * Check if the fields, above, below, left or right from the x-coordinates
     * and y-coordinates that are still available to shoot in.
     *
     * @param field map of the playing field on which the IA is shooting.
     * @param y y-coordinate of the shot to check.
     * @param x x-coordinate of the shot to check.
     * @return returns the direction that can be shot in reflected by a number
     * for each direction, 1 right, 2 is left, 3 is downward 4 is upward
     */
    public int checkDirectionShot(Map<Integer, Map> field, int y, int x)
    {
        int direction = 0;
        Map<Integer, Integer> temp = field.get(y);
        //check if the x coordinate is on the side edge of the playing field
        if (x < 9 && x > 0)
        {
            if (temp.get(x + 1) == 0 || temp.get(x + 1) == 2)
            {
                direction = 1;
            }
            else if (temp.get(x - 1) == 0 || temp.get(x - 1) == 2)
            {
                direction = 2;
            }

        }
        else if (x < 9)
        {
            if (temp.get(x + 1) == 0 || temp.get(x + 1) == 2)
            {
                direction = 1;
            }
        }
        else if (x > 0)
        {
            if (temp.get(x - 1) == 0 || temp.get(x - 1) == 2)
            {
                direction = 2;
            }
        }
        //check if the y coordinate is on the top and bottom edge of the playing field
        else if (y > 0 && y < 9)
        {
            temp = field.get(y + 1);
            if (temp.get(x) == 0 || temp.get(x) == 2)
            {
                direction = 3;
            }
            else
            {
                temp = field.get(y - 1);
                if (temp.get(x) == 0 || temp.get(x) == 2)
                {
                    direction = 4;
                }
            }
        }
        else if (y < 9)
        {
            temp = field.get(y + 1);
            if (temp.get(x) == 0 || temp.get(x) == 2)
            {
                direction = 3;
            }
        }
        else if (y > 0)
        {
            temp = field.get(y - 1);
            if (temp.get(x) == 0 || temp.get(x) == 2)
            {
                direction = 4;
            }
        }
        return direction;
    }

    /**
     * Set up the variables correctly in case of a hit on a ship so it destroys
     * the ship before starting to randomly shoot again to find another ship.
     *
     * @param hit boolean reflecting if the taken shot was a hit
     * @param x Reflecting the x-coordinate of the taken shot
     * @param y Reflecting the y-coordinate of the taken shot
     */
    public void setHit(boolean hit, int x, int y)
    {
        //Check if a ship was already found and if the shot was a hit and updates the last hit coordinates accordingly
        if (hit == true && found == true)
        {
            lastHitShot[0] = x;
            lastHitShot[1] = y;
        }
        //If it is the first shot that hit it sets the orifin and updates the found status
        else if (hit == true && found == false)
        {
            found = true;
            lastHitShot[0] = x;
            lastHitShot[1] = y;
            foundOrigin = lastHitShot.clone();
        }
        //If the taken shot missed returns to the origin to start shooting in the other directions
        else if (hit == false && found == true)
        {
            lastHitShot = foundOrigin.clone();
        }
    }
}
