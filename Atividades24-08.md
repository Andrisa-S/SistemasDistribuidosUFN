# Atividades da Semana 5 - 24-28/08/26

# Relógios Físicos e Lógicos, Exclusão Mútua e Eleição

## 1. Relógios Físicos

- Praticamente todos os computadores têm um circuito para contar o tempo, baseados num cristal de quartzo. Este cristal oscila a uma frequência conhecida, e o circuito conta o número de oscilações, podendo assim determinar o tempo. Ao fim de um determinado número de oscilações, o circuito gera uma interrupção a que se chama **tick de relógio**.

- Como qualquer outra medição, existe uma incerteza associada à frequência do cristal, e por isso a frequência de ticks de relógio pode variar entre relógios. Mesmo para o mesmo relógio, a frequência pode variar com fatores externos como a temperatura. A variação é normalmente pequena, mas o erro acumulado pode levar a que dois relógios apresentem uma diferença significativa no tempo que contam.

- Baseiam-se em padrões como **UTC** (Coordinated Universal Time), mas requerem sincronização frequente para reduzir desvios.

- Num sistema distribuído em que uma das máquinas está instalada com um recetor de UTC, pode-se usar este relógio como referência para sincronizar os outros. Trata-se de **sincronização externa**, em que é usada uma referência externa para obter exatidão e precisão no sistema.

- Porém, a exatidão nem sempre é relevante, sendo mais importante a precisão para que todos os nós do sistema concordem sobre o tempo. Nestes casos usa-se **sincronização interna**, em que os relógios são sincronizados entre si.

### 1.1 Relógio de Processador (Clock)

- Todo processador é uma máquina orientada por um relógio interno. O principal componente de um processador é seu clock, que em intervalos regulares de tempo incrementa um valor.

- Todo computador, para ajustar seu relógio, vai consultar um relógio (ntp.br) e criar uma associação entre seu tempo de clock e um tempo real. Para que esse relógio não fique desatualizado, é mantida uma bateria para que o clock nunca pare, mesmo quando desligado.

- O tempo do clock chamamos de **Relógio Monotônico** (*Monotonic*), visto que ele nunca retrocede e nem avança em intervalos constantes. Já o relógio que tenta refletir o valor retornado pelo servidor ntp.br chamamos de **Relógio Tempo do Dia** (*Time-of-Day Clocks*).

- No Java, os valores desses relógios são retornados pelas chamadas:
  - `System.currentTimeMillis()` — relógio tempo do dia;
  - `System.nanoTime()` — relógio monotônico.

- A primeira dificuldade de sincronização entre esses diversos sistemas é que, mesmo o relógio estando correto, cada relógio irá apresentar um atraso diferente em relação ao servidor NTP. Assim, se fosse possível que todos os processos executassem `System.currentTimeMillis()` sincronamente, cada um teria uma resposta diferente.

- No artigo *Time, Clocks, and the Ordering of Events in a Distributed System*, Lamport define que podemos usar os timestamps de cada processo para ordenar esses eventos mesmo sem existir relação de precedência, e em caso de empate devemos usar uma propriedade dos processos. Segundo Lamport, logicamente podemos fazer duas inferências:

  1. Se dois eventos acontecem em um mesmo processo, é possível definir a relação de precedência e os timestamps são sempre sequenciais.
  2. Se um evento é enviado de um processo para outro, o timestamp de chegada deve ser maior que o timestamp do evento.
  3. Se dois eventos são distintos, não podem ter o mesmo timestamp.

---

## 2. Relógios Lógicos

- Nem sempre é necessário contar o tempo de forma exata. Se dois processos não interagem, a falta de sincronização não pode ser observada, pelo que não poderá causar problemas. Para além disso, em muitos casos, o que realmente é relevante é a ordem em que eventos ocorrem e não o tempo absoluto.

- São abstrações usadas para ordenar eventos em sistemas distribuídos, ignorando o tempo físico real.

### 2.1 Relógio de Lamport

- Usa um contador incremental para representar a ordem de eventos.

- Leslie Lamport propôs um algoritmo simples para capturar a ordem de eventos num sistema distribuído. Cada processo **Pi** mantém um relógio lógico monótono **Li** que pode ser usado para atribuir uma estampilha temporal **Li(e)** a cada evento **e**. Quando o processo em que se atribuiu a estampilha não é relevante, usa-se **L(e)**.

### 2.2 Relógio Vetorial

- Expande a ideia do relógio de Lamport para capturar relações de causalidade entre eventos.

---

## 3. Exclusão Mútua

- Exclusão mútua garante que apenas um processo acesse um recurso compartilhado de cada vez. Isso é fundamental em sistemas distribuídos, onde processos podem estar em nós diferentes.

