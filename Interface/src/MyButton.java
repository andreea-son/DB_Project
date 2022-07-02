import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyButton extends JButton {
    private Color color;
    private Color colorOver;
    private Color borderColor;
    private int radius;
    private String text;
    private int fontSize;

    public MyButton(int radius, Color color, Color colorOver, Color borderColor, String text, int fontSize)
    {
        this.text = text;
        this.radius = radius;
        this.color = color;
        this.colorOver = colorOver;
        this.borderColor = borderColor;
        this.fontSize = fontSize;
        setBackground(color);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(colorOver);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(color);
            }
        });
        setContentAreaFilled(false);
    }

    @Override
    protected void paintComponent (Graphics g){
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(borderColor);
        graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        graphics2D.setColor(getBackground());
        graphics2D.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, radius, radius);
        graphics2D.setColor(borderColor);
        graphics2D.setFont(new Font("Book Antiqua", Font.ITALIC|Font.BOLD, fontSize));
        FontMetrics fm = graphics2D.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(text)) / 2;
        int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
        graphics2D.drawString(text, x, y);
        graphics2D.dispose();
    }
}
