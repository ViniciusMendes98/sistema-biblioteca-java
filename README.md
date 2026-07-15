# Sistema de Biblioteca em Java

Projeto de estudo desenvolvido passo a passo para praticar os fundamentos da linguagem Java e da orientação a objetos.

## Tecnologias

- Java 25;
- API padrão do Java;
- nenhum framework ou dependência externa.

## Etapa atual

Na terceira etapa, o projeto possui:

- uma classe principal;
- uma classe que representa um livro;
- atributos privados e métodos públicos;
- cadastro de livros pelo terminal;
- armazenamento de vários livros em uma `ArrayList`;
- listagem dos livros cadastrados;
- um menu executado no terminal;
- leitura da opção escolhida pelo usuário;
- repetição do menu até que o usuário escolha sair;
- criação dinâmica de objetos `Livro`.

## Como executar

Primeiro, compile o programa:

```bash
javac -encoding UTF-8 src/Livro.java src/Principal.java
```

Depois, execute:

```bash
java -cp src Principal
```

## Próximas etapas

1. Validar os dados digitados pelo usuário.
2. Buscar livros.
3. Emprestar e devolver livros.
4. Salvar os dados em arquivo.
5. Adicionar testes automatizados.
