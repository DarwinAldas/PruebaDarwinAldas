/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prbasinfo.jsf.util;

import java.sql.Connection;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 *
 * @author dvaldas
 */
public class UtilitariosCod {

    private static final Logger LOGGER = Logger.getLogger(UtilitariosCod.class.getName());

    public UtilitariosCod() {
    }

     

    /**
     * Obtener data source name
     *
     * @return
     */
    public Connection getConexion() {
        try {
            Connection con = dataSource().getConnection();
            return con;
        } catch (Exception e) {
            return null;
        }
    }

    private DataSource dataSource() {
        try {
            DataSource ds;
            InitialContext ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("java:/asinfoDS");
            return ds;
        } catch (Exception e) {
            return null;
        }
    }

}
