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
import prbasinfo.ejb.entidades.Cargo;
import prbasinfo.ejb.facade.dao.CargoFacade;

/**
 *
 * @author Admin
 */
@Stateless
@LocalBean
public class CargoServicio {

    @EJB
    private CargoFacade cargoDao;
    private String ENTIDAD_CARGO = "Cargo";

    public List<Cargo> listarCargoPorDepartamento(Map<String, Object> parametros) throws Exception { //Se agrega estado logico DA02042020
        return cargoDao.listarPorCampos(ENTIDAD_CARGO, parametros, "idCargo");
    }

    public Cargo buscarPorIdCargo(int idCargo) throws Exception {
        return cargoDao.buscarPorCampo(ENTIDAD_CARGO, "idCargo", idCargo);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
