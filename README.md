# Estudo de KMP e CMP com API de Livros - OpenLibrary

Este projeto tem como objetivo explorar e entender as estruturas e funcionalidades do **KMP (Kotlin Multiplatform)** e **CMP (Compose Multiplataform)**. Foi utilizado uma **API pública de livros**, a [OpenLibrary](https://openlibrary.org/), que não exige autenticação. O foco é entender como funcionam essas tecnologias no contexto de uma aplicação prática, com ênfase na arquitetura de software e boas práticas de desenvolvimento.

Atualmente, o projeto está em fase inicial e novos recursos serão adicionados à medida que o estudo se desenvolve.

## Arquitetura e Práticas

A arquitetura do projeto segue os seguintes padrões e práticas recomendadas para garantir uma estrutura escalável e manutenível:

-   **MVVM (Model-View-ViewModel)**: Padrão arquitetural que separa a lógica de negócios da interface de usuário, facilitando a manutenção e os testes.
-   **Clean Architecture**: Organização do código em camadas, promovendo a independência entre elas e garantindo alta testabilidade.
-   **UDF (Fluxo de Dados Unidirecional)**: O fluxo de dados entre a **View** e a **ViewModel** segue um padrão unidirecional, garantindo que os dados sejam gerenciados de forma previsível e controlada. Isso evita estados inconsistentes e facilita a rastreabilidade das mudanças.
-   **Clean Code**: Foi adotado práticas de **Clean Code** para garantir que o código seja claro, legível e fácil de entender e modificar. Isso inclui nomes de variáveis descritivos, funções pequenas e focadas, e a remoção de duplicações. O objetivo é garantir que o código permaneça simples e de fácil manutenção ao longo do tempo.

Além disso, foi seguido as seguintes **práticas de desenvolvimento** para melhorar a qualidade e a sustentabilidade do código:

-   **DRY (Don't Repeat Yourself)**: Evitamos duplicação de código, buscando sempre a reutilização de componentes e funções sempre que possível, o que ajuda a manter o código mais limpo e organizado.
-   **KISS (Keep It Simple, Stupid)**: Mantemos o código simples, evitando complexidade desnecessária. Isso torna o código mais legível e facilita a manutenção.
-   **Single Responsibility Principle (SRP)**: Cada classe ou componente tem uma única responsabilidade, o que melhora a coesão do código e facilita testes, manutenção e extensão.

## Funcionalidades

-   Consumo da **API pública de livros** da OpenLibrary para exibir informações sobre os livros.
-   Armazenamento local utilizando o **Room** para persistência de dados.
-   Suporte multiplataforma, com versões para **Android**, **IOS** e **Desktop**.
-   Injeção de dependências usando o **Koin** para garantir flexibilidade e modularidade.
-   Implementação de interfaces utilizando o **Jetpack Compose** e **Compose Multiplataforma**.

## Tecnologias Utilizadas

-   **Kotlin**: Linguagem principal do projeto.
-   **Jetpack Compose**: Framework moderno para construção de interfaces declarativas.
-   **Compose Multiplataforma**: Suporte multiplataforma para Jetpack Compose.
-   **Room**: Biblioteca para persistência de dados local.
-   **Koin**: Framework para injeção de dependências.
-   **Ktor**: Framework para chamadas HTTP assíncronas.
-   **Coil**: Biblioteca para carregamento e exibição de imagens.
-   **Coroutines**: Para operações assíncronas e gerenciamento de threads.
-   **Mockk**: Biblioteca de mocking para Kotlin, usada para criar mocks e stubs durante os testes.

## Como Executar o Projeto

1.  Clone o repositório para sua máquina local:
    
    `git clone https://github.com/jorgemiguel7/CMP-Bookpedia.git` 
    
2.  Abra o projeto no Android Studio ou Fleet
    
3.   Para rodar o projeto em **Android**:
    
    -   Basta selecionar um dispositivo Android (emulador ou dispositivo físico) e executar a aplicação diretamente no Android Studio.
4.   Para rodar o projeto em **Desktop**:
    
    -   // No terminal, dentro do diretório do projeto, execute o seguinte comando para iniciar a aplicação:
        ./gradlew run 
        
5.   Para rodar o projeto em **iOS**:
    
    -   // Certifique-se de ter o Xcode instalado em sua máquina.
    -   Abra o projeto no Xcode e selecione o simulador ou um dispositivo físico iOS.
    -   Execute a aplicação diretamente no Xcode.
