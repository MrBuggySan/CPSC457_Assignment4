public class BroadcastSystem implements Runnable{

  private static byte bLimit = 0;
  private BroadcastAgent[] broadcastAgentList;
  private Thread[] broadcastAgentThreadList;
  
  private boolean doABroadcast;
	private int address;
	private int value;
	private int callerID;
	private int procSize;
  
  private BroadcastSystem(int procSize){
	this.procSize = procSize;
	broadcastAgentList = new BroadcastAgent[procSize];
	broadcastAgentThreadList = new Thread[procSize];
  }

  //only one BroadcastSystem can exist
  public static BroadcastSystem getInstance(int procSize){
    if( bLimit < 1 ){
        BroadcastSystem broadcastSystem = new BroadcastSystem(procSize);
        bLimit = 1;
        return broadcastSystem;
    }
    return null;
  }
  
  
  public void setValue(int value){
	  this.value= value;
  }
  
  public void doaBroadcast(int address, int value, int id){
		doABroadcast = true;
		this.callerID = id;
		this.address = address;
		this.value = value;
	}

  public void run(){
	  while(true){
	  		try{
	  		//wait for interrupt from DSM to do a store
	  	        while(true){
	  	        	Thread.sleep(100);
	  	        }
	  		}catch(InterruptedException e){
	  			try{
	  				if(doABroadcast){
		  				for(int i = 0 ; i < procSize; i++){
		  					if(i == callerID) continue;
		  					broadcastAgentList[i].recieveStore(address, value);
		  					broadcastAgentThreadList[i].interrupt();
		  					//simulate delay in broadcastSystem
		  					Thread.sleep(10);
		  				}
		  				
		  				//finished broadcasting
		  				doABroadcast = false;
		  			}else{
		  				//we should never get here
		  			}	
	  			}catch(InterruptedException t){
	  				//I hope we don't get here
	  			}
	  			
	  		}
	  	}
  }
  
  public void addBroadcastAgent(BroadcastAgent broadcastAgent, Thread broadcastAgentThread){
	  broadcastAgentList[broadcastAgent.getID()] = broadcastAgent;
	  broadcastAgentThreadList[broadcastAgent.getID()]= broadcastAgentThread;
  }
}
