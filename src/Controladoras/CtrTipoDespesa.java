package Controladoras;

import Classes.TipoDespesa;
import JDBC.Banco;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CtrTipoDespesa
{
    private static CtrTipoDespesa instance = null;
    
    private CtrTipoDespesa()
    {
        
    }
    
    public static CtrTipoDespesa instancia()
    {
        if(instance == null)
            instance = new CtrTipoDespesa();
            
        return instance;
    }
    
    public static void finaliza()
    {
        instance = null;
    }
    
    public String getField(Object obj, String campo)
    {
        String field = "";
        
        if(campo.compareTo("codigo") == 0)
            field = "" + ((TipoDespesa)obj).getCodigo();
        else if(campo.compareTo("nome") == 0)
            field = ((TipoDespesa)obj).getNome();
        
        return field;
    }
    
    public Object searchByCodigo(int codigo)
    {
        Object obj = null;
        
        try
        {
            if(Banco.isConectado())
                obj = new TipoDespesa(codigo).searchByCodigo();
            else
                throw new SQLException("Banco Off-Line...");
        }
        catch(SQLException ex)
        {
            Banco.getConexao().setMessagemErro(ex.getMessage());
            System.out.println(ex.getMessage());
        }
        
        return obj;
    }
    
    public ObservableList<Object> searchAll()
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        
        try
        {
            if(Banco.isConectado())
                list = new TipoDespesa().searchAll();
            else
                throw new SQLException("Banco Off-Line...");
        }
        catch(SQLException ex)
        {
            Banco.getConexao().setMessagemErro(ex.getMessage());
            System.out.println(ex.getMessage());
        }
        
        return list;
    }
}
