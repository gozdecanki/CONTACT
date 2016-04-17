package com.example.gozde.denemedeneme;

/**
 * Created by GOZDE on 14.04.2016.
 */
public class PERSON {
    String p_name;
    String p_num1;
    String p_num2;
    int p_imageres;

    public PERSON(String p_name,String p_num1,int p_imageres){

        this.p_name = p_name;
        this.p_num1=p_num1;
        this.p_num2=p_num2;
        this.p_imageres=p_imageres;

    }


    public String getP_name() {
        return p_name;
    }


    public String getP_num1() {
        return p_num1;
    }

    public String getP_num2() {
        return p_num2;
    }

    public int getP_imageres() {
        return p_imageres;
    }

    public void setP_imageres(int p_imageres) {
        this.p_imageres = p_imageres;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public void setP_num1(String p_num1) {
        this.p_num1 = p_num1;
    }

    public void setP_num2(String p_num2) {
        this.p_num2 = p_num2;
    }
}
