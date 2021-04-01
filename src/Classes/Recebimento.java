package Classes;

import Controladoras.CtrCaixa;
import JDBC.Banco;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Recebimento extends Movimento
{
    private int codigo;
    private double valor;
    private int parcela;
    private Date dt_venc;
    private Date dt_pag;
    private Venda venda;
    private Caixa caixa;

    public Recebimento()
    {
    }

    public Recebimento(int codigo)
    {
        this.codigo = codigo;
    }

    public Recebimento(Venda venda)
    {
        this.venda = venda;
    }

    public Recebimento(Caixa caixa)
    {
        this.caixa = caixa;
    }

    public Recebimento(int codigo, Date dt_pag)
    {
        this.codigo = codigo;
        this.dt_pag = dt_pag;
    }

    public Recebimento(double valor, int parcela, Date dt_venc, Caixa caixa)
    {
        this.valor = valor;
        this.parcela = parcela;
        this.dt_venc = dt_venc;
        this.caixa = caixa;
    }
    
    public Recebimento(double valor, int parcela, Date dt_venc, Venda venda, Caixa caixa)
    {
        this.valor = valor;
        this.parcela = parcela;
        this.dt_venc = dt_venc;
        this.venda = venda;
        this.caixa = caixa;
    }

    public Recebimento(int codigo, double valor, int parcela, Date dt_venc, Date dt_pag, Venda venda, Caixa caixa)
    {
        this.codigo = codigo;
        this.valor = valor;
        this.parcela = parcela;
        this.dt_venc = dt_venc;
        this.dt_pag = dt_pag;
        this.venda = venda;
        this.caixa = caixa;
    }

    public int getCodigo()
    {
        return codigo;
    }

    public void setCodigo(int codigo)
    {
        this.codigo = codigo;
    }

    public double getValor()
    {
        return valor;
    }

    public void setValor(double valor)
    {
        this.valor = valor;
    }

    public int getParcela()
    {
        return parcela;
    }

    public void setParcela(int parcela)
    {
        this.parcela = parcela;
    }

    public Date getDt_venc()
    {
        return dt_venc;
    }

    public void setDt_venc(Date dt_venc)
    {
        this.dt_venc = dt_venc;
    }

    public Date getDt_pag()
    {
        return dt_pag;
    }

    public void setDt_pag(Date dt_pag)
    {
        this.dt_pag = dt_pag;
    }

    public Venda getVenda()
    {
        return venda;
    }

    public void setVenda(Venda venda)
    {
        this.venda = venda;
    }

    public Caixa getCaixa()
    {
        return caixa;
    }

    public void setCaixa(Caixa caixa)
    {
        this.caixa = caixa;
    }
    
    public boolean insert()
    {
        String sql = "INSERT INTO Recebimento(rec_codigo, ven_codigo, rec_valor, rec_dt_venc, rec_parcela) ";
        String values = "VALUES(?, ?, ?, ?, ?)";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql + values);
           
            statement.setInt(1, Banco.getConexao().getMaxPK("Recebimento", "rec_codigo") + 1);
            statement.setInt(2, venda.getCodigo());
            statement.setDouble(3, valor);
            statement.setDate(4, dt_venc);
            statement.setInt(5, parcela);

            return statement.executeUpdate() > 0;
        }
        catch(SQLException ex)
        {
            Banco.getConexao().setMessagemErro(ex.getMessage());
        }

        return false;
    }
    
    public boolean pagar()
    {
        String sql = "UPDATE Recebimento SET rec_dt_pag = ?, caixa_codigo = ? WHERE rec_codigo =" + codigo;
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            Caixa cx = (Caixa)CtrCaixa.instancia().searchByToday();
            
            statement.setDate(1, dt_pag);
            statement.setString(2, cx.getCodigo());

            return statement.executeUpdate() > 0;
        }
        catch(SQLException ex)
        {
            Banco.getConexao().setMessagemErro(ex.getMessage());
        }

        return false;
    }
    
    public boolean clearItens()
    {
        String sql = "DELETE FROM Recebimento WHERE ven_codigo = ?";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, venda.getCodigo());
            
            return statement.executeUpdate() > 0;
        }
        catch(SQLException ex)
        {
            Banco.getConexao().setMessagemErro(ex.getMessage());
        }

        return false;
    }
    
    public boolean canEdit()
    {
        String sql = "SELECT COUNT(*) AS n FROM Recebimento WHERE rec_dt_pag IS NOT NULL AND ven_codigo = ?";
        boolean flag = false;
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, venda.getCodigo());
            
            ResultSet rs = statement.executeQuery();
            
            if(rs.next())
                flag = rs.getInt("n") == 0;
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        
        return flag;
    }
    
    public ObservableList<Object> searchByVenda()
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Recebimento WHERE ven_codigo = ?";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, venda.getCodigo());
            
            ResultSet rs = statement.executeQuery();
            
            while(rs.next())
            {
                Caixa cx = new Caixa(rs.getString("caixa_codigo"));
                Venda aqui = new Venda(rs.getInt("ven_codigo"));
                
                list.add(new Recebimento(rs.getInt("rec_codigo"), rs.getDouble("rec_valor"), rs.getInt("rec_parcela"), rs.getDate("rec_dt_venc"), rs.getDate("rec_dt_pag"), aqui, cx));
            }
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        
        return list;
    }
    
    public ObservableList<Object> searchByCaixa()
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Recebimento WHERE caixa_codigo = ? AND rec_dt_pag IS NOT NULL";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, caixa.getCodigo());
            
            ResultSet rs = statement.executeQuery();
            
            while(rs.next())
            {
                Caixa cx = new Caixa(rs.getString("caixa_codigo"));
                Venda aqui = (Venda)new Venda(rs.getInt("ven_codigo")).searchByCodigo();
                
                list.add(new Recebimento(rs.getInt("rec_codigo"), rs.getDouble("rec_valor"), rs.getInt("rec_parcela"), rs.getDate("rec_dt_venc"), rs.getDate("rec_dt_pag"), aqui, cx));
            }
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        
        return list;
    }
    
    public ObservableList<Object> searchAll()
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Recebimento";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            while(rs.next())
            {
                Caixa cx = new Caixa(rs.getString("caixa_codigo"));
                Venda aqui = (Venda)new Venda(rs.getInt("ven_codigo")).searchByCodigo();
                
                
                list.add(new Recebimento(rs.getInt("rec_codigo"), rs.getDouble("rec_valor"), rs.getInt("rec_parcela"), rs.getDate("rec_dt_venc"), rs.getDate("rec_dt_pag"), aqui, cx));
            }
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        
        return list;
    }
    
    public ObservableList<Object> searchAllOpen()
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Recebimento WHERE rec_dt_pag IS NULL";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            while(rs.next())
            {
                Caixa cx = new Caixa(rs.getString("caixa_codigo"));
                Venda aqui = (Venda)new Venda(rs.getInt("ven_codigo")).searchByCodigo();
                
                
                list.add(new Recebimento(rs.getInt("rec_codigo"), rs.getDouble("rec_valor"), rs.getInt("rec_parcela"), rs.getDate("rec_dt_venc"), rs.getDate("rec_dt_pag"), aqui, cx));
            }
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        
        return list;
    }
    
    @Override
    public String toText()
    {
        String str = "Recebimento - " + codigo + " no caixa [" + caixa.getCodigo() + "]\n" +
                     "pago em " + dt_pag.toString() + "\n\n" +
                     venda.toText();
        
        return str;
    }

    @Override
    public String toString()
    {
        String str = "";
        
        if(dt_pag != null)//pago
           str = "Recebimento [" + valor + "]";
        else
           str = parcela + "º " + "$ " + valor + " [" + dt_venc.toString() + "]"; 
        
        
        return str;
    }
}
