
package Modelo;

public class Productos {
    private int id;
    private String codigo;
    private String nombre;
    private double stock;
    private double precio_unitario;
    private double precio_costo;
    private String ganancia;
    
    public Productos(){
        
    }

    public Productos(int id, String codigo, String nombre, double stock, double precio_unitario, double precio_costo, String ganancia) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.stock = stock;
        this.precio_unitario = precio_unitario;
        this.precio_costo = precio_costo;
        this.ganancia = ganancia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public double getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(double precio_unitario) {
        this.precio_unitario = precio_unitario;
    }
    
    public double getPrecio_costo() {
        return precio_costo;
    }

    public void setPrecio_costo(double precio_costo) {
        this.precio_costo = precio_costo;
    }
    
    public String getGanancia() {
        return ganancia;
    }

    public void setGanancia(String ganancia) {
        this.ganancia = ganancia;
    }

    public int length() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
