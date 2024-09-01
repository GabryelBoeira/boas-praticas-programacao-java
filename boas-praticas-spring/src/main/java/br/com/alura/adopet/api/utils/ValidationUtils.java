package br.com.alura.adopet.api.utils;

public class ValidationUtils {

    public static boolean isNumero(String idOuNome) {
        return idOuNome.matches("\\d+");
    }
}
