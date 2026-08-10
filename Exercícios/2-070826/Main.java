import java.util.ArrayList;
import java.util.List;

class MinhaThread extends Thread {
    List<String> lista;
    int tamanho = 2500;

    public MinhaThread(List<String> lista, int tamanho) {
        this.lista = lista;
        this.tamanho = tamanho;
    }

    @Override
    public void run() {
        for (int i = 0; i < tamanho; i++) {
            System.out.println(lista.get(i));
        }
        trim();
    }

    private void trim() {
        if (lista.size() > tamanho) {
            lista = lista.subList(0, tamanho);
        }
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException{
        List<String> lista = new ArrayList<>(5000);
        for (int i = 0; i < 5000; i++) {
            lista.add("String " + i);
        }

        MinhaThread t1 = new MinhaThread(lista, 0);
        t1.start();
        t1.join();
        System.out.println("\nThread finalizada");

        MinhaThread t2 = new MinhaThread(lista, 2500);
        t2.start();
        t2.join();
        System.out.println("\nThread finalizada");

        MinhaThread tFinal = new MinhaThread(lista, 5000);
        tFinal.start();
        tFinal.join();
        System.out.println("\nThreads finalizadas");
    }
}