
public class DSMandBAgentComms {
	private boolean doABroadcast;
	private int address;
	private int value;
	
	public DSMandBAgentComms(){
		doABroadcast = false;
	}
	
	public boolean doIBroadcast(){
		return doABroadcast;
	}
	
	public void finishedBroadcast(){
		doABroadcast = false;
	}
	
	public void doaBroadcast(int address, int value){
		doABroadcast = true;
		this.address = address;
		this.value = value;
	}
	
	public int getAddress(){return address;}
	public int getValue(){return value;}
	
}
