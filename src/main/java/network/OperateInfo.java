package network;

import java.io.Serializable;

public class OperateInfo implements Serializable {
    public String name;
    public int id;
    public double x;
    public double y;
    public String ori;
    public int damage;

    public OperateInfo(String name, int id, double x, double y, String ori, int damage){
        this.name = name;
        this.id = id;
        this.x = x;
        this.y = y;
        this.ori = ori;
        this.damage = damage;
    }
}
