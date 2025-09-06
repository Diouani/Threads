
class Sommeur implements Runnable {
    private int[] tableau;
    private int debut;
    private int fin;
    private long somme;
    

    public Sommeur(int[] tableau, int debut, int fin) {
        this.tableau = tableau;
        this.debut = debut;
        this.fin = fin;
        this.somme = 0;
    }
    

    @Override
    public void run() {
        System.out.println("Thread " + Thread.currentThread().getName() + 
                          " calcule la somme des indices " + debut + " à " + (fin-1));
        
        for (int i = debut; i < fin; i++) {
            somme += tableau[i];
        }
        
        System.out.println("Thread " + Thread.currentThread().getName() + 
                          " terminé. Somme partielle: " + somme);
    }
    
    /**
     * Getter pour récupérer la somme calculée
     * @return La somme de la plage
     */
    public long getSomme() {
        return somme;
    }
}
