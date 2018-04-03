/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.edu.udb.catedra;
import java.sql.*;
import java.util.*;
import javax.swing.JComboBox;

/**
 *
 * @author luigi
 */
public class Conexion {
    private Connection conexion =null;
    private Statement s =null;
    private PreparedStatement st = null;
    private ResultSet rs=null;
    private String query="";
    
    //Contructor
    public Conexion() throws SQLException{
        try
        {
            //obtenemos el driver de para mysql
            Class.forName("com.mysql.jdbc.Driver");
            // Se obtiene una conexión con la base de datos. 2
            conexion = DriverManager.getConnection (
            "jdbc:mysql://localhost/sibylBiblioteca","root", "");
            // Permite ejecutar sentencias SQL sin parámetros
            s = conexion.createStatement();
        }
        catch (ClassNotFoundException e1) {
            //Error si no puedo leer el driver de MySQL
            System.out.println("ERROR:No encuentro el driver de la BD: " +e1.getMessage());
        }
    }
    //Metodo que permite obtener los valores del resulset
    public ResultSet getRs() {
        return rs;
    }
    //Metodo que permite fijar la tabla resultado de la pregunta
    //SQL realizada
    public void setRs(String consulta) {
        try {
            this.rs = s.executeQuery(consulta);
        } catch (SQLException e2) {
            System.out.println("ERROR:Fallo en SQL: "+e2.getMessage()); 
        }
    }
    //Metodo que recibe un sql como parametro que sea un update,insert.delete
    public void setQuery(String query) throws SQLException {
        this.s.executeUpdate(query);
    }
    //Metodo que cierra la conexion
    public void cerrarConexion() throws SQLException{
        conexion.close();
    }
    
    public void insertar(String sql, ArrayList arreglo) throws SQLException{
        try {
            st = conexion.prepareStatement(sql);
            for (int i = 0; i < arreglo.size(); i++) {
                if (arreglo.get(i) instanceof java.lang.String) {
                    st.setString((i + 1), arreglo.get(i).toString());
                } else {
                    st.setInt((i + 1), Integer.parseInt(arreglo.get(i).toString()));
                }
            }
            st.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
    }
    
    public void llenarCombo(JComboBox comboBox) throws SQLException{
        try {
            comboBox.removeAllItems();
            rs = getRs();
            while (rs.next()) {
                comboBox.addItem(new ItemComboBox(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException ex) {
        }
    }
    
    public int getValue(JComboBox comboBox){
        Object item = comboBox.getSelectedItem();
        int value = ((ItemComboBox)item).getId();
        return value;
    }
    
    class ItemComboBox
    {
        private final int id;
        private final String descripcion;
 
        public ItemComboBox(int id, String descripcion)
        {
            this.id = id;
            this.descripcion = descripcion;
        }
 
        public int getId(){ return id; }
 
        public String getDescripcion(){ return descripcion; }
 
        public String toString(){ return descripcion; }
    }
    
}
