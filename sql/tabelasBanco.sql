create table "DDI" (
	nro_ddi int primary key
);

create table "DDD"(
	nro_ddd int primary key
);

create table "FoneUsuario"(
	nro_telefone_usuario varchar(16),
	nro_ddi int,
	nro_ddd int,
	id_usuario int not null,
	primary key (nro_telefone_usuario, nro_ddd, nro_ddi),
	foreign key (nro_ddd) references "DDD" (nro_ddd),
	foreign key (nro_ddi) references "DDI" (nro_ddi),
	foreign key (id_usuario) references "Usuario" (id_usuario)
);

create table "EmailUsuario"(
	id_email_usuario serial primary key,
	email_usuario varchar(100),
	id_usuario int not null,
	foreign key (id_usuario) references "Usuario" (id_usuario)
);

create table "TipoLogradouro"(
	sigla_tipo_logradouro char(2) primary key,
	nome_tipo_logradouro varchar(32) not null
);

create table "UnidadeFederativa"(
	sigla_uf char(2) primary key,
	nome_uf varchar(50) not null
);

create table "Bairro"(
	id_bairro serial primary key,
	nome_bairro varchar(100) not null
);

create table "Cidade"(
	id_cidade serial primary key,
	nome_cidade varchar(50) not null,
	sigla_uf char(2) not null,
	foreign key (sigla_uf) references "UnidadeFederativa" (sigla_uf)
);

create table "Logradouro"(
	id_logradouro serial primary key,
	nome_logradouro varchar(100) not null,
	sigla_tipo_logradouro char(2) not null,
	foreign key (sigla_tipo_logradouro) references "TipoLogradouro" (sigla_tipo_logradouro)
);

create table "Endereco"(
	id_endereco serial primary key,
	cep varchar(10) not null,
	id_cidade int not null,
	id_logradouro int not null,
	id_bairro int not null,
	foreign key (id_cidade) references "Cidade" (id_cidade),
	foreign key (id_logradouro) references "Logradouro" (id_logradouro),
	foreign key (id_bairro) references "Bairro" (id_bairro)
);

create table "Usuario"(
	id_usuario serial primary key,
	nome_usuario varchar(100),
	login varchar(100),
	senha varchar(100),
	crmv varchar(10) null,
	nro_endereco int,
	complemento_endereco varchar(100),
	id_tipo_usuario int not null,
	id_endereco int not null,
	foreign key (id_tipo_usuario) references "TipoUsuario" (id_tipo_usuario),
	foreign key (id_endereco) references "Endereco" (id_endereco)
);

create table "TipoUsuario"(
	id_tipo_usuario serial primary key,
	descricao_tipo_usuario varchar(100)
);

create table "Vacina"(
	id_vacina serial primary key,
	nome_vacina varchar(32),
	descricao_vacina varchar(100)
);

create table "VacinaAplicada"(
	id_vacina_aplicada serial primary key,
	data_aplicacao date,
	dose varchar(10),
	observacao varchar(100),
	id_pet int not null,
	id_vacina int not null,
	id_veterinario int not null,
	foreign key (id_pet) references "Pet" (id_pet),
	foreign key (id_vacina) references "Vacina" (id_vacina),
	foreign key (id_veterinario) references "Usuario" (id_usuario)
);

create table "Pet"(
	id_pet serial primary key,
	nome_pet varchar(50),
	data_nascimento date,
	id_tutor int not null,
	id_especie int not null,
	id_raca int not null,
	id_sexo int not null,
	foreign key (id_tutor) references "Usuario" (id_usuario),
	foreign key (id_especie) references "Especie" (id_especie),
	foreign key (id_raca) references "Raca" (id_raca),
	foreign key (id_sexo) references "Sexo" (id_sexo)
);

create table "Especie"(
	id_especie serial primary key,
	descricao_especie varchar(50)
);

create table "Raca"(
	id_raca serial primary key,
	descricao_raca varchar(50)
);

create table "Sexo"(
	id_sexo serial primary key,
	descricao_sexo varchar(50)
);

create table "Consulta"(
	id_consulta serial primary key,
	data_consulta date,
	hora_consulta varchar(5),
	motivo varchar(50),
	id_pet int not null,
	id_veterinario int not null,
	id_atendente int not null,
	foreign key (id_pet) references "Pet" (id_pet),
	foreign key (id_veterinario) references "Usuario" (id_usuario),
	foreign key (id_atendente) references "Usuario" (id_usuario)
);
