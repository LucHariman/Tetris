

import com.trolltech.qt.core.Qt.PenStyle;
import com.trolltech.qt.gui.*;

public class NextFrm extends QWidget {

    Ui_NextFrm ui = new Ui_NextFrm();
    Forme frm = null;
    QImage fond2, px2;

    public NextFrm(QWidget parent) {
        super(parent);
        ui.setupUi(this);
        fond2 = new QImage("classpath:img/fond2.png");
        px2 = new QImage("classpath:img/px2.png");
    }
    
    void dessinerSuivant(Forme f) {
    	frm = f;
    	repaint();
    }
    
    protected void paintEvent(QPaintEvent e) {
    	if (frm == null)
    		return;
    	int u = 20;
        QPainter p = new QPainter(this);
        p.drawImage(-2, -2, fond2);
    	for (int dy = 0; dy < 4; dy++)
    		for (int dx = 0; dx < 4; dx++)
    			if (frm.obtPx()[dy][dx] == 1)
    				p.drawImage(u * dx - 9, u * dy - 5, px2);
        p.end();

    }
}
