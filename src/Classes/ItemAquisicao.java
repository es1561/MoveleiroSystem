package Classes;

import JDBC.Banco;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ItemAquisicao
{
    private Aquisicao aquisicao;
    private Material material;
    private int quant;
    private double valor;

    public ItemAquisicao()
    {
    }

    public ItemAquisicao(Aquisicao aquisicao)
    {
        this.aquisicao = aquisicao;
    }

    public ItemAquisicao(Material material, int quant, double valor)
    {
        this.material = material;
        this.quant = quant;
        this.valor = valor;
    }
    
    public ItemAquisicao(Aquisicao aquisicao, Material material, int quant, double valor)
    {
        this.aquisicao = aquisicao;
        this.material = material;
        this.quant = quant;
        this.valor = valor;
    }

    public Aquisicao getAquisicao()
    {
        return aquisicao;
    }

    public void setAquisicao(Aquisicao aquisicao)
    {
        this.aquisicao = aquisicao;
    }

    public Material getMaterial()
    {
        return material;
    }

    public void setMaterial(Material material)
    {
        this.material = material;
    }

    public int getQuant()
    {
        return quant;
    }

    public void setQuant(int quant)
    {
        this.quant = quant;
    }

    public double getValor()
    {
        return valor;
    }

    public double getTotal()
    {
        return quant * valor;
    }
    
    public void setValor(double valor)
    {
        this.valor = valor;
    }
    
    public boolean insert()
    {
        String sql = "INSERT INTO ItemAquisicao(item_aqui_codigo, aqui_codigo, mat_codigo, item_aqui_quant, item_aqui_valor) ";
        String values = "VALUES(?, ?, ?, ?, ?)";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql + values);

            statement.setInt(1, Banco.getConexao().getMaxPK("ItemAquisicao", "item_aqui_codigo") + 1);
            statement.setInt(2, aquisicao.getCodigo());
            statement.setInt(3, material.getCodigo());
            statement.setInt(4, quant);
            statement.setDouble(5, valor);
            

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
        String sql = "DELETE FROM ItemAquisicao WHERE aqui_codigo = " + aquisicao.getCodigo();
        
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
    
    public ObservableList<Object> searchByAquisicao()
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM ItemAquisicao WHERE aqui_codigo = " + aquisicao.getCodigo();
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            while(rs.next())
            {
                Material mat = (Material)new Material(rs.getInt("mat_codigo")).searchByCodigo();
                ItemAquisicao item = new ItemAquisicao(aquisicao, mat, rs.getInt("item_aqui_quant"), rs.getDouble("item_aqui_valor"));
                
                list.add(item);
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
        return quant + "x " + material.getNome();
    }
}
