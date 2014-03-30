import java.util.Vector;
import java.io.*;

public class SearchBase {

	private static int PRINT_HOW_OFTEN = 1;
	public static boolean debug = false;


	public static void main(String[] args) throws IOException {
			
        if (args.length < 1) {
            System.out.println("you need to enter a vacuum cleaner world file name.");
            System.exit(1);
        }
        
        BufferedReader br = new BufferedReader(new FileReader(args[0]));
        
        //FileReader stream = new FileReader(args[0]);
		Vector<Character> map = new Vector<Character>();
        
        String line;
        boolean Y = true;
        int y = -1;
        int x = -1;
        
        while((line = br.readLine()) != null)
        {
            try{ if(!(Y)) x = (Integer.parseInt(line.replaceAll("\\s+","")) > 0)? Integer.parseInt(line.replaceAll("\\s+","")) : x;
                 if(Y){ y = (Integer.parseInt(line.replaceAll("\\s+","")) > 0)? Integer.parseInt(line.replaceAll("\\s+","")): y; Y = false;}
                    if(x > 0)break;
                    continue;
            }
            catch (Exception notInt){}
        }
        
        
        while((line = br.readLine()) != null)
        {
            char[] mover = line.replaceAll("\\s+","").toCharArray();
            for (int i = 0; i < mover.length; i++)
                map.add(mover[i]);
        }
        
        
        
		boolean done = false;
        
        
      VCRoom room = new VCRoom(y, x, map);
        room.printMap();
        State start = room.initialize();
        System.out.println(start.getPath());
        
        
        SearchBase sb = new SearchBase();
        if(debug)System.out.println("Start state path length: "+start.getPath().length());
        //a 2 shows up before go is called.
        sb.go(room,start);
        
       
        
        // 1. VCWorld; 2. RBFS; 3. States; 4. VCWorld::getKids();
        
	}


    public void go(VCRoom room, State start)
    {
        State solution = RBFS(room, start, start.getFvalue());
        if(debug)System.out.println(solution.getMost() + "  " +solution);
        
        
    }
    



    public State RBFS(VCRoom room, State current, double flimit)
    { double f_limit = flimit;
            if(debug)System.out.println("(DEBUG on)\nThe state calling RBFS is: "+ current+"\n");
        Vector<State> kids = room.getKids(current);
        if (kids.size() < 0){current.setMost(-1);if(debug)System.out.println("(DEBUG on)\nNo possible moves.\n"); return current;}
        while(true){
            int[] place_holder = {0,0};
            State most = new State(place_holder, 0.0);
            State best = findBest(kids); State second = secondBest(kids);
                if(debug)System.out.println("(DEBUG on)\nBest path here is:\n" + best);
                if(debug)System.out.println("and it's MOST is: " +best.getMost()+ "; it's fValue is: "+best.getFvalue()+"\n");
            if(best.getDirt().size()==0){System.out.println("RESULT FOUND!!!!!!!!!!"); System.out.println(best); return best;}
            if(current.getPath().length() == 0) f_limit = second.getMost();
                if(debug)System.out.println("current F_LIMIT is: "+ f_limit+"\n");
            if(best.getMost() > f_limit){current.setMost(best.getMost()); return best;}
            else most = RBFS(room, best, f_limit);
            if (most.getDirt().size()==0)return most;
                if(debug)System.out.println("(DEBUG on)\nReturning... Setting 'most seen' for: " + best.getPath() +" to " +best.getMost()+"\n");

        }
    





    }

    public State findBest(Vector<State> list){
        if(list.size() == 0) return (new State());
        State w = list.elementAt(0); // default best is first element.
        for (State x: list)
        {
            boolean bigger = false;
            for (State y: list)
                if (x.biggerThan(y))
                    bigger = true;
            if (!(bigger)) return x; // true best is smallest element.
        }
        return w;
        
    }
    
    
    public State secondBest(Vector<State> list){
        if(list.size() == 0)return (new State());
        Vector<State> copy = new Vector<State>();
        for (State x: list)copy.add(x);
        
        State best = findBest(copy);      // find best in list
        int index = copy.indexOf(best);
        copy.remove(index);               // remove it
        
        State second;
        if(copy.size() > 0)
        second = findBest(copy);         // find secondbest in list
        else second = list.elementAt(0);
        return second;
    }
    
    
}
