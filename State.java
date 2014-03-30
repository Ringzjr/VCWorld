import java.util.Vector;


public class State {

    private int[] location;
	private String path = "";
	private double fvalue;
    private double most;       // Highest fvalue seen by branch.
    private Vector<int[]> dirt_remaining = new Vector<int[]>();
    Vector<int[]> backlist = new Vector<int[]>();
    boolean debug = false;
    

	public State(int[] loc, double f_value) {
		location = loc;
        fvalue = f_value;
        most = f_value;
        backlist.add(location);
	}
	public State(int[] loc, State parent, double f_value, boolean remove_backlist) {
        int count = 0;
        fvalue = f_value;
        most = parent.getMost();
		location = loc;
        if(debug)System.out.println("New kid location: ["+location[0]+"]["+location[1]+"]");
        int y = parent.getLocation()[0];
        int x = parent.getLocation()[1];
        char move = '0';
        move = (location[0] < y)? 'N': move;
            if(debug){System.out.println("but parent y: "+y+" and parent x: "+x);
            System.out.println(move);}
        move = (location[0] > y)? 'S': move;
            if(debug)System.out.println(move);
        move = (location[1] < x)? 'W': move;
            if(debug)System.out.println(move);
        move = (location[1] > x)? 'E': move;
            if(debug)System.out.println(move);
        
        path = parent.getPath() + move;
            if(debug)System.out.println("Therefore move is: "+move);
        
        for(int i = 0; i < parent.getBacklist().size(); i++)
            backlist.add(parent.getBacklist().elementAt(i));
        if(remove_backlist)count = backlist.size();         // able to go back
        for(int i=0; i<count; i++)                          // if dirt found
            backlist.remove(0);
        backlist.add(location);
        
	}
    
    public State()
    {
        int[] place_holder = {0,0};
        fvalue = -1.0;
        most = -1.0;
        location = place_holder;
        dirt_remaining.add(place_holder);}
    
    // Gettr Methods
    
    public String getPath()
        {return path; }
	
	public int getDepth()
        {return path.length(); }
    
    public double getMost()
        {if((most > fvalue)||(most < 0))return most;
            else return fvalue;}
    
	public double getFvalue()
        {return fvalue; }
    
    public Vector<int[]> getDirt()
        {return dirt_remaining; }
    
    public int[] getLocation()
        {return location;}
    
    public Vector<int[]> getBacklist()
        {return backlist;}
    
    // Settr Methods
    
    public void setFvalue(double value)
        {fvalue = value;}
    
    public void setDirt(Vector<int[]> Dirt)
        {dirt_remaining = Dirt; }
    
	public void setPath(String p)
        {this.path = p; }
    
    public void setMost(double value)
        {most = value;}
    
    
    // Other Methods
    
    public String toString()
        {return ("Path: " + path + ";\nLocation: [" + location[0]+"]["+location[1]
                  + "];\nfvalue: " + fvalue + "\n"); }
    
    public boolean biggerThan (State other)
    { double me; double you; double yourMost = other.getMost(); double yourFvalue = other.getFvalue();
        me = ((fvalue >= most) && (most > -1))? fvalue : most;
        you = ((yourFvalue >= yourMost) && (yourMost > -1))? yourFvalue: yourMost;
            if(((me > you)&&(you > 0)) || (me < 0)) return true; else return false; }
    
	
	
}
