

import com.trolltech.qt.gui.*;

public class MainWindow extends QMainWindow {

    Ui_MainWindow ui = new Ui_MainWindow();
    Tetris tetris;
    NextFrm nextFrm;

    public static void main(String[] args) {
        QApplication.initialize(args);

        MainWindow testMainWindow = new MainWindow();
        testMainWindow.show();

        QApplication.exec();
    }

    public MainWindow() {
        ui.setupUi(this);
        tetris = new Tetris(this);
        ui.gl.addWidget(tetris);
        nextFrm = new NextFrm(this);
        ui.nextGl.addWidget(nextFrm);
        tetris.initialiser();
        ui.nouvBtn.setVisible(false);
    }

    private void on_nouvBtn_clicked() {
    	tetris.initialiser();
    	ui.nouvBtn.setVisible(false);
    }
    
    void perdu() {
    	ui.msgLb.setText("Vous avez perdu !");
    	ui.nouvBtn.setVisible(true);
    }
    
}
