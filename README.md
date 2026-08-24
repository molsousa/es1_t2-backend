# Implementação da camada de negócios.

**Matéria:** Engenharia de Software I.

**Professor:** Roberto Gil Brasil.

**Estudante:** [Marcos Sousa](https://github.com/molsousa).

--- 

## Descrição

Este trabalho contém a implementação da camada de negócios para o segundo trabalho da disciplina de Engenharia de Software.

## Implementação

O código foi todo feito em Java separado em pacotes para organização e modularização do código-fonte. O código é executado com o servidor Glassfish, tanto para conexão com o SGBD (`postgresql`), quanto para ligar com a camada de apresentação.

## Estrutura dos pacotes 

```ASCII
es1_t2-backend/
├──src/
│   └──backend/
│       └──java/
│           └──com/
│               └──clinica/
│                   ├─col/
│                   │  ├─PetCol.java
│                   │  ├─TutorCol.java
│                   │  └─...(mais 2)
│                   ├─controller/
│                   │  ├─PetController.java
│                   │  ├─TutorController.java
│                   │  └─...(mais 12)
│                   ├─dao/
│                   │  ├─PetDAO.java
│                   │  ├─TutorDAO.java
│                   │  └─...(mais 15)
│                   ├─dto/
│                   │  ├─TelefoneDTO.java
│                   │  ├─TutorCompletoDTO.java
│                   │  └─...(mais 1)
│                   ├─factory/
│                   │  └─ConnectionFactory.java
│                   ├─filter
│                   │  └─CorsFilter.java
│                   ├─model/
│                   │  ├─Pet.java
│                   │  ├─Tutor.java
│                   │  └─...(mais 11)
│                   └─servicos/
│                      ├─PetService.java
│                      ├─TutorService.java
│                      └─...(mais 8)
│ 
└──sql/  
    └──tabelasBanco.sql
```

## Sobre o código-fonte

- O SGBD se conecta com o Glassfish diretamente, não sendo feito conexão explícita em código-fonte.

- Os serviços abrem transação com o banco e fecham após a realização de determinado cadastro.

- Tem serviços incompletos e não implementados, ficando pendentes sua conclusão, como as consultas médicas.

## Descrição dos pacotes

- ### col 

Pacote que guarda classes de validação de dados.

- ### controller 

Pacote que guarda classes que conversam diretamente com a camada de apresentação.

- ### dao 

Pacote que guarda classes que preparam um código SQL e junto com a classe `ConnectionFactory` executa comandos SQL no banco.

- ### dto 

Faz uma coleção de dados sem consulta direta no SGBD, facilitando a recuperação de certos dados para preparar visualização na camada de apresentação.

- ### factory 

Faz a conexão direta com o SGBD.

- ### filter 

Pacote que guarda uma classe que facilita comunicação com a camada de visualização.

- ### model

Pacotes que guardam classes para armazenar e acessar dados temporariamente de modelos.

- ### servicos

Pacotes que guardam os serviços solicitados na camada de apresentação.

## Comunicação entre principais pacotes 

Ao receber um `POST` da camada de apresentação, os dados brutos chegam ao `Controller` correspondente, e lá é chamado um serviço, que valida o dado antes de qualquer execução. 

Caso seja um serviço de transação, é aberto conexão com o banco e então é chamado um método `DAO`, e sendo consulta, cadastro ou exclusão, é executado um código SQL fazendo conexão com o método estático `ConnectionFactory.getConnection()` para manipular o dado no banco da maneira adequada. 

Após a manipulação do dado no banco, o método no pacote `DAO` retorna ao serviço que fecha a transação (se aberta), retornando ao `Controller` e na camada de apresentação mostra a operação bem sucedida ao usuário.

--- 


