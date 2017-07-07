package acm;

import java.util.Scanner;

public class test {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String string = "";
        string = scanner.nextLine().trim();
        int col = Integer.valueOf(string.split(" ")[0]);
        int row = Integer.valueOf(string.split(" ")[1]);
        int[][] num = new int[col][row];
        int[][] map = new int[col][row];
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                map[i][j] = 0;
                num[i][j] = Integer.valueOf(string.split(" ")[i * row + j + 2]);
            }
        }
        int snackX = 0;
        int snackY = 0;
        map[0][0] = 1;
        System.out.printf("%d ", num[snackX][snackY]);
        while (true) {
            if (snackY + 1 < row && map[snackX][snackY + 1] == 0) {
                System.out.printf("%d ", num[snackX][snackY + 1]);
                map[snackX][snackY + 1] = 1;
                snackY = snackY + 1;
            } else if (snackX + 1 < col && map[snackX + 1][snackY] == 0) {
                System.out.printf("%d ", num[snackX + 1][snackY]);
                map[snackX + 1][snackY] = 1;
                snackX = snackX + 1;
            } else if (snackY - 1 >= 0 && map[snackX][snackY - 1] == 0) {
                System.out.printf("%d ", num[snackX][snackY - 1]);
                map[snackX][snackY - 1] = 1;
                snackY = snackY - 1;
            } else if (map[snackX - 1][snackY] == 0) {
                System.out.printf("%d ", num[snackX - 1][snackY]);
                map[snackX - 1][snackY] = 1;
                snackX = snackX - 1;
            } else {
                break;
            }
        }
    }

}