- É um conceito central em sistemas distribuídos para garantir que múltiplos processos, executando em diferentes nós de uma rede, não acessem simultaneamente recursos compartilhados que possam causar inconsistências ou falhas. Ao contrário dos sistemas centralizados, onde um único processo de controle administra o acesso, nos sistemas distribuídos essa coordenação deve ser feita de forma cooperativa, por processos autônomos e comunicantes, sem um ponto único de controle. Este desafio exige algoritmos eficazes que mantenham a integridade dos dados e evitem condições de corrida, deadlocks e starvation.

### 3.1 Algoritmos de Exclusão Mútua

#### Algoritmo Centralizado

Um nó atua como coordenador, controlando o acesso aos recursos.

- **Vantagem:** simples e eficiente em redes pequenas.
- **Desvantagem:** ponto único de falha.
- Um processo do sistema é indicado como coordenador (ver algoritmo de eleição de líder) para controlar o acesso a uma região crítica (RC). Qualquer processo que deseja entrar em uma RC deve pedir ao coordenador. Se o coordenador verificar que nenhum processo está acessando a RC então, o processo solicitante obtém o acesso. Caso contrário, este processo deve esperar até que o processo que está acessando a RC libere o recurso e avise o coordenador.

#### Algoritmo Distribuído

Os processos comunicam entre si para decidir qual terá acesso ao recurso.

- Quando um processo deseja entrar na RC, ele deve enviar uma mensagem para o sistema (todos os outros processos que podem acessar a RC). A mensagem **M** tem o seguinte conteúdo:
  - **Id:** identificador do processo;
  - **Rc:** nome da RC que deseja acessar;
  - **Ts:** uma estampilha de tempo único (timestamp) gerado pelo processo — baseado no algoritmo de Lamport78.

- Qualquer processo ao receber esta mensagem deve responder ao emissor dizendo **OK** (garantindo o acesso ao recurso) ou não envia nada se:
  - Ele é o processo que está usando a RC, então ele enfileira a mensagem M e não responde.
  - Ele não está usando a RC mas está esperando sua vez. Ele compara o timestamp da mensagem M com o seu. Se for menor que o seu, então envia uma resposta OK; caso contrário, ele enfileira M e não responde. *(rever Lamport78)*

- Todo processo, ao terminar de usar a RC, deve enviar um OK aos processos solicitantes.

- Um processo solicitante pode acessar a RC quando tiver o OK de todos os processos do sistema.

- **Exemplo:** baseado no envio de mensagens e marcação de tempos (relógios lógicos).

#### Token Passing

Um token é passado entre os nós. Apenas o nó com o token pode acessar o recurso.

- Neste algoritmo é definido um anel virtual em que um token circula entre os processos do sistema. Um processo de posse do token tem direito de acesso a uma determinada RC.
- Se ele não deseja acessar a RC, o token deve ser passado adiante.
- **Exemplo:** Redes Token Ring.

---

## 4. Eleição

- Eleição é o processo pelo qual os processos de um sistema distribuído escolhem um novo coordenador.

- Algoritmos de eleição identificam um coordenador ou líder em sistemas distribuídos, essencial para funções centralizadas como sincronização.

- A eleição de líder é uma ferramenta importante para melhorar a eficiência, reduzir a coordenação, simplificar arquiteturas e reduzir operações. Por outro lado, a eleição de líder pode introduzir novos modos de falha e gargalos de escalabilidade. Além disso, a eleição de líder pode dificultar a avaliação da exatidão de um sistema.

### 4.1 Quando ocorre uma eleição

A eleição normalmente ocorre quando:

- o coordenador atual falha;
- novos processos entram no sistema;
- há suspeita de falha do líder;
- o sistema precisa reorganizar sua coordenação.

A ideia central é simples: se o líder falhou, alguém precisa assumir o papel de líder.

### 4.2 Garantias de um algoritmo de eleição

Um algoritmo de eleição deve garantir:

- **Segurança:** não deve haver dois coordenadores corretos ao mesmo tempo.
- **Vivacidade:** se houver processos ativos, algum coordenador deve ser eleito.
- **Consenso prático:** todos os processos ativos devem saber quem é o líder.
- **Eficiência:** a eleição deve usar poucas mensagens e terminar rapidamente.
- **Tolerância a Falhas:** deve funcionar mesmo com nós falhando ou se comunicando mal.

### 4.3 Vantagens

