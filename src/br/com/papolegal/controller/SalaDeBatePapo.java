package br.com.papolegal.controller;

import br.com.papolegal.model.Conversa;
import br.com.papolegal.model.Mensagem;
import br.com.papolegal.model.Usuario;
import br.com.papolegal.servidor.Servidor;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.*;

public class SalaDeBatePapo implements Runnable {

    private Usuario usuarioRemetente;
    private final Map<Usuario, Socket> mapaDeUsuariosLogados;
    private final Conversa conversa;

    public SalaDeBatePapo(Usuario usuario, Map<Usuario, Socket> mapaDeUsuariosLogados) {
        this.usuarioRemetente = usuario;
        this.mapaDeUsuariosLogados = mapaDeUsuariosLogados;
        this.conversa = new Conversa();
    }

    @Override
    public void run() {


        System.out.println("Acessando sala de bate-papo: " + usuarioRemetente);

        Socket socketdoRemetente = mapaDeUsuariosLogados.get(usuarioRemetente);
        PrintStream telaDoUsuario;
        Scanner tecladoDoUsuario = null;
        Collection<Socket> socketCollection = mapaDeUsuariosLogados.values();



        try {

            telaDoUsuario = new PrintStream(socketdoRemetente.getOutputStream());
            tecladoDoUsuario = new Scanner(socketdoRemetente.getInputStream());

            socketCollection.forEach(socket -> {
                try {
                    new PrintStream(socket.getOutputStream()).println(usuarioRemetente + " entrou na sala");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });



            telaDoUsuario.println("Acessando sala de bate-papo...");
            telaDoUsuario.println("Usuários na sala: " + mapaDeUsuariosLogados.keySet());


        } catch (Exception e) {
            e.printStackTrace();
        }

        //Mensagem para a sala toda
        while (tecladoDoUsuario.hasNextLine()) {
            String texto = tecladoDoUsuario.nextLine();


            if (texto.equals("/q")) {
                socketCollection.forEach(socket -> {
                    try {
                        new PrintStream(socket.getOutputStream()).println(usuarioRemetente + " saiu da sala");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                Servidor.deslogarUsuario(usuarioRemetente);
                break;
            }

            Mensagem mensagem;
            mensagem = Mensagem.novaMensagem(LocalDateTime.now(), texto, usuarioRemetente.getId());

            Mensagem finalMensagem = mensagem;
            socketCollection.forEach(socket -> {
                try {
                    new PrintStream(socket.getOutputStream()).println(finalMensagem);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            conversa.adicionarMensagem(finalMensagem);

        }





/*
        Conversa conversa = criarConversa();

        //De A para B
        Scanner tecladoDoUsuarioA = conversa.getTecladoDoUsuarioA();
        PrintStream telaDoUsuarioB = conversa.getTelaDoUsuarioB();

        new Thread(new Runnable() {
            @Override
            public void run() {


                trocarMensagem(tecladoDoUsuarioA, telaDoUsuarioB, conversa,conversa.getUsuarioA().getId());
            }
        },"De A para B").start();


        //De B para A
        Scanner tecladoDoUsuarioB = conversa.getTecladoDoUsuarioB();
        PrintStream telaDoUsuarioA = conversa.getTelaDoUsuarioA();

        new Thread(new Runnable() {
            @Override
            public void run() {

                trocarMensagem(tecladoDoUsuarioB, telaDoUsuarioA, conversa,conversa.getUsuarioB().getId());

            }
        },"De B para A").start();


    }

    private void trocarMensagem(Scanner tecladoDoUsuario, PrintStream telaDoUsuario, Conversa conversa, String remetente) {
        Mensagem mensagem = null;

        while (tecladoDoUsuario.hasNextLine()){
            String texto = tecladoDoUsuario.nextLine();

            Usuario usuarioRemetente = listaDeUsuariosLogados.stream().filter(u -> u.getId().equals(remetente)).findAny().orElse(null);
            Socket socketRemente = mapaDeUsuariosLogados.get(usuarioRemetente);

            // FIXME: 5/24/18 Agora tá certo mas se outra pessoa quiser falar com o mesmo usuário, não muda o socket

            if (conversa.getUsuarioB() == null){
                conversa.setSocketDoB(socketRemente);
            }

            mensagem = Mensagem.novaMensagem(LocalDateTime.now(),texto, remetente);
            telaDoUsuario.println(mensagem);

            conversa.adicionarMensagem(mensagem);
        }
    }

    // TODO: 5/24/18 Criar método para destruir a conversa
    private Conversa criarConversa(){

        Conversa conversa = null;

        try {

            Socket socketdoRemetente = mapaDeUsuariosLogados.get(usuarioRemetente);
            Scanner tecladoDoUsuario = new Scanner(socketdoRemetente.getInputStream());

            PrintStream mensagemParaUsuario = new PrintStream(socketdoRemetente.getOutputStream());
            mensagemParaUsuario.println("Escolha com quem quer conversar");

            String conversarCom = tecladoDoUsuario.nextLine();
            Usuario usuarioParaConversar = listaDeUsuariosLogados.stream().filter(usuario -> usuario.getId().equals(conversarCom)).findAny().orElse(null);

            Socket socketDoUsuarioParaConversar = mapaDeUsuariosLogados.get(usuarioParaConversar);

            conversa = new Conversa(usuarioRemetente, usuarioParaConversar, socketdoRemetente, socketDoUsuarioParaConversar);

            mensagemParaUsuario.println("Conversando com " + conversarCom);
            mensagemParaUsuario.println("Para sair, digite 'quit'");


        } catch (Exception e) {
            e.printStackTrace();
        }

        return conversa;

    }

    */


    }
}
