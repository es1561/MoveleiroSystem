package Classes;

import Controladoras.CtrCategoria;
import JDBC.Banco;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TipoMaterial
{
    private int codigo;
    private String desc;
    private Categoria categoria;

    public TipoMaterial()
    {
    }

    public TipoMaterial(int codigo)
    {
        this.codigo = codigo;
    }

    public TipoMaterial(Categoria categoria)
    {
        this.categoria = categoria;
    }

    public TipoMaterial(String desc, Categoria categoria)
    {
        this.desc = desc;
        this.categoria = categoria;
    }

    public TipoMaterial(int codigo, String desc, Categoria categoria)
    {
        this.codigo = codigo;
        this.desc = desc;
        this.categoria = categoria;
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

    public Categoria getCategoria()
    {
        return categoria;
    }

    public void setCategoria(Categoria categoria)
    {
        this.categoria = categoria;
    }
    
    public boolean insert()
    {
        String sql = "INSERT INTO TipoMaterial(tipo_mat_codigo, cat_codigo, tipo_mat_desc) ";
        String values = "VALUES(?, ?, ?)";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql + values);

            statement.setInt(1, codigo);
            statement.setInt(2, categoria.getCodigo());
            statement.setString(3, desc);

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
        String sql = "UPDATE TipoMaterial SET cat_codigo = ?, tipo_mat_desc = ? WHERE tipo_mat_codigo = ?";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, categoria.getCodigo());
            statement.setString(2, desc);
            statement.setInt(3, codigo);
            
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
        String sql = "DELETE FROM TipoMaterial WHERE tipo_mat_codigo = ?";
        
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
        String sql = "SELECT * FROM TipoMaterial WHERE tipo_mat_codigo = " + codigo;
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            if(rs.next())
                obj = new TipoMaterial(rs.getInt("tipo_mat_codigo"), rs.getString("tipo_mat_desc"), (Categoria) CtrCategoria.instancia().searchByCodigo(rs.getInt("cat_codigo")));
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        
        return obj;
    }
    
    public ObservableList<Object> searchbyCategoria()
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM TipoMaterial WHERE cat_codigo = " + categoria.getCodigo();
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            while(rs.next())
                list.add(new TipoMaterial(rs.getInt("tipo_mat_codigo"), rs.getString("tipo_mat_desc"), categoria));
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
        String sql = "SELECT * FROM TipoMaterial";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            while(rs.next())
                list.add(new TipoMaterial(rs.getInt("tipo_mat_codigo"), rs.getString("tipo_mat_desc"), (Categoria) CtrCategoria.instancia().searchByCodigo(rs.getInt("cat_codigo"))));
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
