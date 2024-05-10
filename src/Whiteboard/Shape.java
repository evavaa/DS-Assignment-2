package Whiteboard;

import java.awt.*;
import java.io.Serializable;

public class Shape implements Serializable {
    private int x1;
    private int x2;
    private int y1;
    private int y2;

    private Color color;
    private String message;

    private String shape;
    private int eraserSize;


    // eraser
    public Shape(int x1, int y1,int x2, int y2, Color color, String shape, int eraserSize){
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.color = color;
        this.shape = shape;
        this.eraserSize = eraserSize;
    }

    // text
    public Shape(int x1, int y1, Color color, String shape, String message){
        this.x1 = x1;
        this.y1 = y1;
        this.color = color;
        this.shape = shape;
        this.message = message;
    }

    // circle, line, rectangle, oval, free draw
    public Shape(int x1, int y1, int x2, int y2, Color color, String shape){
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.color = color;
        this.shape = shape;
    }

    public int getX1() {
        return x1;
    }

    public int getX2() {
        return x2;
    }

    public int getY1() {
        return y1;
    }

    public int getY2() {
        return y2;
    }

    public Color getColor() {
        return color;
    }

    public String getMessage() {
        return message;
    }

    public String getShape() {
        return shape;
    }

    public int getEraserSize() {
        return eraserSize;
    }

}
