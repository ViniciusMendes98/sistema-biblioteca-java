# Sistema de Biblioteca em Java

Projeto de estudo desenvolvido passo a passo para praticar os fundamentos da linguagem Java e da orientação a objetos.

## Tecnologias

- Java 25;
- API padrão do Java;
- nenhum framework ou dependência externa.

## Funcionalidades

- cadastro de livros pelo terminal;
- cadastro e listagem de usuários;
- busca de livros e usuários pelo código;
- empréstimo e devolução de livros;
- edição de livros e usuários pelo código;
- exclusão de livros e usuários com confirmação;
- relatório de livros disponíveis;
- relatório de empréstimos ativos;
- resumo com totais da biblioteca;
- persistência automática em arquivo;
- testes automatizados em Java puro.

## Regras de negócio

- a biblioteca pode armazenar qualquer quantidade de livros;
- cada usuário poderá ter somente um empréstimo ativo;
- um livro indisponível não poderá ser emprestado;
- após a devolução, o usuário poderá pegar outro livro e o livro devolvido ficará disponível novamente.
- e-mails de usuários não poderão ser repetidos;
- livros emprestados e usuários com empréstimo ativo não poderão ser excluídos;
- códigos são sequenciais, imutáveis e não são reutilizados após uma exclusão.

Essas regras são protegidas pelos métodos de negócio das classes `Biblioteca`, `Usuario` e `Livro`.

## Persistência dos dados

O programa salva os dados automaticamente no arquivo `dados/biblioteca.properties`
após cadastros, empréstimos e devoluções. Ao iniciar novamente, os dados são carregados
sem necessidade de banco de dados ou biblioteca externa.

A classe `RepositorioDados` concentra a leitura e a gravação. Dessa forma, a classe
`Menu` continua cuidando da interação com o usuário e não precisa conhecer os detalhes
do formato do arquivo.

## Arquitetura MVC

O projeto utiliza uma versão simples do padrão MVC:

- **Model (`modelo`)**: `Biblioteca`, `Livro`, `Usuario` e `DadosBiblioteca` representam os dados e protegem as regras de negócio;
- **View (`visao`)**: `VisaoConsole` lê entradas e apresenta informações no terminal;
- **Controller (`controlador`)**: `BibliotecaControlador` recebe as opções da View e coordena as operações do Model;
- **Repositório (`repositorio`)**: `RepositorioDados` salva e carrega o arquivo, como uma camada de apoio ao MVC;
- **Aplicação (`aplicacao`)**: `Principal` cria os objetos e inicia o sistema.

```text
src/
|-- aplicacao/
|   `-- Principal.java
|-- controlador/
|   `-- BibliotecaControlador.java
|-- modelo/
|   |-- Biblioteca.java
|   |-- DadosBiblioteca.java
|   |-- Livro.java
|   `-- Usuario.java
|-- repositorio/
|   `-- RepositorioDados.java
`-- visao/
    `-- VisaoConsole.java
```

## Conceitos praticados

- orientação a objetos e encapsulamento;
- classes, construtores, métodos, `get`, `set` e `toString`;
- coleções com `List` e `ArrayList`;
- tratamento de exceções;
- `Optional` para resultados de busca;
- leitura e escrita de arquivos;
- separação de responsabilidades;
- testes automatizados sem framework externo.

## Como executar

Primeiro, compile o programa:

```bash
javac -encoding UTF-8 -d saida src/modelo/*.java src/repositorio/*.java src/visao/*.java src/controlador/*.java src/aplicacao/*.java
```

Depois, execute:

```bash
java -cp saida aplicacao.Principal
```

## Como executar os testes

Crie a pasta de saída e compile o projeto com os testes:

```bash
mkdir saida
javac -encoding UTF-8 -d saida src/modelo/*.java src/repositorio/*.java src/visao/*.java src/controlador/*.java src/aplicacao/*.java testes/*.java
```

Execute a suíte:

```bash
java -cp saida testes.TesteRegrasNegocio
```

## Status

MVP concluído. O projeto cobre o fluxo principal de uma biblioteca de terminal e está
pronto para receber melhorias futuras sem depender delas para demonstrar seu funcionamento.
