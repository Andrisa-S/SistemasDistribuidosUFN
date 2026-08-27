# Resumo de Estudo — Avaliação Teórica (28/08)

> Baseado nas notas de aula (Semanas 1–5) e nas pesquisas sobre Relógios, Exclusão Mútua, Eleição e Pool de Threads.

---

## 1. Threads

### 1.1 O que é uma Thread

- Um **subprocesso** (mini processo) pertencente a um processo: possui identificador, nome, endereço, tamanho, tempo e instruções próprias, criado em tempo de programação/execução.
- **Finalidade:** garantir processamento concomitante e, em Sistemas Distribuídos, desbloquear a comunicação (sockets são bloqueantes por natureza).
- **Estados de uma thread:** execução, finalizado/pronto, espera/aguardando, parado, dormindo, cancelado.
- Tipos:
  - **Sem memória compartilhada** — executa apenas rotinas/tarefas isoladas, troca dados por mensagens.
  - **Com memória compartilhada** — rotinas/tarefas + **seção crítica**, exigindo mecanismos de bloqueio (Monitor, Semáforo) para evitar **deadlock**.

### 1.2 Sincronização de Threads por Linguagem

A sincronização é necessária sempre que threads acessam uma **seção crítica** (memória/recurso compartilhado).

| Linguagem | Mecanismo principal | Observações |
|---|---|---|
| **Java** | `synchronized` / `Lock` | Também suporta `Semaphore`. Criação de threads via extensão de `Thread` ou implementação de `Runnable`. Uso comum do **Executor Framework** (`java.util.concurrent`) para gerenciar pools de threads. |
| **Python** | `lock` (módulo `threading`) | Devido ao **GIL** (Global Interpreter Lock), apenas uma thread executa bytecode Python por vez — paralelismo real limitado em tarefas **CPU-bound**. Muito útil para tarefas **I/O-bound**. Para CPU-bound, recomenda-se `multiprocessing`. |
| **C#** | `lock` | Também suporta `Mutex`, `Semaphore`. Criação via classe `Thread` ou (mais moderno) `Task`/`async-await`. Muito usado em servidores ASP.NET. |

**Exemplos básicos:**

```java
// Java
class MinhaThread extends Thread {
    public void run() {
        System.out.println("Thread executando!");
    }
}

public class Main {
    public static void main(String[] args) {
        MinhaThread t = new MinhaThread();
        t.start();
    }
}
```

```python
# Python
import threading

def tarefa():
    print("Thread executando!")

t = threading.Thread(target=tarefa)
t.start()
t.join()  # espera a thread terminar
```

