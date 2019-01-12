
import static java.lang.Thread.sleep;
import java.util.concurrent.TimeUnit;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;

/**
 *
 * @author brian
 */
public class Car implements Runnable {
    int carNumber;  // id for the car
    boolean visitedIsland = false;
    char location; // either 'i' for island or 'm' for mainland
    boolean isAtIsland = false; // used for communicating with ferry. loops until ferry sets it to true
    boolean returnedHome = false; // also used for communicating with ferry.
    
    Message message;
    
    public Car(int c, Message m){
        carNumber = c;
        message = m;
        location = 'm';
    }
    public boolean getVisitedIsland(){
        return visitedIsland;
    }
    public void setVisitedIsland(boolean b){
        visitedIsland = b;
    }
    public char getLocation(){
        return location;
    }
    public void setLocation(char c){
        location = c;
    }
    public void setIsAtIsland(boolean b){
        isAtIsland = b;
    }
    public void setReturnedHome(boolean b){
        returnedHome = b;
    }
    public void releaseFerrySpot(){        
        // calls message function to release the semaphore
        message.releaseFerrySpot(location);        
    }
    @Override
    public void run(){
        try{            
            int sleepDuration = (int)(Math.random() * 10 %5);
            sleep(sleepDuration);
            requestFerry();            
        }catch(Exception e){
            e.printStackTrace();
        }
        returnToMainland();
        System.out.println("Car " + carNumber + " is finally home.  Good-bye!");
    }
            
    
    void requestFerry(){
        System.out.println("Car " + carNumber + " requests the ferry.");
        // System.out.println(message.getMessage());
        try{
            message.reserveFerrySpot(this,location);            
                
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /**
     * The car thread waits until the ferry sets the flag arrivedAtIsland to true
       then go to sleep, and try to return to mainland.
     */
    void returnToMainland(){
        
        System.out.print("Car " + carNumber + " is waiting to get to the island.");
        while(!isAtIsland){
            try{
                Thread.sleep(100);
            }catch(Exception e){
                e.printStackTrace();
            }
            
        }
        
        sleepOnIsland();
        requestFerry();
        while(!returnedHome){            
            try{
                Thread.sleep(100);
            }catch(Exception e){
                e.printStackTrace();
            }
            
        }        
    }
    /**
     * sleep for a random amount of time (less than 1 second).
        call requestFerry() to go back to mainland.
     */
    void sleepOnIsland(){        
        int sleepTime = (int)(Math.random() * 1000 + 1000);
        System.out.println(this.carNumber + " is at the " + getLocationText() + " sleeping for " + sleepTime/1000 + " seconds.");
        try{
            // Thread.sleep(sleepTime);        
            TimeUnit.SECONDS.sleep(sleepTime/1000);
        }catch(Exception e){
            e.printStackTrace();
        }        
    }
    public String getLocationText(){
        String locationText = "";
        if(location == 'i')
            locationText = "island";
        else if(location == 'm')
            locationText = "mainland";
        return locationText;
    }
}
