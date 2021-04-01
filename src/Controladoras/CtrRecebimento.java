package Controladoras;

import Classes.Aquisicao;
import Classes.Venda;
import Classes.Caixa;
import Classes.Fornecedor;
import Classes.Recebimento;
import Classes.Usuario;
import JDBC.Banco;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import moveleirosystem.FXMLDocumentController;

public class CtrRecebimento
{
    private static CtrRecebimento instance = null;
    
    private CtrRecebimento()
    {
        
    }
    
    public static CtrRecebimento instancia()
    {
        if(instance == null)
            instance = new CtrRecebimento();
            
        return instance;
    }
    
    public static void finaliza()
    {
        instance = null;
    }
    
    public Object getField(Object obj, String campo)
    {
        Object field = "";
        
        if(obj != null)
        {
            if(campo.compareTo("codigo") == 0)
                field = ((Recebimento)obj).getCodigo();
            else if(campo.compareTo("venda") == 0)
                field = ((Recebimento)obj).getVenda();
            else if(campo.compareTo("valor") == 0)
                field = ((Recebimento)obj).getValor();
            else if(campo.compareTo("caixa") == 0)
                field = ((Recebimento)obj).getCaixa();
            else if(campo.compareTo("vencimento") == 0)
                field = ((Recebimento)obj).getDt_venc();
            else if(campo.compareTo("pagamento") == 0)
                field = ((Recebimento)obj).getDt_pag();
            else if(campo.compareTo("parcela") == 0)
                field = ((Recebimento)obj).getParcela();
            else if(campo.compareTo("text") == 0)
                field = ((Recebimento)obj).toText();
        }
        
        return field;
    }
    
    public Object makeObject(double valor, int parcela, Date dt_venc)
    {
        Object oc = new Caixa(Date.valueOf(LocalDate.now())).searchByToday();
        Object obj = null;
        
        if(oc != null)
            obj = new Recebimento(valor, parcela, dt_venc, (Caixa)oc);
        
        return obj;
    }
    
    public boolean insert(double valor, int parcela, Object venda)
    {
        boolean flag;        
        
        try
        {
            if(Banco.isConectado())
            {
                Object oc = new Caixa(Date.valueOf(LocalDate.now())).searchByToday();
                
                if(oc != null)
                {    
                    Recebimento obj = new Recebimento(valor, parcela, Date.valueOf(LocalDate.now().plusMonths(parcela)), (Venda)venda, (Caixa)oc);

                    flag = obj.insert();

                    if(flag)
                        Banco.getConexao().getConnection().commit();
                    else
                    {
                        Banco.getConexao().getConnection().rollback();
                        throw new SQLException("Erro ao registrar Venda!");
                    }
                }
                else
                    throw new SQLException("Caixa Fechado...");
                
            }
            else
                throw new SQLException("Banco Off-Line...");
        }
        catch(SQLException ex)
        {
            flag = false;
            Banco.getConexao().setMessagemErro(ex.getMessage());
            System.out.println(ex.getMessage());
        }
        
        return flag;
    }
    
    public boolean receber(int codigo, double valor)
    {
        boolean flag;        
        
        try
        {
            if(Banco.isConectado())
            {
                Object oc = new Caixa(Date.valueOf(LocalDate.now())).searchByToday();
                
                if(oc != null)
                {       
                    Recebimento obj = new Recebimento(codigo, Date.valueOf(LocalDate.now()));

                    flag = obj.pagar();

                    if(flag)
                        Banco.getConexao().getConnection().commit();
                    else
                    {
                        Banco.getConexao().getConnection().rollback();
                        throw new SQLException("Erro ao efetuar Recebimento!");
                    }
                }
                else
                    throw new SQLException("Caixa Fechado...");
                
            }
            else
                throw new SQLException("Banco Off-Line...");
        }
        catch(SQLException ex)
        {
            flag = false;
            Banco.getConexao().setMessagemErro(ex.getMessage());
            System.out.println(ex.getMessage());
        }
        
        return flag;
    }
    
    public ObservableList<Object> searchAllOpen()
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        
        try
        {
            if(Banco.isConectado())
                list = new Recebimento().searchAllOpen();
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
    
    public ObservableList<Object> searchByVenda(Object venda)
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        
        try
        {
            if(Banco.isConectado())
                list = new Recebimento((Venda)venda).searchByVenda();
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
    
    public ObservableList<Object> searchAll()
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        
        try
        {
            if(Banco.isConectado())
                list = new Recebimento().searchAll();
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
