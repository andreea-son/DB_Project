import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;

public class MyTabbedPane extends JTabbedPane {

    public MyTabbedPane(){
        setTabbedPaneBorderColor();
    }

    public void setTabbedPaneBorderColor() {
        UIManager.put("TabbedPane.contentAreaColor", new Color(233, 247, 250));
        UIManager.put("TabbedPane.shadow", new Color(233, 247, 250));
        UIManager.put("TabbedPane.darkShadow", new Color(233, 247, 250));
        this.setUI(new BasicTabbedPaneUI() {
            protected int calculateTabAreaHeight(int tab_placement, int run_count, int max_tab_height) {
                return 0;
            }
            @Override
            protected void installDefaults() {
                super.installDefaults();
                highlight = UIManager.getColor("TabbedPane.light");
                lightHighlight = UIManager.getColor("TabbedPane.highlight");
                shadow = UIManager.getColor("TabbedPane.shadow");
                darkShadow = UIManager.getColor("TabbedPane.darkShadow");
                focus = UIManager.getColor("TabbedPane.focus");
            }
        });
    }
}