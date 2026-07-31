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
------------------------------
   ### SISTEMAS DISTRIBUÍDOS
    - Heterogêneos: diferentes arquiteturas de hardware, sistema operacional e linguagens de programação.
    - Fracamente acoplados (distribuídos geograficamente via protocolos do modelo TCP/IP: endereço de rede, porta lógica, máscara de rede, protocolos de transporte)
    - GRID computacional
    - Arquiteturas: Cliente-Servidor; Ponto-a-Ponto
      - Tolerância a falhas;
      - Escalabilidade;
      - Segurança;
      - Manutenção/Atualização.
   
    - **Objetivo:** Compartilhar recursos (processador e memória)
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
----------------------------
  - Programação Multitarefa (thread)
    - Thread é um mini processo dentro de um processo
    - Thread pode ser com memória compartilhada
      - Sincronismo: monitor, semáforo
    - Thread sem memória compartilhada
    - Importância: execução de processos concomitantemente. E em SD, para liberar comunicação bloqueante.

----------------------------
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
 ### THREAD:
   - Um subprocesso ou um miniprocesso pertencente a um processo (identificador, nome, endereço, tamanho, tempo, instruções) criado em tempo de programação/execução
   - Finalidade de threads é garantir processamento concomitante
   - Estados de um thread: execução, finalizado/pronto, espera/aguardando, parado, dormindo, cancelado, ...
   - Há comandos que garantem

--------------------
## Semana 3 - 10-14/08/26

--------------------
## Semana 4 - 17-21/08/26
