# Sistema de Biblioteca em Java

Projeto de estudo desenvolvido passo a passo para praticar os fundamentos da linguagem Java e da orientação a objetos.

## Tecnologias

- Java 25;
- API padrão do Java;
- nenhum framework ou dependência externa.

## Etapa atual

Na sexta etapa, o projeto possui:

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
- um menu executado no terminal;
- leitura da opção escolhida pelo usuário;
- repetição do menu até que o usuário escolha sair;
- criação dinâmica de objetos `Livro`.

## Regras de negócio planejadas

- a biblioteca pode armazenar qualquer quantidade de livros;
- cada usuário poderá ter somente um empréstimo ativo;
- um livro indisponível não poderá ser emprestado;
- após a devolução, o usuário poderá pegar outro livro e o livro devolvido ficará disponível novamente.

Essas regras serão implementadas com uma classe `Usuario` e métodos de empréstimo e devolução. Elas permanecem documentadas enquanto a funcionalidade ainda não está pronta.

## Como executar

Primeiro, compile o programa:

```bash
javac -encoding UTF-8 src/*.java
```

Depois, execute:

```bash
java -cp src Principal
```

## Próximas etapas

1. Buscar livros e usuários pelo código.
2. Emprestar e devolver livros.
3. Salvar os dados em arquivo.
4. Adicionar testes automatizados.
