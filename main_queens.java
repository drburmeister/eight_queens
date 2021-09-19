//WHO: Author = Dennis Burmeister
//When: Date of completion = 9/18/2021
//What: Evolutionary solution to the 8 Queen's Problem
//Where: Marquette University

package eight_queens;

public class main_queens {
// BEGIN
	public static void main(String[] args) {
		
		// INITIALIZE POPULATION (randomly)
		boolean solution = false;
		int gen = 0;
		Board[] pop = new Board[10];
		for(int i = 0; i < 10; i++) {
			pop[i] = Board.initBoard();
		}
		
		//EVALUATE EACH CANDIDATE (built into sortPop function)
		Board.sortPop(pop);
		Board[] newGen = new Board[2];

		//REPEAT UNTIL ( TERMINATION CONDITION is satisfied) DO
		while ((solution != true) && (gen < 10000))
		{
			//SELECT PARENTS AND RECOMBINE (built into offspring function)
			newGen = Board.offspring(pop);
			//MUTATE, EVALUATE, AND SELECT (built into replace function)
			Board.replace(pop,newGen);
			if (Board.conflicts(pop[0].setup) == 0) {

				solution = true;
			}
			gen++;
		}
		//END
		System.out.println("Final number of generations = " + gen);
		System.out.println("Fancy final board:");
		finalPrint(pop[0]);
		System.out.print("Array form of final board: ");
		printArray(pop[0].setup);
		if(solution != true) System.out.println("Final number of conflict(s) = " + pop[0].conflicts);
		
	}
	
	//method to print in a nice format the board configuration for testing and final print
	public static void printArray(int[] arr) {
		for (int element: arr) {
	        System.out.print((element) + "|");	        
		}
		System.out.println();
	}
	//method to print the conflict values of a population for testing
	public static void printBoardCons(Board[] b) {
		for(int i = 0; i < b.length; i++) {
			System.out.print(b[i].conflicts + ", ");
		}
		System.out.println();
	}
	//method to print the fancy configuration of the solution
	public static void finalPrint(Board b) {
		int[][] board = {{0,0,0,0,0,0,0,0 },
						{ 0,0,0,0,0,0,0,0 },
						{ 0,0,0,0,0,0,0,0 },
						{ 0,0,0,0,0,0,0,0 },
						{ 0,0,0,0,0,0,0,0 },
						{ 0,0,0,0,0,0,0,0 },
						{ 0,0,0,0,0,0,0,0 },
						{ 0,0,0,0,0,0,0,0 }};
		
		for(int i = 0; i < 8; i++) {
			board[7 - b.setup[i]][i] = 1;
		}
		for(int i = 0; i < 8; i++)
		{
			System.out.print( (7-i) + ": ");
		    for(int j = 0; j < 8; j++)
		    {
		        System.out.print(board[i][j] + " ");
		    }
		    if(i == 7) {
		    	System.out.println("\n   ----------------\n   0 1 2 3 4 5 6 7");
		    }
		    else {System.out.println();}
		}		
	}
}
