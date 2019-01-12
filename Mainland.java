
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author brian
 */
public class Mainland {
    Car[] cars;
    int ferrySpace = 2;
    int islandSpace = 3;
    public Mainland(){
        int numCars = (int)(Math.random() * 10%5) + 3;
        
        System.out.println("There are " + numCars + " cars on the mainland.");
        System.out.println("The ferry can hold up to " + ferrySpace + " cars.");
        System.out.println("The island can hold up to " + islandSpace + " cars.\n\n");
        
        final ExecutorService exService = Executors.newFixedThreadPool(numCars + 1);
        final Message message = new Message(ferrySpace);
        final Island island = new Island(islandSpace);
        Ferry ferry = new Ferry(ferrySpace, message,island);
        exService.execute(ferry);
        for(int i = 0;i<numCars;i++){
            Car car = new Car(i,message);
            exService.execute(car);
        }
        
        
        exService.shutdown();
    }
    public static void main(String[] args) {
        Mainland m = new Mainland();
        
    }
    
}
