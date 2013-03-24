package main;

/*
 * Test class for construction of Discrete Cosine Transformation coefficients as used in JPEG compression
 * Uses the table in the example provided on Wikipedia to test that each step returns as it should.
 * This code is merely for testing purposes and is not efficient.
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

		int[][] quant = new int [][] {{ 16,  11,  10,  16,  24,  40,  51,  61},
				{ 12,  12,  14,  19,  26,  58,  60,  55},
				{ 14,  12,  16,  24,  40,  57,  69,  56},
				{ 14,  17,  22,  29,  51,  87,  80,  62},
				{ 18,  22,  37,  56,  68, 109, 103,  77},
				{ 24,  35,  55,  64,  81, 104, 113,  92},
				{ 49,  64,  78,  87, 103, 121, 120, 101},
				{ 72,  92,  95,  98, 112, 100, 103,  99}};

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
		System.out.println();
		System.out.println("*****************************************************************");
		System.out.println("-----------------------------------------------------------------");
		System.out.println("*****************************************************************");

		//This next calculation could really be included in the previous loop, but is performed here because I want both tables
		System.out.println();
		int[][] QDCT = new int[8][8];

		for(int a = 0; a<DCT.length;a++){
			System.out.println();
			for(int b=0;b<DCT.length;b++){

				QDCT[a][b] = (int) Math.round((DCT[a][b]/quant[a][b]));
				System.out.print(QDCT[a][b] + ", ");
			}
		}

		System.out.println();
		System.out.println("*****************************************************************");
		System.out.println("-----------------------------------------------------------------");
		System.out.println("*****************************************************************");

		// Now returning the value to it's unquantised form to see the difference

		System.out.println();
		for(int c=0; c<DCT.length;c++){
			System.out.println();
			for(int d=0;d<DCT.length;d++){
				DCT[c][d] = QDCT[c][d]*quant[c][d];
				System.out.print(DCT[c][d] + ", ");
			}
		}

		System.out.println();
		System.out.println("*****************************************************************");
		System.out.println("-----------------------------------------------------------------");
		System.out.println("*****************************************************************");

		double reDC = 0;
		double[][] result = new double[8][8];
		for(int x=0; x<DCT.length; x++){
			System.out.println();

			for(int y=0; y<DCT.length; y++){

				reDC = 0;
				for(int u=0; u<DCT.length; u++){

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
                        
						reDC = reDC + aU*aV*DCT[u][v]*Math.cos((Math.PI/8)*(x + 0.5)*u)*Math.cos((Math.PI/8)*(y + 0.5)*v);
					}
				}

				// Table produced here is slightly different form example. This is due to rounding errors in the example.
				result[x][y] = Math.round(reDC);
				System.out.print(result[x][y] + ", ");
			}
			
		}
		
		System.out.println();
		System.out.println("*****************************************************************");
		System.out.println("-----------------------------------------------------------------");
		System.out.println("*****************************************************************");
		
		// Completes the final transformation back to YCbCr values by adding 128 to each value
		
		for(int e=0; e<result.length;e++){
			System.out.println();
			for(int f=0; f<result.length;f++){
				result[e][f] = result[e][f] + 128;
				if(result[e][f]< 0){
					result[e][f] = 0;
				}
				else if(result[e][f] > 255){
					result[e][f] = 255;
				}
				System.out.print(result[e][f] + ", ");
			}
		}
	}
}
