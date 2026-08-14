# Notas de Aula

--------------------
## Semana 1 - 27-31/07/26
- **Introdução** - https://github.com/Andrisa-S/SistemasDistribuidosUFN/blob/4d2caadf0f1e9fd2f45835d429ac94ac958c87a4/Introducao.md
- **Arquiteturas de Sistemas:**
  1) Cliente-Servidor
     - Modelo TCP/IP (4 camadas) => Prático
     - Frameworks (SO, HTML, BD, usages)
       - Boas Práticas
       - Reuso
       - Releases constantes

  2) Ponto-a-Ponto (Peer-to-Peer)
     - Modelo TCP/IP
     - receber = receive = read
       - Deserializar
     - enviar = send = write
       - bytes
       - Serializar:
         - string
         - objetos
      - Concomitante ~= Paralelo

  - Processo
    - Thread (*sub-processo*)
      - mini processo
      - *id (obrigatórios)
      - *memória + cpu
      - *tempo
      - *pai (pilha)
      - nome
      - memória não compartilhada
      - memória compartilhada (*seção crítica*)
        - bloqueio
          - Monitor
          - Semáforo
          - deadlock
          
- ### SISTEMAS DISTRIBUÍDOS
    - Heterogêneos: diferentes arquiteturas de hardware, sistema operacional e linguagens de programação.
    - Fracamente acoplados (distribuídos geograficamente via protocolos do modelo TCP/IP: endereço de rede, porta lógica, máscara de rede, protocolos de transporte)
    - GRID computacional
    - Arquiteturas: Cliente-Servidor; Ponto-a-Ponto
      - Tolerância a falhas;
      - Escalabilidade;
      - Segurança;
      - Manutenção/Atualização.
   
    - **Objetivo:** Compartilhar recursos (processador e memória, disco)
      - Ao compartilhar recurso, é necessário controlar SINCRONISMO:
        - Relógio: lógico e físico
        - Recurso: exclusivo mútua

    - SD são fortemente dependente de Sistema Operacional: gestor de processamento; gestor de comunicação, ou um gestor das camadas de serviço;
      - *Observação:* Sistemas distribuídos, na essência, tem comunicação via SOCKET (ip, porta, máscara, objetos escritores/leitores) que é **bloqueante**.
        - *Solução computacional em tempo de programação* é o uso de **THREADS**.
    - Características básicas:
      - Arquitetura:
        - Cliente-Servidor;
        - Ponto-a-Ponto (P2P);
        - Híbrido
      - Comunicação bloqueante
        - Escrever;
        - Ler.

  - Programação Multitarefa (thread)
    - Thread é um mini processo dentro de um processo
    - Thread pode ser com memória compartilhada
      - Sincronismo: monitor, semáforo
    - Thread sem memória compartilhada
    - Importância: execução de processos concomitantemente. E em SD, para liberar comunicação bloqueante.

- ### SISTEMAS PARALELOS  
    - Homogêneos: arquiteturas de hardware, sistema operacional e linguagens de programação idênticas
    - Fortemente acoplados (fixos em um mesmo lugar via protocolos do modelo TCP/IP: endereço de rede, porta lógica, máscara de rede, protocolos de transporte)
    - CLUSTER computacional
    - Arquiteturas: Ponto-a-Ponto
      - Tolerância a falhas;
      - Escalabilidade;
      - Segurança;
      - Manutenção/Atualização.
    - **Objetivo:** Compartilhar recursos (processador e memória)

