package com.estudos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ViaCepResponseDto(@JsonProperty("cep") String cep,

                                @JsonProperty("logradouro") String logradouro,

                                @JsonProperty("complemento") String complemento,

                                @JsonProperty("bairro") String bairro,

                                @JsonProperty("localidade") String localidade,

                                @JsonProperty("uf") String uf,

                                @JsonProperty("ibge") String ibge,

                                @JsonProperty("gia") String gia,

                                @JsonProperty("ddd") String ddd,

                                @JsonProperty("siafi") String siafi,

                                @JsonProperty("erro") Boolean erro) {
    public boolean hasError() {
        return erro != null && erro;
    }

    public boolean isValid() {
        return !hasError() && logradouro != null && !logradouro.trim().isEmpty() && bairro != null && !bairro.trim().
                isEmpty() && localidade != null && !localidade.trim().isEmpty() && uf != null && !uf.trim().isEmpty();
    }
}
