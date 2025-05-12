import java.util.Scanner;

import aeds3.Arquivo;
import classes.*;

public class Principal {
    public static void main(String[] args) {
        Scanner sc;
        Arquivo<Serie> seriesArq = null;
        Arquivo<Episodio> episodiosArq = null;
        Arquivo<Ator> atoresArq = null;

        try {
            seriesArq = new Arquivo<>("series", Serie.class.getConstructor());
            episodiosArq = new Arquivo<>("episodios", Episodio.class.getConstructor());
            atoresArq = new Arquivo<>("atores", Ator.class.getConstructor());

            sc = new Scanner(System.in);
            int select;
            do {

                System.out.println("PUCFlix 1.0\n" +
                        "-----------\n" +
                        "> Inicio\n\n" +
                        "1) Series\n" +
                        "2) Episodios\n" +
                        "3) Atores\n" +
                        "0) Sair\n");

                System.out.print("\nOpcao: ");
                try {
                    select = Integer.valueOf(sc.nextLine());
                } catch (NumberFormatException e) {
                    select = -1;
                }

                switch (select) {
                    case 1:
                        new MenuSeries().menu();
                        break;

                    case 2:
                        new MenuEpisodios().menu();
                        break;

                    case 3:
                        new MenuAtores().menu();
                        break;

                    case 0:
                        break;

                    default:
                        System.out.println("Opção inválida!");
                        break;
                }

            } while (select != 0);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}
