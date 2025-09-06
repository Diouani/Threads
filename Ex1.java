


class Talkative implements Runnable {
    private int valeur;
    

    public Talkative(int valeur) {
        this.valeur = valeur;
    }
    

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println("Thread " + valeur + " - Itération " + (i + 1) + " : " + valeur);
            // Petite pause pour mieux voir l'entrelacement des threads
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("=== Thread " + valeur + " terminé ===");
    }
}