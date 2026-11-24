package com.bancojava.simulador.dto;

import com.bancojava.simulador.model.Cliente;
import com.bancojava.simulador.model.TipoCliente;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public final class ClienteDto {

    private ClienteDto() {
    }

    public record Request(
            @NotBlank(message = "O nome é obrigatório.") @Size(max = 120) String nome,
            @NotBlank(message = "O documento é obrigatório.") @Size(max = 20) String documento,
            @NotNull(message = "O tipo do cliente é obrigatório.") TipoCliente tipo) {
    }

    public record Response(Long id, String nome, String documento, TipoCliente tipo) {

        public static Response from(Cliente cliente) {
            return new Response(cliente.getId(), cliente.getNome(),
                    cliente.getDocumento(), cliente.getTipoCliente());
        }
    }
}