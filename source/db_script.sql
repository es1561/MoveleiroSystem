
CREATE TABLE public.TipoDespesa (
                tipo_des_codigo INTEGER NOT NULL,
                tipo_des_nome VARCHAR(50) NOT NULL,
                CONSTRAINT pk_tipodespesa PRIMARY KEY (tipo_des_codigo)
);


CREATE TABLE public.Categoria (
                cat_codigo INTEGER NOT NULL,
                cat_desc VARCHAR(50) NOT NULL,
                CONSTRAINT pk_categoria PRIMARY KEY (cat_codigo)
);


CREATE TABLE public.TipoMaterial (
                tipo_mat_codigo INTEGER NOT NULL,
                cat_codigo INTEGER NOT NULL,
                tipo_mat_desc VARCHAR(50) NOT NULL,
                CONSTRAINT pk_tipomaterial PRIMARY KEY (tipo_mat_codigo, cat_codigo)
);


CREATE TABLE public.Material (
                mat_codigo INTEGER NOT NULL,
                mat_nome VARCHAR(50) NOT NULL,
                mat_valor NUMERIC(10,2) NOT NULL,
                mat_desc VARCHAR(50),
                mat_estoque INTEGER NOT NULL,
                mat_estoque_min INTEGER NOT NULL,
                tipo_mat_codigo INTEGER NOT NULL,
                cat_codigo INTEGER NOT NULL,
                CONSTRAINT pk_material PRIMARY KEY (mat_codigo)
);


CREATE TABLE public.Cliente (
                cli_codigo INTEGER NOT NULL,
                cli_nome VARCHAR(50) NOT NULL,
                cli_cpf VARCHAR(20) NOT NULL,
                cli_fone VARCHAR(20) NOT NULL,
                cli_email VARCHAR(50),
                cli_cep VARCHAR(20) NOT NULL,
                cli_endereco VARCHAR(50) NOT NULL,
                cli_cidade VARCHAR(50) NOT NULL,
                CONSTRAINT pk_cliente PRIMARY KEY (cli_codigo)
);


CREATE TABLE public.Usuario (
                usu_login VARCHAR(20) NOT NULL,
                usu_fone VARCHAR(20) NOT NULL,
                usu_senha VARCHAR(20) NOT NULL,
                usu_nivel INTEGER NOT NULL,
                usu_nome VARCHAR(50) NOT NULL,
                CONSTRAINT pk_usuario PRIMARY KEY (usu_login)
);


CREATE TABLE public.Caixa (
                caixa_codigo VARCHAR(15) NOT NULL,
                caixa_data DATE NOT NULL,
                caixa_hora_a VARCHAR(15) NOT NULL,
                caixa_hora_f VARCHAR(15),
                caixa_valor_a NUMERIC(10,2) NOT NULL,
                caixa_valor_f NUMERIC(10,2),
                usu_login_a VARCHAR(20) NOT NULL,
                usu_login_f VARCHAR(20),
                caixa_status VARCHAR(1) NOT NULL,
                CONSTRAINT pk_caixa PRIMARY KEY (caixa_codigo)
);


CREATE TABLE public.Despesa (
                des_codigo INTEGER NOT NULL,
                tipo_des_codigo INTEGER NOT NULL,
                des_valor NUMERIC(10,2) NOT NULL,
                des_data DATE NOT NULL,
                caixa_codigo VARCHAR(15) NOT NULL,
                usu_login VARCHAR(20) NOT NULL,
                des_obs VARCHAR(50),
                CONSTRAINT pk_despesa PRIMARY KEY (des_codigo, tipo_des_codigo)
);


CREATE TABLE public.Acerto (
                ace_codigo INTEGER NOT NULL,
                usu_login VARCHAR(20) NOT NULL,
                ace_data DATE NOT NULL,
                ace_obs VARCHAR(50),
                CONSTRAINT pk_acerto PRIMARY KEY (ace_codigo, usu_login)
);


