import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    

    public static void main(String[] args) {
        System.out.println("==========================");
        System.out.println("EXERCICE 1 - Threads Talkative");
        System.out.println("==========================");
        
        exercice1();
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("\n" + "==========================");
        System.out.println("EXERCICE 2 - Pool de threads pour calcul de somme");
        System.out.println("==========================");
        
        exercice2();
    }
    

    public static void exercice1() {
        Thread[] threads = new Thread[10];
        
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(new Talkative(i + 1));
        }
        
        // Démarrage de tous les threads
        for (Thread thread : threads) {
            thread.start();
        }
        
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        System.out.println("\n>>> OBSERVATION EXERCICE 1 <<<");
        System.out.println("Les threads s'exécutent de manière concurrente.");
        System.out.println("L'ordre d'affichage n'est pas prévisible - ils s'entrelacent.");
        System.out.println("Chaque thread affiche sa valeur 100 fois, mais de manière entrelacée.");
    }
    

    public static void exercice2() {
        int tailleTableau = 1000000;
        int[] tableau = new int[tailleTableau];
        
        for (int i = 0; i < tailleTableau; i++) {
            tableau[i] = i + 1;
        }
        
        long sommeAttendue = (long) tailleTableau * (tailleTableau + 1) / 2;
        System.out.println("Tableau de " + tailleTableau + " éléments créé.");
        System.out.println("Somme attendue : " + sommeAttendue);
        
        int nombreThreads = 4;
        ExecutorService executorService = Executors.newFixedThreadPool(nombreThreads);
        
        int tailleParPlage = tailleTableau / nombreThreads;
        List<Sommeur> sommeurs = new ArrayList<>();
        List<Future<?>> futures = new ArrayList<>();
        
        for (int i = 0; i < nombreThreads; i++) {
            int debut = i * tailleParPlage;
            int fin = (i == nombreThreads - 1) ? tailleTableau : (i + 1) * tailleParPlage;
            
            Sommeur sommeur = new Sommeur(tableau, debut, fin);
            sommeurs.add(sommeur);
            
            Future<?> future = executorService.submit(sommeur);
            futures.add(future);
        }
        
        for (Future<?> future : futures) {
            try {
                future.get(); // Bloque jusqu'à ce que la tâche soit terminée
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("Erreur lors de l'exécution : " + e.getMessage());
            }
        }
        
        // Fermeture du pool
        executorService.shutdown();
        
        // Calcul de la somme 
        long sommeTotal = 0;
        for (Sommeur sommeur : sommeurs) {
            sommeTotal += sommeur.getSomme();
        }
        
        System.out.println("\n>>> RÉSULTATS EXERCICE 2 <<<");
        System.out.println("Somme calculée par les threads : " + sommeTotal);
        System.out.println("Somme attendue : " + sommeAttendue);
        System.out.println("Vérification : " + (sommeTotal == sommeAttendue ? "✓ CORRECT" : "✗ ERREUR"));
        
        // Comparaison avec calcul séquentiel
        long debutTemps = System.currentTimeMillis();
        long sommeSequentielle = 0;
        for (int i = 0; i < tailleTableau; i++) {
            sommeSequentielle += tableau[i];
        }
        long finTemps = System.currentTimeMillis();
        
        System.out.println("Calcul séquentiel : " + sommeSequentielle + 
                          " (temps: " + (finTemps - debutTemps) + " ms)");
    }
}