```csharp
// C#
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

**Diferença-chave:** threads **com** compartilhamento de memória dividem o mesmo espaço de endereço (troca rápida de dados, mas exige sincronização como mutexes); threads **sem** compartilhamento isolam os dados e trocam mensagens. Operações **bloqueantes** suspendem a thread até a conclusão da tarefa (ex.: I/O); as **não bloqueantes** permitem continuar sem esperar.

### 1.3 Pool de Threads

- Técnica que **reutiliza** um conjunto fixo de threads em vez de criar/destruir threads repetidamente (operação custosa).
- Um número pré-definido de threads fica em espera; ao concluir uma tarefa, a thread volta ao pool (não é destruída) para ser reutilizada. Há **um pool por processo**.
- **Estrutura:**
  - **Threads trabalhadoras (workers):** executam em laço — retiram tarefa da fila, executam, repetem.
  - **Fila de tarefas:** armazena o trabalho quando não há worker livre (`LinkedBlockingQueue`, `ArrayBlockingQueue`, `SynchronousQueue`).
  - **API de submissão:** `execute(Runnable)`, `submit(Callable)`, `invokeAll(...)`.
- **Vantagens:** menor sobrecarga de criação/destruição; melhor desempenho e escalabilidade; simplifica a lógica de programação; melhor controle do paralelismo.
- **Desvantagens:** pode gerar latência/inanição (starvation) se tarefas mal distribuídas; risco de deadlock/livelock; maior complexidade de depuração; pode limitar portabilidade.

---

## 2. Sistemas Distribuídos (SD) — Sincronização

### 2.1 Por que sincronizar?

- SD compartilham recursos (CPU, RAM, memória secundária) entre máquinas heterogêneas e fracamente acopladas.
- Ao compartilhar recursos, é preciso controlar:
  - **Relógio** (lógico e físico) → ordenar eventos.
  - **Exclusão mútua** → controlar acesso ao recurso compartilhado.
- É complexo porque: não existe relógio global perfeito, mensagens podem chegar fora de ordem ou se perder, e nós podem falhar ou ficar lentos.

### 2.2 Relógios Físicos

- Baseados em um **cristal de quartzo** que oscila em frequência conhecida; cada oscilação contada gera, ao final de um número determinado, um **tick de relógio**.
- Há incerteza/deriva na frequência (varia entre relógios e com fatores como temperatura) → erro acumulado gera diferenças entre relógios.
- Padrão de referência: **UTC** (Coordinated Universal Time).
- **Sincronização externa:** usa uma referência externa (ex.: receptor UTC) para exatidão e precisão.
- **Sincronização interna:** os relógios se sincronizam entre si — usada quando a exatidão importa menos que todos concordarem sobre o tempo (precisão).

**No processador/SO:**
- **Relógio Monotônico** — nunca retrocede, usado para medir intervalos (`System.nanoTime()` em Java).
- **Relógio Tempo do Dia** (*Time-of-Day*) — reflete o valor do servidor NTP (`System.currentTimeMillis()` em Java), sincronizado via NTP (ex.: ntp.br).
- Problema: mesmo correto, cada processo tem um atraso diferente até o servidor NTP → chamadas simultâneas retornam valores distintos.

**Regras de ordenação de Lamport (artigo *Time, Clocks, and the Ordering of Events*):**
1. Eventos no **mesmo processo** → ordem de precedência definida, timestamps sequenciais.
2. Evento **enviado entre processos** → timestamp de chegada > timestamp de envio.
3. Eventos **distintos** nunca têm o mesmo timestamp.

### 2.3 Relógios Lógicos

- Nem sempre é preciso medir tempo exato — o que importa é a **ordem** dos eventos, não o tempo absoluto. Se dois processos não interagem, a falta de sincronismo não é observável.

- **Relógio de Lamport:**
  - Contador incremental **Li** mantido por cada processo **Pi**, que atribui uma estampilha **Li(e)** a cada evento **e** (ou **L(e)** quando o processo não é relevante).

- **Relógio Vetorial:**
  - Expande a ideia do relógio de Lamport para capturar **causalidade** entre eventos (não apenas ordem total).

### 2.4 Exclusão Mútua

- Garante que **apenas um processo** acesse um recurso compartilhado por vez — essencial em SD, onde não há um único ponto de controle central. Evita condições de corrida, deadlocks e starvation.

| Algoritmo | Como funciona | Vantagem | Desvantagem |
|---|---|---|---|
| **Centralizado** | Um coordenador controla o acesso à região crítica (RC): concede acesso se ninguém a estiver usando; senão, o solicitante espera. | Simples e eficiente em redes pequenas | Ponto único de falha |
| **Distribuído** | Processo envia mensagem (Id, Rc, Timestamp de Lamport) a todos; cada um responde OK ou enfileira, conforme comparação de timestamps; acesso liberado só com OK de todos. | Sem ponto único de falha | Mais mensagens, mais complexo |
| **Token Passing** | Um token circula em anel virtual; só quem possui o token acessa a RC; se não precisa, repassa o token. | Simples, evita colisão | Espera pelo token pode gerar latência (ex.: Token Ring) |

### 2.5 Eleição (líder/coordenador)

- Processo pelo qual os processos de um SD escolhem um **novo coordenador**.
- **Ocorre quando:** o coordenador atual falha; entram novos processos; há suspeita de falha do líder; o sistema precisa se reorganizar.
- **Um algoritmo de eleição deve garantir:**
  - **Segurança** — nunca dois coordenadores corretos simultâneos.
  - **Vivacidade** — se há processos ativos, um coordenador é eleito.
  - **Consenso prático** — todos sabem quem é o líder.
  - **Eficiência** — poucas mensagens, término rápido.
  - **Tolerância a falhas** — funciona mesmo com nós falhando.
- **Vantagens de um único líder:** mais fácil de raciocinar sobre o sistema; mais eficiente (informa em vez de negociar consenso); consistência mais simples; permite cache único consistente; software mais simples que abordagens de quórum.
- **Desvantagens:** ponto único de falha; ponto único de escalabilidade (requer rearquitetura para crescer); ponto único de confiança (líder ruim tem grande raio de impacto); dificulta implantações parciais (one-box, A/B, blue/green).

---

## 3. Conexões-chave para lembrar na prova

- **Comunicação em SD é bloqueante** (sockets) → **threads** resolvem isso, permitindo processamento concomitante.
- **Sincronismo em threads** (memória local/compartilhada) usa `synchronized`/`lock`/`Mutex`/`Semaphore`.
- **Sincronismo em SD** (memória distribuída entre máquinas) usa **relógios** (físico/lógico) e **exclusão mútua** (centralizada, distribuída, token) — e, quando precisa de um coordenador, **eleição**.
- Programação **concomitante** = threads (GRID); programação **paralela** = CUDA, OpenMP, MPI (Cluster).
- Pool de Threads é uma otimização prática de gerenciamento de threads, reduzindo o custo de criação/destruição.
- [ ] Sei os 3 algoritmos de exclusão mútua (Centralizado, Distribuído, Token Passing) com vantagens/desvantagens.
- [ ] Sei quando ocorre uma eleição e as 5 garantias que ela deve cumprir.
- [ ] Sei o que é Pool de Threads, sua estrutura e vantagens/desvantagens.
