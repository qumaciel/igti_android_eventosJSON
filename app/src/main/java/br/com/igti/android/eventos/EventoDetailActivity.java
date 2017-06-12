package br.com.igti.android.eventos;

import android.app.Fragment;

import java.util.UUID;


public class EventoDetailActivity extends SingleFragmentActivity implements EventoDetailFragment.Callbacks{

    @Override
    protected Fragment createFragment() {
        UUID id = (UUID) getIntent().getSerializableExtra(EventoDetailFragment.ARG_ID_EVENTO);
        return EventoDetailFragment.newInstance(id);
    }

    @Override
    public void onEventoUpdated(Evento evento) {

    }
}
