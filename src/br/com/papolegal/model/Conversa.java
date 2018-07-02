package br.com.papolegal.model;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Conversa implements Serializable {

    private List<Mensagem> mensagens;

    public Conversa() {
        this.mensagens = new ArrayList<>();
    }

    public void adicionarMensagem(Mensagem mensagem){
        this.mensagens.add(mensagem);
    }

}
