package com.net.multiway.background.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.net.multiway.background.data.DataParameters;
import com.net.multiway.background.data.StopTest;
import com.net.multiway.background.data.DataDevice;
import com.net.multiway.background.receive.ReceiveParameters;
import com.net.multiway.background.receive.ReceiveValues;
import com.net.multiway.background.receive.ReceiveStatus;
import com.net.multiway.background.send.SendParameters;
import com.net.multiway.background.send.SendStopTest;
import com.net.multiway.background.send.SendDevice;
import com.net.multiway.background.send.SendConfirmationSignal;
import com.net.multiway.background.utils.Utils;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeviceComunicator {

    private int door;
    private String ip;

    private DataInputStream in;
    private DataOutputStream out;
    private Socket client;
	private ReceiveStatus receiveStatusData;
	private ArrayList<ReceiveValues> receiveValuesList;
	private ReceiveParameters receiveParametersData;

	
    public DeviceComunicator(String i, int d) {
        this.door = d;
        this.ip = i;
		receiveValuesList = new ArrayList<>();
    }

    public int getDoor() {
        return door;
    }

    public void setDoor(int door) {
        this.door = door;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void initialize() {
        try {
            this.client = new Socket(this.ip, this.door);
            System.out.println("O cliente se conectou ao OTDR!");
            this.in = new DataInputStream(client.getInputStream());
            this.out = new DataOutputStream(client.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(DeviceComunicator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sendPackage(DataParameters data) throws IOException {
        SendParameters s = new SendParameters(this.out, data);
        s.sender();
    }

    private void sendPackage(StopTest data) throws IOException {
        SendStopTest s = new SendStopTest(this.out, data);
        s.sender();
    }

    private void sendPackage(DataDevice data) throws IOException {
        SendDevice s = new SendDevice(this.out, data);
        s.sender();
    }

    private void sendPackage() throws IOException {
        SendConfirmationSignal s = new SendConfirmationSignal(this.out);
        s.sender();
    }

    private void receivePackage() throws IOException {

        // ignorando bytes inuteis
        byte[] b = new byte[60];
        this.in.read(b);
        
        // ignorando cabecario
        b = new byte[44];
        this.in.read(b);

        byte[] CMcode = new byte[4];
        byte[] DataLen = new byte[4];
        this.in.read(CMcode);
        this.in.read(DataLen);

        // codigo de resposta padao
        if (Utils.byte4ToInt(CMcode) == 0xA0000000) {
            receiveStatusData = new ReceiveStatus(this.in, Utils.byte4ToInt(DataLen));
            receiveStatusData.parser();
        } else if (Utils.byte4ToInt(CMcode) == 0x90000001) {
            ReceiveValues r = new ReceiveValues(this.in);
            r.parser();
			receiveValuesList.add(r);
        } else if (Utils.byte4ToInt(CMcode) == 0x90000000) {
            receiveParametersData = new ReceiveParameters(this.in);
            receiveParametersData.parser();
        }
    }

    public void connect(DataParameters data) {

        this.initialize();
        try {
            
            sendPackage(data);
            
            for (int i = 0; i < 10; i++) {
                receivePackage();
                sendPackage();

                System.out.println(i + 1);
            }
            this.client.close();

        } catch (IOException ex) {
            Logger.getLogger(DeviceComunicator.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void connect(StopTest data) {

        this.initialize();
        try {
            
            sendPackage(data);
            sendPackage();
            this.client.close();
            
        } catch (IOException ex) {
            Logger.getLogger(DeviceComunicator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void connect(DataDevice data) {

        this.initialize();
        try {
            
            sendPackage(data);
            sendPackage();
            this.client.close();
        
        } catch (IOException ex) {
            Logger.getLogger(DeviceComunicator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
	public ArrayList<ReceiveValues> getReceiveValuesList()
	{
		return this.receiveValuesList;
	}
	public ReceiveParameters getReceiveParametersData() {
		return receiveParametersData;
	}
	
	

}
