package Classes;

import JDBC.Banco;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ItemVenda
{
    private Venda venda;
    private Material material;
    private int quant;
    private double valor;

    public ItemVenda()
    {
    }

    public ItemVenda(Venda venda)
    {
        this.venda = venda;
    }

    public ItemVenda(Material material, int quant, double valor)
    {
        this.material = material;
        this.quant = quant;
        this.valor = valor;
    }
    
    public ItemVenda(Venda venda, Material material, int quant, double valor)
    {
        this.venda = venda;
        this.material = material;
        this.quant = quant;
        this.valor = valor;
    }

    public Venda getVenda()
    {
        return venda;
    }

    public void setVenda(Venda venda)
    {
        this.venda = venda;
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

    public void setValor(double valor)
    {
        this.valor = valor;
    }

    public double getTotal()
    {
        return quant * valor;
    }
    
    public boolean insert()
    {
        String sql = "INSERT INTO ItemVenda(item_ven_codigo, ven_codigo, mat_codigo, item_ven_quant, item_ven_valor) ";
        String values = "VALUES(?, ?, ?, ?, ?)";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql + values);

            statement.setInt(1, Banco.getConexao().getMaxPK("ItemVenda", "item_ven_codigo") + 1);
            statement.setInt(2, venda.getCodigo());
            statement.setInt(3, material.getCodigo());
            statement.setInt(4, quant);
            statement.setDouble(5, valor);
            

            return statement.executeUpdate() > 0 && material.addEstoque(-quant);
        }
        catch(SQLException ex)
        {
            Banco.getConexao().setMessagemErro(ex.getMessage());
        }

        return false;
    }
    
    public boolean clearItens()
    {
        String sql = "DELETE FROM ItemVenda WHERE ven_codigo = ?";
        
        try
        {
            ObservableList<Object> list = new ItemVenda(venda).searchByVenda();
            boolean flag = false;

            if(list.size() > 0)
            {
                flag = true;
                for (int i = 0; flag && i < list.size(); i++)
                {
                    ItemVenda item = (ItemVenda)list.get(i);

                    item.setVenda(venda);
                    flag = item.getMaterial().addEstoque(item.getQuant());
                }
            }
            
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, venda.getCodigo());
            
            return flag && statement.executeUpdate() > 0;
        }
        catch(SQLException ex)
        {
            Banco.getConexao().setMessagemErro(ex.getMessage());
        }

        return false;
    }
    
    public ObservableList<Object> searchByVenda()
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM ItemVenda WHERE ven_codigo = ?";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, venda.getCodigo());
            
            ResultSet rs = statement.executeQuery();
            
            while(rs.next())
            {
                Material mat = (Material)new Material(rs.getInt("mat_codigo")).searchByCodigo();
                ItemVenda item = new ItemVenda(venda, mat, rs.getInt("item_ven_quant"), rs.getDouble("item_ven_valor"));
                
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
