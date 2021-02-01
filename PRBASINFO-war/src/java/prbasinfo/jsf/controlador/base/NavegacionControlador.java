/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prbasinfo.jsf.controlador.base;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author Admin
 */
@ManagedBean
@SessionScoped
public class NavegacionControlador extends BaseControlador {

    /**
     * Creates a new instance of NavegacionControlador
     */
    //LOGGER 
    private static final Logger LOGGER = Logger.getLogger(NavegacionControlador.class.getName());
    //VARIABLES - ATRIBUTOS
    private MenuModel menu;
    DefaultMenuItem itemBarraMenu;
    DefaultSubMenu subMenuPrincipal;
    DefaultSubMenu subSubMenu;
    private String username;
    private String password;

    //SERVICIOS
    //@EJB
    //private UsuarioServicio usuarioServicio;
    public NavegacionControlador() {
    }

    @PostConstruct
    public void inicializar() {
        username = "";
        password = "";
        try {

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

    public void inicioSesion() {
        try {
            autenticarUsuario();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void autenticarUsuario() {
        try {
            System.out.println("Usuario:" + username + "Pasword:" + password);
            if (username.equals("dvaldas")||username.equals("mandrade") && password.equals("Cambio120")) {
                construirMenu();
                this.getSession().setAttribute("usuario", username);
                redireccionarAPagina("", "inicio");
            } else {
                addErrorMessage("Error", "Datos incorrectos");
                username = null;
                password = null;
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    private void construirMenu() {
        try {
            menu = new DefaultMenuModel();
            DefaultMenuItem itemA2 = new DefaultMenuItem("Salir");
            itemA2.setIcon("ui-icon-power");
            itemA2.setCommand("#{navegacionControlador.cerrarSesion}");
            menu.addElement(itemA2);

            DefaultSubMenu subMenuAdmin = new DefaultSubMenu("Administración");
            DefaultMenuItem itemCatDet = new DefaultMenuItem("Empleados");
            itemCatDet.setIcon("ui-icon-person");
            itemCatDet.setCommand("#{navegacionControlador.redireccionarAPagina('administracion','empleados')}");
            subMenuAdmin.addElement(itemCatDet);
            menu.addElement(subMenuAdmin);

            DefaultSubMenu subMenuReportes = new DefaultSubMenu("Reportes");
            DefaultMenuItem itemRepMyC = new DefaultMenuItem("Empleados");
            itemRepMyC.setIcon("ui-icon-person");
            itemRepMyC.setCommand("#{navegacionControlador.redireccionarAPagina('reportes','repoEmpleados')}");
            subMenuReportes.addElement(itemRepMyC);
            menu.addElement(subMenuReportes);

            //---SubMenu Agencias --
            DefaultMenuItem itemInicio = new DefaultMenuItem("Inicio");
            itemInicio.setIcon("ui-icon-bookmark");
            itemInicio.setCommand("#{navegacionControlador.redireccionarAPagina('','inicio')}");
            menu.addElement(itemInicio);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metodo actualiza los controladores dependiendo a la pantalla
     */
    public void inicializarClasesEnTabView(TabChangeEvent event) throws Exception {
        String idPestania = event.getTab().getId();
        if (idPestania.equals("tab0")) {
            RequestContext.getCurrentInstance().execute("accedeTabCero();");
        }
        if (idPestania.equals("tab1")) {
            RequestContext.getCurrentInstance().execute("accedeTabUno();");
        }
        if (idPestania.equals("tab2")) {
            RequestContext.getCurrentInstance().execute("accedeTabDos();");
        }
    }

    /**
     * Va a una pagina especifica
     */
    public void redireccionarAPagina(String carpeta, String pagina) {
        try {
            if (carpeta.equals("")) {
                redirect(getContextName() + "/paginas/" + pagina + ".xhtml");
            } else {
                redirect(getContextName() + "/paginas/" + carpeta + "/" + pagina + ".xhtml");
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Retorna la session http.
     *
     * @return session
     */
    public HttpSession getSession() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(Boolean.TRUE);
        return session;
    }

    /**
     * Se encarga de ejecutar una redireccion.
     *
     * @param url url de destino
     * @throws IOException en caso de no poder hacer la redireccion
     */
    public void redirect(final String url) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(url);
    }

    /**
     * Cierra la sesion del usuario logueado.
     */
    public void cerrarSesion() {
        try {
            this.getSession().setAttribute("usuario", null);
            this.getSession().invalidate();
            redirect(getContextName() + "/index.xhtml");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

//    public CcdUsuarioCargaTrabajo getUsuarioActual() {
//        return usuarioActual;
//    }
//
//    public void setUsuarioActual(CcdUsuarioCargaTrabajo usuarioActual) {
//        this.usuarioActual = usuarioActual;
//    }
    public MenuModel getMenu() {
        return menu;
    }

    public void setMenu(MenuModel menu) {
        this.menu = menu;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
