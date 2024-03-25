package Vista;

import Modelo.AutocompletarComboBoxModel;
import Modelo.Cliente;
import Modelo.ClienteDAO;
import Modelo.Config;
import Modelo.Detalle;
import Modelo.Eventos;
import Modelo.LoginDAO;
import Modelo.Productos;
import Modelo.ProductosDAO;
import Modelo.Proveedor;
import Modelo.ProveedorDAO;
import Modelo.Venta;
import Modelo.VentaDAO;
import Modelo.login;
import Reportes.Grafico;
import Reportes.Excel;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author coqui
 */
public class Sistema extends javax.swing.JFrame {

    Date fechaVenta = new Date();
    String fechaActual = new SimpleDateFormat("dd/MM/yyyy").format(fechaVenta);
    Cliente cl = new Cliente();
    ClienteDAO client = new ClienteDAO();
    Proveedor pr = new Proveedor();
    ProveedorDAO prDAO = new ProveedorDAO();
    Productos pro = new Productos();
    ProductosDAO proDAO = new ProductosDAO();
    Venta v = new Venta();
    VentaDAO vDAO = new VentaDAO();
    Detalle Dv = new Detalle();
    Config conf = new Config();
    Eventos event = new Eventos();
    login lg = new login();
    LoginDAO login = new LoginDAO();
    DefaultTableModel modelo = new DefaultTableModel();
    DefaultTableModel tmp = new DefaultTableModel();
    int item;
    double Totalpagar = 0.00;
    double Totalventas = 0.00;

