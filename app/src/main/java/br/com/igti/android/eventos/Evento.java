package br.com.igti.android.eventos;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Evento {
    private static final String JSON_ID = "id";
    private static final String JSON_TITULO = "titulo";
    private static final String JSON_DESCRICAO = "descricao";

    private UUID mId;
    private String mTitulo;
    private String mDescricao;
    private Date  mData;

    public Evento(){
        mId   = UUID.randomUUID();
        mData = Calendar.getInstance().getTime();
    }

    public Evento(UUID id, String titulo, String descricao, Date date) {
        mId = id;
        mTitulo = titulo;
        mDescricao = descricao;
        mData = date;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public void setTitulo(String titulo) {
        mTitulo = titulo;
    }

    public Date getData() {
        return mData;
    }

    public void setData(Date data) {
        mData = data;
    }

    public String getDescricao() {
        return mDescricao;
    }

    public void setDescricao(String descricao) {
        mDescricao = descricao;
    }

    @Override
    public String toString() {
        return mTitulo;
    }

    public Evento(JSONObject json) throws JSONException {
        mId = UUID.fromString(json.getString(JSON_ID));
        mTitulo = json.getString(JSON_TITULO);
        mDescricao = json.getString(JSON_DESCRICAO);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId.toString());
        json.put(JSON_TITULO, mTitulo);
        json.put(JSON_DESCRICAO, mDescricao);
        return json;
    }


}
