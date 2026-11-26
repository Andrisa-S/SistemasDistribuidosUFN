/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package remoto;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import model.Sessao;
import util.QRCode;

/**
 *
 * @author laboratorio
 */
public class ServicoSessaoImpl extends UnicastRemoteObject implements ServicoSessao {
    
    private static final int TAMANHO_QRCODE = 200; // pixels

    private final HashMap<String, Sessao> sessoes;
    private final Consumer<String> log; // usado pela tela do servidor para exibir as consultas
    private final String ipServidor;
    private final BiConsumer<String, String> aoEncurtar; // (link, link encurtado) - tela do servidor
    private final Consumer<Boolean> aoGerarQRCode;       // true se o QR Code foi gerado - tela do servidor

    public ServicoSessaoImpl(HashMap<String, Sessao> sessoes, Consumer<String> log) throws RemoteException {
        this(sessoes, log, null, null);
    }

    public ServicoSessaoImpl(HashMap<String, Sessao> sessoes, Consumer<String> log,
            BiConsumer<String, String> aoEncurtar, Consumer<Boolean> aoGerarQRCode) throws RemoteException {
        super(); // exporta o objeto para receber chamadas remotas
        this.sessoes = sessoes;
        this.log = log;
        this.aoEncurtar = aoEncurtar;
        this.aoGerarQRCode = aoGerarQRCode;
        this.ipServidor = descobrirIp();
    }
    
    // Gera o hash do link, guarda no hashmap (hash -> Sessao) e devolve a Sessao com o link encurtado
    @Override
    public Sessao encurtarLink(String link) throws RemoteException {
        String linkLimpo = (link == null) ? "" : link.trim();
        if (linkLimpo.isEmpty()) {
            throw new RemoteException("Link inválido.");
        }
        Sessao sessao;
        String hash;
        synchronized (sessoes) { // o RMI atende cada cliente em uma thread
            hash = Integer.toHexString(linkLimpo.hashCode());
            Sessao existente = sessoes.get(hash);
            int tentativa = 0;
            // colisão: o mesmo hash já pertence a outro link, então gera um novo hash
            while (existente != null && !existente.getLink().equals(linkLimpo)) {
                hash = Integer.toHexString((linkLimpo + (++tentativa)).hashCode());
                existente = sessoes.get(hash);
            }
            if (existente == null) {
                sessao = new Sessao(linkLimpo, "https://" + ipServidor + "/" + hash);
                sessoes.put(hash, sessao);
            } else {
                sessao = existente;
            }
        }
        log.accept("Encurtado: " + linkLimpo + " -> " + sessao.getDadosSessao());
        if (aoEncurtar != null) {
            aoEncurtar.accept(linkLimpo, sessao.getDadosSessao());
        }
        return sessao;
    }
    
    // Gera o QR Code do link informado; o PNG (Base64) vai em dadosSessao
    @Override
    public Sessao gerarQRCode(String link) throws RemoteException {
        String linkLimpo = (link == null) ? "" : link.trim();
        if (linkLimpo.isEmpty()) {
            throw new RemoteException("Link inválido.");
        }
        try {
            String png = QRCode.gerarBase64(linkLimpo, TAMANHO_QRCODE);
            log.accept("QR Code gerado: " + linkLimpo);
            if (aoGerarQRCode != null) {
                aoGerarQRCode.accept(true);
            }
            return new Sessao(linkLimpo, png);
        } catch (IOException | IllegalArgumentException e) {
            log.accept("Falha ao gerar QR Code: " + e.getMessage());
            if (aoGerarQRCode != null) {
                aoGerarQRCode.accept(false);
            }
            throw new RemoteException("Não foi possível gerar o QR Code: " + e.getMessage(), e);
        }
    }

    // IP da máquina na rede (ignora loopback); usado no link encurtado e no RMI
    public static String descobrirIp() {
        try {
            for (NetworkInterface ni : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (!ni.isUp() || ni.isLoopback() || ni.isVirtual()) {
                    continue;
                }
                for (InetAddress ender : Collections.list(ni.getInetAddresses())) {
                    if (ender instanceof Inet4Address && ender.isSiteLocalAddress()) {
                        return ender.getHostAddress();
                    }
                }
            }
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return "127.0.0.1";
        }
    }
}
