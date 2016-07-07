
import com.trolltech.qt.core.QFile;
import com.trolltech.qt.core.QIODevice;
import com.trolltech.qt.core.QTimer;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.PenStyle;
import com.trolltech.qt.gui.*;

public class Tetris extends QWidget {

    Ui_Tetris ui = new Ui_Tetris();
    int u = 20, fx = 4, fy = 0;
    Forme frm, frmSuiv;
    int matrice[][] = new int[20][10];
    QTimer t;
    boolean gameOver, pause;
    int points, niveau;
    QImage fond, px, px2, pauseIm, termineIm;
    
    public Tetris(QWidget parent) {
        super(parent);
        fond = new QImage("classpath:img/fond.png");
        px = new QImage("classpath:img/px.png");
        px2 = new QImage("classpath:img/px2.png");
        pauseIm = new QImage("classpath:img/pause.png");
        termineIm = new QImage("classpath:img/termine.png");
        ui.setupUi(this);
        frm = new Forme();
        frmSuiv = new Forme();
    }
    
    void initialiser() {
    	gameOver = false;
    	pause = false;
    	points = 0;
    	niveau = 1;
    	frm.changer();
    	frmSuiv.changer();
    	((MainWindow)window()).nextFrm.dessinerSuivant(frmSuiv);
		fx = 4;
		fy = -frm.obtLimBas();
        for (int lgn = 0; lgn < 20; lgn++)
        	for (int col = 0; col < 10; col++)
        		matrice[lgn][col] = 0;
        t = new QTimer(this);
        t.timeout.connect(this, "descendre()");
        t.start(1000);
    }
    
    boolean collision() {
    	if (fx + frm.obtLimGauche() < 0)
    		fx = -frm.obtLimGauche(); 
    	else if (fx + frm.obtLimDroite() >= 10)
    		fx = 9 - frm.obtLimDroite();

		for (int lgn = 0; lgn < 4; lgn++)
			for (int col = frm.obtLimGauche(); col <= frm.obtLimDroite(); col++)
				if (fy+lgn >= 0 && frm.obtPx()[lgn][col] == 1 && matrice[fy+lgn][fx+col] == 1)
					return true;
		return false;
    }
    
    private void descendre() {
    	fy++;
    	boolean stop = false;
    	if (fy+4 > 20 && fy != 20-(frm.obtLimBas()+1)) {
    		fy = 20-(frm.obtLimBas()+1);
    		stop = true;
    	} else if (collision()) {
    		fy--;
    		stop = true;
    	}
    	
    	if (stop) {
    		for (int lgn = 0; lgn < 4; lgn++)
    			for (int col = 0; col < 4; col++)
    				if (frm.obtPx()[lgn][col] == 1) {
    					if  (fy+lgn >= 0)
    						matrice[fy+lgn][fx+col] = 1;
    					else {
    						t.stop();
    						gameOver = true;
    						repaint();
    						((MainWindow)window()).perdu();
    						return;
    					}	
    				}
    		int nbSuppr = 0;
    		for (int lgn = 19; lgn >= 0; lgn--) {
    			boolean supprime = true;
    			for (int col = 0; col < 10 && supprime; col++)
    				supprime &= matrice[lgn][col] == 1;
    			if (supprime) {
    				for (int lgn1 = lgn-1; lgn1 >= 0; lgn1--)
    					for (int col1 = 0; col1 < 10; col1++)
    						matrice[lgn1+1][col1] = matrice[lgn1][col1];
    				lgn++;
    				points++;
    				nbSuppr++;
    			}
    		}
    		if (nbSuppr >= 4)
    			points++;
    		Forme tmp = frm;
    		frm = frmSuiv;
    		frmSuiv = tmp;
    		frmSuiv.changer();
    		((MainWindow)window()).nextFrm.dessinerSuivant(frmSuiv);
    		((MainWindow)window()).ui.pointsLb.setText(String.format("%d pts", points*100));
    		if (niveau < (points / 100) + 1) {
    			niveau = (points / 100) + 1;
    			((MainWindow)window()).ui.msgLb.setText(String.format("Niveau %d", niveau));
    		}
    		fx = 4;
    		fy = -frm.obtLimBas();
    	}
    	
    	repaint();
    }
    
    protected void keyPressEvent(QKeyEvent e) {
    	if (gameOver)
    		return;
    	if (e.key() == Qt.Key.Key_Space.value()) {
    		pause = !pause;
    		if (pause)
    			t.stop();
    		else
    			t.start();
    		repaint();
    	}
    	if (pause)
    		return;
    	if (e.key() == Qt.Key.Key_Up.value()) {
    		frm.pivoter();
    		if (collision())
    			for (int i = 0; i < 3; i++)
    				frm.pivoter();
    	} else if (e.key() == Qt.Key.Key_Left.value()) {
    		fx--;
    		if (collision())
    			fx++;
    	} else if (e.key() == Qt.Key.Key_Right.value()) {
    		fx++;
    		if (collision())
    			fx--;
    	} else if (e.key() == Qt.Key.Key_Down.value() && t.interval() != 25)
    		t.setInterval(25);
    	else
    		return;
    	
    	repaint();
    }
    
    protected void keyReleaseEvent(QKeyEvent e) {
    	if (e.key() == Qt.Key.Key_Down.value() && !e.isAutoRepeat())
    		t.setInterval(1000/niveau);
    	repaint();
    }
    
    protected void paintEvent(QPaintEvent e) {
    	QPainter p = new QPainter(this);
    	p.drawImage(0, 0, fond);
    	
    	for (int lgn = 0; lgn < 20; lgn++)
    		for (int col = 0; col < 10; col++)
    			if (matrice[lgn][col] == 1)
    				p.drawImage(u * col - 9, u * lgn - 5, px);

    	for (int dy = 0; dy < 4; dy++)
    		for (int dx = 0; dx < 4; dx++)
    			if (frm.obtPx()[dy][dx] == 1)
    				p.drawImage(u * (fx + dx) - 9, u * (fy + dy) - 5, px2);
    	
    	if (pause)
    		p.drawImage(45, 165, pauseIm);
    	else if (gameOver)
    		p.drawImage(20, 165, termineIm);
    	
        p.end();    	
    }
}
