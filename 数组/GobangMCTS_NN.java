package 数组;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class GobangMCTS_NN extends JFrame {
    private static final int SIZE = 9; // 小棋盘，更快
    private static final int CELL = 50;
    private static final int MARGIN = 30;
    private static final int BLACK = 1;
    private static final int WHITE = 2;
    private static final int SIM_COUNT = 1000;

    private int[][] board = new int[SIZE][SIZE];
    private boolean gameOver = false;
    private JPanel panel;
    private SimpleNN nn = new SimpleNN(); // 轻量级神经网络

    public GobangMCTS_NN() {
        setTitle("MCTS + 神经网络 五子棋");
        setSize(SIZE * CELL + MARGIN * 2, SIZE * CELL + MARGIN * 2 + 30);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                draw(g2);
            }
        };
        panel.setBackground(new Color(225, 195, 150));
        add(panel);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (gameOver) return;
                int col = (e.getX() - MARGIN + CELL / 2) / CELL;
                int row = (e.getY() - MARGIN + CELL / 2) / CELL;

                if (row < 0 || row >= SIZE || col < 0 || col >= SIZE || board[row][col] != 0) return;

                board[row][col] = BLACK;
                panel.repaint();
                if (isWin(row, col, BLACK)) {
                    JOptionPane.showMessageDialog(null, "你赢了");
                    gameOver = true;
                    return;
                }

                // AI：MCTS + 神经网络
                int[] best = mctsAiMove();
                board[best[0]][best[1]] = WHITE;
                panel.repaint();

                if (isWin(best[0], best[1], WHITE)) {
                    JOptionPane.showMessageDialog(null, "MCTS+NN 赢了");
                    gameOver = true;
                }
            }
        });
    }

    // ==========================================
    // 🔥 MCTS + 神经网络 核心
    // ==========================================
    private int[] mctsAiMove() {
        Node root = new Node(-1, -1, null, copyBoard(board), WHITE);
        for (int i = 0; i < SIM_COUNT; i++) {
            Node leaf = select(root);
            expand(leaf);
            double value = nn.evaluate(leaf.board, WHITE); // 神经网络评估
            backup(leaf, value);
        }
        Node best = root.children.stream().max(Comparator.comparingDouble(n -> n.visit)).get();
        return new int[]{best.r, best.c};
    }

    private Node select(Node n) {
        while (!n.children.isEmpty()) {
            n = bestChild(n);
        }
        return n;
    }

    private Node bestChild(Node n) {
        double c = 1.4;
        double best = -999;
        Node bestNode = null;
        for (Node ch : n.children) {
            double uct = ch.win / (ch.visit + 1e-6) + c * Math.sqrt(Math.log(n.visit + 1) / (ch.visit + 1e-6));
            if (uct > best) {
                best = uct;
                bestNode = ch;
            }
        }
        return bestNode;
    }

    private void expand(Node n) {
        int player = n.player == WHITE ? BLACK : WHITE;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (n.board[i][j] == 0) {
                    int[][] b = copyBoard(n.board);
                    b[i][j] = player;
                    n.children.add(new Node(i, j, n, b, player));
                }
            }
        }
    }

    private void backup(Node n, double v) {
        while (n != null) {
            n.visit++;
            n.win += v;
            n = n.parent;
        }
    }

    // ==========================================
    // 🔥 极简神经网络（模拟 AlphaZero 策略+价值网络）
    // 作用：给棋盘局面打分（胜率）
    // ==========================================
    class SimpleNN {
        double evaluate(int[][] b, int player) {
            int score = 0;
            int[][] dirs = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (b[i][j] == 0) continue;
                    for (int[] d : dirs) {
                        int cnt = countLine(b, i, j, d[0], d[1], b[i][j]);
                        if (cnt >= 5) return b[i][j] == WHITE ? 1 : -1;
                        if (cnt == 4) score += (b[i][j] == WHITE ? 10 : -10);
                        if (cnt == 3) score += (b[i][j] == WHITE ? 5 : -5);
                    }
                }
            }
            return Math.tanh(score / 20.0); // 输出 -1~1（胜率）
        }

        private int countLine(int[][] b, int r, int c, int dx, int dy, int p) {
            int cnt = 0;
            for (int i = 0; i < 5; i++) {
                int nr = r + dx * i, nc = c + dy * i;
                if (nr >= 0 && nr < SIZE && nc >= 0 && nc < SIZE && b[nr][nc] == p) cnt++;
                else break;
            }
            return cnt;
        }
    }

    // ==========================================
    // 工具 & 绘图
    // ==========================================
    private int[][] copyBoard(int[][] b) {
        int[][] c = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) System.arraycopy(b[i], 0, c[i], 0, SIZE);
        return c;
    }

    private boolean isWin(int r, int c, int p) {
        int[][] d = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};
        for (int[] k : d) {
            int cnt = 1;
            for (int i = 1; ; i++) {
                int nr = r + k[0] * i;
                int nc = c + k[1] * i;
                if (nr >= 0 && nr < SIZE && nc >= 0 && nc < SIZE && board[nr][nc] == p) cnt++;
                else break;
            }
            for (int i = 1; ; i++) {
                int nr = r - k[0] * i;
                int nc = c - k[1] * i;
                if (nr >= 0 && nr < SIZE && nc >= 0 && nc < SIZE && board[nr][nc] == p) cnt++;
                else break;
            }
            if (cnt >= 5) return true;
        }
        return false;
    }

    private void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        for (int i = 0; i < SIZE; i++) {
            int x = MARGIN + i * CELL;
            int y = MARGIN + i * CELL;
            g.drawLine(x, MARGIN, x, MARGIN + CELL * (SIZE - 1));
            g.drawLine(MARGIN, y, MARGIN + CELL * (SIZE - 1), y);
        }
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == BLACK) {
                    g.setColor(Color.BLACK);
                    g.fillOval(MARGIN + j * CELL - 18, MARGIN + i * CELL - 18, 36, 36);
                } else if (board[i][j] == WHITE) {
                    g.setColor(Color.WHITE);
                    g.fillOval(MARGIN + j * CELL - 18, MARGIN + i * CELL - 18, 36, 36);
                    g.drawOval(MARGIN + j * CELL - 18, MARGIN + i * CELL - 18, 36, 36);
                }
            }
        }
    }

    static class Node {
        int r, c;
        Node parent;
        List<Node> children = new ArrayList<>();
        int[][] board;
        int player;
        double win = 0;
        int visit = 0;

        Node(int r, int c, Node parent, int[][] board, int player) {
            this.r = r;
            this.c = c;
            this.parent = parent;
            this.board = board;
            this.player = player;
        }
    }

    public static void main(String[] args) {
        new GobangMCTS_NN().setVisible(true);
    }
}