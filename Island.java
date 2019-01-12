/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author brian
 */
public class Island {
    final int capacity;
    private int emptySpots;
    
    public int getEmptySpots(){
        return emptySpots;
    }
    public void setEmptySpots(int i){
        emptySpots = i;        
    }
    
    public Island(int c){
        capacity = c;
        emptySpots = capacity;
    }
    void parkCar(Car c){
        System.out.println("Car + " + c.carNumber + "is being parked on the island.");
    
    }
}