    public Sistema() {
        initComponents();
        this.setLocationRelativeTo(null);
        txtIdCliente.setVisible(false);
        txtIdVenta.setVisible(false);
        txtIdPro.setVisible(false);
        txtId_PRO.setVisible(false);
        txtIdProveedor.setVisible(false);
        txtIdConfig.setVisible(false);
        txtDirecVenta.setVisible(false);
        txtTelVenta.setVisible(false);
        txtRazonVenta.setVisible(false);
        ListarConfig();

        ArrayList<String> nombresProductos = proDAO.obtenerNombresProductos();

        // Agregar los nombres de los productos al JComboBox
        for (String nombreProducto : nombresProductos) {
            cbxDescripcionVenta.addItem(nombreProducto);
        }

        // Habilitar el autocompletado en el JComboBox
        AutoCompleteDecorator.decorate(cbxDescripcionVenta);

        // Agregar el ActionListener al JComboBox
        cbxDescripcionVenta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String seleccion = cbxDescripcionVenta.getSelectedItem().toString();
                Productos producto = proDAO.BuscarProPorNombre(seleccion);

                if (producto == null) {
                    producto = proDAO.BuscarPro(seleccion);
                }

                if (producto != null) {
                    txtCodigoVenta.setText(producto.getCodigo());
                    txtPrecioVenta.setText(String.valueOf(producto.getPrecio_unitario()));
                    txtPrecioVenta.setEditable(true);
                    txtStockDisponible.setText(String.valueOf(producto.getStock()));
                    txtCantidadVenta.requestFocus();
                }
            }
        });

        // Agregar el KeyListener al JComboBox para escuchar la tecla "Tab"
        cbxDescripcionVenta.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_TAB) {
                    String seleccion = cbxDescripcionVenta.getSelectedItem().toString();
                    Productos producto = proDAO.BuscarProPorNombre(seleccion);

                    if (producto == null) {
                        producto = proDAO.BuscarPro(seleccion);
                    }

                    if (producto != null) {
                        txtCodigoVenta.setText(producto.getCodigo());
                        txtPrecioVenta.setText(String.valueOf(producto.getPrecio_unitario()));
                        txtPrecioVenta.setEditable(true);
                        txtStockDisponible.setText(String.valueOf(producto.getStock()));
                        txtCantidadVenta.requestFocus();
                    }
                }
            }
        });

    }

    public Sistema(Login priv) {
        initComponents();

    }

    public void ListarCliente() {
        List<Cliente> ListarCl = client.ListarCliente();
        modelo = (DefaultTableModel) TableCliente.getModel();
        Object[] ob = new Object[6];
        for (int i = 0; i < ListarCl.size(); i++) {
            ob[0] = ListarCl.get(i).getId();
            ob[1] = ListarCl.get(i).getDni();
            ob[2] = ListarCl.get(i).getNombre();
            ob[3] = ListarCl.get(i).getTelefono();
            ob[4] = ListarCl.get(i).getDireccion();
            ob[5] = ListarCl.get(i).getRazon();
            modelo.addRow(ob);
        }
        TableCliente.setModel(modelo);
    }

    public void ListarProveedor() {
        List<Proveedor> ListarPr = prDAO.ListarProveedor();
        modelo = (DefaultTableModel) TableProveedor.getModel();
        Object[] ob = new Object[6];
        for (int i = 0; i < ListarPr.size(); i++) {
            ob[0] = ListarPr.get(i).getId();
            ob[1] = ListarPr.get(i).getRuc();
            ob[2] = ListarPr.get(i).getNombre();
            ob[3] = ListarPr.get(i).getTelefono();
            ob[4] = ListarPr.get(i).getDireccion();
            ob[5] = ListarPr.get(i).getRazon();
            modelo.addRow(ob);
        }
        TableProveedor.setModel(modelo);
    }

    public void ListarProductos() {
        List<Productos> ListarPro = proDAO.ListarProductos();
        modelo = (DefaultTableModel) TableProductos.getModel();
        Object[] ob = new Object[7];
        for (int i = 0; i < ListarPro.size(); i++) {
            ob[0] = ListarPro.get(i).getId();
            ob[1] = ListarPro.get(i).getCodigo();
            ob[2] = ListarPro.get(i).getNombre();
            ob[3] = ListarPro.get(i).getStock();
            ob[4] = ListarPro.get(i).getPrecio_unitario();
            ob[5] = ListarPro.get(i).getPrecio_costo();
            ob[6] = ListarPro.get(i).getGanancia();
            modelo.addRow(ob);
        }
        TableProductos.setModel(modelo);
    }

    public void ListarConfig() {
        conf = proDAO.BuscarDatos();
        txtIdConfig.setText("" + conf.getId());
        txtRucConfig.setText("" + conf.getRuc());
        txtNombreConfig.setText("" + conf.getNombre());
        txtTelefonoConfig.setText("" + conf.getTelefono());
        txtDirecConfig.setText("" + conf.getDireccion());
        txtRazonConfig.setText("" + conf.getRazon());
    }

    public void ListarVentas() {
        List<Venta> ListarVenta = vDAO.ListarVentas();
        modelo = (DefaultTableModel) TableVentas.getModel();
        Object[] ob = new Object[4];
        for (int i = 0; i < ListarVenta.size(); i++) {
            ob[0] = ListarVenta.get(i).getId();
            ob[1] = ListarVenta.get(i).getCliente();
            ob[2] = ListarVenta.get(i).getVendedor();
            ob[3] = ListarVenta.get(i).getTotal();
            modelo.addRow(ob);
        }
        TableVentas.setModel(modelo);
    }

    public void LimpiarTable() {
        for (int i = 0; i < modelo.getRowCount(); i++) {
            modelo.removeRow(i);
            i = i - 1;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnNuevaVenta = new javax.swing.JButton();
        btnClientes = new javax.swing.JButton();
        btnProveedor = new javax.swing.JButton();
        btnProductos = new javax.swing.JButton();
        btnVentas = new javax.swing.JButton();
        btnConfig = new javax.swing.JButton();
        LabelVendedor = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btnEliminarVenta = new javax.swing.JButton();
        txtCodigoVenta = new javax.swing.JTextField();
        txtCantidadVenta = new javax.swing.JTextField();
        txtPrecioVenta = new javax.swing.JTextField();
        txtStockDisponible = new javax.swing.JTextField();
        LabelTotal = new javax.swing.JLabel();
        txtDirecVenta = new javax.swing.JTextField();
        txtRazonVenta = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        btnCambio = new javax.swing.JButton();
        txtNombreCLVenta = new javax.swing.JTextField();
        txtTelVenta = new javax.swing.JTextField();
        txtDNIVenta = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtIdPro = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableVenta = new javax.swing.JTable();
        btnGraficar = new javax.swing.JButton();
        MiDate = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        btnGenerarVenta = new javax.swing.JButton();
        jLabel35 = new javax.swing.JLabel();
        txtPrecioCalculado = new javax.swing.JTextField();
        btnListo = new javax.swing.JButton();
        cbxDescripcionVenta = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtDniCliente = new javax.swing.JTextField();
        txtNombreCliente = new javax.swing.JTextField();
        txtTelefonoCliente = new javax.swing.JTextField();
        txtDireccionCliente = new javax.swing.JTextField();
        txtRazonCliente = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        TableCliente = new javax.swing.JTable();
        btnGuardarCL = new javax.swing.JButton();
        btnEditarCL = new javax.swing.JButton();
        btnEliminarCL = new javax.swing.JButton();
        btnNuevoCliente = new javax.swing.JButton();
        txtIdCliente = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txtRucProveedor = new javax.swing.JTextField();
        txtNombreProveedor = new javax.swing.JTextField();
        txtTelefonoProveedor = new javax.swing.JTextField();
        txtDireccionProveedor = new javax.swing.JTextField();
        txtRazonProveedor = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        TableProveedor = new javax.swing.JTable();
        btnGuardarPR = new javax.swing.JButton();
        btnEditarPR = new javax.swing.JButton();
        btnEliminarPR = new javax.swing.JButton();
        btnNuevoPR = new javax.swing.JButton();
        txtIdProveedor = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        txtCodProducto = new javax.swing.JTextField();
        txtDescripcionProducto = new javax.swing.JTextField();
        txtCantidadProducto = new javax.swing.JTextField();
        txtPrecioProducto = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        TableProductos = new javax.swing.JTable();
        btnGuardarPRO = new javax.swing.JButton();
        btnEditarPRO = new javax.swing.JButton();
        btnEliminarPRO = new javax.swing.JButton();
        btnNuevoPRO = new javax.swing.JButton();
        btnExcelPRO = new javax.swing.JButton();
        txtId_PRO = new javax.swing.JTextField();
        txtPrecioCostoProducto = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        txtGananciaProducto = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        TableVentas = new javax.swing.JTable();
        btnPDFVentas = new javax.swing.JButton();
        txtIdVenta = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        LabelTotalVentas = new javax.swing.JLabel();
        btnAnalizar = new javax.swing.JButton();
        lblFechaInicio = new javax.swing.JLabel();
        lblFechaFinal = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        txtRucConfig = new javax.swing.JTextField();
        txtNombreConfig = new javax.swing.JTextField();
        txtTelefonoConfig = new javax.swing.JTextField();
        txtDirecConfig = new javax.swing.JTextField();
        txtRazonConfig = new javax.swing.JTextField();
        btnActualizarConfig = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        txtIdConfig = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(153, 255, 255));

        btnNuevaVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icons8-agregar-a-carrito-de-compras-64.png"))); // NOI18N
        btnNuevaVenta.setText("Nueva Venta");
        btnNuevaVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevaVentaActionPerformed(evt);
            }
        });

        btnClientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icons8-clientes-48.png"))); // NOI18N
        btnClientes.setText("Clientes");
        btnClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClientesActionPerformed(evt);
            }
        });

        btnProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icons8-proveedor-48.png"))); // NOI18N
        btnProveedor.setText("Proveedor");
        btnProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProveedorActionPerformed(evt);
            }
        });

        btnProductos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icons8-producto-48.png"))); // NOI18N
        btnProductos.setText("Productos");
        btnProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductosActionPerformed(evt);
            }
        });

        btnVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icons8-ventas-48.png"))); // NOI18N
        btnVentas.setText("Ventas");
        btnVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVentasActionPerformed(evt);
            }
        });

        btnConfig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icons8-ajustes-48.png"))); // NOI18N
        btnConfig.setText("Configuración");
        btnConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfigActionPerformed(evt);
            }
        });

        LabelVendedor.setFont(new java.awt.Font("Montserrat SemiBold", 1, 14)); // NOI18N
        LabelVendedor.setText("KE LIMPIO S.R.L.");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/logoAct.jpg"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnVentas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnProductos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnNuevaVenta, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
                            .addComponent(btnClientes, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnProveedor, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 1, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnConfig, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(LabelVendedor)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(77, 77, 77)
                .addComponent(LabelVendedor)
                .addGap(45, 45, 45)
                .addComponent(btnNuevaVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnConfig, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(195, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -10, 200, 830));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/fondo_limpieza.jpg"))); // NOI18N
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(-420, 0, 1790, 200));

        jPanel2.setPreferredSize(new java.awt.Dimension(1200, 611));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Codigo");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Descripcion");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Cantidad");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Precio Unitario");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 51, 51));
        jLabel7.setText("Stock Disponible ");

        btnEliminarVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icons8-eliminar-48.png"))); // NOI18N
        btnEliminarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarVentaActionPerformed(evt);
            }
        });

        txtCodigoVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoVentaActionPerformed(evt);
            }
        });
        txtCodigoVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodigoVentaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCodigoVentaKeyTyped(evt);
            }
        });

        txtCantidadVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCantidadVentaActionPerformed(evt);
            }
        });
        txtCantidadVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCantidadVentaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCantidadVentaKeyTyped(evt);
            }
        });

        txtPrecioVenta.setEditable(false);
        txtPrecioVenta.setBackground(new java.awt.Color(255, 255, 255));
        txtPrecioVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPrecioVentaMouseClicked(evt);
            }
        });
        txtPrecioVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrecioVentaActionPerformed(evt);
            }
        });
        txtPrecioVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPrecioVentaKeyPressed(evt);
            }
        });

        LabelTotal.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        LabelTotal.setText("-------");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel10.setText("Total a Pagar:");

        btnCambio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icons8-dinero-48.png"))); // NOI18N
        btnCambio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCambioActionPerformed(evt);
            }
        });

        txtDNIVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDNIVentaKeyPressed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("DNI");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Nombre");

        TableVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CODIGO", "DESCRIPCION", "CANTIDAD", "PRECIO", "TOTAL"
            }
        ));
        jScrollPane1.setViewportView(TableVenta);
        if (TableVenta.getColumnModel().getColumnCount() > 0) {
            TableVenta.getColumnModel().getColumn(0).setPreferredWidth(30);
            TableVenta.getColumnModel().getColumn(1).setPreferredWidth(100);
            TableVenta.getColumnModel().getColumn(2).setPreferredWidth(30);
            TableVenta.getColumnModel().getColumn(3).setPreferredWidth(30);
            TableVenta.getColumnModel().getColumn(4).setPreferredWidth(40);
        }

        btnGraficar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icons8-gráfico-de-anillos-20.png"))); // NOI18N
        btnGraficar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGraficarActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Seleccionar");

        btnGenerarVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icons8-imprimir-48.png"))); // NOI18N
        btnGenerarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarVentaActionPerformed(evt);
            }
        });

        jLabel35.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel35.setText("Precio Calculado");

        txtPrecioCalculado.setEditable(false);
        txtPrecioCalculado.setBackground(new java.awt.Color(255, 255, 255));
        txtPrecioCalculado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrecioCalculadoActionPerformed(evt);
            }
        });

        btnListo.setBackground(new java.awt.Color(255, 204, 51));
        btnListo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnListo.setText("LISTO");
        btnListo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnListoActionPerformed(evt);
            }
        });

        cbxDescripcionVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbxDescripcionVentaKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtCodigoVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(cbxDescripcionVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(txtCantidadVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtPrecioVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtPrecioCalculado, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel35))
                                .addGap(32, 32, 32)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(txtStockDisponible, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(45, 45, 45)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(btnListo)
                                        .addGap(79, 79, 79)
                                        .addComponent(btnEliminarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 30, Short.MAX_VALUE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(btnGraficar)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(MiDate, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel1))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtIdPro, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtDNIVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addGap(102, 102, 102)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel9)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(txtNombreCLVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(25, 25, 25)
                                                .addComponent(txtTelVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtDirecVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtRazonVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnCambio)
                                .addGap(18, 18, 18)
                                .addComponent(btnGenerarVenta)
                                .addGap(55, 55, 55)
                                .addComponent(jLabel10)
                                .addGap(18, 18, 18)
                                .addComponent(LabelTotal)
                                .addGap(80, 80, 80))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1083, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(73, 73, 73))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnGenerarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtPrecioVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtPrecioCalculado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(17, 17, 17)
                                                .addComponent(btnGraficar))
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(MiDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(15, 15, 15)
                                        .addComponent(txtIdPro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnEliminarVenta, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnListo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel3)
                                            .addComponent(jLabel4)
                                            .addComponent(jLabel7))
                                        .addGap(18, 18, Short.MAX_VALUE)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(txtCodigoVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtCantidadVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cbxDescripcionVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel5)
                                                .addComponent(jLabel6)
                                                .addComponent(jLabel35))
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(37, 37, 37)
                                                .addComponent(txtStockDisponible, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(0, 0, Short.MAX_VALUE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtDNIVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNombreCLVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtTelVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtDirecVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtRazonVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel10)
                                    .addComponent(LabelTotal)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(btnCambio, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(3319, 3319, 3319))
        );

        jTabbedPane1.addTab("tab1", jPanel2);

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setText("DNI:");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setText("Nombre:");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setText("Teléfono:");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setText("Dirección:");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setText("Razón Social:");

        TableCliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "DNI", "NOMBRE", "TELEFONO", "DIRECCIÓN", "RAZON SOCIAL"
            }
        ));
        TableCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableClienteMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(TableCliente);
        if (TableCliente.getColumnModel().getColumnCount() > 0) {
            TableCliente.getColumnModel().getColumn(0).setPreferredWidth(20);
            TableCliente.getColumnModel().getColumn(1).setPreferredWidth(50);
            TableCliente.getColumnModel().getColumn(2).setPreferredWidth(100);
            TableCliente.getColumnModel().getColumn(3).setPreferredWidth(50);
            TableCliente.getColumnModel().getColumn(4).setPreferredWidth(80);
            TableCliente.getColumnModel().getColumn(5).setPreferredWidth(80);
        }

        btnGuardarCL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icons8-guardar-48.png"))); // NOI18N
        btnGuardarCL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarCLActionPerformed(evt);
            }
        });

        btnEditarCL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icons8-actualizar-48.png"))); // NOI18N
        btnEditarCL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarCLActionPerformed(evt);
            }
        });

        btnEliminarCL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icons8-eliminar2-48.png"))); // NOI18N
        btnEliminarCL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarCLActionPerformed(evt);
            }
        });

        btnNuevoCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icons8-añadir-usuario-tipo-de-piel-masculina-7-48.png"))); // NOI18N
        btnNuevoCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNuevoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoClienteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel12)
                            .addGap(261, 261, 261))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel13)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(35, 35, 35)
                                        .addComponent(btnGuardarCL))
                                    .addComponent(btnEliminarCL, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(47, 47, 47)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnEditarCL)
                                    .addComponent(btnNuevoCliente)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtRazonCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel15)
                                    .addComponent(jLabel14))
                                .addGap(22, 22, 22)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTelefonoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(txtDniCliente, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtDireccionCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                                        .addComponent(txtIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(48, 48, 48)))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 769, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(txtDniCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(txtNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(txtTelefonoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(txtDireccionCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(txtRazonCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnGuardarCL)
                            .addComponent(btnEditarCL))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnEliminarCL)
                            .addComponent(btnNuevoCliente)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 367, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(182, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab2", jPanel3);

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel17.setText("RUC:");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel18.setText("Nombre:");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel19.setText("Teléfono:");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel20.setText("Dirección:");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel21.setText("Razon Social:");

        TableProveedor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "RUC", "NOMBRE", "TELEFONO", "DIRECCION", "RAZON SOCIAL"
            }
        ));
        TableProveedor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableProveedorMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(TableProveedor);
        if (TableProveedor.getColumnModel().getColumnCount() > 0) {
            TableProveedor.getColumnModel().getColumn(0).setPreferredWidth(20);
            TableProveedor.getColumnModel().getColumn(1).setPreferredWidth(40);
            TableProveedor.getColumnModel().getColumn(2).setPreferredWidth(100);
            TableProveedor.getColumnModel().getColumn(3).setPreferredWidth(50);
            TableProveedor.getColumnModel().getColumn(4).setPreferredWidth(80);
            TableProveedor.getColumnModel().getColumn(5).setPreferredWidth(70);
        }

        btnGuardarPR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icons8-guardar-48.png"))); // NOI18N
        btnGuardarPR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarPRActionPerformed(evt);
            }
        });

        btnEditarPR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icons8-actualizar-48.png"))); // NOI18N
        btnEditarPR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarPRActionPerformed(evt);
            }
        });

        btnEliminarPR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icons8-eliminar2-48.png"))); // NOI18N
        btnEliminarPR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarPRActionPerformed(evt);
            }
        });

        btnNuevoPR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icons8-añadir-usuario-tipo-de-piel-masculina-7-48.png"))); // NOI18N
        btnNuevoPR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoPRActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addComponent(jLabel17)
                            .addComponent(jLabel19))
                        .addGap(52, 52, 52)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNombreProveedor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(txtRucProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtIdProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtTelefonoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addGap(32, 32, 32)
                            .addComponent(btnEliminarPR)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnNuevoPR))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                            .addGap(33, 33, 33)
                            .addComponent(btnGuardarPR)
                            .addGap(31, 31, 31)
                            .addComponent(btnEditarPR)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20)
                            .addComponent(jLabel21))
                        .addGap(33, 33, 33)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtRazonProveedor, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                            .addComponent(txtDireccionProveedor, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(9, 9, 9)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 756, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(96, 96, 96))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(txtRucProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtIdProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addComponent(txtNombreProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(txtTelefonoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(txtDireccionProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(txtRazonProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnGuardarPR)
                            .addComponent(btnEditarPR))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnEliminarPR)
                            .addComponent(btnNuevoPR)))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(182, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab3", jPanel4);

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel22.setText("Codigo:");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel23.setText("Descripción:");

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel24.setText("Cantidad:");

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel25.setText("Precio Unitario:");

        txtCantidadProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCantidadProductoActionPerformed(evt);
            }
        });

        txtPrecioProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecioProductoKeyTyped(evt);
            }
        });

        TableProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "CODIGO", "DESCRIPCION", "STOCK", "PRECIO UNITARIO", "PRECIO COSTO", "GANANCIA"
            }
        ));
        TableProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableProductosMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(TableProductos);
        if (TableProductos.getColumnModel().getColumnCount() > 0) {
            TableProductos.getColumnModel().getColumn(0).setPreferredWidth(20);
            TableProductos.getColumnModel().getColumn(1).setPreferredWidth(50);
            TableProductos.getColumnModel().getColumn(2).setPreferredWidth(100);
            TableProductos.getColumnModel().getColumn(3).setPreferredWidth(50);
            TableProductos.getColumnModel().getColumn(4).setPreferredWidth(50);
            TableProductos.getColumnModel().getColumn(5).setPreferredWidth(50);
            TableProductos.getColumnModel().getColumn(6).setPreferredWidth(20);
        }

        btnGuardarPRO.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icons8-guardar-48.png"))); // NOI18N
        btnGuardarPRO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarPROActionPerformed(evt);
            }
        });

        btnEditarPRO.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icons8-actualizar-48.png"))); // NOI18N
        btnEditarPRO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarPROActionPerformed(evt);
            }
        });

        btnEliminarPRO.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icons8-eliminar2-48.png"))); // NOI18N
        btnEliminarPRO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarPROActionPerformed(evt);
            }
        });

        btnNuevoPRO.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icons8-agregar-archivo-48.png"))); // NOI18N

        btnExcelPRO.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icons8-microsoft-excel-2019-48.png"))); // NOI18N
        btnExcelPRO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelPROActionPerformed(evt);
            }
        });

        txtPrecioCostoProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecioCostoProductoKeyTyped(evt);
            }
        });

        jLabel33.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel33.setText("Precio Costo:");

        txtGananciaProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtGananciaProductoKeyTyped(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel34.setText("Ganancia:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel22)
                                    .addComponent(jLabel24)
                                    .addComponent(jLabel23))
                                .addGap(36, 36, 36))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel25)
                                .addGap(18, 18, 18)))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtCodProducto)
                            .addComponent(txtDescripcionProducto, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCantidadProducto, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPrecioProducto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                        .addComponent(txtId_PRO, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnGuardarPRO)
                                    .addComponent(btnNuevoPRO))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(btnEditarPRO)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnEliminarPRO))
                                    .addComponent(btnExcelPRO)))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel33)
                                        .addGap(32, 32, 32))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel34)
                                        .addGap(51, 51, 51)))
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtGananciaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtPrecioCostoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 810, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(txtCodProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(txtDescripcionProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(txtCantidadProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(79, 79, 79))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(txtId_PRO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addComponent(jLabel23)
                        .addGap(31, 31, 31)
                        .addComponent(jLabel24)
                        .addGap(28, 28, 28)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(txtPrecioProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(txtPrecioCostoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(txtGananciaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGuardarPRO)
                    .addComponent(btnEditarPRO)
                    .addComponent(btnEliminarPRO))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnExcelPRO)
                    .addComponent(btnNuevoPRO))
                .addGap(134, 134, 134))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab4", jPanel5);

        TableVentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "CLIENTE", "VENDEDOR", "TOTAL"
            }
        ));
        TableVentas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableVentasMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                TableVentasMouseEntered(evt);
            }
        });
        jScrollPane5.setViewportView(TableVentas);
        if (TableVentas.getColumnModel().getColumnCount() > 0) {
            TableVentas.getColumnModel().getColumn(0).setPreferredWidth(20);
            TableVentas.getColumnModel().getColumn(1).setPreferredWidth(60);
            TableVentas.getColumnModel().getColumn(2).setPreferredWidth(60);
            TableVentas.getColumnModel().getColumn(3).setPreferredWidth(60);
        }

        btnPDFVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icons8-pdf-48.png"))); // NOI18N
        btnPDFVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPDFVentasActionPerformed(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel26.setText("Total:");

        LabelTotalVentas.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        LabelTotalVentas.setText("-----");

        btnAnalizar.setText("Analizar ");
        btnAnalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnalizarActionPerformed(evt);
            }
        });

        lblFechaInicio.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblFechaInicio.setText("-----");

        lblFechaFinal.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblFechaFinal.setText("-----");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(lblFechaInicio)
                        .addGap(33, 33, 33)
                        .addComponent(lblFechaFinal)
                        .addGap(18, 18, 18)
                        .addComponent(btnAnalizar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(LabelTotalVentas)
                        .addGap(143, 143, 143))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 1008, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(btnPDFVentas)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtIdVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(97, Short.MAX_VALUE))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnPDFVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIdVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26)
                            .addComponent(LabelTotalVentas)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAnalizar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblFechaInicio)
                            .addComponent(lblFechaFinal))))
                .addContainerGap(98, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab5", jPanel6);

        jLabel27.setText("RUC");

        jLabel28.setText("NOMBRE");

        jLabel29.setText("TELEFONO");

        jLabel30.setText("DIRECCION");

        jLabel31.setText("RAZON SOCIAL");

        btnActualizarConfig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icons8-actualizar-48.png"))); // NOI18N
        btnActualizarConfig.setText("Actualizar");
        btnActualizarConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarConfigActionPerformed(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel32.setText("DATOS DE LA EMPRESA");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jPanel7Layout.createSequentialGroup()
                                    .addComponent(txtIdConfig, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel32))
                                .addGroup(jPanel7Layout.createSequentialGroup()
                                    .addComponent(jLabel27)
                                    .addGap(250, 250, 250)
                                    .addComponent(jLabel28)
                                    .addGap(237, 237, 237)))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel30)
                                    .addComponent(txtDirecConfig, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtRucConfig, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(61, 61, 61)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel31)
                                    .addComponent(txtRazonConfig, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addComponent(txtNombreConfig, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(126, 126, 126)
                                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtTelefonoConfig, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel29)))))))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(448, 448, 448)
                        .addComponent(btnActualizarConfig, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(438, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(jLabel32)
                        .addGap(35, 35, 35))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtIdConfig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(txtRucConfig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel28)
                            .addComponent(jLabel27))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtNombreConfig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel29)
                        .addGap(18, 18, 18)
                        .addComponent(txtTelefonoConfig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(47, 47, 47)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtRazonConfig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtDirecConfig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(105, 105, 105)
                .addComponent(btnActualizarConfig)
                .addContainerGap(163, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab6", jPanel7);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 170, 1170, 630));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNuevaVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevaVentaActionPerformed
        jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_btnNuevaVentaActionPerformed

    private void txtPrecioVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrecioVentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrecioVentaActionPerformed

    private void btnGuardarCLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarCLActionPerformed
        if (!"".equals(txtDniCliente.getText()) || !"".equals(txtNombreCliente.getText()) || !"".equals(txtTelefonoCliente.getText()) || !"".equals(txtDireccionCliente.getText())) {
            cl.setDni(txtDniCliente.getText());
            cl.setNombre(txtNombreCliente.getText());
            cl.setTelefono(txtTelefonoCliente.getText());
            cl.setDireccion(txtDireccionCliente.getText());
            cl.setRazon(txtRazonCliente.getText());
            client.RegistrarCliente(cl);
            JOptionPane.showMessageDialog(null, "Cliente Registrado");
            LimpiarTable();
            LimpiarCliente();
            ListarCliente();
        } else {
            JOptionPane.showMessageDialog(null, "Los campos estan vacios");
        }
    }//GEN-LAST:event_btnGuardarCLActionPerformed

    private void btnActualizarConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarConfigActionPerformed
        if (!"".equals(txtRucConfig.getText()) || !"".equals(txtNombreConfig.getText()) || !"".equals(txtTelefonoConfig.getText()) || !"".equals(txtDirecConfig.getText()) || !"".equals(txtRazonConfig.getText())) {
            conf.setRuc(Integer.parseInt(txtRucConfig.getText()));
            conf.setNombre(txtNombreConfig.getText());
            conf.setTelefono(Integer.parseInt(txtTelefonoConfig.getText()));
            conf.setDireccion(txtDirecConfig.getText());
            conf.setRazon(txtRazonConfig.getText());
            conf.setId(Integer.parseInt(txtIdConfig.getText()));
            proDAO.ModificarDatos(conf);
            JOptionPane.showMessageDialog(null, "Datos de la empresa Actualizado");
            ListarConfig();
        } else {
            JOptionPane.showMessageDialog(null, "Los campos estan vacios");
        }
    }//GEN-LAST:event_btnActualizarConfigActionPerformed

    private void btnClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClientesActionPerformed
        LimpiarTable();
        ListarCliente();
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_btnClientesActionPerformed

    private void TableClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableClienteMouseClicked
        int fila = TableCliente.rowAtPoint(evt.getPoint());
        txtIdCliente.setText(TableCliente.getValueAt(fila, 0).toString());
        txtDniCliente.setText(TableCliente.getValueAt(fila, 1).toString());
        txtNombreCliente.setText(TableCliente.getValueAt(fila, 2).toString());
        txtTelefonoCliente.setText(TableCliente.getValueAt(fila, 3).toString());
        txtDireccionCliente.setText(TableCliente.getValueAt(fila, 4).toString());
        txtRazonCliente.setText(TableCliente.getValueAt(fila, 5).toString());
    }//GEN-LAST:event_TableClienteMouseClicked

    private void btnEliminarCLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarCLActionPerformed
        if (!"".equals(txtIdCliente.getText())) {
            int pregunta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar?");
            if (pregunta == 0) {
                int id = Integer.parseInt(txtIdCliente.getText());
                client.EliminarCliente(id);
                LimpiarTable();
                LimpiarCliente();
                ListarCliente();
            }
        }
    }//GEN-LAST:event_btnEliminarCLActionPerformed

    private void btnEditarCLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarCLActionPerformed
        if ("".equals(txtIdCliente.getText())) {
            JOptionPane.showMessageDialog(null, "Seleccione una fila");
        } else {

            if (!"".equals(txtDniCliente.getText()) || !"".equals(txtNombreCliente.getText()) || !"".equals(txtTelefonoCliente.getText()) || !"".equals(txtDireccionCliente.getText()) || !"".equals(txtRazonCliente.getText())) {
                cl.setDni(txtDniCliente.getText());
                cl.setNombre(txtNombreCliente.getText());
                cl.setTelefono(txtTelefonoCliente.getText());
                cl.setDireccion(txtDireccionCliente.getText());
                cl.setRazon(txtRazonCliente.getText());
                cl.setId(Integer.parseInt(txtIdCliente.getText()));
                client.ModificarCliente(cl);
                JOptionPane.showMessageDialog(null, "Cliente Actualizado");
                LimpiarTable();
                LimpiarCliente();
                ListarCliente();
            } else {
                JOptionPane.showMessageDialog(null, "Los campos estan vacios");
            }

        }
    }//GEN-LAST:event_btnEditarCLActionPerformed

    private void btnNuevoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoClienteActionPerformed
        LimpiarCliente();
    }//GEN-LAST:event_btnNuevoClienteActionPerformed

    private void btnGuardarPRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarPRActionPerformed
        if (!"".equals(txtRucProveedor.getText()) || !"".equals(txtNombreProveedor.getText()) || !"".equals(txtTelefonoProveedor.getText()) || !"".equals(txtDireccionProveedor.getText()) || !"".equals(txtRazonProveedor.getText())) {
            pr.setRuc(Integer.parseInt(txtRucProveedor.getText()));
            pr.setNombre(txtNombreProveedor.getText());
            pr.setTelefono(Integer.parseInt(txtTelefonoProveedor.getText()));
            pr.setDireccion(txtDireccionProveedor.getText());
            pr.setRazon(txtRazonProveedor.getText());
            prDAO.RegistrarProveedor(pr);
            JOptionPane.showMessageDialog(null, "Proveedor Registrado");
            LimpiarTable();
            ListarProveedor();
            LimpiarProveedor();
        } else {
            JOptionPane.showMessageDialog(null, "Los campos están vacíos");
        }
    }//GEN-LAST:event_btnGuardarPRActionPerformed

    private void btnProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProveedorActionPerformed
        LimpiarTable();
        ListarProveedor();
        jTabbedPane1.setSelectedIndex(2);

    }//GEN-LAST:event_btnProveedorActionPerformed

    private void TableProveedorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableProveedorMouseClicked
        int fila = TableProveedor.rowAtPoint(evt.getPoint());
        txtIdProveedor.setText(TableProveedor.getValueAt(fila, 0).toString());
        txtRucProveedor.setText(TableProveedor.getValueAt(fila, 1).toString());
        txtNombreProveedor.setText(TableProveedor.getValueAt(fila, 2).toString());
        txtTelefonoProveedor.setText(TableProveedor.getValueAt(fila, 3).toString());
        txtDireccionProveedor.setText(TableProveedor.getValueAt(fila, 4).toString());
        txtRazonProveedor.setText(TableProveedor.getValueAt(fila, 5).toString());
    }//GEN-LAST:event_TableProveedorMouseClicked

    private void btnEliminarPRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarPRActionPerformed
        if (!"".equals(txtIdProveedor.getText())) {
            int pregunta = JOptionPane.showConfirmDialog(null, "Está seguro de eliminar?");
            if (pregunta == 0) {
                int id = Integer.parseInt(txtIdProveedor.getText());
                prDAO.EliminarProveedor(id);
                LimpiarTable();
                ListarProveedor();
                LimpiarProveedor();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione una fila");
        }
    }//GEN-LAST:event_btnEliminarPRActionPerformed

    private void btnEditarPRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarPRActionPerformed
        if ("".equals(txtIdProveedor.getText())) {
            JOptionPane.showMessageDialog(null, "Seleccione una fila");

        } else {
            if (!"".equals(txtRucProveedor.getText()) || !"".equals(txtNombreProveedor.getText()) || !"".equals(txtTelefonoProveedor.getText()) || !"".equals(txtDireccionProveedor.getText()) || !"".equals(txtRazonProveedor.getText())) {
                pr.setRuc(Integer.parseInt(txtRucProveedor.getText()));
                pr.setNombre(txtNombreProveedor.getText());
                pr.setTelefono(Integer.parseInt(txtRucProveedor.getText()));
                pr.setDireccion(txtDireccionProveedor.getText());
                pr.setRazon(txtRazonProveedor.getText());
                pr.setId(Integer.parseInt(txtIdProveedor.getText()));
                prDAO.ModificarProveedor(pr);
                JOptionPane.showMessageDialog(null, "Proveedor Actualizado");
                LimpiarTable();
                ListarProveedor();
                LimpiarProveedor();
            }
        }
    }//GEN-LAST:event_btnEditarPRActionPerformed

    private void btnNuevoPRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoPRActionPerformed
        LimpiarProveedor();
    }//GEN-LAST:event_btnNuevoPRActionPerformed

    private void btnGuardarPROActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarPROActionPerformed
        if (!"".equals(txtCodProducto.getText()) || !"".equals(txtDescripcionProducto.getText()) || !"".equals(txtCantidadProducto.getText()) || !"".equals(txtPrecioProducto.getText())) {
            pro.setCodigo(txtCodProducto.getText());
            pro.setNombre(txtDescripcionProducto.getText());
            pro.setStock(Integer.parseInt(txtCantidadProducto.getText()));
            pro.setPrecio_unitario(Double.parseDouble(txtPrecioProducto.getText()));
            pro.setPrecio_costo(Double.parseDouble(txtPrecioCostoProducto.getText()));
            pro.setGanancia(txtGananciaProducto.getText());
            proDAO.RegistrarProductos(pro);
            JOptionPane.showMessageDialog(null, "Producto Registrado");
            LimpiarTable();
            ListarProductos();
            LimpiarProductos();
        } else {
            JOptionPane.showMessageDialog(null, "Los campos estn vacíos");
        }
    }//GEN-LAST:event_btnGuardarPROActionPerformed

    private void btnProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductosActionPerformed
        LimpiarTable();
        ListarProductos();
        jTabbedPane1.setSelectedIndex(3);
    }//GEN-LAST:event_btnProductosActionPerformed

    private void TableProductosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableProductosMouseClicked
        int fila = TableProductos.rowAtPoint(evt.getPoint());
        txtIdPro.setText(TableProductos.getValueAt(fila, 0).toString());
        txtCodProducto.setText(TableProductos.getValueAt(fila, 1).toString());
        txtDescripcionProducto.setText(TableProductos.getValueAt(fila, 2).toString());
        txtCantidadProducto.setText(TableProductos.getValueAt(fila, 3).toString());
        txtPrecioProducto.setText(TableProductos.getValueAt(fila, 4).toString());
    }//GEN-LAST:event_TableProductosMouseClicked

    private void btnEliminarPROActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarPROActionPerformed
        if (!"".equals(txtIdPro.getText())) {
            int pregunta = JOptionPane.showConfirmDialog(null, "Está seguro que desea eliminar?");
            if (pregunta == 0) {
                int id = Integer.parseInt(txtIdPro.getText());
                proDAO.EliminarProductos(id);
                LimpiarTable();
                LimpiarProductos();
                ListarProductos();
            }
        }
    }//GEN-LAST:event_btnEliminarPROActionPerformed

    private void btnEditarPROActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarPROActionPerformed
        if ("".equals(txtIdPro.getText())) {
            JOptionPane.showMessageDialog(null, "Seleccione una fila");
        } else {
            if (!"".equals(txtCodProducto.getText()) || !"".equals(txtDescripcionProducto.getText()) || !"".equals(txtCantidadProducto.getText()) || !"".equals(txtCantidadProducto.getText()) || !"".equals(txtPrecioProducto.getText()) || !"".equals(txtPrecioCostoProducto.getText()) || !"".equals(txtGananciaProducto.getText())) {
                pro.setCodigo(txtCodProducto.getText());
                pro.setNombre(txtDescripcionProducto.getText());
                pro.setStock(Integer.parseInt(txtCantidadProducto.getText()));
                pro.setPrecio_unitario(Double.parseDouble(txtPrecioProducto.getText()));
                pro.setPrecio_costo(Double.parseDouble(txtPrecioCostoProducto.getText()));
                pro.setGanancia(txtGananciaProducto.getText());
                pro.setId(Integer.parseInt(txtIdPro.getText()));
                proDAO.ModificarProducto(pro);
                JOptionPane.showMessageDialog(null, "Producto Actualizado");
                LimpiarTable();
                ListarProductos();
                LimpiarProductos();
            }
        }
    }//GEN-LAST:event_btnEditarPROActionPerformed

    private void btnExcelPROActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelPROActionPerformed
        Excel.reporte();
    }//GEN-LAST:event_btnExcelPROActionPerformed

    private void txtCodigoVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoVentaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!"".equals(txtCodigoVenta.getText())) {
                String cod = txtCodigoVenta.getText();
                pro = proDAO.BuscarPro(cod);
                if (pro.getNombre() != null) {
                    cbxDescripcionVenta.setSelectedItem(pro.getNombre());
                    txtPrecioVenta.setText("" + pro.getPrecio_unitario());
                    txtPrecioVenta.setEditable(true); // Hacer el campo editable
                    txtStockDisponible.setText("" + pro.getStock());
                    txtCantidadVenta.requestFocus();
                } else {
                    LimpiarVenta();
                    txtCodigoVenta.requestFocus();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Ingrese el codigo del producto");
                txtCodigoVenta.requestFocus();
            }
        }
    }//GEN-LAST:event_txtCodigoVentaKeyPressed

    private void txtCantidadVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadVentaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!"".equals(txtCantidadVenta.getText())) {
                String cod = txtCodigoVenta.getText();
                String descripcion = cbxDescripcionVenta.getSelectedItem().toString();
                double cant = Double.parseDouble(txtCantidadVenta.getText());
                double precio = Double.parseDouble(txtPrecioVenta.getText());
                double total = cant * precio;
                txtPrecioCalculado.setText(String.valueOf(total)); // Actualizamos el label con el resultado
                double stock = Double.parseDouble(txtStockDisponible.getText());
                if (stock >= cant) {
                    item = item + 1;
                    tmp = (DefaultTableModel) TableVenta.getModel();
                    for (int i = 0; i < TableVenta.getRowCount(); i++) {
                        if (TableVenta.getValueAt(i, 1).equals(descripcion)) {
                            JOptionPane.showMessageDialog(null, "El producto ya está registrado");
                            return;
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Stock no disponible");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Ingrese Cantidad");
            }
        }
    }//GEN-LAST:event_txtCantidadVentaKeyPressed


    private void btnEliminarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarVentaActionPerformed
        modelo = (DefaultTableModel) TableVenta.getModel();
        modelo.removeRow(TableVenta.getSelectedRow());
        TotalPagar();
        txtCodigoVenta.requestFocus();

    }//GEN-LAST:event_btnEliminarVentaActionPerformed

    private void txtDNIVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDNIVentaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!"".equals(txtDNIVenta.getText())) {
                int dni = Integer.parseInt(txtDNIVenta.getText());
                cl = client.BuscarCliente(WIDTH);
                if (cl.getNombre() != null) {
                    txtNombreCLVenta.setText("" + cl.getNombre());
                    txtTelVenta.setText("" + cl.getTelefono());
                    txtDirecVenta.setText("" + cl.getDireccion());
                    txtRazonVenta.setText("" + cl.getTelefono());
                } else {
                    txtDNIVenta.setText("");
                    JOptionPane.showMessageDialog(null, "El cliente no existe");
                }
            }
        }
    }//GEN-LAST:event_txtDNIVentaKeyPressed

    private void btnCambioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCambioActionPerformed
        calcularCambio();
    }//GEN-LAST:event_btnCambioActionPerformed

    private void txtCodigoVentaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoVentaKeyTyped
        event.numberKeyPress(evt);
    }//GEN-LAST:event_txtCodigoVentaKeyTyped

    private void txtCantidadVentaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadVentaKeyTyped

    }//GEN-LAST:event_txtCantidadVentaKeyTyped

    private void txtPrecioProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioProductoKeyTyped
        event.numberDecimalKeyPress(evt, txtPrecioProducto);
    }//GEN-LAST:event_txtPrecioProductoKeyTyped

    private void btnConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfigActionPerformed
        jTabbedPane1.setSelectedIndex(5);
    }//GEN-LAST:event_btnConfigActionPerformed

    private void btnVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVentasActionPerformed
        jTabbedPane1.setSelectedIndex(4);
        LimpiarTable();
        ListarVentas();
        TotalVentas();
    }//GEN-LAST:event_btnVentasActionPerformed

    private void TableVentasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableVentasMouseClicked
        int fila = TableVentas.rowAtPoint(evt.getPoint());
        txtIdVenta.setText(TableVentas.getValueAt(fila, 0).toString());
    }//GEN-LAST:event_TableVentasMouseClicked

    private void btnPDFVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPDFVentasActionPerformed
        try {
            int id = Integer.parseInt(txtIdVenta.getText());
            File file = new File("src/pdf/venta.pdf" + id + ".pdf");
            Desktop.getDesktop().open(file);
        } catch (IOException ex) {
            Logger.getLogger(Sistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnPDFVentasActionPerformed

    private void btnGraficarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGraficarActionPerformed
        String fechaReporte = new SimpleDateFormat("dd/MM/yyyy").format(MiDate.getDate());
        Grafico.Graficar(fechaReporte);
    }//GEN-LAST:event_btnGraficarActionPerformed

    private void txtCodigoVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoVentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoVentaActionPerformed

    private void TableVentasMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableVentasMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_TableVentasMouseEntered

    private void btnGenerarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarVentaActionPerformed
        if (TableVenta.getRowCount() > 0) {
            if (!"".equals(txtNombreCLVenta.getText())) {
                RegistrarVenta();
                RegistrarDetalle();
                ActualizarStock();
                pdf();
                LimpiarTableVenta();
                LimpiarClienteVenta();
                LabelTotal.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "Debes buscar un cliente");
            }
        } else {
            JOptionPane.showMessageDialog(null, "No hay productos en la venta");
        }

    }//GEN-LAST:event_btnGenerarVentaActionPerformed

    private void txtPrecioCostoProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioCostoProductoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrecioCostoProductoKeyTyped

    private void txtGananciaProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGananciaProductoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGananciaProductoKeyTyped

    private void txtPrecioCalculadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrecioCalculadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrecioCalculadoActionPerformed

    private void btnListoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnListoActionPerformed
        if (!"".equals(txtCantidadVenta.getText())) {
            String cod = txtCodigoVenta.getText();
            String descripcion = cbxDescripcionVenta.getSelectedItem().toString();
            double cant = Double.parseDouble(txtCantidadVenta.getText());
            double precio = Double.parseDouble(txtPrecioVenta.getText());
            double total = cant * precio;
            txtPrecioCalculado.setText(String.valueOf(total));
            double stock = Double.parseDouble(txtStockDisponible.getText());
            if (stock >= cant) {
                item = item + 1;
                tmp = (DefaultTableModel) TableVenta.getModel();
                for (int i = 0; i < TableVenta.getRowCount(); i++) {
                    if (TableVenta.getValueAt(i, 1).equals(cbxDescripcionVenta.getSelectedItem().toString())) {
                        JOptionPane.showMessageDialog(null, "El producto ya está registrado");
                        return;
                    }
                }
                ArrayList lista = new ArrayList();
                lista.add(item);
                lista.add(cod);
                lista.add(descripcion);
                lista.add(cant);
                lista.add(precio);
                lista.add(total);
                Object[] O = new Object[5];
                O[0] = lista.get(1);
                O[1] = lista.get(2);
                O[2] = lista.get(3);
                O[3] = lista.get(4);
                O[4] = lista.get(5);
                tmp.addRow(O);
                TableVenta.setModel(tmp);
                TotalPagar();
                LimpiarVenta();
                txtCodigoVenta.requestFocus();
            } else {
                JOptionPane.showMessageDialog(null, "Stock no disponible");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Ingrese Cantidad");
        }
    }//GEN-LAST:event_btnListoActionPerformed

    private void txtCantidadVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCantidadVentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCantidadVentaActionPerformed

    private void txtPrecioVentaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPrecioVentaMouseClicked
        txtPrecioVenta.selectAll();
    }//GEN-LAST:event_txtPrecioVentaMouseClicked

    private void txtPrecioVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioVentaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!"".equals(txtPrecioVenta.getText())) {
                double precioUnitarioReal = Double.parseDouble(txtPrecioVenta.getText());
                double cantidad = precioUnitarioReal / pro.getPrecio_unitario();
                txtCantidadVenta.setText(String.valueOf(cantidad));
                calcularTotal();
                txtCantidadVenta.setEditable(true); // Evitar que se edite la cantidad después de hacer el cálculo
                txtCantidadVenta.requestFocus();
            }
        }
    }//GEN-LAST:event_txtPrecioVentaKeyPressed

    private void txtCantidadProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCantidadProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCantidadProductoActionPerformed

    private void btnAnalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnalizarActionPerformed

    }//GEN-LAST:event_btnAnalizarActionPerformed

    private void cbxDescripcionVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbxDescripcionVentaKeyPressed

    }//GEN-LAST:event_cbxDescripcionVentaKeyPressed

    private void calcularTotal() {
        double precioUnitarioReal = Double.parseDouble(txtPrecioVenta.getText());
        double cantidad = Double.parseDouble(txtCantidadVenta.getText());
        double total = pro.getPrecio_unitario() * cantidad;
        txtPrecioCalculado.setText(String.valueOf(total));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Sistema().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LabelTotal;
    private javax.swing.JLabel LabelTotalVentas;
    private javax.swing.JLabel LabelVendedor;
    private com.toedter.calendar.JDateChooser MiDate;
    private javax.swing.JTable TableCliente;
    private javax.swing.JTable TableProductos;
    private javax.swing.JTable TableProveedor;
    private javax.swing.JTable TableVenta;
    private javax.swing.JTable TableVentas;
    private javax.swing.JButton btnActualizarConfig;
    private javax.swing.JButton btnAnalizar;
    private javax.swing.JButton btnCambio;
    private javax.swing.JButton btnClientes;
    private javax.swing.JButton btnConfig;
    private javax.swing.JButton btnEditarCL;
    private javax.swing.JButton btnEditarPR;
    private javax.swing.JButton btnEditarPRO;
    private javax.swing.JButton btnEliminarCL;
    private javax.swing.JButton btnEliminarPR;
    private javax.swing.JButton btnEliminarPRO;
    private javax.swing.JButton btnEliminarVenta;
    private javax.swing.JButton btnExcelPRO;
    private javax.swing.JButton btnGenerarVenta;
    private javax.swing.JButton btnGraficar;
    private javax.swing.JButton btnGuardarCL;
    private javax.swing.JButton btnGuardarPR;
    private javax.swing.JButton btnGuardarPRO;
    private javax.swing.JButton btnListo;
    private javax.swing.JButton btnNuevaVenta;
    private javax.swing.JButton btnNuevoCliente;
    private javax.swing.JButton btnNuevoPR;
    private javax.swing.JButton btnNuevoPRO;
    private javax.swing.JButton btnPDFVentas;
    private javax.swing.JButton btnProductos;
    private javax.swing.JButton btnProveedor;
    private javax.swing.JButton btnVentas;
    private javax.swing.JComboBox<String> cbxDescripcionVenta;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblFechaFinal;
    private javax.swing.JLabel lblFechaInicio;
    private javax.swing.JTextField txtCantidadProducto;
    private javax.swing.JTextField txtCantidadVenta;
    private javax.swing.JTextField txtCodProducto;
    private javax.swing.JTextField txtCodigoVenta;
    private javax.swing.JTextField txtDNIVenta;
    private javax.swing.JTextField txtDescripcionProducto;
    private javax.swing.JTextField txtDirecConfig;
    private javax.swing.JTextField txtDirecVenta;
    private javax.swing.JTextField txtDireccionCliente;
    private javax.swing.JTextField txtDireccionProveedor;
    private javax.swing.JTextField txtDniCliente;
    private javax.swing.JTextField txtGananciaProducto;
    private javax.swing.JTextField txtIdCliente;
    private javax.swing.JTextField txtIdConfig;
    private javax.swing.JTextField txtIdPro;
    private javax.swing.JTextField txtIdProveedor;
    private javax.swing.JTextField txtIdVenta;
    private javax.swing.JTextField txtId_PRO;
    private javax.swing.JTextField txtNombreCLVenta;
    private javax.swing.JTextField txtNombreCliente;
    private javax.swing.JTextField txtNombreConfig;
    private javax.swing.JTextField txtNombreProveedor;
    private javax.swing.JTextField txtPrecioCalculado;
    private javax.swing.JTextField txtPrecioCostoProducto;
    private javax.swing.JTextField txtPrecioProducto;
    private javax.swing.JTextField txtPrecioVenta;
    private javax.swing.JTextField txtRazonCliente;
    private javax.swing.JTextField txtRazonConfig;
    private javax.swing.JTextField txtRazonProveedor;
    private javax.swing.JTextField txtRazonVenta;
    private javax.swing.JTextField txtRucConfig;
    private javax.swing.JTextField txtRucProveedor;
    private javax.swing.JTextField txtStockDisponible;
    private javax.swing.JTextField txtTelVenta;
    private javax.swing.JTextField txtTelefonoCliente;
    private javax.swing.JTextField txtTelefonoConfig;
    private javax.swing.JTextField txtTelefonoProveedor;
    // End of variables declaration//GEN-END:variables
    private void LimpiarCliente() {
        txtIdCliente.setText("");
        txtDniCliente.setText("");
        txtNombreCliente.setText("");
        txtTelefonoCliente.setText("");
        txtDireccionCliente.setText("");
        txtRazonCliente.setText("");
    }

    private void LimpiarProveedor() {
        txtIdProveedor.setText("");
        txtRucProveedor.setText("");
        txtNombreProveedor.setText("");
        txtTelefonoProveedor.setText("");
        txtDireccionProveedor.setText("");
        txtRazonProveedor.setText("");
    }

    private void LimpiarProductos() {
        txtIdPro.setText("");
        txtCodProducto.setText("");
        txtDescripcionProducto.setText("");
        txtCantidadProducto.setText("");
        txtPrecioProducto.setText("");
        txtPrecioCostoProducto.setText("");
        txtGananciaProducto.setText("");
    }

    private void TotalPagar() {
        Totalpagar = 0.00;
        int numFila = TableVenta.getRowCount();
        for (int i = 0; i < numFila; i++) {
            double cal = Double.parseDouble(String.valueOf(TableVenta.getModel().getValueAt(i, 4)));
            Totalpagar = Totalpagar + cal;
        }
        LabelTotal.setText(String.format("$%.2f", Totalpagar));
    }

    private void TotalVentas() {
        Totalventas = 0.00;
        int numFila = TableVentas.getRowCount();
        for (int i = 0; i < numFila; i++) {
            double cal = Double.parseDouble(String.valueOf(TableVentas.getModel().getValueAt(i, 3)));
            Totalventas = Totalventas + cal;
        }
        LabelTotalVentas.setText(String.format("$%.2f", Totalventas));
    }

    private void LimpiarVenta() {
        txtCodigoVenta.setText("");
        cbxDescripcionVenta.setSelectedItem(null);
        txtCantidadVenta.setText("");
        txtStockDisponible.setText("");
        txtPrecioVenta.setText("");
        txtPrecioCalculado.setText("");
        txtIdVenta.setText("");
    }

    private void RegistrarVenta() {
        String cliente = txtNombreCLVenta.getText();
        String vendedor = LabelVendedor.getText();
        double monto = Totalpagar;
        v.setCliente(cliente);
        v.setVendedor(vendedor);
        v.setTotal(monto);
        vDAO.RegistrarVenta(v);
    }

    private void RegistrarDetalle() {
        int id = vDAO.IdVenta();
        for (int i = 0; i < TableVenta.getRowCount(); i++) {
            String cod = TableVenta.getValueAt(i, 0).toString();
            double cant = Double.parseDouble(TableVenta.getValueAt(i, 2).toString());
            double precio = Double.parseDouble(TableVenta.getValueAt(i, 3).toString());
            Dv.setCod_pro(cod);
            Dv.setCantidad(cant);
            Dv.setPrecio(precio);
            Dv.setId(id);
            vDAO.RegistrarDetalle(Dv);
        }
    }

    private void ActualizarStock() {
        for (int i = 0; i < TableVenta.getRowCount(); i++) {
            String cod = TableVenta.getValueAt(i, 0).toString();
            double cant = Double.parseDouble(TableVenta.getValueAt(i, 2).toString());
            pro = proDAO.BuscarPro(cod);
            double StockActual = pro.getStock() - cant;
            vDAO.ActualizarStock(StockActual, cod);

        }
    }

    private void LimpiarTableVenta() {
        tmp = (DefaultTableModel) TableVenta.getModel();
        int fila = TableVenta.getRowCount();
        for (int i = 0; i < fila; i++) {
            tmp.removeRow(0);
        }
    }

    private void LimpiarClienteVenta() {
        txtDNIVenta.setText("");
        txtNombreCLVenta.setText("");
        txtTelVenta.setText("");
        txtDirecVenta.setText("");
        txtRazonVenta.setText("");
    }

    private void pdf() {
        try {
            int id = vDAO.IdVenta();
            FileOutputStream archivo;
            File file = new File("src/pdf/venta.pdf" + id + ".pdf");
            archivo = new FileOutputStream(file);
            Document doc = new Document();
            PdfWriter.getInstance(doc, archivo);
            doc.open();
            Image img = Image.getInstance("src/Img/logo1.png");

            Paragraph fecha = new Paragraph();
            Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE);
            fecha.add(Chunk.NEWLINE);
            Date date = new Date();
            fecha.add("Ticket:" + id + "\n" + "Fecha: " + new SimpleDateFormat("dd-MM-yyyy").format(date) + "\n\n");

            PdfPTable Encabezado = new PdfPTable(4);
            Encabezado.setWidthPercentage(100);
            Encabezado.getDefaultCell().setBorder(0);
            float[] ColumnaEncabezado = new float[]{20f, 30f, 70f, 40f};
            Encabezado.setWidths(ColumnaEncabezado);
            Encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);

            Encabezado.addCell(img);

            String ruc = txtRucConfig.getText();
            String nom = txtNombreConfig.getText();
            String tel = txtTelefonoConfig.getText();
            String dir = txtDirecConfig.getText();
            String ra = txtRazonConfig.getText();

            Encabezado.addCell("");
            Encabezado.addCell("Ruc: " + ruc + "\nNombre: " + nom + "\nTelefono: " + tel + "\nDireccion: " + dir + "\nRazon: " + ra);
            Encabezado.addCell(fecha);
            doc.add(Encabezado);

            Paragraph cli = new Paragraph();
            cli.add(Chunk.NEWLINE);
            cli.add("""
                    Datos de los clientes
                    
                    """);
            doc.add(cli);

            PdfPTable tablacli = new PdfPTable(4);
            tablacli.setWidthPercentage(100);
            tablacli.getDefaultCell().setBorder(0);
            float[] Columnacli = new float[]{20f, 50f, 30f, 40f};
            tablacli.setWidths(Columnacli);
            tablacli.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell cl1 = new PdfPCell(new Phrase("DNI", negrita));
            PdfPCell cl2 = new PdfPCell(new Phrase("Nombre", negrita));
            PdfPCell cl3 = new PdfPCell(new Phrase("Telefono", negrita));
            PdfPCell cl4 = new PdfPCell(new Phrase("Direccion", negrita));
            cl1.setBorder(0);
            cl2.setBorder(0);
            cl3.setBorder(0);
            cl4.setBorder(0);
            tablacli.addCell(cl1);
            tablacli.addCell(cl2);
            tablacli.addCell(cl3);
            tablacli.addCell(cl4);
            tablacli.addCell(txtDNIVenta.getText());
            tablacli.addCell(txtNombreCLVenta.getText());
            tablacli.addCell(txtTelVenta.getText());
            tablacli.addCell(txtDirecVenta.getText());

            doc.add(tablacli);

            //productos
            PdfPTable tablapro = new PdfPTable(4);
            tablapro.setWidthPercentage(100);
            tablapro.getDefaultCell().setBorder(0);
            float[] Columnapro = new float[]{10f, 50f, 15f, 20f};
            tablapro.setWidths(Columnapro);
            tablapro.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell pro1 = new PdfPCell(new Phrase("Cantidad", negrita));
            PdfPCell pro2 = new PdfPCell(new Phrase("Descripcion", negrita));
            PdfPCell pro3 = new PdfPCell(new Phrase("Precio Unitario", negrita));
            PdfPCell pro4 = new PdfPCell(new Phrase("Precio Total", negrita));
            pro1.setBorder(0);
            pro2.setBorder(0);
            pro3.setBorder(0);
            pro4.setBorder(0);
            pro1.setBackgroundColor(BaseColor.GRAY);
            pro2.setBackgroundColor(BaseColor.GRAY);
            pro3.setBackgroundColor(BaseColor.GRAY);
            pro4.setBackgroundColor(BaseColor.GRAY);
            tablapro.addCell(pro1);
            tablapro.addCell(pro2);
            tablapro.addCell(pro3);
            tablapro.addCell(pro4);
            for (int i = 0; i < TableVenta.getRowCount(); i++) {
                String producto = TableVenta.getValueAt(i, 1).toString();
                String cantidad = TableVenta.getValueAt(i, 2).toString();
                String precio = TableVenta.getValueAt(i, 3).toString();
                String total = TableVenta.getValueAt(i, 4).toString();
                tablapro.addCell(cantidad);
                tablapro.addCell(producto);
                tablapro.addCell(precio);
                tablapro.addCell(total);
            }

            doc.add(tablapro);

            Paragraph info = new Paragraph();
            info.add(Chunk.NEWLINE);
            info.add("Total a pagar: " + Totalpagar);
            info.setAlignment(Element.ALIGN_RIGHT);
            doc.add(info);

            Paragraph firma = new Paragraph();
            firma.add(Chunk.NEWLINE);
            firma.add("Cancelacion y firma\n\n");
            firma.add("-----------------------");
            firma.setAlignment(Element.ALIGN_CENTER);
            doc.add(firma);

            Paragraph mensaje = new Paragraph();
            mensaje.add(Chunk.NEWLINE);
            mensaje.add("Gracias por su compra");
            mensaje.setAlignment(Element.ALIGN_CENTER);
            doc.add(mensaje);
            doc.close();
            archivo.close();
        } catch (DocumentException | IOException e) {
            System.out.println(e.toString());
        }
    }

    private void calcularCambio() {
        double totalVenta = Totalpagar;

        // Solicitar al usuario el monto recibido
        String montoRecibidoStr = JOptionPane.showInputDialog(null, "Con cuanto paga?:");

        // Verificar que se ingresó un valor válido
        if (montoRecibidoStr != null && !montoRecibidoStr.isEmpty()) {
            try {
                double montoRecibido = Double.parseDouble(montoRecibidoStr);

                // Calcular el cambio
                double cambio = montoRecibido - totalVenta;

                // Mostrar el cambio al usuario
                JOptionPane.showMessageDialog(null, "Cambio: " + cambio);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ingrese un monto válido");
            }
        }
    }

}
