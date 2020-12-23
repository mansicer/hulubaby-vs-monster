package network;

import javafx.geometry.Point2D;

public class OperateInfoBuilder {
    private String _name="";
    private int _id=0;
    private double _x=0;
    private double _y=0;
    private String _ori="";
    private int _damage=0;

    public OperateInfoBuilder(){}

    public OperateInfo buildOperate(){
        return new OperateInfo(_name,_id,_x,_y,_ori,_damage);
    }
    public OperateInfoBuilder name(String _name){
        this._name = _name;
        return this;
    }
    public OperateInfoBuilder id(int _id){
        this._id = _id;
        return this;
    }
    public OperateInfoBuilder position(Point2D point2D){
        this._x = point2D.getX();
        this._y = point2D.getY();
        return this;
    }
    public OperateInfoBuilder ori(String _ori){
        this._ori = _ori;
        return this;
    }
    public OperateInfoBuilder damage(int damage){
        this._damage = damage;
        return this;
    }
}
