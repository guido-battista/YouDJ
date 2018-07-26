package com.example.guido.youdj.Modelos;

public class Cancion {

    public String titulo;
    public int votos;

    public Cancion(String titulo, int votos)
    {
        this.titulo = titulo;
        this.votos = votos;
    }

    public String getTitulo()
    {
        return this.titulo;
    }

    public int getVotos()
    {
        return this.votos;
    }
}
