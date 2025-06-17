package com.unicesumar.film_list.request;

import java.time.LocalDate;

public class AssistirFilmeRequest {
    private LocalDate dataAssistido;

    public LocalDate getDataAssistido() {
        return dataAssistido;
    }

    public void setDataAssistido(LocalDate dataAssistido) {
        this.dataAssistido = dataAssistido;
    }
}
