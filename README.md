# Sistema de Biblioteca em Java

Projeto de estudo desenvolvido passo a passo para praticar os fundamentos da linguagem Java e da orientação a objetos.

## Tecnologias

- Java 25;
- API padrão do Java;
- nenhum framework ou dependência externa.

## Etapa atual

Na décima terceira etapa, o projeto possui:

- uma classe principal;
- uma classe que representa um livro;
- atributos privados e métodos públicos;
- cadastro de livros pelo terminal;
- armazenamento de vários livros em uma `ArrayList`;
- listagem dos livros cadastrados;
- validação de números com tratamento de exceções;
- validação de título e autor obrigatórios;
- validação do ano de publicação;
- classe `Menu` responsável pela interação no terminal;
- classe `Principal` responsável apenas por iniciar a aplicação;
- métodos `get`, `set`, `is` e `toString` seguindo convenções Java;
- validações também protegidas dentro da classe `Livro`;
- classe `Usuario` com código, nome e e-mail;
- cadastro e listagem de usuários;
- validação de formato de e-mail;
- bloqueio de e-mails duplicados;
- código sequencial e imutável para cada usuário;
- código sequencial e imutável para cada livro;
- busca de livros e usuários pelo código;
- uso de `Optional` para representar resultados de busca;
- empréstimo e devolução de livros;
- limite de um empréstimo ativo por usuário;
- controle de disponibilidade pela própria classe `Livro`;
- testes automatizados das regras de negócio em Java puro;
- persistência de livros, usuários e empréstimos em arquivo;
- restauração automática dos dados ao iniciar o programa;
- edição de livros e usuários pelo código;
- preservação do código identificador durante a edição;
- exclusão de livros e usuários com confirmação;
- bloqueio de exclusão quando existe empréstimo ativo;
- relatório de livros disponíveis;
- relatório de empréstimos ativos;
- resumo com totais da biblioteca;
- um menu executado no terminal;
- leitura da opção escolhida pelo usuário;
- repetição do menu até que o usuário escolha sair;
- criação dinâmica de objetos `Livro`.

## Regras de negócio

- a biblioteca pode armazenar qualquer quantidade de livros;
- cada usuário poderá ter somente um empréstimo ativo;
- um livro indisponível não poderá ser emprestado;
- após a devolução, o usuário poderá pegar outro livro e o livro devolvido ficará disponível novamente.

Essas regras são protegidas pelos métodos de negócio das classes `Usuario` e `Livro`.

## Persistência dos dados

O programa salva os dados automaticamente no arquivo `dados/biblioteca.properties`
após cadastros, empréstimos e devoluções. Ao iniciar novamente, os dados são carregados
sem necessidade de banco de dados ou biblioteca externa.

A classe `RepositorioDados` concentra a leitura e a gravação. Dessa forma, a classe
`Menu` continua cuidando da interação com o usuário e não precisa conhecer os detalhes
do formato do arquivo.

## Como executar

Primeiro, compile o programa:

```bash
javac -encoding UTF-8 src/*.java
```

Depois, execute:

```bash
java -cp src Principal
```

## Como executar os testes

Crie a pasta de saída e compile o projeto com os testes:

```bash
mkdir saida
javac -encoding UTF-8 -d saida src/*.java testes/*.java
```

Execute a suíte:

```bash
java -cp saida TesteRegrasNegocio
```

## Próximas etapas

1. Separar as operações da biblioteca da classe `Menu`.
