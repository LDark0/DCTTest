package main;

/*
 * Test class for construction of Discrete Cosine Transformation coefficients as used in JPEG compression
 * Uses the table in the example provided on Wikipedia to test that each step returns as it should
 */

public class DCTCalculator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// create table of YCbCr values
		
		double[][] block = new double[][] {{ 52,  55,  61,  66,  70,  61,  64,  73},
										   { 63,  59,  55,  90, 109,  85,  69,  72},		
										   { 62,  59,  68, 113, 144, 104,  66,  73},
										   { 63,  58,  71, 122, 154, 106,  70,  69},
										   { 67,  61,  68, 104, 126,  88,  68,  70},
										   { 79,  65,  60,  70,  77,  68,  58,  75},
										   { 85,  71,  64,  59,  55,  61,  65,  83}, 
										   { 87,  79,  69,  68,  65,  76,  78,  94}};

		
		//Print out the values minus 128
		
		for(int i=0; i<block.length; i++){
			for(int j=0; j<block.length; j++){
				block[i][j] = block[i][j] - 128;
				if(block[i][j] > -10 && block[i][j] < 10 || block[i][j] > 0){
				System.out.print(" " + block[i][j] + ", ");
				}
				else{
					System.out.print(block[i][j] + ", ");
				}
			}
			System.out.println();
		}
		
		System.out.println("*****************************************************************");
		System.out.println("-----------------------------------------------------------------");
		System.out.println("*****************************************************************");
		
		double[] [] DCT = new double[8][8];
		
		double aU = 0;
		double aV = 0;
		double DC = 0;
		
		// Perform the transformation, square root numbers included to make process orthogonal
		
		for(int u=0; u<DCT.length; u++){
			System.out.println();
			if(u == 0){
				aU = Math.sqrt(0.125);
			}
			else{
				aU = Math.sqrt(0.25);
			}
			for(int v=0; v<DCT.length; v++){
				if(v == 0){
					aV = Math.sqrt(0.125);
				}
				else{
					aV = Math.sqrt(0.25);
				}
				DC = 0;
				for(int x=0; x<block.length; x++){
					for(int y=0; y<block.length; y++){
						DC = DC + aU*aV*block[x][y]*Math.cos((Math.PI/8)*(x + 0.5)*u)*Math.cos((Math.PI/8)*(y + 0.5)*v);
					}
				}
				
				//This should print out the same table as in the wiki diagram, before quantization
				DCT[u][v] = DC;
				System.out.print(DCT[u][v] + ", ");
			}

		}
	}

}