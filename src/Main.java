import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        boolean jogarNovamente = true;
        int placarJogador1 = 0;
        int placarJogador2 = 0;
        int placarEmpates = 0;

        // Menu inicial
        System.out.println("Bem-vindo ao Jogo da Velha!");
        System.out.println("Jogador 1, digite seu nome: ");
        String nome1 = scan.nextLine();
        System.out.println("Escolha seu time (X ou O): ");
        char time1 = scan.next().toUpperCase().charAt(0);
        scan.nextLine(); // Limpar

        System.out.println("Jogador 2, digite seu nome: ");
        String nome2 = scan.nextLine();
        char time2 = (time1 == 'X') ? 'O' : 'X';
        System.out.printf("%s, seu time será: %c%n", nome2, time2);

        // Inicializa jogadores
        Jogador jogador1 = new Jogador(nome1, time1);
        Jogador jogador2 = new Jogador(nome2, time2);

        while (jogarNovamente) {
            // Inicializar o tabuleiro
            Campo[][] velha = new Campo[3][3];
            char simboloAtual = time1;  // O primeiro jogador começa

            // Inicializa o tabuleiro com campos vazios
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    velha[i][j] = new Campo();
                }
            }

            boolean game = true;
            while (game) {
                desenhaJogo(velha);

                // Verifica se há um vencedor
                String vitoria = verificaVitoria(velha);
                if (!vitoria.equals("")) {
                    if (vitoria.equals(String.valueOf(time1))) {
                        System.out.printf("Jogador %s venceu!%n", nome1);
                        placarJogador1++; // Incrementa o placar do Jogador 1
                    } else if (vitoria.equals(String.valueOf(time2))) {
                        System.out.printf("Jogador %s venceu!%n", nome2);
                        placarJogador2++; // Incrementa o placar do Jogador 2
                    }
                    game = false;
                    continue; // Sai do loop para exibir o placar
                }

                // Verifica se há empate
                if (empate(velha)) {
                    System.out.println("Empate! Nenhum jogador venceu.");
                    placarEmpates++; // Incrementa o placar de empates
                    game = false;
                    continue; // Sai do loop para exibir o placar
                }

                // Executa a jogada do jogador atual
                try {
                    int[] jogada = jogar(scan, simboloAtual);
                    if (verificarJogada(velha, jogada, simboloAtual)) {
                        // Troca o símbolo para o próximo jogador
                        simboloAtual = (simboloAtual == 'X') ? 'O' : 'X';
                    } else {
                        System.out.println("Posição ocupada! Tente novamente.");
                    }
                } catch (Exception e) {
                    System.out.println("Erro: Jogada inválida.");
                    scan.nextLine();
                }
            }
            // Exibe o placar
            exibirPlacar(nome1, nome2, placarJogador1, placarJogador2, placarEmpates);

            // Perguntar se deseja jogar novamente.
            System.out.println("Fim de jogo! Deseja jogar novamente? (s/n): ");
            char resposta = scan.next().toLowerCase().charAt(0);
            jogarNovamente = (resposta == 's');
        }
        scan.close();
    }

    public static void desenhaJogo(Campo[][] velha) {
        limparTela();
        System.out.println("    0   1   2 ");
        System.out.printf("0   %c | %c | %c %n", velha[0][0].getSimbolo(), velha[0][1].getSimbolo(), velha[0][2].getSimbolo());
        System.out.println("   -----------");
        System.out.printf("1   %c | %c | %c %n", velha[1][0].getSimbolo(), velha[1][1].getSimbolo(), velha[1][2].getSimbolo());
        System.out.println("   -----------");
        System.out.printf("2   %c | %c | %c %n", velha[2][0].getSimbolo(), velha[2][1].getSimbolo(), velha[2][2].getSimbolo());
    }

    public static void limparTela() {
        for (int i = 0; i < 50; i++)
            System.out.println();
    }

    public static int[] jogar(Scanner scan, char simboloAtual) {
        int[] p = new int[2];
        System.out.printf("Quem joga: %c%n", simboloAtual);
        System.out.print("Informe a linha: ");
        p[0] = scan.nextInt();
        System.out.print("Informe a coluna: ");
        p[1] = scan.nextInt();
        return p;
    }

    public static boolean verificarJogada(Campo[][] velha, int[] p, char simboloAtual) {
        if (velha[p[0]][p[1]].getSimbolo() == ' ') {
            velha[p[0]][p[1]].setSimbolo(simboloAtual);
            return true;
        } else {
            return false;
        }
    }

    public static boolean empate(Campo[][] velha) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (velha[i][j].getSimbolo() == ' ') {
                    return false; // Verificar espaços vazios, não é empate
                }
            }
        }
        return true; // Todos os campos estão preenchidos e não houve vitória, é empate
    }

    public static String verificaVitoria(Campo[][] velha) {
        // Verificar linhas e colunas
        for (int i = 0; i < 3; i++) {
            if (velha[i][0].getSimbolo() == velha[i][1].getSimbolo() &&
                    velha[i][1].getSimbolo() == velha[i][2].getSimbolo() &&
                    velha[i][0].getSimbolo() != ' ') {
                return String.valueOf(velha[i][0].getSimbolo()); // Vitória na linha
            }
            if (velha[0][i].getSimbolo() == velha[1][i].getSimbolo() &&
                    velha[1][i].getSimbolo() == velha[2][i].getSimbolo() &&
                    velha[0][i].getSimbolo() != ' ') {
                return String.valueOf(velha[0][i].getSimbolo()); // Vitória na coluna
            }
        }

        // Verificar diagonais
        if (velha[0][0].getSimbolo() == velha[1][1].getSimbolo() &&
                velha[1][1].getSimbolo() == velha[2][2].getSimbolo() &&
                velha[0][0].getSimbolo() != ' ') {
            return String.valueOf(velha[0][0].getSimbolo()); // Vitória na diagonal principal
        }

        if (velha[0][2].getSimbolo() == velha[1][1].getSimbolo() &&
                velha[1][1].getSimbolo() == velha[2][0].getSimbolo() &&
                velha[0][2].getSimbolo() != ' ') {
            return String.valueOf(velha[0][2].getSimbolo()); // Vitória na diagonal secundária
        }

        return ""; // Nenhum vencedor ainda
    }

    public static void exibirPlacar(String nome1, String nome2, int placarJogador1, int placarJogador2, int placarEmpates) {
        System.out.println("\n--- Placar Atual ---");
        System.out.printf("%s: %d vitórias%n", nome1, placarJogador1);
        System.out.printf("%s: %d vitórias%n", nome2, placarJogador2);
        System.out.printf("Empates: %d%n", placarEmpates);
        System.out.println("-----------------------\n");
    }
}