--------------------
## Semana 2 - 03-07/08/26
  - **Revisão:**
    - *Sistemas Distribuídos:* Compartilhamento de recursos em prol de uma tarefa, heterogêneo, fracamente acoplados;
        - Compartilhamento de recursos por comunicação através de rede;
          - *Protocolo de comunicação:*
            - Lexemas (símbolos);
            - Sintaxe;
            - Semântica.
          - *Comunicação bloqueante:*
            - Meio/recurso
              - **Seção Crítica**
            - Compartilhamento de memória
              - **Sincronismo**
                - Tempo;
                - Bloqueio.
            ```
            popular_lista(listaA, 100);  // 1°) Objeto thread (NOME)
            popular_lista(listaB, 1000); // 2°) new Thread
            popular_lista(listaC, 50);
            ``` 
  - Diferença de threads com compartilhamento de memória e sem compartilhamento de memória: Threads com compartilhamento de memória dividem o mesmo espaço de endereço, facilitando a troca rápida de dados, mas exigindo sincronização (como mutexes) para evitar conflitos, enquanto as sem compartilhamento isolam os dados e dependem de troca de mensagens. Por sua vez, operações bloqueantes suspendem a execução da thread até que uma tarefa (como I/O) seja concluída, ao passo que as não bloqueantes permitem que a thread prossiga imediatamente com outras instruções sem aguardar o término da operação.
  - Threads nas 3 linguagens: ideia geral
    
    - ### Java
        - Threads nativas;
        - Criar threads:
          - Estendendo a classe `thread` e sobrescrevendo o método `run()`.
          - Implementando a interface `Runnable`e passando para uma Thread.
        - Uso comum do **Executor Framework** (java.util.concurrent) para gerenciar pools de threads, facilitando a execução concorrente e o reaproveitamento de threads.
        - Suporta sincronização via `synchronized`, `Lock`, `Semaphore`, entre outros.
        - Bom suporte para comunicação e sincronização entre threads.
          
        **Exemplo básico:**
          ```java
            class MinhaThread extends Thread {
              public void run() {
                  System.out.println("Thread executando!");
              }
            }
          
            public class Main {
                public static void main(String[] args) {
                    MinhaThread t = new MinhaThread();
                    t.start();  // inicia a thread
                }
            }
          ```
      
    - ### Python
        - Suporte a threads via módulo `threading`;
        - **Limitação importante**: devido ao **GIL (Global Interpreter Lock)**, o Python executa apenas uma thread de bytecode Python por vez, o que limita o paralelismo real em CPU-bound.
        - Contudo, threads são muito úteis para tarefas **I/O-bound** (entrada/saída), como chamadas de rede, leitura de arquivos, etc.
        - Para CPU-bound, o Python recomenda o uso do módulo `multiprocessing` (processos em vez de threads).
        - Fácil criação e controle de threads via `threading.Thread`.
        
        **Exemplo básico:**
        ```python
        import threading
        
        def tarefa():
            print("Thread executando!")
        
        t = threading.Thread(target=tarefa)
        t.start()
        t.join()  # espera a thread terminar
        ```
  
    - ### C#
        - Suporte nativo a threads pelo namespace `System.Threading`.
        - Criação de threads com a classe `Thread` ou, mais moderno, usando **Tasks** (`System.Threading.Tasks`) que facilitam o trabalho assíncrono.
        - O C# também suporta **async/await** para programação assíncrona, que em muitos casos substitui a necessidade de criar threads manualmente.
        - Suporte robusto para sincronização (`lock`, `Mutex`, `Semaphore`, etc.).
        - Muito usado em servidores distribuídos com ASP.NET, onde o gerenciamento eficiente de threads é crítico.
       
        **Exemplo básico:**
    
        ```csharp
        using System;
        using System.Threading;
        
        class Program {
            static void MinhaThread() {
                Console.WriteLine("Thread executando!");
            }
        
            static void Main() {
                Thread t = new Thread(new ThreadStart(MinhaThread));
                t.Start();
            }
        }
        ```
    - Desafio 1: Jogo da cobrinha
---
 ### THREAD:
   - Um subprocesso ou um mini processo pertencente a um processo (identificador, nome, endereço, tamanho, tempo, instruções) criado em tempo de programação/execução
   - Finalidade de threads é garantir processamento concomitante
   - Estados de um thread: execução, finalizado/pronto, espera/aguardando, parado, dormindo, cancelado, ...
   - Há comandos que garantem
---

### ARQUITETURAS

  - #### Arquitetura Cliente-Servidor 
    - **Modelo centralizado:** Existe uma ou mais máquinas que atuam como **servidores**, responsáveis por fornecer serviços, dados ou recursos.
    - Os **clientes** são máquinas que solicitam serviços ou recursos dos servidores.
    - Os servidores gerenciam, controlam e atendem as requisições dos clientes.
    - Comunicação é normalmente **unidirecional na requisição:** clientes fazem pedidos, servidores respondem.
    - Exemplo típico: um site web, onde o navegador é cliente e o servidor web entrega as páginas.
    
    - **Características:**
    
      - **Centralização:** servidores têm um papel chave.
      - **Dependência:** se o servidor ficar indisponível, os clientes podem perder acesso ao serviço.
      - **Gerenciamento:** fácil controle e administração centralizada.
  
  - #### Arquitetura Ponto-a-Ponto (P2P)
    - **Modelo descentralizado:** todos os nós (ou “pares”) têm papéis equivalentes — eles podem atuar tanto como clientes quanto como servidores.
    - Cada nó pode **solicitar e fornecer** recursos diretamente para outros nós, sem precisar de um servidor central.
    - Comunicação é **direta entre pares**, podendo ser mais distribuída e resiliente.
    - Exemplo típico: redes de compartilhamento de arquivos como BitTorrent.
    
    - **Características:**
    
      - **Descentralização:** não há um ponto único de falha.
      - **Escalabilidade:** facilmente escalável porque cada nó contribui com recursos.
      - **Resiliência:** se um nó falhar, o sistema continua funcionando.
  
  
  #### Resumo da diferença
  
  | Aspecto              | Cliente-Servidor                       | Ponto-a-Ponto (P2P)                  |
  | -------------------- | -------------------------------------- | ------------------------------------ |
  | Estrutura            | Centralizada (servidor + clientes)     | Descentralizada (nós equivalentes)   |
  | Papel dos nós        | Servidores fornecem, clientes consomem | Todos podem fornecer e consumir      |
  | Comunicação          | Cliente → Servidor                     | Nó ↔ Nó direto                       |
  | Ponto único de falha | Sim (servidor)                         | Não (descentralizado)                |
  | Escalabilidade       | Limitada pelo servidor                 | Alta, pois recursos são distribuídos |
  | Exemplo              | Web, bancos de dados                   | BitTorrent, redes blockchain         |

