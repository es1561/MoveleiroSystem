package JDBC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Banco
{

    static private Conexao con = null;
    private static final String port = "5432";

    private Banco()
    {
    }

    public static Conexao getConexao()
    {
        return con;
    }

    static public boolean conectar()
    {
        boolean flag = true;

        if (con == null)
        {
            con = new Conexao();

            flag = con.conectar("jdbc:postgresql://localhost:" + port + "/", "dbmoveleiro", "postgres", "postgres123");

            try
            {
                if (con.getEstadoConexao())
                    con.getConnection().setAutoCommit(false);
            } catch (Exception ex)
            {
                System.out.println(ex.getMessage());
            }
        }

        return flag;
    }

    static public boolean isConectado()
    {
        return con.getEstadoConexao();
    }

    public static boolean criarBD(String BD)
    {
        try
        {
            //Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:" + port + "/";
            Connection connect = DriverManager.getConnection(url, "postgres", "postgres123");

            Statement statement = connect.createStatement();
            statement.execute("CREATE DATABASE " + BD + "\n"
                    + "    WITH \n"
                    + "    OWNER = postgres\n"
                    + "    ENCODING = 'UTF8'\n"
                    + "    CONNECTION LIMIT = -1;");
            statement.close();
            connect.close();

        } catch (Exception e)
        {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    private static void deletaBD(String BD)
    {
        try
        {
            //Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:" + port + "/";
            Connection connect = DriverManager.getConnection(url, "postgres", "postgres123");

            Statement statement = connect.createStatement();
            statement.execute("DELETE DATABASE " + BD);
            statement.close();
            connect.close();

        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    public static boolean criarTabelas(String BD)
    {
        try
        {
            //Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:" + port + "/" + BD;
            Connection connect = DriverManager.getConnection(url, "postgres", "postgres123");

            try
            {
                Statement statement = connect.createStatement();

                statement.execute("CREATE TABLE public.TipoDespesa (\n"
                        + "                tipo_des_codigo INTEGER NOT NULL,\n"
                        + "                tipo_des_nome VARCHAR(50) NOT NULL,\n"
                        + "                tipo_des_desc VARCHAR(50),\n"
                        + "                CONSTRAINT pk_tipodespesa PRIMARY KEY (tipo_des_codigo)\n"
                        + ");\n"
                        + "\n"
                        + "\n"
                        + "CREATE TABLE public.Caixa (\n"
                        + "                caixa_data DATE NOT NULL,\n"
                        + "                caixa_valor_ini NUMERIC(10,2) NOT NULL,\n"
                        + "                caixa_valor_fin NUMERIC(10,2) NOT NULL,\n"
                        + "                CONSTRAINT pk_caixa PRIMARY KEY (caixa_data)\n"
                        + ");\n"
                        + "\n"
                        + "\n"
                        + "CREATE TABLE public.Despesa (\n"
                        + "                des_codigo INTEGER NOT NULL,\n"
                        + "                caixa_data DATE NOT NULL,\n"
                        + "                des_valor NUMERIC(10,2) NOT NULL,\n"
                        + "                des_venc DATE NOT NULL,\n"
                        + "                tipo_des_codigo INTEGER NOT NULL,\n"
                        + "                CONSTRAINT pk_despesa PRIMARY KEY (des_codigo, caixa_data)\n"
                        + ");\n"
                        + "\n"
                        + "\n"
                        + "CREATE TABLE public.Categoria (\n"
                        + "                cat_codigo INTEGER NOT NULL,\n"
                        + "                cat_desc VARCHAR(50) NOT NULL,\n"
                        + "                CONSTRAINT pk_categoria PRIMARY KEY (cat_codigo)\n"
                        + ");\n"
                        + "\n"
                        + "\n"
                        + "CREATE TABLE public.TipoMaterial (\n"
                        + "                tipo_mat_codigo INTEGER NOT NULL,\n"
                        + "                cat_codigo INTEGER NOT NULL,\n"
                        + "                tipo_mat_desc VARCHAR(50) NOT NULL,\n"
                        + "                CONSTRAINT pk_tipomaterial PRIMARY KEY (tipo_mat_codigo, cat_codigo)\n"
                        + ");\n"
                        + "\n"
                        + "\n"
                        + "CREATE TABLE public.Material (\n"
                        + "                mat_codigo INTEGER NOT NULL,\n"
                        + "                mat_nome VARCHAR(50) NOT NULL,\n"
                        + "                mat_valor NUMERIC(10,2) NOT NULL,\n"
                        + "                mat_desc VARCHAR(50),\n"
                        + "                mat_estoque INTEGER NOT NULL,\n"
                        + "                mat_estoque_min INTEGER NOT NULL,\n"
                        + "                tipo_mat_codigo INTEGER NOT NULL,\n"
                        + "                cat_codigo INTEGER NOT NULL,\n"
                        + "                CONSTRAINT pk_material PRIMARY KEY (mat_codigo)\n"
                        + ");\n"
                        + "\n"
                        + "\n"
                        + "CREATE TABLE public.Cliente (\n"
                        + "                cli_codigo INTEGER NOT NULL,\n"
                        + "                cli_nome VARCHAR(50) NOT NULL,\n"
                        + "                cli_cpf VARCHAR(20) NOT NULL,\n"
                        + "                cli_fone VARCHAR(20) NOT NULL,\n"
                        + "                cli_email VARCHAR(50),\n"
                        + "                cli_cep VARCHAR(20) NOT NULL,\n"
                        + "                cli_endereco VARCHAR(50) NOT NULL,\n"
                        + "                cli_cidade VARCHAR(50) NOT NULL,\n"
                        + "                CONSTRAINT pk_cliente PRIMARY KEY (cli_codigo)\n"
                        + ");\n"
                        + "\n"
                        + "\n"
                        + "CREATE TABLE public.Usuario (\n"
                        + "                usu_login VARCHAR(20) NOT NULL,\n"
                        + "                usu_fone VARCHAR(20) NOT NULL,\n"
                        + "                usu_senha VARCHAR(20) NOT NULL,\n"
                        + "                usu_nivel INTEGER NOT NULL,\n"
                        + "                usu_nome VARCHAR(50) NOT NULL,\n"
                        + "                CONSTRAINT pk_usuario PRIMARY KEY (usu_login)\n"
                        + ");\n"
                        + "\n"
                        + "\n"
                        + "CREATE TABLE public.Acerto (\n"
                        + "                ace_codigo INTEGER NOT NULL,\n"
                        + "                usu_login VARCHAR(20) NOT NULL,\n"
                        + "                ace_data DATE NOT NULL,\n"
                        + "                ace_obs VARCHAR(50),\n"
                        + "                CONSTRAINT pk_acerto PRIMARY KEY (ace_codigo, usu_login)\n"
                        + ");\n"
                        + "\n"
                        + "\n"
                        + "CREATE TABLE public.ItemRecibo (\n"
                        + "                ace_codigo INTEGER NOT NULL,\n"
                        + "                mat_codigo INTEGER NOT NULL,\n"
                        + "                item_reci_quant INTEGER NOT NULL,\n"
                        + "                CONSTRAINT pk_itemrecibo PRIMARY KEY (ace_codigo, mat_codigo)\n"
                        + ");\n"
                        + "\n"
                        + "\n"
                        + "CREATE TABLE public.Venda (\n"
                        + "                ven_codigo INTEGER NOT NULL,\n"
                        + "                cli_codigo INTEGER NOT NULL,\n"
                        + "                ven_data DATE NOT NULL,\n"
                        + "                ven_total NUMERIC(10,2) NOT NULL,\n"
                        + "                ven_parcelas INTEGER NOT NULL,\n"
                        + "                usu_login VARCHAR(20) NOT NULL,\n"
                        + "                CONSTRAINT pk_venda PRIMARY KEY (ven_codigo, cli_codigo)\n"
                        + ");\n"
                        + "\n"
                        + "\n"
                        + "CREATE TABLE public.ItemVenda (\n"
                        + "                mat_codigo INTEGER NOT NULL,\n"
                        + "                ven_codigo INTEGER NOT NULL,\n"
                        + "                item_ven_valor NUMERIC(10,2) NOT NULL,\n"
                        + "                item_ven_quant INTEGER NOT NULL,\n"
                        + "                CONSTRAINT pk_itemvenda PRIMARY KEY (mat_codigo, ven_codigo)\n"
                        + ");\n"
                        + "\n"
                        + "\n"
                        + "CREATE TABLE public.Fornecedores (\n"
                        + "                for_cod INTEGER NOT NULL,\n"
                        + "                for_nome VARCHAR(50) NOT NULL,\n"
                        + "                for_cnpj VARCHAR(20),\n"
                        + "                for_fone VARCHAR(20) NOT NULL,\n"
                        + "                for_email VARCHAR(50),\n"
                        + "                for_contato VARCHAR(50) NOT NULL,\n"
                        + "                CONSTRAINT pk_fornecedores PRIMARY KEY (for_cod)\n"
                        + ");\n"
                        + "\n"
                        + "\n"
                        + "CREATE TABLE public.Aquisicao (\n"
                        + "                aqui_codigo INTEGER NOT NULL,\n"
                        + "                aqui_data DATE NOT NULL,\n"
                        + "                aqui_total NUMERIC(10,2) NOT NULL,\n"
                        + "                aqui_parcelas INTEGER NOT NULL,\n"
                        + "                usu_login VARCHAR(20) NOT NULL,\n"
                        + "                for_cod INTEGER NOT NULL,\n"
                        + "                CONSTRAINT pk_aquisicao PRIMARY KEY (aqui_codigo)\n"
                        + ");\n"
                        + "\n"
                        + "\n"
                        + "CREATE TABLE public.Recebimento (\n"
                        + "                rec_codigo INTEGER NOT NULL,\n"
                        + "                ven_codigo INTEGER NOT NULL,\n"
                        + "                rec_valor NUMERIC(10,2) NOT NULL,\n"
                        + "                rec_dt_venc NUMERIC NOT NULL,\n"
                        + "                rec_dt_pag DATE NOT NULL,\n"
                        + "                rec_parcela INTEGER NOT NULL,\n"
                        + "                caixa_data DATE NOT NULL,\n"
                        + "                aqui_codigo INTEGER NOT NULL,\n"
                        + "                CONSTRAINT pk_recebimento PRIMARY KEY (rec_codigo, ven_codigo)\n"
                        + ");\n"
                        + "\n"
                        + "\n"
                        + "CREATE TABLE public.Pagamento (\n"
                        + "                pag_codigo INTEGER NOT NULL,\n"
                        + "                aqui_codigo INTEGER NOT NULL,\n"
                        + "                pag_valor NUMERIC(10,2) NOT NULL,\n"
                        + "                pag_dt_venc DATE NOT NULL,\n"
                        + "                pag_dt_pag DATE,\n"
                        + "                pag_parcela INTEGER NOT NULL,\n"
                        + "                caixa_data DATE NOT NULL,\n"
                        + "                CONSTRAINT pk_pagamento PRIMARY KEY (pag_codigo, aqui_codigo)\n"
                        + ");\n"
                        + "\n"
                        + "\n"
                        + "CREATE TABLE public.ItemAquisicao (\n"
                        + "                aqui_codigo INTEGER NOT NULL,\n"
                        + "                mat_codigo INTEGER NOT NULL,\n"
                        + "                item_aqui_valor NUMERIC(10,2) NOT NULL,\n"
                        + "                item_aqui_quant INTEGER NOT NULL,\n"
                        + "                CONSTRAINT pk_itemaquisicao PRIMARY KEY (aqui_codigo, mat_codigo)\n"
                        + ");\n"
                        + "\n"
                        + "\n"
                        + "ALTER TABLE public.Despesa ADD CONSTRAINT tipodespesa_despesa_fk\n"
                        + "FOREIGN KEY (tipo_des_codigo)\n"
                        + "REFERENCES public.TipoDespesa (tipo_des_codigo)\n"
                        + "ON DELETE NO ACTION\n"
                        + "ON UPDATE NO ACTION\n"
                        + "NOT DEFERRABLE;\n"
                        + "\n"
                        + "ALTER TABLE public.Pagamento ADD CONSTRAINT caixa_pagamento_fk\n"
                        + "FOREIGN KEY (caixa_data)\n"
                        + "REFERENCES public.Caixa (caixa_data)\n"
                        + "ON DELETE NO ACTION\n"
                        + "ON UPDATE NO ACTION\n"
                        + "NOT DEFERRABLE;\n"
                        + "\n"
                        + "ALTER TABLE public.Recebimento ADD CONSTRAINT caixa_recebimento_fk\n"
                        + "FOREIGN KEY (caixa_data)\n"
                        + "REFERENCES public.Caixa (caixa_data)\n"
                        + "ON DELETE NO ACTION\n"
                        + "ON UPDATE NO ACTION\n"
                        + "NOT DEFERRABLE;\n"
                        + "\n"
                        + "ALTER TABLE public.Despesa ADD CONSTRAINT caixa_despesa_fk\n"
                        + "FOREIGN KEY (caixa_data)\n"
                        + "REFERENCES public.Caixa (caixa_data)\n"
                        + "ON DELETE NO ACTION\n"
                        + "ON UPDATE NO ACTION\n"
                        + "NOT DEFERRABLE;\n"
                        + "\n"
                        + "ALTER TABLE public.TipoMaterial ADD CONSTRAINT categoria_tipomaterial_fk\n"
                        + "FOREIGN KEY (cat_codigo)\n"
                        + "REFERENCES public.Categoria (cat_codigo)\n"
                        + "ON DELETE NO ACTION\n"
                        + "ON UPDATE NO ACTION\n"
                        + "NOT DEFERRABLE;\n"
                        + "\n"
                        + "ALTER TABLE public.Material ADD CONSTRAINT tipomaterial_material_fk\n"
                        + "FOREIGN KEY (tipo_mat_codigo, cat_codigo)\n"
                        + "REFERENCES public.TipoMaterial (tipo_mat_codigo, cat_codigo)\n"
                        + "ON DELETE NO ACTION\n"
                        + "ON UPDATE NO ACTION\n"
                        + "NOT DEFERRABLE;\n"
                        + "\n"
                        + "ALTER TABLE public.ItemAquisicao ADD CONSTRAINT material_itemaquisicao_fk\n"
                        + "FOREIGN KEY (mat_codigo)\n"
                        + "REFERENCES public.Material (mat_codigo)\n"
                        + "ON DELETE NO ACTION\n"
                        + "ON UPDATE NO ACTION\n"
                        + "NOT DEFERRABLE;\n"
                        + "\n"
                        + "ALTER TABLE public.ItemVenda ADD CONSTRAINT material_itemvenda_fk\n"
                        + "FOREIGN KEY (mat_codigo)\n"
                        + "REFERENCES public.Material (mat_codigo)\n"
                        + "ON DELETE NO ACTION\n"
                        + "ON UPDATE NO ACTION\n"
                        + "NOT DEFERRABLE;\n"
                        + "\n"
                        + "ALTER TABLE public.ItemRecibo ADD CONSTRAINT material_itemrecibo_fk\n"
                        + "FOREIGN KEY (mat_codigo)\n"
                        + "REFERENCES public.Material (mat_codigo)\n"
                        + "ON DELETE NO ACTION\n"
                        + "ON UPDATE NO ACTION\n"
                        + "NOT DEFERRABLE;\n"
                        + "\n"
                        + "ALTER TABLE public.Venda ADD CONSTRAINT cliente_venda_fk\n"
                        + "FOREIGN KEY (cli_codigo)\n"
                        + "REFERENCES public.Cliente (cli_codigo)\n"
                        + "ON DELETE NO ACTION\n"
                        + "ON UPDATE NO ACTION\n"
                        + "NOT DEFERRABLE;\n"
                        + "\n"
                        + "ALTER TABLE public.Aquisicao ADD CONSTRAINT usuario_aquisicao_fk\n"
                        + "FOREIGN KEY (usu_login)\n"
                        + "REFERENCES public.Usuario (usu_login)\n"
                        + "ON DELETE NO ACTION\n"
                        + "ON UPDATE NO ACTION\n"
                        + "NOT DEFERRABLE;\n"
                        + "\n"
                        + "ALTER TABLE public.Venda ADD CONSTRAINT usuario_venda_fk\n"
                        + "FOREIGN KEY (usu_login)\n"
                        + "REFERENCES public.Usuario (usu_login)\n"
                        + "ON DELETE NO ACTION\n"
                        + "ON UPDATE NO ACTION\n"
                        + "NOT DEFERRABLE;\n"
                        + "\n"
                        + "ALTER TABLE public.Acerto ADD CONSTRAINT usuario_recibo_fk\n"
                        + "FOREIGN KEY (usu_login)\n"
                        + "REFERENCES public.Usuario (usu_login)\n"
                        + "ON DELETE NO ACTION\n"
                        + "ON UPDATE NO ACTION\n"
                        + "NOT DEFERRABLE;\n"
                        + "\n"
                        + "ALTER TABLE public.ItemRecibo ADD CONSTRAINT recibo_itemrecibo_fk\n"
                        + "FOREIGN KEY (ace_codigo)\n"
                        + "REFERENCES public.Acerto (ace_codigo)\n"
                        + "ON DELETE NO ACTION\n"
                        + "ON UPDATE NO ACTION\n"
                        + "NOT DEFERRABLE;\n"
                        + "\n"
                        + "ALTER TABLE public.ItemVenda ADD CONSTRAINT venda_itemvenda_fk\n"
                        + "FOREIGN KEY (ven_codigo)\n"
                        + "REFERENCES public.Venda (ven_codigo)\n"
                        + "ON DELETE NO ACTION\n"
                        + "ON UPDATE NO ACTION\n"
                        + "NOT DEFERRABLE;\n"
                        + "\n"
                        + "ALTER TABLE public.Recebimento ADD CONSTRAINT venda_recebimento_fk\n"
                        + "FOREIGN KEY (ven_codigo)\n"
                        + "REFERENCES public.Venda (ven_codigo)\n"
                        + "ON DELETE NO ACTION\n"
                        + "ON UPDATE NO ACTION\n"
                        + "NOT DEFERRABLE;\n"
                        + "\n"
                        + "ALTER TABLE public.Aquisicao ADD CONSTRAINT fornecedores_aquisicao_fk\n"
                        + "FOREIGN KEY (for_cod)\n"
                        + "REFERENCES public.Fornecedores (for_cod)\n"
                        + "ON DELETE NO ACTION\n"
                        + "ON UPDATE NO ACTION\n"
                        + "NOT DEFERRABLE;\n"
                        + "\n"
                        + "ALTER TABLE public.ItemAquisicao ADD CONSTRAINT aquisicao_itemaquisicao_fk\n"
                        + "FOREIGN KEY (aqui_codigo)\n"
                        + "REFERENCES public.Aquisicao (aqui_codigo)\n"
                        + "ON DELETE NO ACTION\n"
                        + "ON UPDATE NO ACTION\n"
                        + "NOT DEFERRABLE;\n"
                        + "\n"
                        + "ALTER TABLE public.Pagamento ADD CONSTRAINT aquisicao_pagamento_fk\n"
                        + "FOREIGN KEY (aqui_codigo)\n"
                        + "REFERENCES public.Aquisicao (aqui_codigo)\n"
                        + "ON DELETE NO ACTION\n"
                        + "ON UPDATE NO ACTION\n"
                        + "NOT DEFERRABLE;\n"
                        + "\n"
                        + "ALTER TABLE public.Recebimento ADD CONSTRAINT aquisicao_recebimento_fk\n"
                        + "FOREIGN KEY (aqui_codigo)\n"
                        + "REFERENCES public.Aquisicao (aqui_codigo)\n"
                        + "ON DELETE NO ACTION\n"
                        + "ON UPDATE NO ACTION\n"
                        + "NOT DEFERRABLE;");

                connect.close();
            } 
            catch (SQLException ex)
            {
                System.out.println(ex.getCause());
                deletaBD(BD);
            }

        } catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public static void backup()
    {
        try
        {
            executeBAT("copiar.bat");
            new Alert(Alert.AlertType.INFORMATION, "Backup realizado com sucesso!", ButtonType.OK).showAndWait();
        } catch (IOException ex)
        {
            new Alert(Alert.AlertType.ERROR, "Erro no backup!" + ex.getMessage(), ButtonType.OK).showAndWait();
        }

    }

    public static void backup(String path)
    {
        try
        {
            DateTimeFormatter form = DateTimeFormatter.ofPattern("dd_MM_uuuu");

            String st = ".\\bkp\\pg_dump.exe --dbname=postgresql://postgres:postgres123@localhost:" + port
                    + "/dbpapelaria --format custom --blobs --verbose --file \"" + path
                    + "\\" + form.format(LocalDate.now()) + ".sql\"";
            System.out.println(st);
            Process p = Runtime.getRuntime().exec(st);

            if (p != null)
            {
                InputStreamReader str = new InputStreamReader(p.getErrorStream());
                BufferedReader reader = new BufferedReader(str);
                String linha;
                while ((linha = reader.readLine()) != null)
                    System.out.println(linha);
            }
            new Alert(Alert.AlertType.INFORMATION, "Backup realizado com sucesso!", ButtonType.OK).showAndWait();

        } catch (Exception ex)
        {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erro no backup!" + ex.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    public static void restore()
    {
        try
        {
            executeBAT("restaurar.bat");
            new Alert(Alert.AlertType.INFORMATION, "Restauração realizada com sucesso!", ButtonType.OK).showAndWait();
        } catch (IOException ex)
        {
            new Alert(Alert.AlertType.ERROR, "Erro na restauração!" + ex.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    public static void restore(String path)
    {
        try
        {
            String st = ".\\bkp\\pg_restore --clean --exit-on-error --verbose --dbname=postgresql://postgres:postgres123@localhost:" + port + "/dbpapelaria \"" + path + "\"";
            System.out.println(st);
            Process p = Runtime.getRuntime().exec(st);

            if (p != null)
            {
                InputStreamReader str = new InputStreamReader(p.getErrorStream());
                BufferedReader reader = new BufferedReader(str);
                String linha;
                while ((linha = reader.readLine()) != null)
                    System.out.println(linha);
            }
            new Alert(Alert.AlertType.INFORMATION, "Restauração realizada com sucesso!", ButtonType.OK).showAndWait();

        } catch (Exception ex)
        {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erro na restauração" + ex.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    private static void executeBAT(String name) throws IOException
    {
        Runtime r = Runtime.getRuntime();
        Process p = r.exec("bkp\\" + name);
        if (p != null)
        {
            InputStreamReader str = new InputStreamReader(p.getErrorStream());
            BufferedReader reader = new BufferedReader(str);
            String linha;
            while ((linha = reader.readLine()) != null)
                System.out.println(linha);
        }
    }
}
