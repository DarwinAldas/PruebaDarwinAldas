/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prbasinfo.jsf.controlador.modulos;

import prbasinfo.jsf.controlador.base.BaseControlador;
import prbasinfo.jsf.util.UtilitariosCod;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.util.JRLoader;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Admin
 */
@ManagedBean
@ViewScoped
public class GesReportes extends BaseControlador {

    /**
     * Creates a new instance of GestionCatalogosControlador
     */
    //LOGGER 
    private static final Logger LOGGER = Logger.getLogger(GesReportes.class.getName());
    //VARIABLES - ATRIBUTOS

    private Date fechaActual;
    String pathReporte;
    private StreamedContent file;
    Thread carga = new Thread();
    private Date fechaInicial;
    private Date fechaFinal;
    //SERVICIOS

    private double progreso = 1;
    InputStream archivoMuestra;
    Integer cod_cues;
    String cod_periodo;
    String tipo_muestra;
    int fila;
    int columna;
    boolean filasFalse;
    String nColumna;

    public GesReportes() {
    }

    @PostConstruct
    public void inicializar() {
        try {
            fechaActual = new Date();
            pathReporte = "";

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }

    public String reporteEmpleados() throws Exception {
        //Conexion con local datasource
        UtilitariosCod util = new UtilitariosCod();
        Connection conexion = util.getConexion();
        byte[] fichero = null;
        JasperReport jasperReport = null;
        Map parameters = new HashMap();
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
            ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
            session.removeAttribute("reporteInforme");
            String real = servletContext.getRealPath("/reportes/");
            parameters.put("SUBREPORT_DIR", real);
            pathReporte = "reportes/totalEmpleados.jasper";
            jasperReport = (JasperReport) JRLoader.loadObject(servletContext.getRealPath(pathReporte));
            fichero = JasperRunManager.runReportToPdf(jasperReport, parameters, conexion);
            session.setAttribute("reporteInforme", fichero);
        } catch (JRException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        } finally {
            if (conexion != null) {
                conexion.close();
            }
        }
        inicializar();
        return null;
    }

    //GET Y SET
    public double getProgreso() {
        return progreso;
    }

    public void setProgreso(double progreso) {
        this.progreso = progreso;
    }

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }
}
