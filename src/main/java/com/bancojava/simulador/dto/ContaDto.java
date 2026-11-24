package com.bancojava.simulador.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.bancojava.simulador.model.Conta;
import com.bancojava.simulador.model.ContaCorrente;
import com.bancojava.simulador.model.ContaPoupanca;
import com.bancojava.simulador.model.TipoTransacao;
import com.bancojava.simulador.model.Transacao;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public final class ContaDto {

    private ContaDto() {
    }

    // ----------------------------- Requisições -----------------------------

    public record CorrenteRequest(
            @NotNull @Positive Integer numero,
            @NotNull @Positive Integer agencia,
            @NotNull Long clienteId,
            @NotNull @PositiveOrZero BigDecimal limiteEspecial,
            @NotNull @PositiveOrZero BigDecimal taxaManutencao) {
    }

    public record PoupancaRequest(
            @NotNull @Positive Integer numero,
            @NotNull @Positive Integer agencia,
            @NotNull Long clienteId,
            @NotNull @Positive BigDecimal taxaRendimento) {
    }

    /** Usado em depósitos e saques. */
    public record Operacao(@NotNull @Positive BigDecimal valor) {
    }

    public record Transferencia(
            @NotNull @Positive Integer numeroDestino,
            @NotNull @Positive BigDecimal valor) {
    }

    // ----------------------------- Respostas -----------------------------

    public record Response(
            Long id,
            String tipo,
            Integer numero,
            Integer agencia,
            BigDecimal saldo,
            String titular,
            BigDecimal limiteEspecial,
            BigDecimal taxaManutencao,
            BigDecimal taxaRendimento) {

        public static Response from(Conta conta) {
            BigDecimal limite = null;
            BigDecimal taxaManutencao = null;
            BigDecimal taxaRendimento = null;

            if (conta instanceof ContaCorrente corrente) {
                limite = corrente.getLimiteEspecial();
                taxaManutencao = corrente.getTaxaManutencao();
            } else if (conta instanceof ContaPoupanca poupanca) {
                taxaRendimento = poupanca.getTaxaRendimento();
            }

            return new Response(conta.getId(), conta.getTipoConta(), conta.getNumero(),
                    conta.getAgencia(), conta.getSaldo(), conta.getCliente().getNome(),
                    limite, taxaManutencao, taxaRendimento);
        }
    }

    /** Lançamento do extrato. */
    public record Movimento(
            Long id,
            LocalDateTime dataHora,
            BigDecimal valor,
            TipoTransacao tipo,
            String descricao) {

        public static Movimento from(Transacao transacao) {
            return new Movimento(transacao.getId(), transacao.getDataHora(),
                    transacao.getValor(), transacao.getTipo(), transacao.getDescricao());
        }
    }
}