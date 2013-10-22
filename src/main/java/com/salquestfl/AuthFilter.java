package com.salquestfl;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
 
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
 
import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.util.Base64;
import org.jboss.resteasy.logging.Logger;
 
/**
 * This filter verifies the access permissions for a user based on username and passowrd provided in request
 * */
@Provider
@PreMatching
public class AuthFilter implements ContainerRequestFilter
{
    Logger logger = Logger.getLogger(AuthFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException
    {
	String auth = requestContext.getHeaders().getFirst("Authorization");
	if (auth == null) {
	  //requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("User cannot access the resource.").build());
	} else {
	  String decodedAuth = new String(Base64.decode(auth));
	  int pos = decodedAuth.indexOf(":");
	  if (pos <= 0) {
	    //requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("User cannot access the resource.").build());
	  } else {
	    String login = decodedAuth.substring(0, pos);
	    String password = decodedAuth.substring(pos + 1); 
	    //String cryptedPassword = SCryptUtil.scrypt(password, 16384, 8, 1);
	    //userService.authenticate(login, password);
	    if (!login.equals("admin") || !password.equals("admin")) { //!SCryptUtil.check(password, cryptedPassword)) {
	      //requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("User cannot access the resource.").build());
	    }   
	  }   
        }
    }
}
