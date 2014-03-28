import java.util.Vector;


public class State implements Comparable<State> {

	private String rep;
	private Vector<String> path;
	private int depth;
	private int gvalue = 0;
	private int fvalue = 0;

	public State(String r) {
		rep = r;
		path = new Vector<String>();
		path.add(r);
		depth = 0;
	

	}
	public State(State s, String n) {
		rep = n;
		path = new Vector<String>(s.path);
		path.add(n);
		depth = s.depth+1;
	}
	public State(String r, String goal) {
		rep = r;
		path = new Vector<String>();
		path.add(r);
		depth = 0;
		gvalue = heuristic(r,goal);
		fvalue = gvalue + depth;
	}
	public State(State s, String n, String goal) {
		rep = n;
		path = new Vector<String>(s.path);
		path.add(n);
		depth = s.depth+1;
		gvalue = heuristic(n,goal);
		fvalue = gvalue + depth;
	}
	
	public int printPath() {
		int count = 1;
		for (String step : path)
		{
			System.out.println(count+"\n"+convert(step));
			count++;
		}
		return 0;
	}
	public int heuristic(String r, String goal){
	
	char[] n = r.toCharArray();
	char[] g = goal.toCharArray();
	char[] me = new char[r.length() - 2];
	char[] goal2 = new char[goal.length() - 2];
	int size = 0;	int gv = 0;
	int count1 = -1; int count2 = -1;
	
	for (int i=0; i < goal.length(); i++){if (n[i]!='/'){++count1; me[count1] = n[i];}
	if (g[i]!='/'){++count2; goal2[count2] = g[i];}
	  } 
	
	for (char l: g){if (l == '/')break; size++; }
	for (char a: me){
	int bad = new String(me).indexOf(a);    int good = new String(goal2).indexOf(a);
	   int dist = bad - good;
	int hold = bad; 

	while(dist != 0){
		if (dist < 0){
			if(size+hold < me.length && size+dist <= 0){dist += size; hold+=size; gv+=1;}
			else{dist+=1; hold+=1; gv+=1;}
				}
		else if(hold-size >= 0 && dist-size >= 0){dist -= size; hold-=size; gv+=1;}
			else{dist-=1; hold-=1; gv+=1;}
		}
	}
	return gv;
		} 
	
	
	

	public int compareTo(State other) {

		if (!(other instanceof State)) {
			System.exit(3);
			return 0;
		}
		/*
		int thisF = this.depth + this.heuristic()
		int otherF = ((State)other).getDepth() + ((State)other).heuristic()
		return thisF - otherF
		*/
		// deeper is larger
		//else return ((State)other).getDepth() -depth;
		//deeper is smaller  
		else return depth - ((State)other).getDepth();

	}

	private String convert(String step) {
		String[] pieces = step.split("/");
		String answer = "";
		for (String p : pieces)
			answer = answer +p +"\n";
		return answer;
	}

	public int getDepth() {
		return depth;
	}
	public int getFvalue(){
	return fvalue;}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public Vector<String> getPath() {
		return path;
	}
	public void setPath(Vector<String> path) {
		this.path = path;
	}
	public String getRep() {
		return rep;
	}
	public void setRep(String rep) {
		this.rep = rep;
	}
}
