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


@Path("devices")
@Produces(MediaType.APPLICATION_JSON)
public class DeviceService {

   private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
   static {
       logger.setLevel(Level.INFO);
   }

   private EntityManagerFactory emf;

   public DeviceService() {
       try {
           emf = Persistence.createEntityManagerFactory("jreadings");
       } catch (Exception e) {
           logger.severe("Database connection error: " + e.toString());
       }
   }

    @PermitAll
    @GET
    public List<Device> getDevices() throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createQuery("select d from Device d");
            List<Device> result = (List<Device>) query.getResultList();
            return result;
        } finally {
            em.close();
        }
    }

    @RolesAllowed("admin")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createDevice(Map<String, String> valueMap) throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createQuery("select dt from DeviceType dt where dt.id = :device_type_id");
            List<DeviceType> deviceTypes = (List<DeviceType>) query.setParameter("device_type_id",
                    Integer.parseInt(valueMap.get("device_type_id"))).getResultList();
            Map<String, String> result = new HashMap<String, String>();
            if (!deviceTypes.isEmpty()) {
                DeviceType deviceType = deviceTypes.get(0);
                Device device = new Device(deviceType, valueMap.get("mac_addr"), new Timestamp(new Date().getTime()), null);
                EntityTransaction et = em.getTransaction();
                et.begin();
                em.persist(device);
                et.commit();
                result.put("status", "ok");
                return Response.status(200).entity(result).build();
            } else {
                result.put("status", "wrong device type");
                return Response.status(401).entity(result).build();
            }
        } finally {
            em.close();
        }
    }

    @PermitAll
    @GET
    @Path("{ deviceMacAddr : \\d+ }")
    public Device getDevice(@PathParam("deviceMacAddr") String deviceMacAddr) throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createQuery("select d from Device d where d.mac_addr = :mac_addr");
            List<Device> devices = (List<Device>) query.setParameter("mac_addr", deviceMacAddr).getResultList();
          if (!devices.isEmpty()) {
              Device result = devices.get(0);
              return result;
          } else {
              throw new BadRequestException("No such device: " + deviceMacAddr);
          }
        } finally {
            em.close();
        }
    }

    @PermitAll
    @GET
    @Path("{ deviceMacAddr : \\d+ }/readings")
    public List<Reading> getDeviceReadings(@PathParam("deviceMacAddr") String deviceMacAddr,
            @QueryParam("from") String from, @QueryParam("to") String to) throws SQLException {
        EntityManager em = emf.createEntityManager();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Query query;
        try {
            if (from == null && to == null) {
                query = em.createQuery("select r from Reading r where r.device.mac_addr = :device_mac_addr");
                query.setParameter("device_mac_addr", deviceMacAddr);
            } else if (from == null) {
                Date toDate = df.parse(to, new ParsePosition(0));
                query = em.createQuery("select r from Reading r where r.device.mac_addr = :device_mac_addr and r.created_at < :created_before");
                query.setParameter("device_mac_addr", deviceMacAddr);
                query.setParameter("created_before", toDate, TemporalType.DATE);
            } else if (to == null) {
                Date fromDate = df.parse(from, new ParsePosition(0));
                query = em.createQuery("select r from Reading r where r.device.mac_addr = :device_mac_addr and r.created_at > :created_after");
                query.setParameter("device_mac_addr", deviceMacAddr);
                query.setParameter("created_after", fromDate, TemporalType.DATE);
            } else {
                Date fromDate = df.parse(from, new ParsePosition(0));
                Date toDate = df.parse(to, new ParsePosition(0));
                query = em.createQuery("select r from Reading r where r.device.mac_addr = :device_mac_addr and r.created_at between :created_after and :created_before");
                query.setParameter("device_mac_addr", deviceMacAddr);
                query.setParameter("created_after", fromDate, TemporalType.DATE);
                query.setParameter("created_before", toDate, TemporalType.DATE);
            }
          List<Reading> result = (List<Reading>) query.getResultList();
          return result;
        } finally {
          em.close();
        }
    }

    @RolesAllowed("admin")
    @POST
    @Path("{ deviceMacAddr : \\d+ }/readings")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createDeviceReadings(@PathParam("deviceMacAddr") String deviceMacAddr, Map<String, String> valueMap) throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
          Query query = em.createQuery("select d from Device d where d.mac_addr = :mac_addr");
          List<Device> devices = (List<Device>) query.setParameter("mac_addr", deviceMacAddr).getResultList();
          Map<String, String> result = new HashMap<String, String>();
          if (!devices.isEmpty()) {
              Device device = devices.get(0);
              Reading reading = new Reading(device, valueMap.get("value"), new Timestamp(new Date().getTime()));
              EntityTransaction et = em.getTransaction();
              et.begin();
              em.persist(reading);
              et.commit();
              result.put("status", "ok");
              return Response.status(200).entity(result).build();
          } else {
              result.put("status", "wrong device address");
              return Response.status(401).entity(result).build();
          }
        } finally {
          em.close();
        }
    }
}
