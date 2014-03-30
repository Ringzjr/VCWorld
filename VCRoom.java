import java.util.Vector;

public class VCRoom{
    private char[][] map;
    private int[] start = new int[2];
    private Vector<int[]> dirtspots = new Vector<int[]>();
    private boolean debug = false;
    
    

    public VCRoom (int y, int x)
    { map = new char[y][x]; }
    
    public VCRoom (int y, int x, Vector<Character> m)
    { map = new char[y][x];
        for (int i = 0; i < y; i++)
            for (int k = 0; k < x; k++)
                fill(i, k, m.elementAt(i*x + k));
    }
    
    
    public void fill(int y, int x, char e)
        { map[y][x] = e;
            int[] spot = {y,x};
            start = (e==('s')||e==('S'))? spot : start;
            if (e==('d')||e==('D'))
                dirtspots.add(spot);
        }
    
    
    public State initialize()
    {   State fresh = new State(start, hvalue(start, dirtspots));
        fresh.setDirt(dirtspots);
        //fresh.setFvalue(hvalue(fresh));
        //fresh.setMost(fresh.getFvalue());
        return fresh;
    }


    public void printMap()
    { for (int i = 0; i < map.length; i++){
        for (int k = 0; k < map[i].length; k++)
            System.out.print(map[i][k] + " ");
        System.out.println();}
        }

    
    public Vector<State> getKids(State current)
    {//Sets each kid's fvalue, remaining_dirt, and location.
        Vector<State> kids = new Vector<State>();
        Vector<int[]> locations = new Vector<int[]>(); 
        
        checkMoves(current.getLocation(), locations);  // Did not add anything to location
            if(debug)for(int[] x:locations)System.out.println("y: "+x[0]+"; x: "+x[1]);
        if(debug)System.out.println("Total amount of moves possible: "+locations.size());
        
        if(debug){System.out.println("Possible moves from: ["+current.getLocation()[0]+"]["+current.getLocation()[1]+"]");
                        if(debug)for(int i = 0; i < locations.size(); i++){
                        System.out.println((i+1) + ": [" +locations.elementAt(i)[0] +"]["+ locations.elementAt(i)[1]+"]");}
        }
        
        //Each location is a kid. Each kid had it's own dirt remaining vector. Create a new vector, for each location.
        for(int i = 0; i < locations.size(); i++){
            
            Vector<int []> dirt_remaining = new Vector<int[]>();
            for(int[] x : current.getDirt())
                if ((x[0] != locations.elementAt(i)[0]) || (x[1] != locations.elementAt(i)[1])) dirt_remaining.add(x);
            
            double gvalue = (double) current.getPath().length() + 1;
            double f_value = gvalue + hvalue(locations.elementAt(i), dirt_remaining);
            State kid = new State(locations.elementAt(i), current, f_value); // location and path set
            if(debug)System.out.println("kid's location: ["+kid.getLocation()[0]+"]["+kid.getLocation()[1]+"]");
            
            kid.setDirt(dirt_remaining);                             // dirt_remaning set
            //kid.setFvalue(kid.getPath().length() + hvalue(kid));     // fvalue set
            kids.add(kid);
        }
        return kids;
    }

    public double hvalue(State s)
    {//s should have dirt_remaining, and path set.
     // first find nearest dirt
        int[] loc = s.getLocation();
        int[] nearestDirt = {0,0};
        if(s.getDirt().size()>0)nearestDirt = s.getDirt().elementAt(0); // what if there is no dirt?
        else return (0.0);
        double dist = 6000;
        for (int[] x : s.getDirt())
        { boolean bigger = false;
            nearestDirt = x;
          dist = distance(loc, x);    // <-- distance between nearest dirt and location = hvalue
          for (int [] y : s.getDirt())
          { double dist2 = distance(loc, y);
              if (dist2 < dist) bigger = true;}
            if (!bigger) break;
        }
        if(debug)System.out.println("Nearest dirt at: "+nearestDirt[0] + " " + nearestDirt[1]);
        return dist;
    }
    
