package util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.imageio.ImageIO;

/**
 * Gerador de QR Code sem dependências externas (modo byte, correção de erro M,
 * versões 1 a 10). Suporta links de até 213 bytes (UTF-8).
 */
public final class QRCode {

    // Correção de erro nível M, versões 1..10 (índice 0 = versão 1)
    private static final int[] EC_POR_BLOCO = {10, 16, 26, 18, 24, 16, 18, 22, 22, 26};
    // {blocos do grupo 1, dados por bloco no grupo 1, blocos do grupo 2, dados por bloco no grupo 2}
    private static final int[][] BLOCOS = {
        {1, 16, 0, 0}, {1, 28, 0, 0}, {1, 44, 0, 0}, {2, 32, 0, 0}, {2, 43, 0, 0},
        {4, 27, 0, 0}, {4, 31, 0, 0}, {2, 38, 2, 39}, {3, 36, 2, 37}, {4, 43, 1, 44}
    };
    private static final int[][] ALINHAMENTO = {
        {}, {6, 18}, {6, 22}, {6, 26}, {6, 30}, {6, 34}, {6, 22, 38}, {6, 24, 42}, {6, 26, 46}, {6, 28, 50}
    };

    private final int versao;
    private final int tamanho;
    private final boolean[][] modulos;
    private final boolean[][] funcao;

    private QRCode(int versao, byte[] codewords) {
        this.versao = versao;
        this.tamanho = 17 + 4 * versao;
        this.modulos = new boolean[tamanho][tamanho];
        this.funcao = new boolean[tamanho][tamanho];
        desenharPadroesFuncao();
        desenharCodewords(codewords);
        escolherMascara();
    }

    // ---------------------------------------------------------------- API pública

    /** Gera a matriz de módulos (true = módulo escuro) do texto informado. */
    public static boolean[][] gerarMatriz(String texto) {
        byte[] dados = texto.getBytes(StandardCharsets.UTF_8);
        int v = 0;
        for (int i = 0; i < BLOCOS.length; i++) {
            if (dados.length <= capacidadeDados(i + 1) - (i + 1 >= 10 ? 3 : 2)) {
                v = i + 1;
                break;
            }
        }
        if (v == 0) {
            throw new IllegalArgumentException("Link muito longo para gerar o QR Code (máx. 213 bytes).");
        }
        QRCode qr = new QRCode(v, intercalar(v, montarDados(v, dados)));
        return qr.modulos;
    }

    /** Gera a imagem do QR Code (com borda branca) com aproximadamente o tamanho em pixels informado. */
    public static BufferedImage gerarImagem(String texto, int pixels) {
        boolean[][] m = gerarMatriz(texto);
        int borda = 4;
        int escala = Math.max(1, pixels / (m.length + 2 * borda));
        int lado = (m.length + 2 * borda) * escala;
        BufferedImage img = new BufferedImage(lado, lado, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < lado; y++) {
            for (int x = 0; x < lado; x++) {
                int mx = x / escala - borda, my = y / escala - borda;
                boolean escuro = mx >= 0 && my >= 0 && mx < m.length && my < m.length && m[my][mx];
                img.setRGB(x, y, escuro ? 0x000000 : 0xFFFFFF);
            }
        }
        return img;
    }