CREATE TABLE public.ItemAcerto (
                mat_codigo INTEGER NOT NULL,
                ace_codigo INTEGER NOT NULL,
                usu_login VARCHAR(20) NOT NULL,
                item_reci_quant INTEGER NOT NULL,
                CONSTRAINT pk_itemrecibo PRIMARY KEY (mat_codigo, ace_codigo, usu_login)
);


CREATE TABLE public.Venda (
                ven_codigo INTEGER NOT NULL,
                cli_codigo INTEGER NOT NULL,
                ven_data DATE NOT NULL,
                ven_total NUMERIC(10,2) NOT NULL,
                ven_parcelas INTEGER NOT NULL,
                usu_login VARCHAR(20) NOT NULL,
                CONSTRAINT pk_venda PRIMARY KEY (ven_codigo, cli_codigo)
);


CREATE TABLE public.ItemVenda (
                mat_codigo INTEGER NOT NULL,
                ven_codigo INTEGER NOT NULL,
                cli_codigo INTEGER NOT NULL,
                item_ven_valor NUMERIC(10,2) NOT NULL,
                item_ven_quant INTEGER NOT NULL,
                CONSTRAINT pk_itemvenda PRIMARY KEY (mat_codigo, ven_codigo, cli_codigo)
);


CREATE TABLE public.Recebimento (
                rec_codigo INTEGER NOT NULL,
                ven_codigo INTEGER NOT NULL,
                cli_codigo INTEGER NOT NULL,
                rec_valor NUMERIC(10,2) NOT NULL,
                rec_dt_venc NUMERIC NOT NULL,
                rec_dt_pag DATE NOT NULL,
                rec_parcela INTEGER NOT NULL,
                caixa_codigo VARCHAR(15) NOT NULL,
                CONSTRAINT pk_recebimento PRIMARY KEY (rec_codigo, ven_codigo, cli_codigo)
);


CREATE TABLE public.Fornecedores (
                for_cod INTEGER NOT NULL,
                for_nome VARCHAR(50) NOT NULL,
                for_cnpj VARCHAR(20),
                for_fone VARCHAR(20) NOT NULL,
                for_email VARCHAR(50),
                for_contato VARCHAR(50) NOT NULL,
                CONSTRAINT pk_fornecedores PRIMARY KEY (for_cod)
);


CREATE TABLE public.Aquisicao (
                aqui_codigo VARCHAR(20) NOT NULL,
                aqui_data DATE NOT NULL,
                aqui_total NUMERIC(10,2) NOT NULL,
                aqui_parcelas INTEGER NOT NULL,
                usu_login VARCHAR(20) NOT NULL,
                for_cod INTEGER NOT NULL,
                CONSTRAINT pk_aquisicao PRIMARY KEY (aqui_codigo)
);


CREATE TABLE public.Pagamento (
                pag_codigo INTEGER NOT NULL,
                aqui_codigo VARCHAR(20) NOT NULL,
                pag_valor NUMERIC(10,2) NOT NULL,
                pag_dt_venc DATE NOT NULL,
                pag_dt_pag DATE,
                pag_parcela INTEGER NOT NULL,
                caixa_codigo VARCHAR(15),
                CONSTRAINT pk_pagamento PRIMARY KEY (pag_codigo, aqui_codigo)
);


CREATE TABLE public.ItemAquisicao (
                aqui_codigo VARCHAR(20) NOT NULL,
                mat_codigo INTEGER NOT NULL,
                item_aqui_codigo INTEGER NOT NULL,
                item_aqui_valor NUMERIC(10,2) NOT NULL,
                item_aqui_quant INTEGER NOT NULL,
                CONSTRAINT pk_itemaquisicao PRIMARY KEY (aqui_codigo, mat_codigo, item_aqui_codigo)
);


