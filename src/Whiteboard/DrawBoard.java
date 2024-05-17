package Whiteboard;

import remote.IRemoteWhiteboard;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;

public class DrawBoard extends JPanel {
    // store shapes and texts on whiteboard as a list of shapes
    private ConcurrentHashMap<Integer, Shape> draft = new ConcurrentHashMap<>();

    private IRemoteWhiteboard remoteWhiteboard;

    private int x1, x2, y1, y2;
    private String currentTool = "free draw";
    private Color color = Color.black;
    private int eraserSize;
    private final BasicStroke defaultStroke = new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    private final Font font = new Font("SANS_SERIF", Font.PLAIN, 16);

    public DrawBoard() {
        setBackground(Color.white);
        init();
    }

    /**
     * This method is invoked when the repaint function is called.
     * Draw all the shapes on the whiteboard.
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        ConcurrentHashMap<Integer, Shape> shapes = null;

        try {
            shapes = remoteWhiteboard.getShapes();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        for(Shape s : shapes.values()){
            g2.setColor(s.getColor());
            g2.setStroke(defaultStroke);
            if (s.getShape().equals("line")){
                g2.drawLine(s.getX1(), s.getY1(), s.getX2(), s.getY2());
            }
            else if (s.getShape().equals("eraser")){
                g2.setStroke(new BasicStroke(3 * (int) Math.pow(2, s.getEraserSize()), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(s.getX1(), s.getY1(), s.getX2(), s.getY2());
            }
            else if (s.getShape().equals("rectangle")){
                g2.drawRect(s.getX1(), s.getY1(), s.getX2(), s.getY2());
            }
            else if (s.getShape().equals("oval") || s.getShape().equals("circle")){
                g2.drawOval(s.getX1(), s.getY1(), s.getX2(), s.getY2());
            }
            else if (s.getShape().equals("text")) {
                g2.setFont(font);
                g2.drawString(s.getMessage(), s.getX1(), s.getY1());
            }
        }

        if (draft.size() > 0){
            Shape s = draft.get(0);
            g2.setColor(s.getColor());
            g2.setStroke(defaultStroke);

            if (s.getShape().equals("line")){
                g2.drawLine(s.getX1(), s.getY1(), s.getX2(), s.getY2());
            }
            else if (s.getShape().equals("rectangle")){
                g2.drawRect(s.getX1(), s.getY1(), s.getX2(), s.getY2());
            }
            else if (s.getShape().equals("oval") || s.getShape().equals("circle")){
                g2.drawOval(s.getX1(), s.getY1(), s.getX2(), s.getY2());
            }
            draft.remove(0);
        }
    }

    /**
     * Set up mouse listener.
     */
    private void init() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (currentTool.equals("text")) {
                    x1 = e.getX();
                    y1 = e.getY();
                    String input = JOptionPane.showInputDialog(null, null, "Enter the text:", JOptionPane.PLAIN_MESSAGE);
                    if (input != null) {
                        try {
                            remoteWhiteboard.addShape(new Shape(x1, y1, color, "text", input));
                        } catch (RemoteException ex) {
                            ex.printStackTrace();
                        }
                        repaint();
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                x1 = e.getX();
                y1 = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (currentTool.equals("line")) {
                    try {
                        remoteWhiteboard.addShape(new Shape(x1, y1, x2, y2, color, "line"));
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                    repaint();
                }
                // draw the circle given centre and radius
                else if (currentTool.equals("circle")) {
                    int diameter = (int) Math.round(Math.hypot(x1-x2, y1-y2));
                    try {
                        remoteWhiteboard.addShape(new Shape(Math.min(x1, x2), Math.min(y1, y2), diameter, diameter, color, currentTool));
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                    repaint();
                }
                else if (currentTool.equals("oval") || currentTool.equals("rectangle")) {
                    int width = Math.abs(x1 - x2);
                    int height = Math.abs(y1 - y2);
                    try {
                        remoteWhiteboard.addShape(new Shape(Math.min(x1, x2), Math.min(y1, y2), width, height, color, currentTool));
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                    repaint();
                }
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                x2 = e.getX();
                y2 = e.getY();

                if (currentTool.equals("free draw")) {
                    try {
                        remoteWhiteboard.addShape(new Shape(x1, y1, x2, y2, color, "line"));
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                    repaint();
                    x1 = x2;
                    y1 = y2;
                } else if (currentTool.equals("eraser")) {
                    try {
                        remoteWhiteboard.addShape(new Shape(x1, y1, x2, y2, Color.white, "eraser", eraserSize));
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                    repaint();
                    x1 = x2;
                    y1 = y2;
                } else if (currentTool.equals("line")) {
                    draft.put(draft.size(), new Shape(x1, y1, x2, y2, color, "line"));
                    repaint();
                } else if (currentTool.equals("oval") || currentTool.equals("rectangle")) {
                    int width = Math.abs(x1 - x2);
                    int height = Math.abs(y1 - y2);
                    draft.put(draft.size(), new Shape(Math.min(x1, x2), Math.min(y1, y2), width, height, color, currentTool));
                    repaint();
                } else if (currentTool.equals("circle")) {
                    int diameter = (int) Math.round(Math.hypot(x1-x2, y1-y2));
                    draft.put(draft.size(), new Shape(Math.min(x1, x2), Math.min(y1, y2), diameter, diameter, color, currentTool));
                    repaint();
                }
            }
        });
    }

    public void setCurrentTool(String currentTool) {
        this.currentTool = currentTool;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setEraserSize(int eraserSize) {
        this.eraserSize = eraserSize;
    }

    public void setRemoteWhiteboard(IRemoteWhiteboard remoteWhiteboard) {
        this.remoteWhiteboard = remoteWhiteboard;
    }
}
