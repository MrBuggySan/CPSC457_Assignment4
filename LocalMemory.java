import java.util.Random;

public class LocalMemory{
  private ArrayList<Integer> memoryArray;
  private final arraySize = 10;

  public LocalMemory(){

    Random rand = new Random();
    int n;

    memoryArray = new ArrayList<>();
    for(int i = 0; i < arraySize; i++){
      //fill the local memory with random values between 50 and 1, to replicate garbage values
      n = rand.nextInt(50) + 1;
      memoryArray.add(new Integer(n));
    }
  }

  public int load(int address){
    return memoryArray.get(address);
  }

  public void store(int address, int value){
    memoryArray.set(address, new Integer (value));
  }

}