- Um único líder torna mais fácil para os humanos pensarem sobre o sistema. Ele coloca a simultaneidade no sistema em um único lugar, reduz os modos de falha parcial e adiciona um único lugar para procurar registros e métricas.
- Um único líder pode operar de maneira mais eficiente. Ele pode simplesmente informar outros sistemas sobre alterações, em vez de criar consenso sobre as alterações a serem feitas.
- Líderes únicos podem oferecer consistência aos clientes facilmente, porque podem ver e controlar todas as alterações feitas no estado do sistema.
- Um único líder pode melhorar a performance ou reduzir o custo fornecendo um único cache de dados consistente, que pode ser usado todas as vezes.
- Escrever o software para um único líder pode ser mais fácil do que outras abordagens, como o quórum. O único líder não precisa considerar que outros sistemas possam estar trabalhando no mesmo estado ao mesmo tempo.

### 4.4 Desvantagens

- Um único líder é um único ponto de falha. Se o sistema falhar em detectar ou corrigir um líder inválido, todo o sistema poderá ficar indisponível.
- Um único líder significa um único ponto de escalabilidade, tanto em tamanho de dados quanto em taxa de solicitação. Quando um sistema com líder eleito precisa ser ampliado para além de um único líder, é necessária uma rearquitetura completa.
- Um líder é um único ponto de confiança. Se um líder estiver fazendo o trabalho errado sem ninguém verificar, ele pode causar problemas em todo o sistema rapidamente. Um líder ruim tem um grande raio de impacto.
- Implantações parciais podem ser difíceis de aplicar em sistemas com líder eleito. Muitas práticas de segurança de software na Amazon dependem de implantações parciais, como one-box, testes A-B, implantação azul/verde e implantação incremental com reversão automática.
  
---

# Teoria de Pool de Threads

## 1. Definição

- Técnica de gerenciamento de threads que permite a reutilização de um conjunto fixo de threads para executar tarefas simultaneamente. Em vez de criar e destruir threads repetidamente, o que pode ser custoso em termos de desempenho, o Thread Pool mantém um número limitado de threads prontas para serem utilizadas, otimizando o uso de recursos do sistema e melhorando a eficiência do processamento.

- Os threads de pool de threads são threads de segundo plano. Cada thread usa o tamanho da pilha padrão, é executado com prioridade padrão e está no apartamento de vários threads. Depois que um tópico no pool de tópicos concluir sua tarefa, ele será retornado para uma fila de tópicos de espera. A partir deste momento, ele pode ser reutilizado. Essa reutilização permite que os aplicativos evitem o custo de criação de um novo thread para cada tarefa. Há apenas um pool de threads por processo.

## 2. Funcionamento

- O funcionamento de um Thread Pool se baseia na criação de um número pré-definido de threads que ficam em espera, prontas para executar tarefas assim que elas são disponibilizadas. Quando uma nova tarefa é recebida, uma thread disponível é atribuída a ela. Após a conclusão da tarefa, a thread não é destruída, mas sim retornada ao pool, onde pode ser reutilizada para outra tarefa. Isso reduz a sobrecarga associada à criação e destruição de threads, resultando em um desempenho mais eficiente.

## 3. Estrutura

- **Threads trabalhadoras (workers):** um conjunto limitado de threads trabalhadoras. As workers executam um laço: retira uma tarefa da fila, executa, retira a próxima, repete. Elas vivem pelo tempo de vida do pool (ou até ficar ociosas por tempo demais, dependendo da política).

- **Fila de tarefas:** quando você submete trabalho e nenhuma worker está livre, a tarefa vai para cá. O tipo de fila — `LinkedBlockingQueue`, `ArrayBlockingQueue`, `SynchronousQueue` — afeta como o pool cresce sob carga.

- **API de submissão:** `execute(Runnable)`, `submit(Callable)`, `invokeAll(...)` — as formas de colocar trabalho no pool.

## 4. Vantagens

- Reduzem a sobrecarga de criação e destruição de threads, o que pode ser caro e demorado.
- Melhoram o desempenho e a escalabilidade reutilizando threads existentes e evitando opções de contexto.
- Simplificam a lógica de programação e evitam problemas de simultaneidade, abstraindo os detalhes do gerenciamento de threads do aplicativo.
- Permitem um melhor controle e ajuste do nível de paralelismo e da utilização de recursos.

## 5. Desvantagens

- Podem introduzir latência e inanição se as tarefas não forem distribuídas uniformemente ou priorizadas entre os threads.
- Podem causar deadlock ou livelock se as tarefas tiverem dependências ou requisitos de sincronização que não são tratados corretamente.
- Podem aumentar a complexidade e a dificuldade de depuração e teste do aplicativo, especialmente se as tarefas tiverem efeitos colaterais ou estado compartilhado.
- Podem limitar a flexibilidade e a portabilidade do aplicativo se não forem compatíveis com a plataforma ou o ambiente de destino.
