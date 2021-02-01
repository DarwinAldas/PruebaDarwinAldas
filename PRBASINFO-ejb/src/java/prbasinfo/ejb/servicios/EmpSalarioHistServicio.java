/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prbasinfo.ejb.servicios;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import prbasinfo.ejb.entidades.EmpleadoSalarioHistorico;
import prbasinfo.ejb.facade.dao.EmpSalHistoricoFacade;

/**
 *
 * @author Admin
 */
@Stateless
@LocalBean
public class EmpSalarioHistServicio {

    @EJB
    private EmpSalHistoricoFacade empSalHistoricoDao;
    private String ENTIDAD_EMPSALHISTORICO = "Empleado";

    public String crearEmpSalHistorico(EmpleadoSalarioHistorico codEmpSalHis) throws Exception {
        empSalHistoricoDao.crear(codEmpSalHis);
        return "Se ha creado el registro" + codEmpSalHis;
    }

    public String editarEmpSalHistorico(EmpleadoSalarioHistorico codEmpSalHis) throws Exception {
        empSalHistoricoDao.editar(codEmpSalHis);
        return "Se ha modificado el registro" + codEmpSalHis;
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
