/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package deskcalc;

/**
 *　ステータスの列挙
 * @author masato
 */
public enum Status {
    INIT,
    NUM1,
    OP,  
    RUTE, 
    LOG,
    NUM2;
    
    public String toString(){
        return(super.toString()+"state");
        
    }
}
