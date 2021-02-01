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
import prbasinfo.ejb.entidades.Departamento;
import prbasinfo.ejb.facade.dao.DepartamentoFacade;

/**
 *
 * @author Admin
 */
@Stateless
@LocalBean
public class DepartamentoServicio {

    @EJB
    private DepartamentoFacade departamentoDao;

    private String ENTIDAD_DEPARTAMENTO = "Departamento";

    public List<Departamento> listarDepartamentosTodos(Map<String, Object> parametros) throws Exception {
        return departamentoDao.listarPorCampos(ENTIDAD_DEPARTAMENTO, parametros, "idDepartamento");
    }

    public Departamento buscarPorIdDepartamento(int idDepartamento) throws Exception {
        return departamentoDao.buscarPorCampo(ENTIDAD_DEPARTAMENTO, "idDepartamento", idDepartamento);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
