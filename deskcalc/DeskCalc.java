package deskcalc;

import java.util.Observable;

/**
 * モデル
 *
 * @author masato
 */
public class DeskCalc extends Observable {
    /**
     * 状態を表す変数
     */
    private Status status = Status.INIT;
    //============================================================
    /**
     * 今入力されている数字を記憶する変数
     */
    private double num = 0;
    /**
     * 一つ前の数字を記憶する変数
     */
    private double num0 = 0;
    /**
     * 演算の結果を格納する変数
     */
    private double result = 0;
    //============================================================
    /**
     * 現在どの操作が行われているかを格納する変数
     */
    private Op op = Op.NO;
    /**
     * 一つ前にどの操作が行われているかを格納する変数
     */
    private Op op0 = Op.NO;
    //============================================================
    /**
     * メモリ機能のための変数
     */
    private Memory memory = null;
    //============================================================
    /**
     * 小数点以下を入力中にtrue,さもなくばfalse
     */
    private boolean p = true;
    /**
     * 小数点以下を入力中に、ベースとなる値 （例）小数点以下第一位の時は、0,1
     */
    private double mag = 1f;

    /**
     * コンストラクタ
     */
    public DeskCalc() {
        clear();
    }
    /**
     * 初期化メソッド
     */
    public void clear() {

        num = 0;
        num0 = 0;
        result = 0;
        Op op = Op.NO;
        Op op0 = Op.NO;
        //=================
        p = false;
        mag = 1d;
        //=================
        moveStatus(Status.INIT);
        setChanged();
        notifyObservers();
    }

    /**
     * 状態遷移
     *
     * @param nextStatus 次の状態
     */
    private void moveStatus(Status nextStatus) {
        status = nextStatus;
        setChanged();
        notifyObservers();
    }

    /**
     * メモリ機能の有無の確認
     * @return 
     */
    public boolean hasMemory() {
        return (memory != null);
    }

    /**
     * メモリのクリア
     */
    public void pushMemoryClearKeyEvent() {
        if (memory != null) {
            memory = null;
        }
        setChanged();
        notifyObservers();
    }
    /**
     * メモリに足しこむ
     */
    public void pushMemoryPlusKeyEvent() {
        if (memory != null) {
            memory.plus(result);
        } else {
            memory = new Memory(result);
        }
        setChanged();
        notifyObservers();
    }
    
    /**
     * メモリに格納されている数字を表示
     */
    public void pushMemoryRestorKeyEvent() {
        if (memory != null) {
            num = memory.getNum();
            result = num;
        }
        setChanged();
        notifyObservers();
    }

    /**
     * メモリから引く
     */
    public void pushMemoryMinusKeyEvent() {
        if (memory != null) {
            memory.minus(result);
        } else {
            memory = new Memory(-result);
        }
        setChanged();
        notifyObservers();
    }

    /**
     * クリアボタン
     */
    public void pushClearKeyEvent() {
        clear();
        moveStatus(Status.INIT);
    }

    /**
     * 数字ボタンが押された時のメソッド
     * @param d 数字キー
     */
    public void pushDigitKeyEvent(char d) {
        if (status == Status.NUM1) {
            inputNum(d);
            moveStatus(Status.NUM1);
        } else if (status == Status.INIT) {
            resetNum();
            inputNum(d);
            moveStatus(Status.NUM1);
        } else if (status == Status.OP) {
            inputNumAndMove(d);
            moveStatus(Status.NUM2);
        } else if (status == Status.NUM2) {
            inputNum(d);
            moveStatus(Status.NUM2);
        } else if (status == Status.RUTE || status == Status.LOG) {
            resetNum();
            inputNum(d);
            if (op0 == Op.NO) {
                moveStatus(Status.NUM1);
            } else {
                moveStatus(Status.NUM2);
            }

            setChanged();
            notifyObservers();
        }
    }