---
### COMUNICAÇÃO
#### Comunicação entre máquinas

Em sistemas distribuídos, as máquinas (ou nós) estão fisicamente separadas e conectadas via rede. Para que trabalhem em conjunto, elas precisam **trocar informações** — isso é a comunicação distribuída.

* **Papel:** Permitir que processos rodando em máquinas diferentes se comuniquem, coordenem ações, compartilhem dados, e cooperem para realizar tarefas maiores.
* **Como acontece:** Via troca de mensagens usando protocolos de rede (TCP/IP, HTTP, RPC, etc).
* **Desafios:**

  * Latência e largura de banda da rede.
  * Falhas na transmissão (perda de mensagens, duplicação, atraso).
  * Heterogeneidade dos sistemas.
* **Importância:** Sem comunicação eficiente, o sistema não funciona, pois os nós não conseguem sincronizar suas ações nem trocar dados necessários.


#### Sincronização

Em um sistema distribuído, processos ou nós muitas vezes precisam coordenar suas ações para:

* Evitar conflitos (ex: acesso concorrente a um recurso compartilhado).
* Garantir a ordem correta de eventos.
* Manter a consistência dos dados.

**Sincronismo** é o mecanismo que permite essa coordenação, apesar das máquinas estarem separadas e se comunicarem via rede (que é lenta e não confiável).

* Pode ser **sincronismo temporal** (relógios sincronizados para ordenar eventos).
* Ou **sincronismo de ações** (esperar resposta, barreiras, locks distribuídos).
* **Exemplo prático:** Dois nós que querem atualizar um banco de dados compartilhado precisam garantir que as atualizações não causem inconsistência — usam sincronização para coordenar quem pode alterar o dado primeiro.


#### Por que a sincronização é complexa em sistemas distribuídos?

* Não existe um relógio global perfeitamente sincronizado.
* Mensagens podem chegar fora de ordem ou se perder.
* Nós podem falhar ou ficar lentos.
* Por isso, técnicas como relógios lógicos (Lamport), algoritmos de consenso (Paxos, Raft), e mecanismos de exclusão mútua distribuída são fundamentais.

#### Resumo:

| Aspecto                    | Descrição                                                                                  |
| -------------------------- | ------------------------------------------------------------------------------------------ |
| Comunicação entre máquinas | Troca de mensagens entre processos/nós distantes para cooperar                             |
| Recurso de sincronismo     | Mecanismos que garantem coordenação, ordenação e consistência entre processos distribuídos |

---
### Exercícios
- 1. Divisão e Conquista: Soma de sublistas
  - <img width="1259" height="150" alt="image" src="https://github.com/user-attachments/assets/f4cb047f-a9f0-4c88-8578-8f77dd35d86e" />
  -  Sem memória compartilhada (Runnable), MVC orientado a objeto
- 2. Filtro de Dados Independente (Map)
  - <img width="1320" height="172" alt="image" src="https://github.com/user-attachments/assets/aede7758-fe7f-430b-9986-f380b7efeb77" />
  - Uso de trim e UpperCase

--------------------
## Semana 3 - 10-14/08/26
- Revisão aula interativa

  ### Configuração Firewall
  - Windows Defender Firewall com Segurança Avançada
    - Regras de saída
      - Nova regra
        - Porta -> TCP -> 12345
    
- Introdução de conceito de Servidor
- Sockets e Pool
- Código de Servidor Multithread
--------------------
## Semana 4 - 17-21/08/26
