package Classes;

import JDBC.Banco;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Categoria
{
    private int codigo;
    private String desc;

    public Categoria()
    {
    }

    public Categoria(int codigo)
    {
        this.codigo = codigo;
    }

    public Categoria(String desc)
    {
        this.desc = desc;
    }

    public Categoria(int codigo, String desc)
    {
        this.codigo = codigo;
        this.desc = desc;
    }

    public int getCodigo()
    {
        return codigo;
    }

    public void setCodigo(int codigo)
    {
        this.codigo = codigo;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }
    
    public boolean insert()
    {
        String sql = "INSERT INTO Categoria(cat_codigo, cat_desc) ";
        String values = "VALUES(?, ?)";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql + values);

            statement.setInt(1, codigo);
            statement.setString(2, desc);

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
        String sql = "UPDATE Categoria SET cat_desc = ? WHERE cat_codigo = ?";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, desc);
            statement.setInt(2, codigo);
            
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
        String sql = "DELETE FROM Categoria WHERE cat_codigo = ?";
        
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
    
    public Object searchByCodigo()
    {
        Object obj = null;
        String sql = "SELECT * FROM Categoria WHERE cat_codigo = " + codigo;
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            if(rs.next())
                obj = new Categoria(rs.getInt("cat_codigo"), rs.getString("cat_desc"));
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
        String sql = "SELECT * FROM Categoria";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            while(rs.next())
                list.add(new Categoria(rs.getInt("cat_codigo"), rs.getString("cat_desc")));
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
        return desc;
    }
}
