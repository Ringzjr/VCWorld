import java.util.Scanner;
import java.util.Vector;

public class SearchBase {

	private static int PRINT_HOW_OFTEN = 1;
	public static boolean debug = false;


	public static void main(String[] args)  {
			
		
		
		
	}

	private void process(int depth_limit, String choice, boolean hasLimit) {

		//String start = "216/4x8/753";
		//String goal = "123/8x4/765";
		String start = "2D34/18E5/xC96/BAF7";
		String goal = "1234/CDE5/BxF6/A987";
		int size = 4;


		if (choice.equals("bfs")){
		CarryBoolean p = new CarryBoolean();
		int THIS_DEPTH = search(p , depth_limit, new EightPuzzle(start, goal,size), new PQasList(), hasLimit);
		// best first search uses PQ as search list, it is breadth-first search.
		// PQ.poll is expanding the nodes that entered first. (BFS)
		// Remove depth limiter. And make it return first path it finds.  
		
		// The vector search is our DFS. It will keep it's depth limiter.
		
		// DFID will be a DFS trapped in a while loop, incrementing
		// it's depth limiter from 0 until it reaches the depth limit specified. 
		
		// A's heuristic: Can be how many chars are in the correct spot for now
		// IDA will need to remember the smallest F value each iteration.
		// and use that as a limit
		// f(n) = depth + 2(correct chars);
		 
		System.out.println("The goal was found: "+p.getValue());
		System.out.println("Last depth was "+THIS_DEPTH);
		
		}
		
		if (choice.equals("dfs")){
		while(true){
		CarryBoolean p = new CarryBoolean();
		int NEXT_DEPTH = search(p , depth_limit, new EightPuzzle(start, goal,size), new VectorAsList(), hasLimit);
		System.out.println("The goal was found: "+p.getValue());
		System.out.println("Suggested next depth is "+NEXT_DEPTH+"\n");

		System.out.println("Would you like to re-enter a depth-limit?  (type 'no' to quit)");
		String answer = new Scanner(System.in).next();
		if (answer.toLowerCase().equals("no")) break;
		else {System.out.println("What depth limit would you like?");
				answer = new Scanner(System.in).next();
				int answer2 = -1;
				try{answer2 = Integer.parseInt(answer);}
				catch(Exception nodepth){System.out.println("You typed: "+answer);
				System.out.println("The depth limit must be a number."); break;}
				if (answer2 < 0) break;}
				}
		}
		
		if (choice.equals("dfid")){
		int false_limit = -1;
		CarryBoolean p = new CarryBoolean();
		while(true){
			while(false_limit < depth_limit){
			false_limit += 1;
			int NEXT_DEPTH = search(p , false_limit, new EightPuzzle(start, goal,size), new VectorAsList(), hasLimit);
			System.out.println("Goal found: "+p.getValue());
			System.out.println("Increasing depth to: "+NEXT_DEPTH+"\n");}

			System.out.println("Would you like to re-enter a depth-limit?  (type 'no' to quit)");
			String answer = new Scanner(System.in).next();
			if (answer.toLowerCase().equals("no")) break;
			else {System.out.println("What depth limit would you like?");
					answer = new Scanner(System.in).next();
					int answer2 = -1;
					try{answer2 = Integer.parseInt(answer);}
					catch(Exception nodepth){System.out.println("You typed: "+answer);
					System.out.println("The depth limit must be a number."); break;}
					if (answer2 < 0) break;}
					}
			}
			
			
		if (choice.equals("a")){
		
		CarryBoolean p = new CarryBoolean();
			int NEXT_DEPTH = search(p , depth_limit, new EightPuzzle(start, goal,size), new Asearch(), hasLimit);
			System.out.println("Goal found: "+p.getValue());
			System.out.println("Last depth was: " + NEXT_DEPTH);
					
			}
		
		if (choice.equals("ida")){
		hasLimit = true;
		int false_limit = 0;
		CarryBoolean p = new CarryBoolean();
		while(true){
			int NEXT_DEPTH = search(p , false_limit, new FifteenPuzzle(start, goal,size), new Asearch(), hasLimit);
			System.out.println("Goal found: "+p.getValue());
			if(!p.getValue()){
			System.out.println("Increasing depth to: " + NEXT_DEPTH);
			false_limit = NEXT_DEPTH;}
			else break;
			
					}
			}
		
		
		
		
	}

	public int search(CarryBoolean done, int limit, StateSpace ssp, SearchList open, boolean hasLimit) {

		if(hasLimit)System.out.println("Starting search with limit "+ limit);

		if(!(open instanceof Asearch))open.add(new State(ssp.getStart()));
		if(open instanceof Asearch)open.add(new State(ssp.getStart(),ssp.getGoal()));
		State current = new State("null");
		int count = 0;

		while (!done.getValue()) {
		
			if(!hasLimit) limit+=1;

			if (open.size()==0) {
				System.out.println("open list empty at "+count);
				break;
			}

			current =  open.remove();
			count++;
			if (count % PRINT_HOW_OFTEN == 0) {
				if(hasLimit)System.out.print("Search limit "+limit+" at ");
				System.out.println("Node # "+
						count+" Open list length:"+open.size()+" Current Node "+
						current.getRep()+"  Depth: "+current.getDepth());
			}
			if (ssp.isGoal(current.getRep())) {
				done.set(true);
				System.out.println(count+": found goal at "+current.getRep()+" at depth "+current.getDepth());
				current.printPath();
				break;
			}

			if (current.getDepth() <= limit) {
				Vector<String> kids = ssp.getKids(current.getRep());
				
				if(!(open instanceof Asearch)){
				for (String v : kids) {
					if (!current.getPath().contains(v)) 
						open.add(new State(current,v));
					}}
				if(open instanceof Asearch){
				for (String v : kids) {
					if (!current.getPath().contains(v)) 
						open.add(new State(current,v, ssp.getGoal()));
					}}
				
				
			}
		}
		if(hasLimit)return limit+6;
		else return current.getDepth();
	}


	/* 
		We don't need these since we're using if statements to choose
		what searchlist search() uses. 
	public int vectorSearch(CarryBoolean done, int limit, StateSpace ssp) {

		return search(done,limit,ssp,new VectorAsList());
	}


	public int bestFirstSearch(CarryBoolean done, int limit, StateSpace ssp) {

		return search(done,limit,ssp,new PQasList());
	} */


}
