package Whiteboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class DrawBoard extends JPanel {
    // store shapes and texts on whiteboard as a list of shapes
    public ArrayList<Shape> shapes = new ArrayList<>();
    private int x1, x2, y1, y2;
    private String currentTool = "free draw";
    private Color color = Color.black;
    private BasicStroke defaultStroke = new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    public void setCurrentTool(String currentTool) {
        this.currentTool = currentTool;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public DrawBoard() {
        setBackground(Color.white);
        init();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        for(Shape s : shapes){
            g2.setColor(s.getColor());
            g2.setStroke(defaultStroke);
            if (s.getShape().equals("line")){
                g2.drawLine(s.getX1(), s.getY1(), s.getX2(), s.getY2());
            }
            else if (s.getShape().equals("eraser")){
                g2.setStroke(new BasicStroke(6 * s.getEraserSize(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(s.getX1(), s.getY1(), s.getX2(), s.getY2());
            }
//            else if (s.getShape() == RECTANGLE){
//
//                g2.drawRect(s.getx1(), s.gety1(), s.getx2(), s.gety2());
//                if(s.transparent == false){
//                    g2.setColor(s.getfillColor());
//                    g2.fillRect(s.getx1(), s.gety1(), s.getx2(), s.gety2());
//                }
//            }
//            else if (s.getShape() == CIRCLE){
//                g2.drawOval(s.getx1(), s.gety1(), s.getx2(), s.gety2());
//                if(s.transparent == false){
//                    g2.setColor(s.getfillColor());
//                    g2.fillOval(s.getx1(), s.gety1(), s.getx2(), s.gety2());
//                }
//            }
//            else if (s.getShape() == TEXT) {
//                g2.setFont(s.getFont());
//                g2.drawString(s.getMessage(), s.getx1(), s.gety1());
//            }
        }
    }

    private void init() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x1 = e.getX();
                y1 = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                System.out.println("released");

            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                x2 = e.getX();
                y2 = e.getY();

                if (currentTool.equals("free draw")) {
                    shapes.add(new Shape(x1, y1, x2, y2, color, "line"));
                    repaint();
                    x1 = x2;
                    y1 = y2;
                } else if (currentTool.equals("eraser")) {
                    shapes.add(new Shape(x1, y1, x2, y2, Color.white, "eraser", 1));
                    repaint();
                    x1 = x2;
                    y1 = y2;
                }
            }
        });
    }


}
