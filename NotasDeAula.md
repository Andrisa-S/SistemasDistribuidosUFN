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
  -------------------------------
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

   ### SISTEMAS DISTRIBUÍDOS
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


  ### SISTEMAS PARALELOS  
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
    ### Java
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

    ### Python
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

    ### C#
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
 ### THREAD:
   - Um subprocesso ou um mini processo pertencente a um processo (identificador, nome, endereço, tamanho, tempo, instruções) criado em tempo de programação/execução
   - Finalidade de threads é garantir processamento concomitante
   - Estados de um thread: execução, finalizado/pronto, espera/aguardando, parado, dormindo, cancelado, ...
   - Há comandos que garantem

--------------------
## Semana 3 - 10-14/08/26

--------------------
## Semana 4 - 17-21/08/26
