package org.projii.commons.utils;

/**
 * Created with IntelliJ IDEA.
 * User: SunShelbi
 * Date: 5/11/13
 * Time: 1:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class Size {
    public Size(){

    }
    public Size(float width,float height)
    {
        this.width=width;
        this.height=height;
    }
    public float getHeight()
    {
        return height;
    }
    public void setHeigh(float height){
        this.height=height;
    }
    public float getWidth()
    {
        return width;
    }
    public void setWidth(float width){
        this.width=width;
    }
    private float height;
    private float width;
}
