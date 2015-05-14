/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package deskcalc;

/**
 *  演算子の列挙
 * @author masato
 */
public enum Op {
    NO,
    ADD,
    SUB,
    MUL,    
    DIV,
    EQUAL,
    CLEAR;
    
    public String toString(){
        return( super.toString()+ "op");
    }
    
}
