package com.alex;

import java.awt.*;

public class Tile {

    TileType type;
    Point location;

    private enum TileType {
        Snake, //Parts of the Snake's body
        Apple, //Eat it to lengthen the Snake's body by one segment
        Wall, //If you bump on it, you lose the game
        Garbage //If you eat it, you lose one segment off your body
    }
}
