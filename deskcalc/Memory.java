/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deskcalc;

/**
 * メモリクラス
 *
 * @author masato
 */
public class Memory {

    /**
     * 入力数字の変数
     */
    private double num = 0;
    
    /**
     * コンストラクタ
     */
    public Memory() {
        num = 0;
    }

    public Memory(double num) {
        this.num = num;
    }
    
    /**
     * メモリに足しこむメソッド
     * @param x 
     */
    public void plus(double x) {
        num += x;
    }

    /**
     * メモリから引くメソッド
     * @param y 
     */
    public void minus(double y) {
        num -= y;
    }

    /**
     * 数字を呼び出すメソッド
     * @return 
     */
    public double getNum() {
        return (num);
    }

    /**
     * クリアするメソッド
     */
    public void clear() {
        num = 0;
    }

}
