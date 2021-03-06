package com.ws;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.PersistenceException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;

import com.beans.IUsuariosRemote;
import com.entities.Usuario;
import com.exception.ServiciosException;

@Stateless
@Path("/usuarios2")
public class UsuariosRest2 {

	@EJB
	private IUsuariosRemote usuariosBeans;
	
	
	@GET
    @Path("/getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Usuario> getAllUsuarios() throws ServiciosException {
		try{
			List<Usuario> listaUsuarios = usuariosBeans.getAllUsuarios(); 
			return listaUsuarios;
		}catch(PersistenceException e){
			throw new ServiciosException("No se pudo obtener lista de usuarios");
		}
    }

	@GET
    @Path("/getById/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Usuario getUsuario(@PathParam("id") Long id) throws ServiciosException {
		try{
			System.out.println("getByIdUsuario-id " + id.toString() ); 
			Usuario usuario = usuariosBeans.getUsuario(id);
			return usuario;
		}catch(PersistenceException e){
			throw new ServiciosException("No se pudo obtener usuario con id " + id.toString());
		}
    }
	
    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public void addUsuario(Usuario usuario) throws ServiciosException{
        try{
            System.out.println("addUsuario-nombre " + usuario.getNombre() );    
            usuariosBeans.addUsuario(usuario);
			return;
        }catch(PersistenceException e){
            e.printStackTrace();
            throw new ServiciosException("No se pudo agregar usuario");
        }
    }
	
    @PUT
    @Path("/update/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    	// aca siguiendo la logica del teorico deberia decir solo Usuario usuario como parametro
      public void updateUsuario(@PathParam("id") Long id, Usuario usuario) throws ServiciosException{
        try{
            System.out.println("updateUsuario-nombre " + usuario.getNombre());
            usuario.setId(id);
            usuariosBeans.updateUsuario(usuario);
            return;
        }catch(PersistenceException e){
            e.printStackTrace();
            throw new ServiciosException("No se pudo modificar usuario");
        }
    }
    
    
    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteUsuario(@PathParam("id") Long id) throws ServiciosException {
		try{
			System.out.println("deleteUsuario-id " + id.toString());
			usuariosBeans.removeUsuario(id);
			return;
		}catch(PersistenceException e){
			throw new ServiciosException("No se pudo borrar usuario");
		}
    }

    
    //////////// CHEQUEAR SI NO SE PUEDE MEJORAR EL RETURN CON ALGUN CODIGO DE RESPUESTA
    
    
	@GET
    @Path("/getLogin/{nomAcceso}/{pass}")
    @Produces(MediaType.APPLICATION_JSON)
    public Usuario getLogin(@PathParam("nomAcceso") String nomAcceso, @PathParam("pass") String pass) throws ServiciosException {
		try{
			System.out.println("getLogin-nomAcceso-pass " + nomAcceso + " : " + pass); 
			Usuario usuario= usuariosBeans.getUnUsuarioBynomAcceso(nomAcceso); 
			
			if (usuario != null ) {   //existe el usuario
				if (nomAcceso.equals(usuario.getNomAcceso()) && pass.equals(usuario.getContrasena())) {
					usuario.setContrasena("+Login-Ok!");
					return usuario; //existe el usuario, y el nomAcceso y contraseņa concuerdan
				}else	{
					usuario.setContrasena("+Error!");
					return usuario; //usuario y contraseņa no concuerdan, por lo que se devuelve false
				}
		    }else {
		    	return usuario; // NO existe el usuario
		    }			
			
		}catch(PersistenceException e){
			throw new ServiciosException("No se pudo obtener usuario con nomAcceso " + nomAcceso );
		}
    }

}

