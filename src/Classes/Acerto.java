package Classes;

import JDBC.Banco;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Acerto
{
    private int codigo;
    private Date data;
    private Usuario user;
    private String obs;
    private ObservableList<Object> itens;

    public Acerto()
    {
    }

    public Acerto(int codigo)
    {
        this.codigo = codigo;
    }

    public Acerto(Date data)
    {
        this.data = data;
    }

    public Acerto(Usuario user)
    {
        this.user = user;
    }

    public Acerto(int codigo, String obs)
    {
        this.codigo = codigo;
        this.obs = obs;
    }

    public Acerto(Date data, Usuario user, String obs)
    {
        this.data = data;
        this.user = user;
        this.obs = obs;
    }

    public Acerto(int codigo, Date data, Usuario user, String obs)
    {
        this.codigo = codigo;
        this.data = data;
        this.user = user;
        this.obs = obs;
    }

    public int getCodigo()
    {
        return codigo;
    }

    public void setCodigo(int codigo)
    {
        this.codigo = codigo;
    }

    public Date getData()
    {
        return data;
    }

    public void setData(Date data)
    {
        this.data = data;
    }

    public Usuario getUser()
    {
        return user;
    }

    public void setUser(Usuario user)
    {
        this.user = user;
    }

    public String getObs()
    {
        return obs;
    }

    public void setObs(String obs)
    {
        this.obs = obs;
    }

    public ObservableList<Object> getItens()
    {
        return itens;
    }

    public void setItens(ObservableList<Object> itens)
    {
        this.itens = itens;
    }
    
    private boolean insertSelf()
    {
        String sql = "INSERT INTO Acerto(ace_codigo, ace_data, ace_obs, usu_login) ";
        String values = "VALUES(?, ?, ?, ?)";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql + values);

            codigo = Banco.getConexao().getMaxPK("Acerto", "ace_codigo") + 1;
            statement.setInt(1, codigo);
            statement.setDate(2, data);
            statement.setString(3, obs);
            statement.setString(4, user.getLogin());

            return statement.executeUpdate() > 0;
        }
        catch(SQLException ex)
        {
            Banco.getConexao().setMessagemErro(ex.getMessage());
        }

        return false;
    }
    
    public boolean insert()
    {
        boolean flag = insertSelf();
        
        if(flag)
        {
            for (int i = 0; flag && i < itens.size(); i++)
            {
                ItemAcerto item = (ItemAcerto)itens.get(i);
                
                item.setAcerto(this);
                flag = item.insert();
            }
        }
        
        return flag;
    }
    
    private boolean updateSelf()
    {
        String sql = "UPDATE Acerto SET ace_obs = ? WHERE ace_codigo = ?";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, obs);
            statement.setInt(2, codigo);

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
        boolean flag = updateSelf();
        
        if(flag)
        {
            flag = new ItemAcerto(this).clearItens();

            if(flag)
            {
                for (int i = 0; flag && i < itens.size(); i++)
                {
                    ItemAcerto item = (ItemAcerto)itens.get(i);

                    item.setAcerto(this);
                    flag = item.insert();
                }
            }
        }
        
        return flag;
    }
    
    private boolean deleteSelf()
    {
        String sql = "DELETE FROM Acerto WHERE ace_codigo = ?";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, codigo);
            
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
        boolean flag = new ItemAcerto(this).clearItens();
        
        if(flag)
            flag = deleteSelf();
        
        return flag;
    }
    
    public Object searchByCodigo()
    {
        Object obj = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Acerto WHERE ace_codigo = ?";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, codigo);
            
            ResultSet rs = statement.executeQuery();
            
            if(rs.next())
            {
                Usuario _user = (Usuario) new Usuario(rs.getString("usu_login"), "").searchByLogin().get(0);
                Acerto aqui = new Acerto(rs.getInt("ace_codigo"), rs.getDate("ace_data"), _user, rs.getString("ace_obs"));
                ObservableList<Object> itens = new ItemAcerto(aqui).searchByAcerto();
                
                aqui.setItens(itens);
                obj = aqui;
            }
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        
        return obj;
    }
    
    public ObservableList<Object> searchAll()
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Acerto";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            while(rs.next())
            {
                Usuario _user = (Usuario) new Usuario(rs.getString("usu_login"), "").searchByLogin().get(0);
                Acerto aqui = new Acerto(rs.getInt("ace_codigo"), rs.getDate("ace_data"), _user, rs.getString("ace_obs"));
                ObservableList<Object> itens = new ItemAcerto(aqui).searchByAcerto();
                
                aqui.setItens(itens);
                list.add(aqui);
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
        return "" + codigo;
    }
    
    public String toText()
    {
        String str = "Acerto - " + codigo + "\n" +
                     "Efetuada por [" + user.getLogin() + "]\n na data" + data.toString() + "\n\nItens da Aquisição\n";
        
        for (Object item : itens)
        {
            ItemAcerto obj = (ItemAcerto)item;
            
            str += obj.toString() + "\n";
        }
        
        return str;
    }
}
