package br.com.papolegal.servidor;

import br.com.papolegal.controller.SalaDeBatePapo;
import br.com.papolegal.model.Usuario;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor {

    private ServerSocket serverSocket;
    private ExecutorService poolDeThreads;
    private static Map<Usuario, Socket> mapaDeSocketParaUsuario;



    public Servidor() throws IOException {
        System.out.println("Iniciando o servidor...");

        this.serverSocket = new ServerSocket(12345);
        this.poolDeThreads = Executors.newCachedThreadPool();
        mapaDeSocketParaUsuario = new HashMap<>();

        System.out.println("Servidor Iniciado");
    }


    public void rodar() throws IOException {

        System.out.println("Servidor pronto para aceitar conexões");

        while (true) {

            Socket socket = this.serverSocket.accept();

            //Associar o socket ao usuário usando um MAP?
            String idDoUsuario = pegaIdDoUsuario(socket);
            Usuario usuario = new Usuario(idDoUsuario);
            logarUsuario(socket, usuario);

            //Log de usuarios
            System.out.println("Usuários logados: " + mapaDeSocketParaUsuario.values());

            System.out.println("Socket: " + mapaDeSocketParaUsuario.get(usuario));
            System.out.println("ID: " + idDoUsuario);

            poolDeThreads.execute(new SalaDeBatePapo(usuario, mapaDeSocketParaUsuario));
            System.out.println("Conexão aceita: " + idDoUsuario);


        }
    }

    private static void logarUsuario(Socket socket, Usuario usuario) {

        mapaDeSocketParaUsuario.put(usuario, socket);


    }

    public static void deslogarUsuario(Usuario usuario) {
            try {
                mapaDeSocketParaUsuario.get(usuario).close();
                System.out.println("Conexão de " + usuario.getId() + " fechada");
            } catch (IOException e) {
                e.printStackTrace();
            }
            mapaDeSocketParaUsuario.remove(usuario);
    }

    private String pegaIdDoUsuario(Socket socket) throws IOException {

        InputStream is = socket.getInputStream();
        Scanner tecladoDoUsuario = new Scanner(is);

        OutputStream os = socket.getOutputStream();
        PrintStream telaDoUsuario = new PrintStream(os);

        String id = "";

        telaDoUsuario.println("Digite o seu ID");

        while (tecladoDoUsuario.hasNextLine()) {
            id = tecladoDoUsuario.nextLine();
            break;
        }

        return id;


    }

    public static void main(String[] args) throws IOException {

        Servidor servidor = new Servidor();
        servidor.rodar();
    }


}
