package Classes;

import Controladoras.CtrDespesa;
import JDBC.Banco;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TimeZone;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Caixa
{
    private String codigo;
    private Date data;
    private Usuario user_a;
    private Usuario user_f;
    private String hora_a;
    private String hora_f;
    private double valor_a;
    private double valor_f;
    private String status;
    private ObservableList<Object> list;

    public Caixa()
    {
    }

    public Caixa(String codigo)
    {
        this.codigo = codigo;
    }

    public Caixa(Date data)
    {
        this.data = data;
    }

    public Caixa(Usuario user_a)
    {
        this.user_a = user_a;
    }

    public Caixa(String codigo, Date data, Usuario user_a, String hora_a, double valor_a)
    {
        this.codigo = codigo;
        this.data = data;
        this.user_a = user_a;
        this.hora_a = hora_a;
        this.valor_a = valor_a;
        this.status = "A";
    }

    public Caixa(String codigo, Date data, Usuario user_a, Usuario user_f, String hora_a, String hora_f, double valor_a, double valor_f, String status)
    {
        this.codigo = codigo;
        this.data = data;
        this.user_a = user_a;
        this.user_f = user_f;
        this.hora_a = hora_a;
        this.hora_f = hora_f;
        this.valor_a = valor_a;
        this.valor_f = valor_f;
        this.status = status;
    }

    public String getCodigo()
    {
        return codigo;
    }

    public Date getData()
    {
        return data;
    }

    public Usuario getUser_a()
    {
        return user_a;
    }
    
    public Usuario getUser_f()
    {
        return user_f;
    }

    public String getHora_a()
    {
        return hora_a;
    }

    public String getHora_f()
    {
        return hora_f;
    }

    public double getValor_a()
    {
        return valor_a;
    }

    public double getValor_f()
    {
        return valor_f;
    }

    public String getStatus()
    {
        return status;
    }
    
    public double getSaldo()
    {
        double value = valor_a;
        
        for (Object obj : list)
        {
            if(obj instanceof Despesa)
            {
                Despesa des = (Despesa)obj;
                
                if(des.getTipo().getCodigo() == 1)//suprimento
                    value += des.getValor();
                else
                    value -= des.getValor();
            }
            else if(obj instanceof Pagamento)
            {
                Pagamento pag = (Pagamento)obj;
                
                value -= pag.getValor();
            }
            else if(obj instanceof Recebimento)
            {
                Recebimento rec = (Recebimento)obj;
                
                value += rec.getValor();
            }
        }
        
        return value;
    }
    
    public ObservableList<Object> getList()
    {
        return list;
    }
    
    public void setUser_f(Usuario user_f)
    {
        this.user_f = user_f;
    }
    
    public void setHora_f(String hora_f)
    {
        this.hora_f = hora_f;
    }

    public void setValor_f(double valor_f)
    {
        this.valor_f = valor_f;
    }

    public void setList(ObservableList<Object> list)
    {
        this.list = list;
    }
    
    public boolean isOpen()
    {
        return status.compareTo("A") == 0;
    }
    
    public boolean insert()
    {
        String sql = "INSERT INTO Caixa(caixa_codigo, caixa_data, caixa_hora_a, caixa_valor_a, usu_login_a, caixa_status) ";
        String values = "VALUES(?, ?, ?, ?, ?, ?)";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql + values);
            int n = Banco.getConexao().getCount("Caixa") + 1;
           
            statement.setString(1, codigo + (n < 10 ? ("0" + n) : n));
            statement.setDate(2, data);
            statement.setString(3, hora_a);
            statement.setDouble(4, valor_a);
            statement.setString(5, user_a.getLogin());
            statement.setString(6, status);

            return statement.executeUpdate() > 0;
        }
        catch(SQLException ex)
        {
            Banco.getConexao().setMessagemErro(ex.getMessage());
        }

        return false;
    }
    
    public boolean update()
    {
        String sql = "UPDATE Caixa SET caixa_valor_f = ? WHERE caixa_codigo = ?";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
           
            statement.setDouble(1, valor_f);
            statement.setString(2, codigo);

            return statement.executeUpdate() > 0;
        }
        catch(SQLException ex)
        {
            Banco.getConexao().setMessagemErro(ex.getMessage());
        }

        return false;
    }
    
    public boolean close()
    {
        String sql = "UPDATE Caixa SET usu_login_f = ?, caixa_hora_f = ?, caixa_valor_f = ?, caixa_status = 'F' WHERE caixa_codigo = ?";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
           
            statement.setString(1, user_f.getLogin());
            statement.setString(2, hora_f);
            statement.setDouble(3, valor_f);
            statement.setString(4, codigo);

            return statement.executeUpdate() > 0;
        }
        catch(SQLException ex)
        {
            Banco.getConexao().setMessagemErro(ex.getMessage());
        }

        return false;
    }
    
    public Object searchByCodigo()
    {
        Object obj = null;
        String sql = "SELECT * FROM Caixa WHERE caixa_codigo = ?";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, codigo);
            ResultSet rs = statement.executeQuery();
            
            if(rs.next())
            {
                Usuario user_a = (Usuario)(new Usuario(rs.getString("usu_login_a"), "").searchByLogin().get(0));
                Usuario user_f = rs.getString("usu_login_f") != null ? (Usuario)(new Usuario(rs.getString("usu_login_f"), "").searchByLogin().get(0)) : null;
                Caixa caixa = new Caixa(rs.getString("caixa_codigo"), rs.getDate("caixa_data"), user_a, user_f, rs.getString("caixa_hora_a"), rs.getString("caixa_hora_f"), rs.getDouble("caixa_valor_a"), rs.getDouble("caixa_valor_f"), rs.getString("caixa_status"));
                ObservableList<Object> _list = FXCollections.observableArrayList(), _list_des, _list_pag;
                
                _list_des = new Despesa(caixa).searchByCaixa();
                _list_pag = new Pagamento(caixa).searchByCaixa();
                
                for (Object item : _list_des)
                    _list.add(item);
                
                for (Object item : _list_pag)
                    _list.add(item);
                
                caixa.setList(_list);
                obj = caixa;
            }
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        
        return obj;
    }
    
    public Object searchByToday()
    {
        Object obj = null;
        String sql = "SELECT * FROM Caixa WHERE caixa_data = ? AND caixa_status = 'A'";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setDate(1, data);
            ResultSet rs = statement.executeQuery();
            
            if(rs.next())
            {
                Usuario user_a = (Usuario)(new Usuario(rs.getString("usu_login_a"), "").searchByLogin().get(0));
                Usuario user_f = rs.getString("usu_login_f") != null ? (Usuario)(new Usuario(rs.getString("usu_login_f"), "").searchByLogin().get(0)) : null;
                Caixa caixa = new Caixa(rs.getString("caixa_codigo"), rs.getDate("caixa_data"), user_a, user_f, rs.getString("caixa_hora_a"), rs.getString("caixa_hora_f"), rs.getDouble("caixa_valor_a"), rs.getDouble("caixa_valor_f"), rs.getString("caixa_status"));
                ObservableList<Object> _list = FXCollections.observableArrayList(), _list_des, _list_pag, _list_rec;
                
                _list_des = new Despesa(caixa).searchByCaixa();
                _list_pag = new Pagamento(caixa).searchByCaixa();
                _list_rec = new Recebimento(caixa).searchByCaixa();
                
                for (Object item : _list_des)
                    _list.add(item);
                
                for (Object item : _list_pag)
                    _list.add(item);
                
                for (Object item : _list_rec)
                    _list.add(item);
                
                caixa.setList(_list);
                obj = caixa;
            }
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        
        return obj;
    }
    
    public ObservableList<Object> searchByData()
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Caixa WHERE caixa_data = ?";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setDate(1, data);
            ResultSet rs = statement.executeQuery();
            
            while(rs.next())
            {
                Object obj;
                Usuario user_a = (Usuario)(new Usuario(rs.getString("usu_login_a"), "").searchByLogin().get(0));
                Usuario user_f = rs.getString("usu_login_f") != null ? (Usuario)(new Usuario(rs.getString("usu_login_f"), "").searchByLogin().get(0)) : null;
                Caixa caixa = new Caixa(rs.getString("caixa_codigo"), rs.getDate("caixa_data"), user_a, user_f, rs.getString("caixa_hora_a"), rs.getString("caixa_hora_f"), rs.getDouble("caixa_valor_a"), rs.getDouble("caixa_valor_f"), rs.getString("caixa_status"));
                ObservableList<Object> _list = FXCollections.observableArrayList(), _list_des, _list_pag;
                
                _list_des = new Despesa(caixa).searchByCaixa();
                _list_pag = new Pagamento(caixa).searchByCaixa();
                
                for (Object item : _list_des)
                    _list.add(item);
                
                for (Object item : _list_pag)
                    _list.add(item);
                
                caixa.setList(_list);
                obj = caixa;
                
                list.add(obj);
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
        String sql = "SELECT * FROM Caixa";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            while(rs.next())
            {
                Object obj;
                Usuario user_a = (Usuario)(new Usuario(rs.getString("usu_login_a"), "").searchByLogin().get(0));
                Usuario user_f = rs.getString("usu_login_f") != null ? (Usuario)(new Usuario(rs.getString("usu_login_f"), "").searchByLogin().get(0)) : null;
                Caixa caixa = new Caixa(rs.getString("caixa_codigo"), rs.getDate("caixa_data"), user_a, user_f, rs.getString("caixa_hora_a"), rs.getString("caixa_hora_f"), rs.getDouble("caixa_valor_a"), rs.getDouble("caixa_valor_f"), rs.getString("caixa_status"));
                ObservableList<Object> _list = FXCollections.observableArrayList(), _list_des, _list_pag;
                
                _list_des = new Despesa(caixa).searchByCaixa();
                _list_pag = new Pagamento(caixa).searchByCaixa();
                
                for (Object item : _list_des)
                    _list.add(item);
                
                for (Object item : _list_pag)
                    _list.add(item);
                
                caixa.setList(_list);
                obj = caixa;
                
                list.add(obj);
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
        return codigo;
    }
}
