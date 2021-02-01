/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prbasinfo.jsf.controlador.modulos;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import prbasinfo.ejb.entidades.Cargo;
import prbasinfo.ejb.entidades.Departamento;
import prbasinfo.ejb.entidades.Empleado;
import prbasinfo.ejb.entidades.EmpleadoSalarioHistorico;
import prbasinfo.ejb.servicios.CargoServicio;
import prbasinfo.ejb.servicios.DepartamentoServicio;
import prbasinfo.ejb.servicios.EmpSalarioHistServicio;
import prbasinfo.ejb.servicios.EmpleadoServicio;
import prbasinfo.jsf.controlador.base.BaseControlador;

/**
 *
 * @author Admin
 */
@ManagedBean
@ViewScoped
public class GesEmpleadoControlador extends BaseControlador {

    /**
     * Creates a new instance of GestionPeriodoControlador
     */
    //LOGGER 
    private static final Logger LOGGER = Logger.getLogger(GesEmpleadoControlador.class.getName());
    // VARIABLES Y ATRIBUTOS
    Empleado empleadoActual;
    Empleado empleadoSupActual;
    Departamento departamentoActual;
    Departamento departamentoSupActual;
    Cargo cargoActual;
    EmpleadoSalarioHistorico empSalHistoricoActual;
    private List<Empleado> listaEmpleado;
    private List<Empleado> listaEmpleadoSup;
    private List<Departamento> listaDepartamento;
    private List<Cargo> listaCargo;
    private boolean editaEmpleado;
    private boolean editaSueldo;
    private String nomEmpleadoSuperior;
    private Date fechaActual;
    private String usuarioActual;

    //SERVICIOS
    @EJB
    private EmpleadoServicio empleadoServicio;
    @EJB
    private DepartamentoServicio departamentoServicio;
    @EJB
    private CargoServicio cargoServicio;
    @EJB
    private EmpSalarioHistServicio empSalarioHistServicio;

    public GesEmpleadoControlador() {
    }

