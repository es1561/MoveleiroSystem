package Classes;

import JDBC.Banco;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ItemAcerto
{
    private Acerto acerto;
    private Material material;
    private int quant;

    public ItemAcerto()
    {
    }

    public ItemAcerto(Acerto acerto)
    {
        this.acerto = acerto;
    }

    public ItemAcerto(Material material, int quant)
    {
        this.material = material;
        this.quant = quant;
    }
    
    public ItemAcerto(Acerto acerto, Material material, int quant)
    {
        this.acerto = acerto;
        this.material = material;
        this.quant = quant;
    }

    public Acerto getAcerto()
    {
        return acerto;
    }

    public void setAcerto(Acerto acerto)
    {
        this.acerto = acerto;
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

    
    
    public boolean insert()
    {
        String sql = "INSERT INTO ItemAcerto(ace_codigo, mat_codigo, item_reci_quant) ";
        String values = "VALUES(?, ?, ?)";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql + values);

            statement.setInt(1, acerto.getCodigo());
            statement.setInt(2, material.getCodigo());
            statement.setInt(3, quant);
            

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
        String sql = "DELETE FROM ItemAcerto WHERE ace_codigo = ?";
        
        try
        {
            ObservableList<Object> list = new ItemAcerto(acerto).searchByAcerto();
            boolean flag = false;

            if(list.size() > 0)
            {
                flag = true;
                for (int i = 0; flag && i < list.size(); i++)
                {
                    ItemAcerto item = (ItemAcerto)list.get(i);

                    item.setAcerto(acerto);
                    flag = item.getMaterial().addEstoque(item.getQuant());
                }
            }
            
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, acerto.getCodigo());
            
            return flag && statement.executeUpdate() > 0;
        }
        catch(SQLException ex)
        {
            Banco.getConexao().setMessagemErro(ex.getMessage());
        }

        return false;
    }
    
    public ObservableList<Object> searchByAcerto()
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM ItemAcerto WHERE ace_codigo = ?";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, acerto.getCodigo());
            
            ResultSet rs = statement.executeQuery();
            
            while(rs.next())
            {
                Material mat = (Material)new Material(rs.getInt("mat_codigo")).searchByCodigo();
                ItemAcerto item = new ItemAcerto(acerto, mat, rs.getInt("item_reci_quant"));
                
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
        return (quant > 0 ? "-" : "+") + Math.abs(quant) + " " + material.getNome();
    }
}