ALTER TABLE public.Despesa ADD CONSTRAINT tipodespesa_despesa_fk
FOREIGN KEY (tipo_des_codigo)
REFERENCES public.TipoDespesa (tipo_des_codigo)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.TipoMaterial ADD CONSTRAINT categoria_tipomaterial_fk
FOREIGN KEY (cat_codigo)
REFERENCES public.Categoria (cat_codigo)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.Material ADD CONSTRAINT tipomaterial_material_fk
FOREIGN KEY (tipo_mat_codigo, cat_codigo)
REFERENCES public.TipoMaterial (tipo_mat_codigo, cat_codigo)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.ItemAquisicao ADD CONSTRAINT material_itemaquisicao_fk
FOREIGN KEY (mat_codigo)
REFERENCES public.Material (mat_codigo)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.ItemVenda ADD CONSTRAINT material_itemvenda_fk
FOREIGN KEY (mat_codigo)
REFERENCES public.Material (mat_codigo)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.ItemAcerto ADD CONSTRAINT material_itemrecibo_fk
FOREIGN KEY (mat_codigo)
REFERENCES public.Material (mat_codigo)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.Venda ADD CONSTRAINT cliente_venda_fk
FOREIGN KEY (cli_codigo)
REFERENCES public.Cliente (cli_codigo)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.Aquisicao ADD CONSTRAINT usuario_aquisicao_fk
FOREIGN KEY (usu_login)
REFERENCES public.Usuario (usu_login)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.Venda ADD CONSTRAINT usuario_venda_fk
FOREIGN KEY (usu_login)
REFERENCES public.Usuario (usu_login)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.Acerto ADD CONSTRAINT usuario_acerto_fk
FOREIGN KEY (usu_login)
REFERENCES public.Usuario (usu_login)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.Caixa ADD CONSTRAINT usuario_caixa_fk
FOREIGN KEY (usu_login_f)
REFERENCES public.Usuario (usu_login)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.Despesa ADD CONSTRAINT usuario_despesa_fk
FOREIGN KEY (usu_login)
REFERENCES public.Usuario (usu_login)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.Caixa ADD CONSTRAINT usuario_caixa_fk1
FOREIGN KEY (usu_login_a)
REFERENCES public.Usuario (usu_login)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.Pagamento ADD CONSTRAINT caixa_pagamento_fk
FOREIGN KEY (caixa_codigo)
REFERENCES public.Caixa (caixa_codigo)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.Recebimento ADD CONSTRAINT caixa_recebimento_fk
FOREIGN KEY (caixa_codigo)
REFERENCES public.Caixa (caixa_codigo)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.Despesa ADD CONSTRAINT caixa_despesa_fk
FOREIGN KEY (caixa_codigo)
REFERENCES public.Caixa (caixa_codigo)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.ItemAcerto ADD CONSTRAINT acerto_itemacerto_fk
FOREIGN KEY (ace_codigo, usu_login)
REFERENCES public.Acerto (ace_codigo, usu_login)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.Recebimento ADD CONSTRAINT venda_recebimento_fk
FOREIGN KEY (ven_codigo, cli_codigo)
REFERENCES public.Venda (ven_codigo, cli_codigo)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.ItemVenda ADD CONSTRAINT venda_itemvenda_fk
FOREIGN KEY (ven_codigo, cli_codigo)
REFERENCES public.Venda (ven_codigo, cli_codigo)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.Aquisicao ADD CONSTRAINT fornecedores_aquisicao_fk
FOREIGN KEY (for_cod)
REFERENCES public.Fornecedores (for_cod)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.ItemAquisicao ADD CONSTRAINT aquisicao_itemaquisicao_fk
FOREIGN KEY (aqui_codigo)
REFERENCES public.Aquisicao (aqui_codigo)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.Pagamento ADD CONSTRAINT aquisicao_pagamento_fk
FOREIGN KEY (aqui_codigo)
REFERENCES public.Aquisicao (aqui_codigo)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;
