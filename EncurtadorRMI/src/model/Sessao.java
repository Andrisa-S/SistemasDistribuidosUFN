/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Andrisa Santos
 */
public class Sessao implements Serializable {
    private static final long serialVersionUID = 1L;
    
    String link;
    String dadosSessao;

    public Sessao(String link, String dadosSessao) {
        this.link = link;
        this.dadosSessao = dadosSessao;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDadosSessao() {
        return dadosSessao;
    }

    public void setDadosSessao(String dadosSessao) {
        this.dadosSessao = dadosSessao;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.link);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Sessao other = (Sessao) obj;
        return Objects.equals(this.link, other.link);
    }

    @Override
    public String toString() {
        return "Sessao{" + "link=" + link + ", dadosSessao=" + dadosSessao + '}';
    }
}
