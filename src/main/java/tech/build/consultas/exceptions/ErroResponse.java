package tech.build.consultas.exceptions;

public class ErroResponse {
    private String mensagem;
    private int status;

    public ErroResponse(String mensagem, int status) {
        this.mensagem = mensagem;
        this.status = status;
    }

    public String getMensagem() {
        return mensagem;
    }

    public int getStatus() {
        return status;
    }
}