    /** Gera o QR Code como PNG codificado em Base64 (formato trafegado pelo RMI dentro da Sessao). */
    public static String gerarBase64(String texto, int pixels) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(gerarImagem(texto, pixels), "png", out);
        return Base64.getEncoder().encodeToString(out.toByteArray());
    }

    /** Converte o Base64 de um PNG de volta para imagem. */
    public static BufferedImage base64ParaImagem(String base64) throws IOException {
        return ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(base64)));
    }

    // ---------------------------------------------------------------- dados

    private static int capacidadeDados(int v) {
        int[] b = BLOCOS[v - 1];
        return b[0] * b[1] + b[2] * b[3];
    }

    /** Monta o fluxo de bits: modo byte + tamanho + dados + terminador + preenchimento. */
    private static byte[] montarDados(int v, byte[] dados) {
        int total = capacidadeDados(v);
        StringBuilder bits = new StringBuilder();
        anexar(bits, 0b0100, 4);
        anexar(bits, dados.length, v >= 10 ? 16 : 8);
        for (byte b : dados) {
            anexar(bits, b & 0xFF, 8);
        }
        anexar(bits, 0, Math.min(4, total * 8 - bits.length()));
        while (bits.length() % 8 != 0) {
            bits.append('0');
        }
        byte[] r = new byte[total];
        int n = bits.length() / 8;
        for (int i = 0; i < n; i++) {
            r[i] = (byte) Integer.parseInt(bits.substring(i * 8, i * 8 + 8), 2);
        }
        for (int i = n, pad = 0; i < total; i++, pad++) {
            r[i] = (byte) (pad % 2 == 0 ? 0xEC : 0x11);
        }
        return r;
    }

    private static void anexar(StringBuilder sb, int valor, int qtd) {
        for (int i = qtd - 1; i >= 0; i--) {
            sb.append((valor >>> i) & 1);
        }
    }

    /** Divide em blocos, calcula Reed-Solomon e intercala dados e correção. */
    private static byte[] intercalar(int v, byte[] dados) {
        int[] cfg = BLOCOS[v - 1];
        int numBlocos = cfg[0] + cfg[2];
        int ec = EC_POR_BLOCO[v - 1];
        byte[][] blocosDados = new byte[numBlocos][];
        byte[][] blocosEc = new byte[numBlocos][];
        byte[] divisor = divisorRS(ec);
        int pos = 0;
        for (int i = 0; i < numBlocos; i++) {
            int len = i < cfg[0] ? cfg[1] : cfg[3];
            blocosDados[i] = java.util.Arrays.copyOfRange(dados, pos, pos + len);
            blocosEc[i] = restoRS(blocosDados[i], divisor);
            pos += len;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int maxDados = Math.max(cfg[1], cfg[3]);
        for (int i = 0; i < maxDados; i++) {
            for (byte[] b : blocosDados) {
                if (i < b.length) {
                    out.write(b[i]);
                }
            }
        }
        for (int i = 0; i < ec; i++) {
            for (byte[] b : blocosEc) {
                out.write(b[i]);
            }
        }
        return out.toByteArray();
    }

    // ---------------------------------------------------------------- Reed-Solomon (GF 256)

    private static int mulGF(int x, int y) {
        int z = 0;
        for (int i = 7; i >= 0; i--) {
            z = (z << 1) ^ ((z >>> 7) * 0x11D);
            z ^= ((y >>> i) & 1) * x;
        }
        return z;
    }

    private static byte[] divisorRS(int grau) {
        byte[] r = new byte[grau];
        r[grau - 1] = 1;
        int raiz = 1;
        for (int i = 0; i < grau; i++) {
            for (int j = 0; j < r.length; j++) {
                r[j] = (byte) mulGF(r[j] & 0xFF, raiz);
                if (j + 1 < r.length) {
                    r[j] ^= r[j + 1];
                }
            }
            raiz = mulGF(raiz, 0x02);
        }
        return r;
    }

    private static byte[] restoRS(byte[] dados, byte[] divisor) {
        byte[] r = new byte[divisor.length];
        for (byte b : dados) {
            int fator = (b ^ r[0]) & 0xFF;
            System.arraycopy(r, 1, r, 0, r.length - 1);
            r[r.length - 1] = 0;
            for (int i = 0; i < r.length; i++) {
                r[i] ^= mulGF(divisor[i] & 0xFF, fator);
            }
        }
        return r;
    }

    // ---------------------------------------------------------------- desenho da matriz

    private void set(int x, int y, boolean escuro) {
        modulos[y][x] = escuro;
        funcao[y][x] = true;
    }

    private void desenharPadroesFuncao() {
        for (int i = 0; i < tamanho; i++) { // timing
            set(6, i, i % 2 == 0);
            set(i, 6, i % 2 == 0);
        }
        desenharFinder(3, 3);
        desenharFinder(tamanho - 4, 3);
        desenharFinder(3, tamanho - 4);
        int[] pos = ALINHAMENTO[versao - 1];
        for (int i = 0; i < pos.length; i++) {
            for (int j = 0; j < pos.length; j++) {
                boolean sobreFinder = (i == 0 && j == 0) || (i == 0 && j == pos.length - 1) || (i == pos.length - 1 && j == 0);
                if (!sobreFinder) {
                    desenharAlinhamento(pos[i], pos[j]);
                }
            }
        }
        desenharFormato(0); // reserva a área
        desenharVersao();
    }

    private void desenharFinder(int cx, int cy) {
        for (int dy = -4; dy <= 4; dy++) {
            for (int dx = -4; dx <= 4; dx++) {
                int x = cx + dx, y = cy + dy;
                if (x >= 0 && x < tamanho && y >= 0 && y < tamanho) {
                    int d = Math.max(Math.abs(dx), Math.abs(dy));
                    set(x, y, d != 2 && d != 4);
                }
            }
        }
    }

    private void desenharAlinhamento(int cx, int cy) {
        for (int dy = -2; dy <= 2; dy++) {
            for (int dx = -2; dx <= 2; dx++) {
                set(cx + dx, cy + dy, Math.max(Math.abs(dx), Math.abs(dy)) != 1);
            }
        }
    }

    private void desenharFormato(int mascara) {
        int dados = (0 << 3) | mascara; // nível M = 00
        int resto = dados;
        for (int i = 0; i < 10; i++) {
            resto = (resto << 1) ^ ((resto >>> 9) * 0x537);
        }
        int bits = ((dados << 10) | resto) ^ 0x5412;
        for (int i = 0; i <= 5; i++) set(8, i, bit(bits, i));
        set(8, 7, bit(bits, 6));
        set(8, 8, bit(bits, 7));
        set(7, 8, bit(bits, 8));
        for (int i = 9; i < 15; i++) set(14 - i, 8, bit(bits, i));
        for (int i = 0; i < 8; i++) set(tamanho - 1 - i, 8, bit(bits, i));
        for (int i = 8; i < 15; i++) set(8, tamanho - 15 + i, bit(bits, i));
        set(8, tamanho - 8, true); // módulo escuro fixo
    }

    private void desenharVersao() {
        if (versao < 7) {
            return;
        }
        int resto = versao;
        for (int i = 0; i < 12; i++) {
            resto = (resto << 1) ^ ((resto >>> 11) * 0x1F25);
        }
        int bits = (versao << 12) | resto;
        for (int i = 0; i < 18; i++) {
            int a = tamanho - 11 + i % 3, b = i / 3;
            set(a, b, bit(bits, i));
            set(b, a, bit(bits, i));
        }
    }

    private static boolean bit(int valor, int i) {
        return ((valor >>> i) & 1) != 0;
    }

    private void desenharCodewords(byte[] dados) {
        int i = 0;
        for (int dir = tamanho - 1; dir >= 1; dir -= 2) {
            if (dir == 6) {
                dir = 5;
            }
            for (int v = 0; v < tamanho; v++) {
                for (int j = 0; j < 2; j++) {
                    int x = dir - j;
                    boolean subindo = ((dir + 1) & 2) == 0;
                    int y = subindo ? tamanho - 1 - v : v;
                    if (!funcao[y][x] && i < dados.length * 8) {
                        modulos[y][x] = bit(dados[i >>> 3], 7 - (i & 7));
                        i++;
                    }
                }
            }
        }
    }

    // ---------------------------------------------------------------- máscara

    private void aplicarMascara(int m) {
        for (int y = 0; y < tamanho; y++) {
            for (int x = 0; x < tamanho; x++) {
                boolean inverte;
                switch (m) {
                    case 0: inverte = (x + y) % 2 == 0; break;
                    case 1: inverte = y % 2 == 0; break;
                    case 2: inverte = x % 3 == 0; break;
                    case 3: inverte = (x + y) % 3 == 0; break;
                    case 4: inverte = (x / 3 + y / 2) % 2 == 0; break;
                    case 5: inverte = x * y % 2 + x * y % 3 == 0; break;
                    case 6: inverte = (x * y % 2 + x * y % 3) % 2 == 0; break;
                    default: inverte = ((x + y) % 2 + x * y % 3) % 2 == 0; break;
                }
                if (inverte && !funcao[y][x]) {
                    modulos[y][x] ^= true;
                }
            }
        }
    }

    private void escolherMascara() {
        int melhor = 0;
        int menorPenalidade = Integer.MAX_VALUE;
        for (int m = 0; m < 8; m++) {
            aplicarMascara(m);
            desenharFormato(m);
            int p = penalidade();
            if (p < menorPenalidade) {
                menorPenalidade = p;
                melhor = m;
            }
            aplicarMascara(m); // desfaz
        }
        aplicarMascara(melhor);
        desenharFormato(melhor);
    }

    private int penalidade() {
        int total = 0;
        // regra 1: sequências de 5+ módulos iguais (linhas e colunas)
        for (int a = 0; a < tamanho; a++) {
            for (int dir = 0; dir < 2; dir++) {
                int seq = 1;
                for (int b = 1; b < tamanho; b++) {
                    boolean atual = dir == 0 ? modulos[a][b] : modulos[b][a];
                    boolean ant = dir == 0 ? modulos[a][b - 1] : modulos[b - 1][a];
                    if (atual == ant) {
                        seq++;
                        if (seq == 5) total += 3;
                        else if (seq > 5) total++;
                    } else {
                        seq = 1;
                    }
                }
            }
        }
        // regra 2: blocos 2x2
        for (int y = 0; y < tamanho - 1; y++) {
            for (int x = 0; x < tamanho - 1; x++) {
                boolean c = modulos[y][x];
                if (c == modulos[y][x + 1] && c == modulos[y + 1][x] && c == modulos[y + 1][x + 1]) {
                    total += 3;
                }
            }
        }
        // regra 3: padrão parecido com o finder (1011101 com 4 claros de um lado)
        boolean[] padA = {true, false, true, true, true, false, true, false, false, false, false};
        boolean[] padB = {false, false, false, false, true, false, true, true, true, false, true};
        for (int a = 0; a < tamanho; a++) {
            for (int b = 0; b + 11 <= tamanho; b++) {
                boolean okLinhaA = true, okLinhaB = true, okColA = true, okColB = true;
                for (int k = 0; k < 11; k++) {
                    okLinhaA &= modulos[a][b + k] == padA[k];
                    okLinhaB &= modulos[a][b + k] == padB[k];
                    okColA &= modulos[b + k][a] == padA[k];
                    okColB &= modulos[b + k][a] == padB[k];
                }
                if (okLinhaA) total += 40;
                if (okLinhaB) total += 40;
                if (okColA) total += 40;
                if (okColB) total += 40;
            }
        }
        // regra 4: proporção de módulos escuros
        int escuros = 0;
        for (boolean[] linha : modulos) {
            for (boolean c : linha) {
                if (c) escuros++;
            }
        }
        int k = Math.abs(escuros * 20 - tamanho * tamanho * 10) / (tamanho * tamanho);
        return total + k * 10;
    }
}
