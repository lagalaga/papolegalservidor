package br.com.papolegal.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Mensagem implements Serializable {

    private String texto;
    private String rementente;
    private LocalDateTime data;

    private Mensagem(LocalDateTime data, String texto, String rementente) {
        this.texto = texto;
        this.data = data;
        this.rementente = rementente;
    }

    public static Mensagem novaMensagem(LocalDateTime data, String texto, String remetente){

        Mensagem mensagem = new Mensagem(data, texto, remetente);
        return mensagem;

    }

    @Override
    public String toString() {
        return DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").format(this.data) + " - " + this.rementente + " escreveu: " + this.texto;
    }
}
