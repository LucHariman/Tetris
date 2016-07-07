import java.util.Random;

class Forme {
	int rot[] = {2, 2, 4, 1, 2, 4, 4};
	int frm, orientation;
	int pix[][][] = {{{0,0,0,0},
					  {0,1,1,0},
					  {0,1,1,0},
					  {0,0,0,0}},
					 {{0,0,0,0},
					  {1,1,1,1},
					  {0,0,0,0},
					  {0,0,0,0}},
					 {{0,0,0,0},
					  {0,1,0,0},
					  {1,1,1,0},
					  {0,0,0,0}},
					 {{0,0,1,0},
					  {0,1,1,0},
					  {0,1,0,0},
					  {0,0,0,0}},
					 {{0,1,0,0},
					  {0,1,1,0},
					  {0,0,1,0},
					  {0,0,0,0}},
					 {{0,0,0,0},
					  {1,0,0,0},
					  {1,1,1,0},
					  {0,0,0,0}},
					 {{0,0,0,0},
					  {0,0,1,0},
					  {1,1,1,0},
					  {0,0,0,0}},
	         		};
	
	int frmActu[][]; 
	
	Forme() {
		changer();
	}
	
	void changer() {
		frm = new Random().nextInt(7);
		frmActu = pix[frm];
		for (int i = 0, nbRot = new Random().nextInt(3); i < nbRot; i++)
			pivoter();
	}
	
	void pivoter() {
		for (int lgn = 0; lgn < 4; lgn++)
			for (int col = lgn+1; col < 4; col++) {
				int tmp = frmActu[lgn][col];
				frmActu[lgn][col] = frmActu[col][lgn];
				frmActu[col][lgn] = tmp;
			}
		for (int lgn = 0; lgn < 4; lgn++)
			for (int col = 0; col < 2; col++) {
				int tmp = frmActu[lgn][col];
				frmActu[lgn][col] = frmActu[lgn][3-col];
				frmActu[lgn][3-col] = tmp;
			}
	}
	
	int obtLimGauche() {
		int col1 = 0;
		boolean trouve = false;
		while (!trouve) {
			for (int lgn = 0; lgn < 4 && !trouve; lgn++)
				trouve = frmActu[lgn][col1] == 1;
			if (!trouve)
				col1++;
		}
		return col1;
	}
	
	int obtLimDroite() {
		int col1 = 3;
		boolean trouve = false;
		while (!trouve) {
			for (int lgn = 0; lgn < 4 && !trouve; lgn++)
				trouve = frmActu[lgn][col1] == 1;
			if (!trouve)
				col1--;
		}
		return col1;
	}
	
	int obtLimBas() {
		int lgn1 = 3;
		boolean trouve = false;
		while (!trouve) {
			for (int col = 0; col < 4 && !trouve; col++)
				trouve = frmActu[lgn1][col] == 1;
			if (!trouve)
				lgn1--;
		}
		return lgn1;	
	}
	
	int [][] obtPx() {
		return frmActu;
	}
}