    /**
     * 演算子キーを押された時のメソッド
     * @param op 演算子キー
     */
    public void pushOpKeyEvent(Op op) {
        if (status == Status.NUM1 || status == Status.INIT) {
            setOpAndMove(op);
            if (op == Op.EQUAL) {
                moveStatus(Status.INIT);
            } else {
                moveStatus(Status.OP);
            }
        } else if (status == Status.OP) {
            setOp(op);
            if (op == Op.EQUAL) {
                num = result;
                moveStatus(Status.INIT);
            } else {
                moveStatus(Status.OP);
            }
        } else if (status == Status.NUM2) {
            setOpAndCalc(op);
            if (op == Op.EQUAL) {
                moveStatus(Status.INIT);
            } else {
                moveStatus(Status.OP);
            }
        } else if (status == Status.RUTE || status == Status.LOG) {
            if (op == Op.EQUAL) {
                if (op0 == Op.NO) {
                    result = num;
                    setChanged();
                    notifyObservers();
                } else {
                    num0 = calc(op0, num0, num);
                    result = num0;
                    setChanged();
                    notifyObservers();
                }
                moveStatus(Status.INIT);
            } else {
                if (op == Op.NO) {
                    setOpAndMove(op);
                } else {
                    setOpAndCalc(op);
                }
                moveStatus(Status.OP);
            }
        }
    }

    /**
     * 小数点メソッド
     */
    public void pushPointKeyEvent() {
        p = true;
        mag = 0.1d;
    }
    
    /**
     * ルートメソッド
     */
    public void pushRutoKeyEvent() {
        num = Math.sqrt(num);
        result = num;
        moveStatus(Status.RUTE);
        setChanged();
        notifyObservers();
    }

    /**
     * 自然対数メソッド
     */
    public void pushLogKeyEvent() {
        num = Math.log(num);
        result = num;
        moveStatus(Status.LOG);
        setChanged();
        notifyObservers();
    }
    /**
     * 数字入力メソッド
     * @param d 
     */
    public void inputNum(char d) {
        if (!p) {
            num = num * 10 + (d - '0');
        } else {
            num = num + (d - '0') * mag;
            mag = mag / 10;
        }
        result = num;
        setChanged();
        notifyObservers();
    }
    
    /**
     * 演算子入力中に数字が押された場合のメソッド
     * @param d 
     */
    public void inputNumAndMove(char d) {
        inputNum(d);
        op0 = op;
    }
    
    /**
     * 演算子キーが連続で押された場合のメソッド
     * @param x 
     */
    public void setOp(Op x) {
        op = x;
        //p = false;
        //mag = 1f;
    }

    /**
     * 入力された数値のリセット
     */
    private void resetNum() {
        num = 0;
        p = false;
        mag = 1f;
    }

    /**
     * 演算子をずらして格納するメソッド
     * @param x 
     */
    public void setOpAndMove(Op x) {
        if (x == Op.EQUAL) {
            result = num;
            setChanged();
            notifyObservers();
            //  resetNum();
        } else {
            op0 = op;
            op = x;
            num0 = num;
            resetNum();
        }
    }

    /**
     * 演算と演算子の格納をするメソッド
     * @param x 
     */
    public void setOpAndCalc(Op x) {
        if (x == Op.EQUAL) {
            num0 = calc(op0, num0, num);
            result = num0;
            num = result;
            setChanged();
            notifyObservers();
            //resetNum();
        } else {
            op0 = op;
            op = x;
            num0 = calc(op0, num0, num);
            result = num0;
            setChanged();
            notifyObservers();
            resetNum();
        }
    }

    /**
     * 実際の計算を行うメソッド
     * @param op
     * @param x
     * @param y
     * @return 
     */
    public double calc(Op op, double x, double y) {
        switch (op) {
            case ADD:
                return (x + y);
            case SUB:
                return (x - y);
            case MUL:
                return (x * y);
            case DIV:
                return (x / y);

        }
        return (num0);
    }

    /**
     * [値getter] 数値numのgetter
     *
     * @return 数値のnumを返す
     */
    public double getNum() {
        return (num);
    }

    /**
     * [値getter] 結果resultのgetter
     *
     * @return 計算結果resultを返す
     */
    public double getResult() {
        return (result);
    }

    /**
     *　状態を返す
     */
    public Status getStatus() {
        return (status);
    }
}