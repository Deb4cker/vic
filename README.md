[🇧🇷 Português](#PT-BR) | [🇺🇸 English](#EN-US)
# VPL Implementation Checker (VIC)

---

#### PT-BR

O VPL Implementation Checker é uma aplicação desenvolvida para ser utilizada em ambientes VPL dentro do Moodle. Sua principal função é avaliar automaticamente implementações Java enviadas por alunos, com base em diagramas de classe fornecidos por professores no formato .uxf. O professor disponibiliza o diagrama, e o sistema verifica se o código do aluno segue corretamente a estrutura, os relacionamentos e os requisitos definidos no modelo original.

## 📦 Instalação e Execução Local

O projeto VIC é um projeto Java modular baseado em Maven. Para executá-lo localmente, siga as instruções para a sua IDE preferida:

### Importando um Projeto Maven com Múltiplos Módulos

Independentemente da IDE, o processo geral para importar um projeto Maven com múltiplos módulos é o seguinte:

1.  **Abrir/Importar Projeto Maven**: Selecione a opção para abrir ou importar um projeto Maven existente.
2.  **Navegar até o `pom.xml` principal**: O arquivo `pom.xml` principal está na raiz do projeto (onde este `README.md` está localizado).
3.  **Aguardar a Sincronização**: A IDE irá detectar automaticamente os módulos e suas dependências. Aguarde a sincronização completa do projeto.

### IntelliJ IDEA

1.  **Abrir Projeto**: Vá em `File` > `Open`.
2.  **Selecionar `pom.xml`**: Navegue até a pasta raiz do projeto VIC e selecione o arquivo `pom.xml` principal. O IntelliJ irá reconhecer automaticamente o projeto Maven e seus módulos.
3.  **Configurar SDK**: Certifique-se de que um SDK Java (JDK) esteja configurado para o projeto.

### Eclipse

1.  **Importar Projeto Maven**: Vá em `File` > `Import...` > `Maven` > `Existing Maven Projects` e clique em `Next`.
2.  **Selecionar Diretório Raiz**: Clique em `Browse...` e selecione a pasta raiz do projeto VIC. O Eclipse detectará o `pom.xml` e os módulos.
3.  **Finalizar Importação**: Clique em `Finish`.

### NetBeans

1.  **Abrir Projeto**: Vá em `File` > `Open Project...`.
2.  **Navegar até o Projeto**: Navegue até a pasta raiz do projeto VIC e selecione-a. O NetBeans reconhecerá o projeto Maven.
3.  **Abrir**: Clique em `Open Project`.

### Visual Studio Code

1.  **Abrir Pasta**: Vá em `File` > `Open Folder...`.
2.  **Selecionar Pasta Raiz**: Selecione a pasta raiz do projeto VIC.
3.  **Instalar Extensões (se necessário)**: Certifique-se de ter as extensões Java Extension Pack e Maven for Java instaladas para um suporte completo.
4.  **Ativar Suporte Maven**: O VS Code geralmente detecta o projeto Maven automaticamente. Você pode usar a aba `Maven` no Explorer para gerenciar os módulos.

### Uso com a pasta `local-dev/`

O projeto VIC utiliza uma pasta `local-dev/` na raiz para simular entradas durante o desenvolvimento local. Você pode usar o parâmetro `--dev` de diferentes formas:

*   `-d`: Executa todos os arquivos `.uxf` encontrados em todas as subpastas dentro de `local-dev/`.
*   `-d <nome-da-pasta>`: Executa todos os arquivos `.uxf` dentro da subpasta especificada em `local-dev/`.
*   `-d <nome-da-pasta> <nome-do-arquivo-uxf>`: Executa apenas o arquivo `.uxf` específico dentro da subpasta indicada em `local-dev/`.

## 📌 Futuras Melhorias

Para organizar o desenvolvimento e o rastreamento de novas funcionalidades e bugs, issues serão abertas futuramente no repositório do projeto.

## 🤝 Como Contribuir

Sua contribuição é muito bem-vinda! Siga os passos abaixo para colaborar com o projeto VIC:

1.  **Fork do Projeto**: Faça um fork deste repositório para sua conta do GitHub.
2.  **Clonar o Repositório**: Clone o seu fork para sua máquina local:
    ```bash
    git clone https://github.com/seu-usuario/VIC.git
    ```
3.  **Criar uma Branch**: Crie uma nova branch para suas alterações:
    ```bash
    git checkout -b feature/sua-nova-feature
    ```
4.  **Submeter Pull Request**: Após fazer suas alterações e testá-las, submeta um Pull Request para a branch `main` deste repositório.

## 📄 Licença

Este projeto está licenciado sob a Licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---
# VPL Implementation Checker (VIC)

---

#### EN-US
The VPL Implementation Checker is an application designed to run within VPL environments on Moodle. Its main purpose is to automatically evaluate Java implementations submitted by students, based on class diagrams provided by instructors in the .uxf format. The teacher submits the diagram, and the system checks whether the student's code correctly follows the structure, relationships, and requirements defined in the original model.

## 📦 Installation and Local Execution

The VIC project is a modular Java project based on Maven. To run it locally, follow the instructions for your preferred IDE:

### Importing a Multi-Module Maven Project

Regardless of the IDE, the general process for importing a multi-module Maven project is as follows:

1.  **Open/Import Maven Project**: Select the option to open or import an existing Maven project.
2.  **Navigate to the Main `pom.xml`**: The main `pom.xml` file is located at the root of the project (where this `README.md` is located).
3.  **Wait for Synchronization**: The IDE will automatically detect the modules and their dependencies. Wait for the project to fully synchronize.

### IntelliJ IDEA

1.  **Open Project**: Go to `File` > `Open`.
2.  **Select `pom.xml`**: Navigate to the root folder of the VIC project and select the main `pom.xml` file. IntelliJ will automatically recognize the Maven project and its modules.
3.  **Configure SDK**: Ensure that a Java Development Kit (JDK) is configured for the project.

### Eclipse

1.  **Import Maven Project**: Go to `File` > `Import...` > `Maven` > `Existing Maven Projects` and click `Next`.
2.  **Select Root Directory**: Click `Browse...` and select the root folder of the VIC project. Eclipse will detect the `pom.xml` and the modules.
3.  **Finish Import**: Click `Finish`.

### NetBeans

1.  **Open Project**: Go to `File` > `Open Project...`.
2.  **Navigate to Project**: Navigate to the root folder of the VIC project and select it. NetBeans will recognize the Maven project.
3.  **Open**: Click `Open Project`.

### Visual Studio Code

1.  **Open Folder**: Go to `File` > `Open Folder...`.
2.  **Select Root Folder**: Select the root folder of the VIC project.
3.  **Install Extensions (if necessary)**: Make sure you have the Java Extension Pack and Maven for Java extensions installed for full support.
4.  **Enable Maven Support**: VS Code usually detects the Maven project automatically. You can use the `Maven` tab in the Explorer to manage modules.

### Usage with the `local-dev/` folder

The VIC project uses a `local-dev/` folder at the root to simulate inputs during local development. You can use the `--dev` parameter in different ways:

*   `-d`: Executes all `.uxf` files found in all subfolders within `local-dev/`.
*   `-d <folder-name>`: Executes all `.uxf` files within the specified subfolder in `local-dev/`.
*   `-d <folder-name> <uxf-file-name>`: Executes only the specific `.uxf` file within the indicated subfolder in `local-dev/`.

## 📌 Future Improvements

To organize the development and tracking of new features and bugs, issues will be opened in the project repository in the future.

## 🤝 How to Contribute

Your contribution is very welcome! Follow the steps below to collaborate on the VIC project:

1.  **Fork the Project**: Fork this repository to your GitHub account.
2.  **Clone the Repository**: Clone your fork to your local machine:
    ```bash
    git clone https://github.com/your-username/VIC.git
    ```
3.  **Create a Branch**: Create a new branch for your changes:
    ```bash
    git checkout -b feature/your-new-feature
    ```
4.  **Submit Pull Request**: After making your changes and testing them, submit a Pull Request to the `main` branch of this repository.

## 📄 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.


