package eight_queens;
import java.util.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Arrays;


//Setting up "BOARD" data type that will be used
public class Board {
	//Setup is the configuration of the Queens on the board with index = column and value = row
	int[] setup;
	//Conflicts is the number of Queens that "put each other in check" for a given setup
	int conflicts;
	
	public Board(int[] setup, int conflicts) {
		super();
		this.setup = setup;
		this.conflicts = conflicts;
	}
	
	public Board(Board b) {
		this.setup = b.setup;
		this.conflicts = b.conflicts;
	}
	
	//method to generate a random board
	public static int[] randBoard() {
		//Array of the possible coordinates of rows for a setup
		int[] newSetup = {0,1,2,3,4,5,6,7};
		//Randomizes the setup
		shuffleArray(newSetup);
		return newSetup;
		
	}
	//method that creates the setup for a new board and sets its conflicts values
	public static Board initBoard()
	{
		Board board = new Board(null,0);
		board.setup = Board.randBoard();
		board.conflicts = Board.conflicts(board.setup);
		return board;
	}
	// Source for the array randomizer https://stackoverflow.com/questions/1519736/random-shuffling-of-an-array
	// method to create setup for board with no repeating values and no conflicts in columns/rows
	// also comes into play when selecting parents and mutation
	public static void shuffleArray(int[] arr)
	{
		Random r = ThreadLocalRandom.current();
		for (int i = arr.length - 1; i > 0; i--)
		{
			int index = r.nextInt(i + 1);
			// Simple swap
			int x = arr[index];
			arr[index] = arr[i];
			arr[i] = x;
	  	}
	}
	//method to check conflict count for a given setup, (only checks for collisions along diagonals)
	public static int conflicts(int[] board)
	{
		int conflicts = 0;
		for(int i = 0; i < 7; i++) 
		{
			for(int j = i+1; j < 8; j++) 
			{
				//We check left to right so "backwards diagonals will only result in duplications
				//Formula for a diagonal in this case is x = |y|
				if(Math.abs(board[i]-board[j]) == (Math.abs(i-j))) 
				{
					 conflicts++;
				}
			}
		}
		return conflicts;
	}
	//method to determine which 2 setups to use as our parents for the next gen
	public static Board[] parentSelection(Board[] pop) {
		Board[] parents = new Board[2];
		//randomly select 5 setups/conflicts as candidates for parents
		int[] hold = {0,1,2,3,4,5,6,7,8,9};
		shuffleArray(hold);
		Board[] five = {pop[hold[0]],pop[hold[1]],pop[hold[2]],pop[hold[3]],pop[hold[4]]};
		//evaluate parents and pick the 2 fittest ones as parents
		sortPop(five);
		parents[0] = five[0]; parents[1] = five[1]; 
		return parents;
	}
	//method to create a new board based upon the selected parents
	public static Board createChild(Board parent1, Board parent2) {
		Board child = new Board(new int[] {-1,-1,-1,-1,-1,-1,-1,-1},0);
		int crossoverPoint = 3;
		for(int i = 0; i < crossoverPoint; i++) {
			child.setup[i] = parent1.setup[i];
		}
		int index = 3;
		for(int j = 0; j < 8; j++) {
			if(! contains(child.setup, parent2.setup[j])) {
				child.setup[index] = parent2.setup[j];
				index++;
			}
		}
		child.conflicts = conflicts(child.setup);
		mutate(child);
		return child;
	}
	//method to add mutation into the evolution process at a probability of 80%
	//Will swap the Queen placement of two random Queens if mutation is rolled 
	public static void mutate(Board child){
		Random rng = new Random();
		double prob = rng.nextDouble();
		if(prob < 0.8) {
			int[] index = new int[] {0,1,2,3,4,5,6,7};
			shuffleArray(index);
			int hold = child.setup[index[0]];
			child.setup[index[0]] = child.setup[index[1]];
			child.setup[index[1]] = hold;
		}
	}
	//method to conglomerate the next generation process and create two children
	public static Board[] offspring(Board[] pop) {
		Board[] OS = new Board[2];
		Board[] Parents = parentSelection(pop);
		OS[0] = createChild(Parents[0],Parents[1]);
		OS[1] = createChild(Parents[1],Parents[0]);
		return OS;
	}
	//method to keep our population size at 10 while introducing a next generation
	public static void replace(Board[] pop, Board[] OS)
	{
		int l = pop.length + OS.length;
		Board[] hold = new Board[l];
		for(int i = 0; i < pop.length; i++)
		{
			hold[i] = pop[i];
		}
		for(int i = pop.length; i < l; i++)
		{
			hold[i] = OS[i-pop.length];
		}
		sortPop(hold);
		for(int i = 0; i < pop.length; i++)
		{
			pop[i] = hold[i];
		}
	}
	//method to sort a population from least to greatest based on their conflicts (also evaluates and sets conflicts)
	public static void sortPop(Board[] board){
		for(int c = 0; c < board.length; c++) board[c].conflicts = conflicts(board[c].setup);
		for (int i = 0; i < board.length; i++){  
			for (int j = i + 1; j < board.length; j++)   
			{  
				Board tmp = new Board(null,0);  
				if (board[i].conflicts > board[j].conflicts){  
					tmp = board[i];  
					board[i] = board[j];  
					board[j] = tmp;  
				}  
			}
		}
	}
	//method to check if a value already exists within an array (used in child creation)
	//Source is from a Java library
	public static boolean contains(final int[] arr, final int key) {
	    return Arrays.stream(arr).anyMatch(i -> i == key);
	}
}
