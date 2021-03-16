package Classes;

import JDBC.Banco;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Despesa extends Movimento
{
    private int codigo;
    private TipoDespesa tipo;
    private double valor;
    private Date data;
    private Caixa caixa;
    private Usuario user;
    private String obs;

    public Despesa()
    {
    }

    public Despesa(int codigo)
    {
        this.codigo = codigo;
    }

    public Despesa(TipoDespesa tipo)
    {
        this.tipo = tipo;
    }

    public Despesa(Date data)
    {
        this.data = data;
    }

    public Despesa(Caixa caixa)
    {
        this.caixa = caixa;
    }

    public Despesa(Usuario user)
    {
        this.user = user;
    }
    
    public Despesa(TipoDespesa tipo, double valor, Date data, Caixa caixa, Usuario user, String obs)
    {
        this.tipo = tipo;
        this.valor = valor;
        this.data = data;
        this.caixa = caixa;
        this.user = user;
        this.obs = obs;
    }

    public Despesa(int codigo, TipoDespesa tipo, double valor, Date data, Caixa caixa, Usuario user)
    {
        this.codigo = codigo;
        this.tipo = tipo;
        this.valor = valor;
        this.data = data;
        this.caixa = caixa;
        this.user = user;
    }

    public Despesa(int codigo, TipoDespesa tipo, double valor, Date data, Caixa caixa, Usuario user, String obs)
    {
        this.codigo = codigo;
        this.tipo = tipo;
        this.valor = valor;
        this.data = data;
        this.caixa = caixa;
        this.user = user;
        this.obs = obs;
    }

    public int getCodigo()
    {
        return codigo;
    }

    public TipoDespesa getTipo()
    {
        return tipo;
    }

    public double getValor()
    {
        return valor;
    }

    public Date getData()
    {
        return data;
    }

    public Caixa getCaixa()
    {
        return caixa;
    }

    public Usuario getUser()
    {
        return user;
    }

    public String getObs()
    {
        return obs;
    }

    public void setObs(String obs)
    {
        this.obs = obs;
    }
    
    public boolean insert()
    {
        String sql = "INSERT INTO Despesa(des_codigo, tipo_des_Codigo, des_valor, des_data, caixa_codigo, usu_login, des_obs) ";
        String values = "VALUES(?, ?, ?, ?, ?, ?, ?)";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql + values);

            statement.setInt(1, Banco.getConexao().getMaxPK("Despesa", "des_codigo") + 1);
            statement.setInt(2, tipo.getCodigo());
            statement.setDouble(3, valor);
            statement.setDate(4, data);
            statement.setString(5, caixa.getCodigo());
            statement.setString(6, user.getLogin());
            statement.setString(7, obs);

            return statement.executeUpdate() > 0;
        }
        catch(SQLException ex)
        {
            Banco.getConexao().setMessagemErro(ex.getMessage());
        }

        return false;
    }
    
    public boolean delete()
    {
        String sql = "DELETE FROM Despesa WHERE des_codigo = " + codigo;
        
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
    
    public ObservableList<Object> searchByCaixa()
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Despesa WHERE caixa_codigo = ?";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, caixa.getCodigo());
            
            ResultSet rs = statement.executeQuery();
            
            while(rs.next())
            {
                Usuario user = (Usuario)(new Usuario(rs.getString("usu_login"), "").searchByLogin().get(0));
                TipoDespesa tipo = (TipoDespesa)(new TipoDespesa(rs.getInt("tipo_des_codigo")).searchByCodigo());
                Caixa caixa = new Caixa(rs.getString("caixa_codigo"));
                
                list.add(new Despesa(rs.getInt("des_codigo"), tipo, rs.getDouble("des_valor"), rs.getDate("des_data"), caixa, user, rs.getString("des_obs")));
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
        String sql = "SELECT * FROM Despesa WHERE tipo_des_codigo > 2";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            while(rs.next())
            {
                Usuario user = (Usuario)(new Usuario(rs.getString("usu_login"), "").searchByLogin().get(0));
                TipoDespesa tipo = (TipoDespesa)(new TipoDespesa(rs.getInt("tipo_des_codigo")).searchByCodigo());
                Caixa caixa = new Caixa(rs.getString("caixa_codigo"));
                
                list.add(new Despesa(rs.getInt("des_codigo"), tipo, rs.getDouble("des_valor"), rs.getDate("des_data"), caixa, user, rs.getString("des_obs")));
            }
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        
        return list;
    }

    @Override
    public String toString()
    {
        String str = "";
        
        switch(tipo.getCodigo())
        {
            case 1: str += "Suprimento "; break;
            case 2: str += "Sangria "; break;
            default: str += "Despesa ";
        }
        
        return str + " [" + valor + "]";
    }
    
    @Override
    public String toText()
    {
        String str = tipo.getNome() + " - " + codigo + " no caixa [" + caixa.getCodigo() + "]\n" +
                     "realizado por " + user.getLogin() + "\n no valor de R$ " + valor + "\n\n" + obs;
        
        return str;
    }

}
