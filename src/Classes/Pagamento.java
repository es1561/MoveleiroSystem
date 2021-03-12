package Classes;

import JDBC.Banco;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Pagamento extends Movimento
{
    private int codigo;
    private double valor;
    private int parcela;
    private Date dt_venc;
    private Date dt_pag;
    private Aquisicao aquisicao;
    private Caixa caixa;

    public Pagamento()
    {
    }

    public Pagamento(int codigo)
    {
        this.codigo = codigo;
    }

    public Pagamento(Aquisicao aquisicao)
    {
        this.aquisicao = aquisicao;
    }

    public Pagamento(Caixa caixa)
    {
        this.caixa = caixa;
    }

    public Pagamento(int codigo, Date dt_pag)
    {
        this.codigo = codigo;
        this.dt_pag = dt_pag;
    }

    public Pagamento(double valor, int parcela, Date dt_venc, Aquisicao aquisicao, Caixa caixa)
    {
        this.valor = valor;
        this.parcela = parcela;
        this.dt_venc = dt_venc;
        this.aquisicao = aquisicao;
        this.caixa = caixa;
    }

    public Pagamento(int codigo, double valor, int parcela, Date dt_venc, Date dt_pag, Aquisicao aquisicao, Caixa caixa)
    {
        this.codigo = codigo;
        this.valor = valor;
        this.parcela = parcela;
        this.dt_venc = dt_venc;
        this.dt_pag = dt_pag;
        this.aquisicao = aquisicao;
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

    public Aquisicao getAquisicao()
    {
        return aquisicao;
    }

    public void setAquisicao(Aquisicao aquisicao)
    {
        this.aquisicao = aquisicao;
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
        String sql = "INSERT INTO Pagamento(pag_codigo, aqui_codigo, pag_valor, pag_dt_venc, pag_parcela, caixa_codigo) ";
        String values = "VALUES(?, ?, ?, ?, ?, ?)";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql + values);
           
            statement.setInt(1, Banco.getConexao().getMaxPK("Pagamento", "pag_codigo") + 1);
            statement.setInt(2, aquisicao.getCodigo());
            statement.setDouble(3, valor);
            statement.setDate(4, dt_venc);
            statement.setInt(5, parcela);
            statement.setString(6, caixa.getCodigo());

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
        String sql = "UPDATE Pagamento SET pag_dt_pag = ? WHERE pag_codigo =" + codigo;
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setDate(1, dt_pag);

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
        String sql = "DELETE FROM Pagamento WHERE aqui_codigo = " + aquisicao.getCodigo();
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

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
        String sql = "SELECT COUNT(*) AS n FROM Pagamento WHERE pag_dt_pag IS NOT NULL AND aqui_codigo = " + aquisicao.getCodigo();
        boolean flag = false;
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

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
    
    public ObservableList<Object> searchByAquisicao()
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Pagamento WHERE aqui_codigo = " + aquisicao.getCodigo();
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            while(rs.next())
            {
                Caixa cx = new Caixa(rs.getString("caixa_codigo"));
                Aquisicao aqui = new Aquisicao(rs.getInt("aqui_codigo"));
                
                list.add(new Pagamento(rs.getInt("pag_codigo"), rs.getDouble("pag_valor"), rs.getInt("pag_parcela"), rs.getDate("pag_dt_venc"), rs.getDate("pag_dt_pag"), aqui, cx));
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
        String sql = "SELECT * FROM Pagamento WHERE caixa_codigo = ? AND pag_dt_pag IS NOT NULL";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, caixa.getCodigo());
            
            ResultSet rs = statement.executeQuery();
            
            while(rs.next())
            {
                Caixa cx = new Caixa(rs.getString("caixa_codigo"));
                Aquisicao aqui = (Aquisicao)new Aquisicao(rs.getInt("aqui_codigo")).searchByCodigo();
                
                list.add(new Pagamento(rs.getInt("pag_codigo"), rs.getDouble("pag_valor"), rs.getInt("pag_parcela"), rs.getDate("pag_dt_venc"), rs.getDate("pag_dt_pag"), aqui, cx));
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
        String sql = "SELECT * FROM Pagamento";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            while(rs.next())
            {
                Caixa cx = new Caixa(rs.getString("caixa_codigo"));
                Aquisicao aqui = (Aquisicao)new Aquisicao(rs.getInt("aqui_codigo")).searchByCodigo();
                
                
                list.add(new Pagamento(rs.getInt("pag_codigo"), rs.getDouble("pag_valor"), rs.getInt("pag_parcela"), rs.getDate("pag_dt_venc"), rs.getDate("pag_dt_pag"), aqui, cx));
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
        String sql = "SELECT * FROM Pagamento WHERE pag_dt_pag IS NULL";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            while(rs.next())
            {
                Caixa cx = new Caixa(rs.getString("caixa_codigo"));
                Aquisicao aqui = (Aquisicao)new Aquisicao(rs.getInt("aqui_codigo")).searchByCodigo();
                
                
                list.add(new Pagamento(rs.getInt("pag_codigo"), rs.getDouble("pag_valor"), rs.getInt("pag_parcela"), rs.getDate("pag_dt_venc"), rs.getDate("pag_dt_pag"), aqui, cx));
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
        String str = "Pagamento - " + codigo + " no caixa [" + caixa.getCodigo() + "]\n" +
                     "pago em " + dt_pag.toString() + "\n\n" +
                     aquisicao.toText();
        
        return str;
    }

    @Override
    public String toString()
    {
        return "Pagamento [" + valor + "]";
    }
}
