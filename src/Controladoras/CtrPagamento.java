package Controladoras;

import Classes.Aquisicao;
import Classes.Caixa;
import Classes.Fornecedor;
import Classes.Pagamento;
import Classes.Usuario;
import JDBC.Banco;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import moveleirosystem.FXMLDocumentController;

public class CtrPagamento
{
    private static CtrPagamento instance = null;
    
    private CtrPagamento()
    {
        
    }
    
    public static CtrPagamento instancia()
    {
        if(instance == null)
            instance = new CtrPagamento();
            
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
                field = ((Pagamento)obj).getCodigo();
            else if(campo.compareTo("aquisicao") == 0)
                field = ((Pagamento)obj).getAquisicao();
            else if(campo.compareTo("valor") == 0)
                field = ((Pagamento)obj).getValor();
            else if(campo.compareTo("caixa") == 0)
                field = ((Pagamento)obj).getCaixa();
            else if(campo.compareTo("vencimento") == 0)
                field = ((Pagamento)obj).getDt_venc();
            else if(campo.compareTo("pagamento") == 0)
                field = ((Pagamento)obj).getDt_pag();
            else if(campo.compareTo("parcela") == 0)
                field = ((Pagamento)obj).getParcela();
        }
        
        return field;
    }
    
    public Object makeObject(double valor, int parcela, Date dt_venc)
    {
        Object oc = new Caixa(Date.valueOf(LocalDate.now())).searchByToday();
        Object obj = null;
        
        if(oc != null)
            obj = new Pagamento(valor, parcela, dt_venc, (Caixa)oc);
        
        return obj;
    }
    
    public boolean insert(double valor, int parcela, Object aqui)
    {
        boolean flag;        
        
        try
        {
            if(Banco.isConectado())
            {
                Object oc = new Caixa(Date.valueOf(LocalDate.now())).searchByToday();
                
                if(oc != null)
                {    
                    Pagamento obj = new Pagamento(valor, parcela, Date.valueOf(LocalDate.now().plusMonths(parcela)), (Aquisicao)aqui, (Caixa)oc);

                    flag = obj.insert();

                    if(flag)
                        Banco.getConexao().getConnection().commit();
                    else
                    {
                        Banco.getConexao().getConnection().rollback();
                        throw new SQLException("Erro ao registrar Pagamento!");
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
    
    public boolean pagar(int codigo, double valor)
    {
        boolean flag;        
        
        try
        {
            if(Banco.isConectado())
            {
                Object oc = new Caixa(Date.valueOf(LocalDate.now())).searchByToday();
                
                if(oc != null)
                {       
                    if((((Caixa)oc).getSaldo() - valor) >= 0)
                    {
                        Pagamento obj = new Pagamento(codigo, Date.valueOf(LocalDate.now()));

                        flag = obj.pagar();

                        if(flag)
                            Banco.getConexao().getConnection().commit();
                        else
                        {
                            Banco.getConexao().getConnection().rollback();
                            throw new SQLException("Erro ao efetuar Pagamento!");
                        }
                    }
                    else
                        throw new SQLException("Fundos insuficiente...");
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
                list = new Pagamento().searchAllOpen();
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
    
    public ObservableList<Object> searchByAquisicao(Object aquisicao)
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        
        try
        {
            if(Banco.isConectado())
                list = new Pagamento((Aquisicao)aquisicao).searchByAquisicao();
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
                list = new Pagamento().searchAll();
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
