package Modelo;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductosDAO {

    Connection con;
    Conexion cn = new Conexion();
    PreparedStatement ps;
    ResultSet rs;

    public boolean RegistrarProductos(Productos pro) {
        String sql = "INSERT INTO productos (codigo, nombre, stock, precio_unitario, precio_costo, ganancia) VALUES (?,?,?,?,?,?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, pro.getCodigo());
            ps.setString(2, pro.getNombre());
            ps.setDouble(3, pro.getStock());
            ps.setDouble(4, pro.getPrecio_unitario());
            ps.setDouble(5, pro.getPrecio_costo());
            ps.setString(6, pro.getGanancia());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public List ListarProductos() {
        List<Productos> ListaPro = new ArrayList();
        String sql = "SELECT * FROM productos";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Productos pro = new Productos();
                pro.setId(rs.getInt("id"));
                pro.setCodigo(rs.getString("codigo"));
                pro.setNombre(rs.getString("nombre"));
                pro.setStock(rs.getDouble("stock"));
                pro.setPrecio_unitario(rs.getDouble("precio_unitario"));
                pro.setPrecio_costo(rs.getDouble("precio_costo"));
                pro.setGanancia(rs.getString("ganancia"));
                ListaPro.add(pro);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return ListaPro;
    }

    public boolean EliminarProductos(int id) {
        String sql = "DELETE FROM productos WHERE id = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }

    public boolean ModificarProducto(Productos pro) {
        String sql = "UPDATE productos SET codigo=?, nombre=?, stock=?, precio_unitario=?, precio_costo=?, ganancia=? WHERE id=?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, pro.getCodigo());
            ps.setString(2, pro.getNombre());
            ps.setDouble(3, pro.getStock());
            ps.setDouble(4, pro.getPrecio_unitario());
            ps.setDouble(5, pro.getPrecio_costo());
            ps.setString(6, pro.getGanancia());
            ps.setInt(7, pro.getId());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }

    public Config BuscarDatos() {
        Config conf = new Config();
        String sql = "SELECT * FROM config = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                conf.setId(rs.getInt("id"));
                conf.setRuc(rs.getInt("ruc"));
                conf.setNombre(rs.getString("nombre"));
                conf.setTelefono(rs.getInt("telefono"));
                conf.setDireccion(rs.getString("direccion"));
                conf.setRazon(rs.getString("razon"));
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return conf;
    }

    public Productos BuscarPro(String cod) {
        Productos producto = new Productos();
        String sql = "SELECT * FROM productos WHERE codigo = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, cod);
            rs = ps.executeQuery();
            if (rs.next()) {
                producto.setNombre(rs.getString("nombre"));
                producto.setPrecio_unitario(rs.getDouble("precio_unitario"));
                producto.setStock(rs.getInt("stock"));
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return producto;
    }

    public Productos BuscarProPorNombre(String nombreProducto) {
        Productos producto = new Productos();
        String sql = "SELECT * FROM productos WHERE nombre = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, nombreProducto);
            rs = ps.executeQuery();
            if (rs.next()) {
                producto.setCodigo(rs.getString("codigo"));
                producto.setNombre(rs.getString("nombre"));
                producto.setPrecio_unitario(rs.getDouble("precio_unitario"));
                producto.setStock(rs.getInt("stock"));
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return producto;
    }

    public boolean ModificarDatos(Config conf) {
        String sql = "UPDATE config SET ruc=?, nombre=?, telefono=?, direccion=?, razon=? WHERE id=?";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, conf.getRuc());
            ps.setString(2, conf.getNombre());
            ps.setInt(3, conf.getTelefono());
            ps.setString(4, conf.getDireccion());
            ps.setString(5, conf.getRazon());
            ps.setInt(6, conf.getId());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }
    
    public ArrayList<String> obtenerNombresProductos() {
        ArrayList<String> nombresProductos = new ArrayList<>();
        String sql = "SELECT nombre FROM productos"; // Suponiendo que "productos" es el nombre de tu tabla de productos en la base de datos.

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String nombreProducto = rs.getString("nombre");
                nombresProductos.add(nombreProducto);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }

        return nombresProductos;
    }
}
