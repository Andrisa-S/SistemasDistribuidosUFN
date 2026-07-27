# Notas de Aula

--------------------
## Aula 1 - 27/07/26
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
      - *id (obrigatório)
      - *memória + cpu
      - *tempo
      - *pai (pilha)
      - nome
      - sem compartilhamento de memória
      - com compartilhamento de memória (*seção crítica*)
        - bloqueio
          - Monitor
          - Semáforo
          - deadlock

--------------------
## Aula 2 - 31/07/26

--------------------
## Aula 3 - 03/08/26

--------------------
## Aula 4 - 07/08/26
