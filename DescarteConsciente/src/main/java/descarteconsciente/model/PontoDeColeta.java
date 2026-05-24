package descarteconsciente.model;

/**
 * Entidade que representa um ponto de coleta de medicamentos vencidos.
 */
public class PontoDeColeta {

    // ---- Tipos possíveis ----
    public static final String FARMACIA        = "FARMACIA";
    public static final String DROGARIA        = "DROGARIA";
    public static final String POSTO_SAUDE     = "POSTO_SAUDE";
    public static final String PONTO_MUNICIPAL = "PONTO_MUNICIPAL";

    private int     id;
    private String  nome;
    private String  tipo;
    private String  rua;
    private String  numero;
    private String  bairro;
    private String  cidade;
    private String  cep;
    private String  telefone;
    private String  horarioSegSex;
    private String  horarioSab;
    private String  horarioDom;
    private boolean aceitaControlados;
    private boolean aceitaLiquidos;
    private boolean aceitaComprimidos;
    private boolean aceitaPerfurocortantes;
    private double  latitude;
    private double  longitude;
    private boolean ativo;

    public PontoDeColeta() {}

    // ---- Métodos utilitários ----

    /** Endereço formatado para exibição. */
    public String getEnderecoCompleto() {
        StringBuilder sb = new StringBuilder();
        if (rua    != null) sb.append(rua);
        if (numero != null) sb.append(", ").append(numero);
        if (bairro != null) sb.append(" - ").append(bairro);
        if (cidade != null) sb.append(", ").append(cidade);
        return sb.toString();
    }

    /** Ícone Unicode conforme o tipo do ponto. */
    public String getIcone() {
        return switch (tipo) {
            case FARMACIA        -> "💊";
            case DROGARIA        -> "💊";
            case POSTO_SAUDE     -> "🏥";
            case PONTO_MUNICIPAL -> "♻";
            default              -> "📍";
        };
    }

    /** Rótulo legível para o tipo. */
    public String getTipoLabel() {
        return switch (tipo) {
            case FARMACIA        -> "Farmácia";
            case DROGARIA        -> "Drogaria";
            case POSTO_SAUDE     -> "UBS / Posto de Saúde";
            case PONTO_MUNICIPAL -> "Ponto Municipal";
            default              -> tipo;
        };
    }

    /** Horários formatados para exibição. */
    public String getHorariosFormatados() {
        StringBuilder sb = new StringBuilder();
        if (horarioSegSex != null && !horarioSegSex.isBlank())
            sb.append("Seg–Sex: ").append(horarioSegSex);
        if (horarioSab != null && !horarioSab.isBlank()) {
            if (!sb.isEmpty()) sb.append("  |  ");
            sb.append("Sáb: ").append(horarioSab);
        }
        if (horarioDom != null && !horarioDom.isBlank()) {
            if (!sb.isEmpty()) sb.append("  |  ");
            sb.append("Dom: ").append(horarioDom);
        }
        return sb.isEmpty() ? "Horário não informado" : sb.toString();
    }

    // ---- Getters / Setters ----

    public int getId()                                    { return id; }
    public void setId(int id)                             { this.id = id; }
    public String getNome()                               { return nome; }
    public void setNome(String nome)                      { this.nome = nome; }
    public String getTipo()                               { return tipo; }
    public void setTipo(String tipo)                      { this.tipo = tipo; }
    public String getRua()                                { return rua; }
    public void setRua(String rua)                        { this.rua = rua; }
    public String getNumero()                             { return numero; }
    public void setNumero(String numero)                  { this.numero = numero; }
    public String getBairro()                             { return bairro; }
    public void setBairro(String bairro)                  { this.bairro = bairro; }
    public String getCidade()                             { return cidade; }
    public void setCidade(String cidade)                  { this.cidade = cidade; }
    public String getCep()                                { return cep; }
    public void setCep(String cep)                        { this.cep = cep; }
    public String getTelefone()                           { return telefone; }
    public void setTelefone(String telefone)              { this.telefone = telefone; }
    public String getHorarioSegSex()                      { return horarioSegSex; }
    public void setHorarioSegSex(String h)                { this.horarioSegSex = h; }
    public String getHorarioSab()                         { return horarioSab; }
    public void setHorarioSab(String h)                   { this.horarioSab = h; }
    public String getHorarioDom()                         { return horarioDom; }
    public void setHorarioDom(String h)                   { this.horarioDom = h; }
    public boolean isAceitaControlados()                  { return aceitaControlados; }
    public void setAceitaControlados(boolean v)           { this.aceitaControlados = v; }
    public boolean isAceitaLiquidos()                     { return aceitaLiquidos; }
    public void setAceitaLiquidos(boolean v)              { this.aceitaLiquidos = v; }
    public boolean isAceitaComprimidos()                  { return aceitaComprimidos; }
    public void setAceitaComprimidos(boolean v)           { this.aceitaComprimidos = v; }
    public boolean isAceitaPerfurocortantes()             { return aceitaPerfurocortantes; }
    public void setAceitaPerfurocortantes(boolean v)      { this.aceitaPerfurocortantes = v; }
    public double getLatitude()                           { return latitude; }
    public void setLatitude(double latitude)              { this.latitude = latitude; }
    public double getLongitude()                          { return longitude; }
    public void setLongitude(double longitude)            { this.longitude = longitude; }
    public boolean isAtivo()                              { return ativo; }
    public void setAtivo(boolean ativo)                   { this.ativo = ativo; }
}