    @PostConstruct
    public void inicializar() {
        try {
            empleadoActual = new Empleado();
            departamentoActual = new Departamento();
            cargoActual = new Cargo();
            empSalHistoricoActual = new EmpleadoSalarioHistorico();
            listarEmpleados();
            listaDepartamentos();
            editaEmpleado = false;
            editaSueldo = false;
            nomEmpleadoSuperior = "";
            fechaActual = new Date();
            usuarioActual = (String) getSession().getAttribute("usuario");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    //Metodos
    public void guardarEmpleado() {
        try {
            if (editaEmpleado == false) {
                if (empleadoServicio.existeIdentificadorEmpleado(empleadoActual.getEmpIdentificador())) {
                    addWarningMessage("El identificador ingresado ya existe en la base", "");
                } else {

                   if (controlaMismoDepartamento() && controlaJerarquiaEmpleado()) {
                    empleadoActual.setEstadoLogico(true);
                    empleadoActual.setCodDepartamento(departamentoActual);
                    empleadoActual.setCodCargo(cargoActual);
                    Timestamp fec1 = new Timestamp(fechaActual.getTime());
                    empleadoActual.setEmpFechaRegistro(fec1);
                    empleadoServicio.crearEmpleado(empleadoActual);
                    verificaEstructuraMail();
                    guardarHistorialSalarioInicial();
                    addSuccessMessage("Registro Guardado", "");
                    listarEmpleados();
                    refrescarObjEmpleado();
                      }
                }
            } else {

                if (controlaMismoDepartamento() && controlaJerarquiaEmpleado()) {
                    empleadoActual.setCodDepartamento(departamentoActual);
                    empleadoActual.setCodCargo(cargoActual);
                    Timestamp fec1 = new Timestamp(fechaActual.getTime());
                    empleadoActual.setEmpFechaActualiza(fec1);
                    empleadoServicio.editarEmpleado(empleadoActual);
                    verificaEstructuraMail();
                    addSuccessMessage("Registro Actualizado", "");
                    listarEmpleados();
                    editaEmpleado = false;
                    empleadoActual = null;
                    refrescarObjEmpleado();
                }

            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void guardarHistorialSalarioInicial() {
        try {
            empSalHistoricoActual.setCodEmpleado(empleadoActual);
            empSalHistoricoActual.setHisDescripcion("Salario Inicial");
            empSalHistoricoActual.setHisSalario(empleadoActual.getEmpSalario());
            Timestamp fec1 = new Timestamp(fechaActual.getTime());
            empSalHistoricoActual.setHisFechaRegistro(fec1);
            empSalHistoricoActual.setEstadoLogico(true);
            empSalarioHistServicio.crearEmpSalHistorico(empSalHistoricoActual);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void guardarHistorialSalarioActualizado() {
        try {
            empSalHistoricoActual.setCodEmpleado(empleadoActual);
            Timestamp fec1 = new Timestamp(fechaActual.getTime());
            empSalHistoricoActual.setHisFechaRegistro(fec1);
            empSalHistoricoActual.setEstadoLogico(true);
            empSalarioHistServicio.crearEmpSalHistorico(empSalHistoricoActual);
            empleadoActual.setEmpSalario(empSalHistoricoActual.getHisSalario());
            empleadoServicio.editarEmpleado(empleadoActual);
            listarEmpleados();
            addSuccessMessage("Se ha actualizado el sueldo");
            refrescarObjEmpleado();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void refrescarObjEmpleado() {
        try {
            empleadoActual = null;
            departamentoActual = null;
            cargoActual = null;
            editaEmpleado = false;
            editaSueldo = false;
            empSalHistoricoActual = null;
            nomEmpleadoSuperior = "";
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void recuperarCamposEmpleado(Empleado empleado) {
        try {
            empleadoActual = new Empleado();
            empleadoActual = empleado;
            departamentoActual = departamentoServicio.buscarPorIdDepartamento(empleadoActual.getCodDepartamento().getIdDepartamento());
            cargoActual = cargoServicio.buscarPorIdCargo(empleadoActual.getCodCargo().getIdCargo());
            listarCargoPorDepartamento();
            if (empleadoActual.getCodEmpleadoPadre() != null) {
                nomEmpleadoSuperior = empleado.getCodEmpleadoPadre().getEmpNombre() + " " + empleado.getCodEmpleadoPadre().getEmpApellido();
                empleadoSupActual=empleadoActual.getCodEmpleadoPadre();
            } else {
                nomEmpleadoSuperior = "";
            }
            editaEmpleado = true;
            editaSueldo = true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void confirmaEliminarEmpleado(Empleado empleado) {
        try {
            empleado.setEstadoLogico(false);
            empleadoServicio.editarEmpleado(empleado);
            addSuccessMessage("Registro Eliminado", "");
            listarEmpleados();
            empleadoActual = null;
            editaEmpleado = false;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
            addWarningMessage("No se puede eliminar el registro", "");
        }
    }

    public void listarEmpleados() throws Exception {
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("estadoLogico", true);
        listaEmpleado = empleadoServicio.listarEmpleadosTodos(parametros);
    }

    public void listaDepartamentos() throws Exception {
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("estadoLogico", true);
        listaDepartamento = departamentoServicio.listarDepartamentosTodos(parametros);
    }

    public void listarCargoPorDepartamento() throws Exception {
        System.out.println("Depar:" + departamentoActual.getIdDepartamento());
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("codDepartamento.idDepartamento", departamentoActual.getIdDepartamento());
        parametros.put("estadoLogico", true);
        listaCargo = cargoServicio.listarCargoPorDepartamento(parametros);
    }

    public void cargarEmpleadoNivelSuperior() {
        try {
            departamentoSupActual = new Departamento();
            empleadoSupActual = new Empleado();
            listaDepartamentos();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void listarEmpleadoPorDepartamento() {
        try {
            Map<String, Object> parametros = new HashMap<String, Object>();
            parametros.put("codDepartamento.idDepartamento", departamentoSupActual.getIdDepartamento());
            parametros.put("estadoLogico", true);
            listaEmpleadoSup = empleadoServicio.listarEmpleadoPorDepartamento(parametros);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void aceptarEmpleadoNivelSuperior() {
        try {
            Empleado objEmpleadoAux = new Empleado();
            objEmpleadoAux = empleadoServicio.buscarPorIdEmpleado(empleadoSupActual.getIdEmpleado());
            nomEmpleadoSuperior = objEmpleadoAux.getEmpNombre() + " " + objEmpleadoAux.getEmpApellido();
            empleadoActual.setCodEmpleadoPadre(objEmpleadoAux);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void verificaEstructuraMail() {
        try {
            if (!empleadoActual.getEmpCorreoElectronico().equals("") && empleadoActual.getEmpCorreoElectronico().trim().length() != 0) {
                Pattern pattern = Pattern
                        .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
                // El email a validar
                String email = empleadoActual.getEmpCorreoElectronico();
                Matcher mather = pattern.matcher(email);
                if (mather.find() == true) {
                } else {
                    addWarningMessage("El email ingresado es inválido.");
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public boolean controlaMismoDepartamento() {
        boolean mismoDepartamento = true;
        try {
            if (!nomEmpleadoSuperior.equals("")) {
                   Empleado objEmpleadoAux = new Empleado();
                    objEmpleadoAux = empleadoServicio.buscarPorIdEmpleado(empleadoSupActual.getIdEmpleado());
                if (departamentoActual != null && objEmpleadoAux.getCodDepartamento() != null) {
                    if (departamentoActual.getIdDepartamento() == objEmpleadoAux.getCodDepartamento().getIdDepartamento()) {
                        mismoDepartamento = true;
                    } else {
                        mismoDepartamento = false;
                        addErrorMessage("EL empleado y su superior deben pertenecer al mismo departamento");
                    }
                }
            } else {
                mismoDepartamento = true;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
        return mismoDepartamento;
    }

    public boolean controlaJerarquiaEmpleado() {
        boolean jerarquiaCorrecta = true;
        System.out.println("cargoActual:" + cargoActual.getIdCargo());
        System.out.println("cargoActual nivel:" + cargoActual.getCarNivel());

        try {
            if (!nomEmpleadoSuperior.equals("")) {
                 Empleado objEmpleadoAux = new Empleado();
                    objEmpleadoAux = empleadoServicio.buscarPorIdEmpleado(empleadoSupActual.getIdEmpleado());
                if (cargoActual != null && empleadoSupActual != null) {                   
                    Cargo objCargoAux = new Cargo();
                    objCargoAux = cargoServicio.buscarPorIdCargo(cargoActual.getIdCargo());
                    if (objCargoAux.getCarNivel() > objEmpleadoAux.getCodCargo().getCarNivel()) {
                        jerarquiaCorrecta = true;
                    } else {
                        jerarquiaCorrecta = false;
                        addErrorMessage("El empleado a quien reporta debe tener una jerarquia superior");
                    }
                }
            } else {
                jerarquiaCorrecta = true;
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
        return jerarquiaCorrecta;
    }

    public void onDateSelect(SelectEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(event.getObject())));
    }

    public Empleado getEmpleadoActual() {
        return empleadoActual;
    }

    public void setEmpleadoActual(Empleado empleadoActual) {
        this.empleadoActual = empleadoActual;
    }

    public List<Empleado> getListaEmpleado() {
        return listaEmpleado;
    }

    //Get y Set
    public void setListaEmpleado(List<Empleado> listaEmpleado) {
        this.listaEmpleado = listaEmpleado;
    }

    public boolean isEditaEmpleado() {
        return editaEmpleado;
    }

    public void setEditaEmpleado(boolean editaEmpleado) {
        this.editaEmpleado = editaEmpleado;
    }

    public Departamento getDepartamentoActual() {
        return departamentoActual;
    }

    public void setDepartamentoActual(Departamento departamentoActual) {
        this.departamentoActual = departamentoActual;
    }

    public List<Departamento> getListaDepartamento() {
        return listaDepartamento;
    }

    public void setListaDepartamento(List<Departamento> listaDepartamento) {
        this.listaDepartamento = listaDepartamento;
    }

    public Cargo getCargoActual() {
        return cargoActual;
    }

    public void setCargoActual(Cargo cargoActual) {
        this.cargoActual = cargoActual;
    }

    public List<Cargo> getListaCargo() {
        return listaCargo;
    }

    public void setListaCargo(List<Cargo> listaCargo) {
        this.listaCargo = listaCargo;
    }

    public Empleado getEmpleadoSupActual() {
        return empleadoSupActual;
    }

    public void setEmpleadoSupActual(Empleado empleadoSupActual) {
        this.empleadoSupActual = empleadoSupActual;
    }

    public Departamento getDepartamentoSupActual() {
        return departamentoSupActual;
    }

    public void setDepartamentoSupActual(Departamento departamentoSupActual) {
        this.departamentoSupActual = departamentoSupActual;
    }

    public List<Empleado> getListaEmpleadoSup() {
        return listaEmpleadoSup;
    }

    public void setListaEmpleadoSup(List<Empleado> listaEmpleadoSup) {
        this.listaEmpleadoSup = listaEmpleadoSup;
    }

    public String getNomEmpleadoSuperior() {
        return nomEmpleadoSuperior;
    }

    public void setNomEmpleadoSuperior(String nomEmpleadoSuperior) {
        this.nomEmpleadoSuperior = nomEmpleadoSuperior;
    }

    public boolean isEditaSueldo() {
        return editaSueldo;
    }

    public void setEditaSueldo(boolean editaSueldo) {
        this.editaSueldo = editaSueldo;
    }

    public EmpleadoSalarioHistorico getEmpSalHistoricoActual() {
        return empSalHistoricoActual;
    }

    public void setEmpSalHistoricoActual(EmpleadoSalarioHistorico empSalHistoricoActual) {
        this.empSalHistoricoActual = empSalHistoricoActual;
    }

    public String getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(String usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

}
