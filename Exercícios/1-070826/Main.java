
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class MinhaThread extends Thread {
    private List<Integer> lista;
    Integer tamanho;
    int soma;
    int somaTotal;

    public MinhaThread(List<Integer> lista) {
        this.lista = lista;
        this.tamanho = 2500;
    }

    @Override
    public void run() {
        Random random = new Random();
        for (int i = 0; i < tamanho; i++) {
            lista.add(random.nextInt(10000));
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (Integer i : lista) {
            soma += i;
        }
        System.out.println("Soma dos elementos da lista: " + soma);
    }
}


public class Main {
    public static void main(String[] args) throws InterruptedException {
        List<Integer> lista1 = new ArrayList<>(10000);

        MinhaThread t1 = new MinhaThread(lista1);
        t1.start();
        t1.join();
        System.out.println("Thread 1 finalizada");

        MinhaThread t2 = new MinhaThread(lista1);
        t2.start();
        t2.join();
        System.out.println("Thread 2 finalizada");

        MinhaThread t3 = new MinhaThread(lista1);
        t3.start();
        t3.join();
        System.out.println("Thread 3 finalizada");

        MinhaThread t4 = new MinhaThread(lista1);
        t4.start();
        t4.join();
        System.out.println("Thread 4 finalizada");

        System.out.println("Soma total dos elementos da lista: " + (t1.soma + t2.soma + t3.soma + t4.soma));
    }
}