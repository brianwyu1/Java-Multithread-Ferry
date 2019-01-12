
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;

public class Message {
    private String message;
    private int ferrySpace;
    private final Semaphore ferrySpaceToIsland;
    private final Semaphore ferrySpaceToMainland;
    private ArrayList <Car> reservationListToIsland;
    private ArrayList <Car> reservationListToMainland;
    private char ferryLocation; // 'm' for mainland, 'i' for island, 'z' for unknown
    
    public Message(int ferrySpace){
        this.ferrySpace = ferrySpace;
        ferrySpaceToIsland = new Semaphore(ferrySpace, true);
        ferrySpaceToMainland = new Semaphore(ferrySpace, true);
        reservationListToIsland = new ArrayList <>();
        reservationListToMainland = new ArrayList <>();
        ferryLocation = 'z';
    }
 
    public void setFerryLocation(char c){
        ferryLocation = c;
    }
    public char getFerryLocation(){
        return ferryLocation;
    }
    public String getMessage(){
        return message;
    }
    public synchronized void setMessage(String s){
        message = s;
    }
    public ArrayList getReservationListToIsland(){
        return reservationListToIsland;
    }
    public ArrayList getReservationListToMainland(){
        return reservationListToMainland;
    }
    public boolean reserveFerrySpot(Car c,char location) throws Exception{
        // if successfully reserved spot, returns true
        // location is either 'm' or 'c'
        // after getting semaphore, add car to the appropriate ArrayList
       
        if(location == 'm')  {          
            ferrySpaceToIsland.acquire();
            System.out.println("Car " + c.carNumber + " reserved a spot on the ferry to the island.");
            reservationListToIsland.add(c);
            // ferrySpaceToIsland.release(); 
            return true;
        }
        else if(location == 'i'){
            ferrySpaceToMainland.acquire();
            System.out.println("Car " + c.carNumber + " reserved a spot on the ferry to the mainland.");
            reservationListToMainland.add(c);
            // ferrySpaceToMainland.release();  
            return true;
        }
        
        sleep(3000);
        System.out.println("Car " + c.carNumber + " releases a spot.");
            return true;
    }
    public void releaseFerrySpot(char destination){
        if(destination == 'i')
            ferrySpaceToIsland.release(); 
        else 
            ferrySpaceToMainland.release();  
    }
    public void removeFromIslandList(int numCars){
        if(numCars > reservationListToIsland.size()){
            reservationListToIsland.clear();
            return;
        }
        for(int i = 0; i < numCars; i++){
            if(reservationListToIsland.size() > 0)
                reservationListToIsland.remove(0);
        }
    }
    public void removeFromMainlandList(int numCars){
        for(int i = 0; i < numCars; i++){            
            if(reservationListToMainland.size() > 0)
                reservationListToMainland.remove(0);
        }
    }
}
