package Classes;

import Controladoras.CtrTipoMaterial;
import JDBC.Banco;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Material
{
    private int codigo;
    private String nome;
    private double valor;
    private String desc;
    private int estoque;
    private int estoque_min;
    private TipoMaterial tipo;

    public Material()
    {
    }

    public Material(int codigo)
    {
        this.codigo = codigo;
    }

    public Material(String nome, double valor, String desc, int estoque, int estoque_min, Object tipo)
    {
        this.nome = nome;
        this.valor = valor;
        this.desc = desc;
        this.estoque = estoque;
        this.estoque_min = estoque_min;
        this.tipo = (TipoMaterial) tipo;
    }

    public Material(int codigo, String nome, double valor, String desc, int estoque, int estoque_min, Object tipo)
    {
        this.codigo = codigo;
        this.nome = nome;
        this.valor = valor;
        this.desc = desc;
        this.estoque = estoque;
        this.estoque_min = estoque_min;
        this.tipo = (TipoMaterial) tipo;
    }

    public int getCodigo()
    {
        return codigo;
    }

    public void setCodigo(int codigo)
    {
        this.codigo = codigo;
    }

    public String getNome()
    {
        return nome;
    }

    public void setNome(String nome)
    {
        this.nome = nome;
    }

    public double getValor()
    {
        return valor;
    }

    public void setValor(double valor)
    {
        this.valor = valor;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public int getEstoque()
    {
        return estoque;
    }

    public void setEstoque(int estoque)
    {
        this.estoque = estoque;
    }

    public int getEstoque_min()
    {
        return estoque_min;
    }

    public void setEstoque_min(int estoque_min)
    {
        this.estoque_min = estoque_min;
    }

    public TipoMaterial getTipo()
    {
        return tipo;
    }

    public void setTipo(TipoMaterial tipo)
    {
        this.tipo = tipo;
    }
    
    public String getTipoDesc()
    {
        return tipo.getDesc();
    }
    
    public Categoria getCategoria()
    {
        return this.tipo.getCategoria();
    }
    
    public String getCategoriaDesc()
    {
        return this.tipo.getCategoria().getDesc();
    }
    
    public boolean insert()
    {
        String sql = "INSERT INTO Material(mat_codigo, mat_nome, mat_valor, mat_desc, mat_estoque, mat_estoque_min, tipo_mat_codigo, cat_codigo) ";
        String values = "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql + values);

            statement.setInt(1, codigo);
            statement.setString(2, nome);
            statement.setDouble(3, valor);
            statement.setString(4, desc);
            statement.setInt(5, estoque);
            statement.setInt(6, estoque_min);
            statement.setInt(7, tipo.getCodigo());
            statement.setInt(8, tipo.getCategoria().getCodigo());

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
        String sql = "UPDATE Material SET mat_nome = ?, mat_valor = ?, mat_desc = ?, mat_estoque = ?, mat_estoque_min = ?, tipo_mat_codigo = ?, cat_codigo = ? WHERE mat_codigo = ?";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, nome);
            statement.setDouble(2, valor);
            statement.setString(3, desc);
            statement.setInt(4, estoque);
            statement.setInt(5, estoque_min);
            statement.setInt(6, tipo.getCodigo());
            statement.setInt(7, tipo.getCategoria().getCodigo());
            statement.setInt(8, codigo);

            return statement.executeUpdate() > 0;
        }
        catch(SQLException ex)
        {
            Banco.getConexao().setMessagemErro(ex.getMessage());
        }

        return false;
    }
    
    private void updateEstoque()
    {
        String sql = "SELECT mat_estoque FROM Material WHERE mat_codigo = " + codigo;
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            while(rs.next())
                estoque = rs.getInt("mat_estoque");
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
    
    public boolean addEstoque(int value)
    {
        String sql = "UPDATE Material SET mat_estoque = ? WHERE mat_codigo = ?";
        
        try
        {
            updateEstoque();
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, estoque + value);
            statement.setInt(2, codigo);

            if((estoque + value) >= 0)
                return statement.executeUpdate() > 0;
            else
                throw new SQLException("Estoque insuficiente!\nEstoque Atual: " + estoque + "; Entrada: " + Math.abs(value));
        }
        catch(SQLException ex)
        {
            Banco.getConexao().setMessagemErro(ex.getMessage());
        }

        return false;
    }
    
    public boolean delete()
    {
        String sql = "DELETE FROM Material WHERE mat_codigo = ?";
        
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
        String sql = "SELECT * FROM Material WHERE mat_codigo = " + codigo;
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            while(rs.next())
                obj = new Material(rs.getInt("mat_codigo"), rs.getString("mat_nome"), rs.getDouble("mat_valor"), rs.getString("mat_desc"), rs.getInt("mat_estoque"), rs.getInt("mat_estoque_min"), (TipoMaterial) CtrTipoMaterial.instancia().searchByCodigo(rs.getInt("tipo_mat_codigo")));
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        
        return obj;
    }
    
    public ObservableList<Object> searchByNome()
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Material WHERE LOWER(mat_nome) LIKE '" + nome + "%'";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            while(rs.next())
                list.add(new Material(rs.getInt("mat_codigo"), rs.getString("mat_nome"), rs.getDouble("mat_valor"), rs.getString("mat_desc"), rs.getInt("mat_estoque"), rs.getInt("mat_estoque_min"), (TipoMaterial) CtrTipoMaterial.instancia().searchByCodigo(rs.getInt("tipo_mat_codigo"))));
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        
        return list;
    }
    
    public ObservableList<Object> searchByCategoria()
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Material INNER JOIN Categoria ON Material.cat_codigo = Categoria.cat_codigo WHERE cat_desc = '" + desc + "'";
                
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            while(rs.next())
                list.add(new Material(rs.getInt("mat_codigo"), rs.getString("mat_nome"), rs.getDouble("mat_valor"), rs.getString("mat_desc"), rs.getInt("mat_estoque"), rs.getInt("mat_estoque_min"), (TipoMaterial) CtrTipoMaterial.instancia().searchByCodigo(rs.getInt("tipo_mat_codigo"))));
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        
        return list;
    }
    
    public ObservableList<Object> searchByTipo()
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        String sql = "SELECT # FROM Material INNER JOIN TipoMaterial ON Material.tipo_mat_codigo = TipoMaterial.tipo_mat_codigo WHERE tipo_mat_desc LIKE '" + desc + "%'";
        
        sql = sql.replace("#", "mat_codigo, mat_nome, mat_valor, mat_desc, mat_estoque, mat_estoque_min, Material.tipo_mat_codigo");
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            while(rs.next())
                list.add(new Material(rs.getInt("mat_codigo"), rs.getString("mat_nome"), rs.getDouble("mat_valor"), rs.getString("mat_desc"), rs.getInt("mat_estoque"), rs.getInt("mat_estoque_min"), (TipoMaterial) CtrTipoMaterial.instancia().searchByCodigo(rs.getInt("tipo_mat_codigo"))));
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
        String sql = "SELECT * FROM Material";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            while(rs.next())
                list.add(new Material(rs.getInt("mat_codigo"), rs.getString("mat_nome"), rs.getDouble("mat_valor"), rs.getString("mat_desc"), rs.getInt("mat_estoque"), rs.getInt("mat_estoque_min"), (TipoMaterial) CtrTipoMaterial.instancia().searchByCodigo(rs.getInt("tipo_mat_codigo"))));
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
        return nome;
    }
}
