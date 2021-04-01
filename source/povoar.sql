delete from itemvenda
delete from itemaquisicao
delete from recebimento
delete from pagamento
delete from aquisicao
delete from venda
delete from despesa
delete from caixa

insert into cliente values(1, 'Cliente A', '123.456.789-10', '', '', '', 'Rua A', 'Cidade A');
insert into cliente values(2, 'Cliente B', '987.654.321-09', '', '', '', 'Rua B', 'Cidade A');

insert into fornecedores values(1, 'Fornecedor A', '', '', '', 'Fulano A');
insert into fornecedores values(2, 'Fornecedor B', '', '', '', 'Fulano B');

insert into categoria values(1, 'Categoria A');
insert into categoria values(2, 'Categoria B');

insert into tipomaterial values(1, 1, 'Tipo AA');
insert into tipomaterial values(2, 1, 'Tipo BA');
insert into tipomaterial values(3, 2, 'Tipo AB');
insert into tipomaterial values(4, 2, 'Tipo BB');

insert into material values(1, 'Material A', 1.0, '', 10, 10, 1, 1);
insert into material values(2, 'Material B', 1.0, '', 10, 10, 2, 1);
insert into material values(3, 'Material C', 1.0, '', 10, 10, 3, 2);
insert into material values(4, 'Material D', 1.0, '', 10, 10, 4, 2);

insert into tipodespesa values(1, 'Suprimento');
insert into tipodespesa values(2, 'Sangria');
insert into tipodespesa values(3, 'Agua');
insert into tipodespesa values(4, 'Energia');
insert into tipodespesa values(5, 'Internet');
insert into tipodespesa values(6, 'Outros');