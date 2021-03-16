delete from itemaquisicao;
delete from pagamento;
delete from despesa;
delete from aquisicao;
delete from caixa;
update material set mat_estoque = 10

insert into fornecedores values(1, 'Fornecedor A', '', '', '', '');
insert into fornecedores values(2, 'Fornecedor B', '', '', '', '');

insert into tipodespesa values(1, 'Suprimento');
insert into tipodespesa values(2, 'Sangria');
insert into tipodespesa values(3, 'Tipo A');
insert into tipodespesa values(4, 'Tipo B');

insert into categoria values(1, 'Categoria A');
insert into categoria values(2, 'Categoria B');

insert into tipomaterial values(1, 1, 'Tipo A');
insert into tipomaterial values(2, 1, 'Tipo B');
insert into tipomaterial values(3, 2, 'Tipo C');
insert into tipomaterial values(4, 2, 'Tipo D');

insert into material values(1, 'Material A', 1.5, '', 10, 10, 1, 1);
insert into material values(2, 'Material B', 2.5, '', 10, 10, 2, 1);
insert into material values(3, 'Material C', 3.5, '', 10, 10, 3, 2);
insert into material values(4, 'Material D', 4.5, '', 10, 10, 4, 2);

select * from aquisicao
select * from pagamento
