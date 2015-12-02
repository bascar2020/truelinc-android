package co.com.indibyte.truelink.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by CharlieMolina on 24/09/15.
 */

@ParseClassName("Tarjetas")
public class Tarjetas extends ParseObject {

    public Tarjetas(String theClassName) {
        super(theClassName);
    }

    public Tarjetas() {

    }

    public String getObjectID(){
        return getObjectId();
    }

    public void setObjectID(String objectID){
        put("objectId", objectID);
    }

    public static ParseQuery<Tarjetas> getQuery(){
        return ParseQuery.getQuery(Tarjetas.class);
    }

    public String getTwit() {return getString("Twit"); }
    public String getCargo() {return getString("Cargo"); }
    public String getDireccion() {return getString("Direccion"); }
    public String getCiudad() {return getString("Ciudad"); }
    public String getEmail() {return getString("Email"); }
    public String getTelefono() {return getString("Telefono"); }
    public String getEmpresa() {return getString("Empresa"); }
    public String getNombre() {return getString("Nombre"); }
    public ParseFile getFoto(){return getParseFile("Foto");}
    public ParseFile getQr(){return getParseFile("Qr");}
    public ParseFile getLogo(){return getParseFile("LogoEmpresa");}
    public Boolean getPrivada() {return getBoolean("Privada"); }
    public String getTwiter() {return getString("twiter"); }
    public String getFacebook() {return getString("facebook"); }

    public void setQr(ParseFile Qr){put("Qr", Qr);}
    public void setFoto(ParseFile photo){put("Foto", photo);}
    public void setTwit(String Twit){ put("Twit", Twit.toString());   }
    public void setCargo(String Cargo){ put("Cargo", Cargo.toString());   }
    public void setDireccion(String Direccion){ put("Direccion", Direccion.toString());   }
    public void setCiudad(String Ciudad){ put("Ciudad", Ciudad.toString());   }
    public void setEmail(String Email){ put("Email", Email.toString());   }
    public void setTelefono(String Telefono){ put("Telefono", Telefono.toString());   }
    public void setEmpresa(String Empresa){ put("Empresa", Empresa.toString());   }
    public void setNombre(String Nombre){ put("Nombre", Nombre.toString());   }
    public void setLogo(ParseFile logo) {put("LogoEmpresa",logo);    }
    public void setPrivada(Boolean privada) {put("Privada",privada);    }
    public void setTwiter(String Nombre){ put("twiter", Nombre.toString());   }
    public void setFacebook(String Nombre){ put("facebook", Nombre.toString());   }
}
