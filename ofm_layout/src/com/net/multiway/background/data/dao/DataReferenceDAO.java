/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.net.multiway.background.data.dao;

import com.net.multiway.background.MainApp;
import com.net.multiway.background.data.DataDevice;
import com.net.multiway.background.data.DataParameters;
import com.net.multiway.background.data.DataReceive;
import com.net.multiway.background.data.DataReceiveEvents;
import com.net.multiway.background.data.DataReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Phelipe
 */
public class DataReferenceDAO {

    private EntityManagerFactory emf = null;

    public DataReferenceDAO() {
        emf = Persistence.createEntityManagerFactory("BackgroundDB");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    private void create(DataReceive dr, DataDevice dd, DataParameters dp) {
        DataReceiveDAO daor = new DataReceiveDAO();
        daor.create(dr);
        String msg = "DataReceive inserido na base.";
        Logger.getLogger(MainApp.class.getName()).log(Level.INFO, msg);

        DataReceiveEventsDAO edao = new DataReceiveEventsDAO();
        for (DataReceiveEvents receiveEvents : dr.getEvents()) {
            edao.create(receiveEvents);
        }
        msg = "DataReceiveEvents inserido na base.";
        Logger.getLogger(MainApp.class.getName()).log(Level.INFO, msg);

        DataDeviceDAO daod = new DataDeviceDAO();
        daod.create(dd);
        msg = "DataDevice inserido na base.";
        Logger.getLogger(MainApp.class.getName()).log(Level.INFO, msg);

        DataParametersDAO daop = new DataParametersDAO();
        daop.create(dp);
        msg = "DataParameters inserido na base.";
        Logger.getLogger(MainApp.class.getName()).log(Level.INFO, msg);
    }

    public void create(DataReference data) {
        create(data.getDataReceive(), data.getDevice(), data.getParameters());

        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DataReceive r = data.getDataReceive();
            if (r != null) {
                r = em.getReference(r.getClass(), r.getID());
                data.setDataReceive(r);
            }
            DataParameters dp = data.getParameters();

            if (dp != null) {
                dp = em.getReference(dp.getClass(), dp.getID());
                data.setParameters(dp);
            }
            DataDevice d = data.getDevice();

            if (d != null) {
                d = em.getReference(d.getClass(), d.getID());
                data.setDevice(d);
            }

            em.persist(data);

            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDataReference(data.getID()) != null) {
                System.out.println("Data " + data.toString() + " already exists.");
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
        String msg = "DataReference inserido na base.";
        Logger.getLogger(MainApp.class.getName()).log(Level.INFO, msg);
    }

    public void edit(DataReference data) {
        EntityManager em = null;

        try {
            em = getEntityManager();
            DataReference d = em.find(DataReference.class, data.getID());
            em.getTransaction().begin();
            d.copy(data);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = data.getID();
                if (findDataReference(id) == null) {
                    System.out.println("The data with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public DataReference findDataReference(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DataReference.class, id);
        } finally {
            em.close();
        }
    }
}