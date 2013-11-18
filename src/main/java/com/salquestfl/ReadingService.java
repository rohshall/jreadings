package com.salquestfl;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.sql.SQLException;
import java.util.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;

import org.jboss.resteasy.spi.BadRequestException;

import java.util.logging.Logger;
import java.util.logging.Level;

import javax.persistence.Persistence;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

import com.salquestfl.model.*;

@Path("readings")
@Produces(MediaType.APPLICATION_JSON)
public class ReadingService {

    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    static {
        logger.setLevel(Level.INFO);
    }

    private EntityManagerFactory emf;

    public ReadingService() {
        try {
            emf = Persistence.createEntityManagerFactory("jreadings");
        } catch (Exception e) {
            logger.severe("Database connection error: " + e.toString());
        }
    }

    @PermitAll
    @GET
    public List<Reading> getAllReadings() throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createQuery("select r from Reading r");
            List<Reading> result = (List<Reading>) query.getResultList();
            return result;
        } finally {
          em.close();
        }
    }
}
