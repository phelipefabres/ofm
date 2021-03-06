/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.net.multiway.background.data;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Phelipe
 */
@Entity
@Table(name = "DATARECEIVEVALUES")
@XmlRootElement
public class DataReceiveValues implements Serializable {

    private int lenght;
    private int data[];
    private Long ID;
    private int numberOfCalls;

    public DataReceiveValues() {
        this.lenght = 0;
        this.numberOfCalls = 0;
    }

    @Basic(optional = false)
    @Column(name = "LENGHT")
    public int getLenght() {
        return lenght;
    }

    public void setLenght(int lenght) {
        this.lenght = lenght;
        data = new int[this.lenght];
    }

    public int[] getData() {
        return data;
    }

    public void setData(int[] data) {
        this.data = data;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    @Basic(optional = false)
    @Column(name = "NUMBEROFCALLS")
    public int getNumberOfCalls() {
        return numberOfCalls;
    }

    public void setNumberOfCalls(int numberOfCalls) {
        this.numberOfCalls = numberOfCalls;
    }

    public void addValueDataIndex(int i, int value) {
        this.data[i] += value;
    }

    public void processData() {
        for (int i = 0; i < data.length; i++) {
            data[i] = data[i] / numberOfCalls;
        }
    }

    public void copy(DataReceiveValues data) {
        this.setLenght(data.getLenght());
        this.setID(data.getID());
        this.setData(data.getData());
    }
}

//@Entity
//@Table(name = "DATARECEIVEVALUES")
//@XmlRootElement
//public class DataReceiveValues implements Serializable {
//
//    private List<int[]> lstValues;
//    private Long ID;
//
//    public DataReceiveValues() {
//        lstValues = new ArrayList<>();
//    }
//
//    public List<int[]> getData() {
//        return lstValues;
//    }
//
//    public void setData(List<int[]> data) {
//        this.lstValues = data;
//    }
//
//    @Id
//    @Basic(optional = false)
//    @Column(name = "ID")
//    public Long getID() {
//        return ID;
//    }
//
//    public void setID(Long ID) {
//        this.ID = ID;
//    }
//
//    public void add(int[] data) {
//        this.lstValues.add(data);
//    }
//}
