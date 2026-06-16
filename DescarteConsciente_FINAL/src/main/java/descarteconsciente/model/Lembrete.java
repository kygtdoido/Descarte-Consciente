package descarteconsciente.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Representa um lembrete de validade de medicamento cadastrado pelo usuário.
 */
public class Lembrete {

    private int       id;
    private int       usuarioId;
    private String    medicamento;
    private LocalDate dataValidade;
    private boolean   notificado;

    public Lembrete() {}

    public Lembrete(int usuarioId, String medicamento, LocalDate dataValidade) {
        this.usuarioId    = usuarioId;
        this.medicamento  = medicamento;
        this.dataValidade = dataValidade;
    }

    // ---- Utilitários ----

    /** Dias restantes até o vencimento (negativo = já vencido). */
    public long diasParaVencer() {
        return ChronoUnit.DAYS.between(LocalDate.now(), dataValidade);
    }

    public boolean isVencido()      { return diasParaVencer() < 0; }
    public boolean isVenceHoje()    { return diasParaVencer() == 0; }
    public boolean isVenceEmBreve() { return diasParaVencer() >= 0 && diasParaVencer() <= 30; }

    public String getStatusLabel() {
        long dias = diasParaVencer();
        if (dias < 0)  return "Vencido há " + Math.abs(dias) + " dia(s)";
        if (dias == 0) return "Vence hoje!";
        if (dias <= 30) return "Vence em " + dias + " dia(s)";
        return "Válido";
    }

    // ---- Getters / Setters ----

    public int getId()                          { return id; }
    public void setId(int id)                   { this.id = id; }
    public int getUsuarioId()                   { return usuarioId; }
    public void setUsuarioId(int usuarioId)     { this.usuarioId = usuarioId; }
    public String getMedicamento()              { return medicamento; }
    public void setMedicamento(String m)        { this.medicamento = m; }
    public LocalDate getDataValidade()          { return dataValidade; }
    public void setDataValidade(LocalDate d)    { this.dataValidade = d; }
    public boolean isNotificado()               { return notificado; }
    public void setNotificado(boolean n)        { this.notificado = n; }
}