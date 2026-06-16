package descarteconsciente.model;

/**
 * Entidade que representa um ponto de coleta de medicamentos vencidos.
 */
public class PontoDeColeta {

    public static final String FARMACIA = "FARMACIA";
    public static final String DROGARIA = "DROGARIA";
    public static final String POSTO_SAUDE = "POSTO_SAUDE";
    public static final String PONTO_MUNICIPAL = "PONTO_MUNICIPAL";

    private int id;
    private String nome;
    private String tipo;
    private String rua;
    private String numero;
    private String bairro;
    private String cidade;
    private String cep;
    private String telefone;
    private String horarioSegSex;
    private String horarioSab;
    private String horarioDom;
    private boolean aceitaControlados;
    private boolean aceitaLiquidos;
    private boolean aceitaComprimidos;
    private boolean aceitaPerfurocortantes;
    private double latitude;
    private double longitude;
    private boolean ativo;

    public PontoDeColeta() {
    }

    public String getEnderecoCompleto() {
        StringBuilder sb = new StringBuilder();
        if (rua != null) sb.append(rua);
        if (numero != null) sb.append(", ").append(numero);
        if (bairro != null) sb.append(" - ").append(bairro);
        if (cidade != null) sb.append(", ").append(cidade);
        return sb.toString();
    }

    public String getIcone() {
        if (FARMACIA.equals(tipo)) return "F";
        if (DROGARIA.equals(tipo)) return "D";
        if (POSTO_SAUDE.equals(tipo)) return "P";
        if (PONTO_MUNICIPAL.equals(tipo)) return "M";
        return "*";
    }

    public String getTipoLabel() {
        if (FARMACIA.equals(tipo)) return "Farmácia";
        if (DROGARIA.equals(tipo)) return "Drogaria";
        if (POSTO_SAUDE.equals(tipo)) return "UBS / Posto de Saúde";
        if (PONTO_MUNICIPAL.equals(tipo)) return "Ponto Municipal";
        return tipo;
    }

    public String getHorariosFormatados() {
        StringBuilder sb = new StringBuilder();
        if (horarioSegSex != null && !horarioSegSex.trim().isEmpty()) {
            sb.append("Seg-Sex: ").append(horarioSegSex);
        }
        if (horarioSab != null && !horarioSab.trim().isEmpty()) {
            if (sb.length() > 0) sb.append("  |  ");
            sb.append("Sáb: ").append(horarioSab);
        }
        if (horarioDom != null && !horarioDom.trim().isEmpty()) {
            if (sb.length() > 0) sb.append("  |  ");
            sb.append("Dom: ").append(horarioDom);
        }
        return sb.length() == 0 ? "Horário não informado" : sb.toString();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getRua() { return rua; }
    public void setRua(String rua) { this.rua = rua; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getHorarioSegSex() { return horarioSegSex; }
    public void setHorarioSegSex(String horarioSegSex) { this.horarioSegSex = horarioSegSex; }
    public String getHorarioSab() { return horarioSab; }
    public void setHorarioSab(String horarioSab) { this.horarioSab = horarioSab; }
    public String getHorarioDom() { return horarioDom; }
    public void setHorarioDom(String horarioDom) { this.horarioDom = horarioDom; }
    public boolean isAceitaControlados() { return aceitaControlados; }
    public void setAceitaControlados(boolean aceitaControlados) { this.aceitaControlados = aceitaControlados; }
    public boolean isAceitaLiquidos() { return aceitaLiquidos; }
    public void setAceitaLiquidos(boolean aceitaLiquidos) { this.aceitaLiquidos = aceitaLiquidos; }
    public boolean isAceitaComprimidos() { return aceitaComprimidos; }
    public void setAceitaComprimidos(boolean aceitaComprimidos) { this.aceitaComprimidos = aceitaComprimidos; }
    public boolean isAceitaPerfurocortantes() { return aceitaPerfurocortantes; }
    public void setAceitaPerfurocortantes(boolean aceitaPerfurocortantes) { this.aceitaPerfurocortantes = aceitaPerfurocortantes; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}