    public double hvalue(int[] location, Vector<int []> dirt)
    {
        int[] loc = location;
        int[] nearestDirt = {0,0};
        if(dirt.size()>0)nearestDirt = dirt.elementAt(0);
        else return (0.0);
        double dist = 6000;
        for (int[] x : dirt)
        { boolean bigger = false;
            nearestDirt = x;
            dist = distance(loc, x);    // <-- distance between nearest dirt and location = hvalue
            for (int [] y : dirt)
            { double dist2 = distance(loc, y);
                if (dist2 < dist) bigger = true;}
            if (!bigger) break;
        }
        if(debug)System.out.println("Nearest dirt at: "+nearestDirt[0] + " " + nearestDirt[1]);
        return dist;
    }
    
    
    public double distance(int[] here, int[] there)
        {
        int y1 = here[0]; int x1 = here[1];
        int y2 = there[0]; int x2 = there[1];
        double xsquare = Math.pow((Math.abs(x1 - x2)),2);
            if(debug)System.out.println("x1: " +x1+ "x2: " + x2+ "xsquare: " + xsquare);
        double ysquare = Math.pow((Math.abs(y1 - y2)),2);
            if(debug)System.out.println("y1: " + y1 + "; y2: "+ y2);
        double dist = Math.sqrt(xsquare + ysquare);
            if(debug)System.out.println("ysquare: " + ysquare + "; so dist: " + dist);
            return dist;
        }



    public void checkMoves(int[] loc, Vector<int[]> moves)
    {int y = loc[0];  int x = loc[1]; int[] child = new int[2]; int[] child2 = new int[2]; int[] child3 = new int[2]; int[] child4 = new int[2];
        // Testing Up move
        try{ if (!(map[--y][x]==('x'))&&!(map[y][x]==('X')))
        {child[0] = y; child[1] = x; moves.add(child); if(debug)System.out.println("Can go North from: ["+loc[0]+"]["+loc[1]+"] to ["+y+"]["+x+"]");}
        }
        catch (Exception offmapSouth){if(debug)System.out.println("Cannot go North from: ["+loc[0]+"]["+loc[1]+"]");}
        y++;
        if(debug)for (int i = 0; i < moves.size(); i++)
            System.out.println("current moves list: [" + moves.elementAt(i)[0]+"]["+moves.elementAt(i)[1]+"]");
        // Down
        try{ if (!(map[++y][x]==('x'))&&!(map[y][x]==('X')))
        {child2[0] = y; child2[1] = x; moves.add(child2);
            if(debug)System.out.println("Can go South from: ["+loc[0]+"]["+loc[1]+"] to ["+y+"]["+x+"]");}
        }
        catch (Exception offmapNorth){if(debug)System.out.println("Cannot go South from: ["+loc[0]+"]["+loc[1]+"]");}
        // Left
        y--;
        if(debug)for (int i = 0; i < moves.size(); i++)
            System.out.println("current moves list: [" + moves.elementAt(i)[0]+"]["+moves.elementAt(i)[1]+"]");
        try{ if (!(map[y][--x]==('x'))&&!(map[y][x]==('X')))
        {child3[0] = y; child3[1] = x;moves.add(child3);if(debug)System.out.println("Can go West from: ["+loc[0]+"]["+loc[1]+"] to ["+y+"]["+x+"]");}
        }
        catch (Exception offmapEast){if(debug)System.out.println("here is a: "+map[y][x]);System.out.println("Cannot go West from: ["+loc[0]+"]["+loc[1]+"]  ---- "+ offmapEast.getMessage() );}
        x++;
        if(debug)for (int i = 0; i < moves.size(); i++)
            System.out.println("current moves list: [" + moves.elementAt(i)[0]+"]["+moves.elementAt(i)[1]+"]");
        // Right
        try{ if (!(map[y][++x]==('x'))&&!(map[y][x]==('X')))
        {child4[0] = y; child4[1] = x; moves.add(child4);if(debug)System.out.println("Can go East from: ["+loc[0]+"]["+loc[1]+"] to ["+y+"]["+x+"]");}
        }
        catch (Exception offmapWest){/*if(debug)*/System.out.println("Cannot go East from: ["+loc[0]+"]["+loc[1]+"]");}
        x--;
        if(debug)for (int i = 0; i < moves.size(); i++)
        System.out.println("current moves list: [" + moves.elementAt(i)[0]+"]["+moves.elementAt(i)[1]+"]");
    
    }







}