/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prbasinfo.ejb.servicios;

import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import prbasinfo.ejb.entidades.Empleado;
import prbasinfo.ejb.facade.dao.EmpleadoFacade;

/**
 *
 * @author Admin
 */
@Stateless
@LocalBean
public class EmpleadoServicio {

    @EJB
    private EmpleadoFacade empleadoDao;
    private String ENTIDAD_EMPLEADO = "Empleado";

    public String crearEmpleado(Empleado codEmpleado) throws Exception {
        empleadoDao.crear(codEmpleado);
        return "Se ha creado el empleado" + codEmpleado;
    }

    public String editarEmpleado(Empleado codEmpleado) throws Exception {
        empleadoDao.editar(codEmpleado);
        return "Se ha modificado el empleado" + codEmpleado;
    }

    public List<Empleado> listarEmpleadosTodos(Map<String, Object> parametros) throws Exception {
        return empleadoDao.listarPorCampos(ENTIDAD_EMPLEADO, parametros, "idEmpleado");
    }

    public List<Empleado> listarEmpleadoPorDepartamento(Map<String, Object> parametros) throws Exception { //Se agrega estado logico DA02042020
        return empleadoDao.listarPorCampos(ENTIDAD_EMPLEADO, parametros, "idEmpleado");
    }

    public Empleado buscarPorIdEmpleado(int codEmpleado) throws Exception {
        return empleadoDao.buscarPorCampo(ENTIDAD_EMPLEADO, "idEmpleado", codEmpleado);
    }

    public boolean existeIdentificadorEmpleado(String identificador) throws Exception {
        return empleadoDao.existePorCampo(ENTIDAD_EMPLEADO, "empIdentificador", identificador);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
