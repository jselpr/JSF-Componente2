/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package abmc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import modelo.DAO;


/**
 *
 * @author usuario
 */
@FacesComponent(value = "miComponenteABMC")
public class Componente extends UINamingContainer {

    private enum Estado {

        LISTADO, EDICION, INSERCION, BORRADO
    };
    private Estado estado = Estado.LISTADO;

    public Boolean getEnEdicion() {
        return estado == Estado.EDICION;
    }

    public Boolean getEnListado() {
        return estado == Estado.LISTADO;
    }

    public Boolean getEnInsercion() {
        return estado == Estado.INSERCION;
    }

    public Boolean getEnBorrado() {
        return estado == Estado.BORRADO;
    }
    
    private Object actual;

    public Object getActual() {
        return actual;
    }

    @Override
    public void markInitialState() {
        super.markInitialState();
        String facetaOrigen = "listado";
        final String dt = "dt";
        moverContenidoFacetaAComponente(facetaOrigen, dt);
        //moverContenidoFacetaAComponente("borrado", "panelBorrado");
        moverContenidoFacetaAComponente("edicion", "panelEdicion");
        moverContenidoFacetaAComponente("insercion", "panelInsercion");
        
    }

    private void moverContenidoFacetaAComponente(String facetaOrigen, final String dt) {
        //To change body of generated methods, choose Tools | Templates.
        UIComponent facetaListado = this.getFacet(facetaOrigen);
        if(facetaListado instanceof UIPanel){
            List<UIComponent> children = facetaListado.getChildren();
            findComponent(dt).getChildren().addAll(0,children);
        } else {
            findComponent(dt).getChildren().add(0,facetaListado);
        }
    }
    
    
    public String iniciarBorrado(Object actual){
        estado=Estado.BORRADO;
        this.actual = actual;
        return "";
    }

    public String iniciarEdicion(Object actual){
        estado=Estado.EDICION;
        this.actual = actual;
        return "";
    }
    
    public String iniciarInsertar(){
        estado=Estado.INSERCION;
        String clase = (String) this.getAttributes().get("clase");
        try {
            Class cl = Class.forName(clase);
            this.actual = cl.newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }
    
    public String cancelarBorrado(){
        estado=Estado.LISTADO;
        this.actual=null;
        return "";
    }
    public String cancelarEdicion(){
        estado=Estado.LISTADO;
        this.actual=null;
        return "";
    }
    public String cancelarInsercion(){
        estado=Estado.LISTADO;
        this.actual=null;
        return "";
    }
    
    public String borrar(){
        String mensaje;
        try {
            DAO dao = (DAO) this.getAttributes().get("dao");
            dao.borrar(actual);
            estado=Estado.LISTADO;
            mensaje="Registro borrado.";
        } catch (Exception e) {
            mensaje="No se pudo borrar. "+e.getMessage();
        }
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage("dt",new FacesMessage(mensaje));
        return "";
    }
    public String editar(){
        String mensaje;
        try {
            DAO dao = (DAO) this.getAttributes().get("dao");
            dao.actualizar(actual);
            estado=Estado.LISTADO;
            mensaje="Registro guardado.";
        } catch (Exception e) {
            mensaje="No se pudo guardar. "+e.getMessage();
        }
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage("dt",new FacesMessage(mensaje));
        return "";
    }
    public String insertar(){
        String mensaje;
        try {
            DAO dao = (DAO) this.getAttributes().get("dao");
            dao.insertar(actual);
            estado=Estado.LISTADO;
            mensaje="Insertado guardado.";
        } catch (Exception e) {
            mensaje="No se pudo insertar. "+e.getMessage();
        }
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage("dt",new FacesMessage(mensaje));
        return "";
    }
    //JSF crea un objeto en cada petición, por ello guardamos el estado
    //para que sea recuperado por el nuevo objeto.
     @Override
    public void restoreState(FacesContext context, Object state) {
        Map<String,Object> valores = (Map<String,Object>) state;
        estado = (Estado) valores.get("estado");
        actual = valores.get("actual");
    }

    @Override
    public Object saveState(FacesContext context) {
        Map<String,Object> valores = new HashMap<>();
        valores.put("estado", estado);
        valores.put("actual", actual);
        return valores;
    }
}
