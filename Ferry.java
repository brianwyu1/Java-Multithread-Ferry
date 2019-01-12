
import java.util.ArrayList;


/**
 *
 * @author brian
 */
public class Ferry implements Runnable{
    private final int capacity;
    private char location;
    private Message message;
    private Car[] carRequestsToIsland;
    private ArrayList<Car> onboardCars; 
    private Island island;
    
    public Ferry(int c,Message m, Island i){
        capacity = c;
        message = m;
        location = 'm';
        island = i;
        onboardCars = new ArrayList<>();
        
    }
    
    void transportCars(Car[] cars){
        
    
    }
    void pickUpCar(ArrayList<Car> list, char destination){
        // if destination is island, check space available. 
        // get minimum of ferry size and island free space. 
        // if destination is mainland, use ferry capacity as the max number of cars that can be picked up
        // can onboard up to the calculated max number of cars.
        // after picking up cars, remove those cars from the reservation list
        int maxCars;  
        Car tempCar;        
        System.out.print("At the " + getLocationText() + ", the ferry is picking up cars: ");
        if(destination == 'i'){
            int emptySpots = island.getEmptySpots();
            maxCars = (emptySpots > capacity)? capacity : emptySpots;
            for(int i = 0; i < maxCars; i++){
                if(list.size() > i) {
                    tempCar = list.get(i);
                    onboardCars.add(tempCar);
                    System.out.print(tempCar.carNumber + ", ");
                }                
            }
            System.out.println();
            message.removeFromIslandList(onboardCars.size());
        }
        else{
            for(int i = 0; i < capacity; i++){
                if(list.size() > i) {
                    tempCar = list.get(i);
                    onboardCars.add(tempCar);
                    System.out.print(tempCar.carNumber + ", ");
                }                
            }
            System.out.println();
            message.removeFromMainlandList(onboardCars.size());
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
    public void dropOffCar(){
    // update car's location
    // car releases ferrySpace semaphores
    // if at island, update car's visitedIsland and car sleeps on island    
    // update island's free space. if ferry is at island, subtract. if ferry is at mainland, add.
    // clear ferry's onboard list
        
        for(Car c :onboardCars){
            System.out.println("At the " + getLocationText() + ", the Ferry drops off car  " +  c.carNumber + ".");
            c.setLocation(location);
            c.releaseFerrySpot();
            if(c.location == 'i'){ 
                c.setVisitedIsland(true);
                c.setIsAtIsland(true);
            } 
            else if(location == 'm')
                c.setReturnedHome(true);
        }
        int emptySpots = island.getEmptySpots();
        int newEmptySpots;
        if(location == 'i'){
            newEmptySpots = emptySpots - onboardCars.size();            
        }
        else{
            newEmptySpots = emptySpots + onboardCars.size();            
        }
        island.setEmptySpots(newEmptySpots);
        System.out.println("Island now has " + newEmptySpots + " empty spots.");
        onboardCars.clear();
        
    }
    void goTo(char c){
        location = c;
        if(c == 'm') System.out.println("Ferry went to the Mainland.\n");
        else System.out.println("Ferry went to the Island.\n");
    }
    void checkIslandCapacity(){
        
    }
    void ferryRequest(){
        
    }
    int checkToIslandReservations(){
        ArrayList<Car> temp = message.getReservationListToIsland();
       if(temp == null) 
           return 0;
       if(!temp.isEmpty()){
           String s = "";
           for(Car c: temp){
               s = s + "Car " + c.carNumber + ", ";
           }
           System.out.println("At the " + getLocationText() + ", the Ferry is checking reservations to the island.\n" + 
                   "The reservation list has " + s);                      
       }
       return temp.size();
    }
    int checkToMainlandReservations(){
        ArrayList<Car> temp = message.getReservationListToMainland();
        if( temp == null) 
           return 0;
        if(!temp.isEmpty()){
            String s = "";
            for(Car c: temp){
                s = s + "Car " + c.carNumber + ", ";
            }
            System.out.println("At the " + getLocationText() + ", the Ferry is checking reservations to the mainland.\n" + 
                   "The reservation list has " + s); 
        }           
        return temp.size();
    }
    @Override
    public void run(){
        System.out.println("Ferry is at the mainland.");
        // check current location
        // check to see if there are any cars wanting to go to the other side
        // if yes and other side has room, pick them up and go to the other side.
        // if no, check to see if there are any cars waiting at the other side
        // if yes and this side has room, go to the other side and repeat
        // if no, sleep and repeat
        int toIslandRequests;
        int toMainlandRequests;
        for(int i = 0; i < 10000; i++){
            try{
                Thread.sleep(500);
            }catch(Exception e){
                e.printStackTrace();
            }
            if(location == 'm'){
                toIslandRequests = checkToIslandReservations();
                if(toIslandRequests > 0 && island.getEmptySpots() > 0){
                    pickUpCar(message.getReservationListToIsland(),'i');
                    goTo('i');
                    dropOffCar();
                }
                else{
                    toMainlandRequests = checkToMainlandReservations();
                    if(toMainlandRequests > 0 ){
                        goTo('i');
                    }
                }
            }
            else if(location == 'i'){
                toMainlandRequests = checkToMainlandReservations();
                if(toMainlandRequests > 0 ){
                    pickUpCar(message.getReservationListToMainland(),'m');
                    goTo('m');
                    dropOffCar();
                }
                else{
                    toIslandRequests = checkToIslandReservations();
                    if(toIslandRequests > 0 && island.getEmptySpots() > 0){                        
                        goTo('m');                        
                    }
                }
            }
        }
      
    }
    
}
