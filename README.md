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

## Organização das classes

- `Principal`: inicia a aplicação;
- `Menu`: recebe dados e apresenta mensagens no terminal;
- `Biblioteca`: gerencia coleções, códigos, buscas e exclusões;
- `Livro` e `Usuario`: protegem os dados e as regras de negócio;
- `RepositorioDados`: salva e carrega o arquivo;
- `DadosBiblioteca`: transporta os dados entre a biblioteca e o repositório.

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

## Status

MVP concluído. O projeto cobre o fluxo principal de uma biblioteca de terminal e está
pronto para receber melhorias futuras sem depender delas para demonstrar seu funcionamento.
