class PrimeiraThread extends Thread {
    public void run() {
        for(int i = 0; i < 50; i++) {
            System.out.println("Thread1 executando!");
        }
    }
}

class SegundaThread extends Thread {
    public void run() {
        for(int i = 0; i < 50; i++) {
            System.out.println("Thread2 em ação!");
        }
    }
}

