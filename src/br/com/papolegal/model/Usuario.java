package br.com.papolegal.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Usuario {

    private String id;
    private List<Conversa> conversas;

    public Usuario(String id){
        this.id = id;
        this.conversas = new ArrayList<>();
    }

    public void adicionarConversa(Conversa conversa){
        this.conversas.add(conversa);
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
