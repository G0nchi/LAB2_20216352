package com.example.lab2_20216352;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;

import com.example.lab2_20216352.databinding.ActivityEquipoListBinding;

import java.util.ArrayList;
import java.util.List;

public class EquipoListActivity extends AppCompatActivity {

    public static final String EXTRA_CODIGO_PUCP = "extra_codigo_pucp";

    private ActivityEquipoListBinding binding;
    private final List<Equipo> equiposCompletos = new ArrayList<>();
    private final List<Equipo> equiposFiltrados = new ArrayList<>();
    private EquipoAdapter equipoAdapter;

    private String filtroTipo;
    private String filtroEstado;

    private ActionMode actionMode;
    private Equipo equipoSeleccionado;

    private final ActivityResultLauncher<Intent> formLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    cargarYFiltrarEquipos();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEquipoListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarLista);
        String codigoPucp = getIntent().getStringExtra(EXTRA_CODIGO_PUCP);
        if (codigoPucp != null && !codigoPucp.isEmpty()) {
            binding.toolbarLista.setSubtitle(getString(R.string.hint_codigo_pucp) + ": " + codigoPucp);
        }

        setupFiltros();
        setupListado();
        setupFab();

        cargarYFiltrarEquipos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_refrescar) {
            resetFiltros();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupFiltros() {
        List<String> tipos = new ArrayList<>();
        tipos.add(getString(R.string.todos));
        String[] tiposEquipo = getResources().getStringArray(R.array.tipos_equipo);
        for (String tipo : tiposEquipo) {
            tipos.add(tipo);
        }

        List<String> estados = new ArrayList<>();
        estados.add(getString(R.string.todos));
        estados.add(getString(R.string.operativo));
        estados.add(getString(R.string.mantenimiento));
        estados.add(getString(R.string.fuera_servicio));

        ArrayAdapter<String> tipoAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tipos);
        ArrayAdapter<String> estadoAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, estados);

        binding.acFiltroTipo.setAdapter(tipoAdapter);
        binding.acFiltroEstado.setAdapter(estadoAdapter);

        filtroTipo = getString(R.string.todos);
        filtroEstado = getString(R.string.todos);
        binding.acFiltroTipo.setText(filtroTipo, false);
        binding.acFiltroEstado.setText(filtroEstado, false);

        binding.acFiltroTipo.setOnItemClickListener((parent, view, position, id) -> {
            filtroTipo = (String) parent.getItemAtPosition(position);
            aplicarFiltros();
        });

        binding.acFiltroEstado.setOnItemClickListener((parent, view, position, id) -> {
            filtroEstado = (String) parent.getItemAtPosition(position);
            aplicarFiltros();
        });
    }

    private void setupListado() {
        equipoAdapter = new EquipoAdapter(this, equiposFiltrados);
        binding.lvEquipos.setAdapter(equipoAdapter);
        binding.lvEquipos.setEmptyView(binding.tvEmpty);

        binding.lvEquipos.setOnItemLongClickListener((parent, view, position, id) -> {
            equipoSeleccionado = equiposFiltrados.get(position);
            if (actionMode != null) {
                actionMode.finish();
            }
            actionMode = startSupportActionMode(actionModeCallback);
            return true;
        });
    }

    private final ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            getMenuInflater().inflate(R.menu.menu_context_equipo, menu);
            mode.setTitle(getString(R.string.titulo_lista));
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (equipoSeleccionado == null) {
                mode.finish();
                return true;
            }

            int itemId = item.getItemId();
            if (itemId == R.id.action_editar) {
                abrirFormulario(equipoSeleccionado.getCodigo());
                mode.finish();
                return true;
            }

            if (itemId == R.id.action_eliminar) {
                confirmarEliminacion(equipoSeleccionado);
                mode.finish();
                return true;
            }

            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            equipoSeleccionado = null;
        }
    };

    private void setupFab() {
        binding.fabAgregar.setOnClickListener(v -> abrirFormulario(null));
    }

    private void abrirFormulario(String codigoEquipo) {
        Intent intent = new Intent(this, EquipoFormActivity.class);
        if (codigoEquipo != null) {
            intent.putExtra(EquipoFormActivity.EXTRA_CODIGO, codigoEquipo);
        }
        formLauncher.launch(intent);
    }

    private void confirmarEliminacion(Equipo equipo) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirmar_eliminar_titulo)
                .setNegativeButton(R.string.cancelar, null)
                .setPositiveButton(R.string.aceptar, (dialog, which) -> {
                    EquipoRepository.delete(equipo.getCodigo());
                    cargarYFiltrarEquipos();
                })
                .show();
    }

    private void resetFiltros() {
        filtroTipo = getString(R.string.todos);
        filtroEstado = getString(R.string.todos);
        binding.acFiltroTipo.setText(filtroTipo, false);
        binding.acFiltroEstado.setText(filtroEstado, false);
        aplicarFiltros();
    }

    private void cargarYFiltrarEquipos() {
        equiposCompletos.clear();
        equiposCompletos.addAll(EquipoRepository.getAll());
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        equiposFiltrados.clear();

        for (Equipo equipo : equiposCompletos) {
            if (cumpleFiltroTipo(equipo) && cumpleFiltroEstado(equipo)) {
                equiposFiltrados.add(equipo);
            }
        }

        equipoAdapter.notifyDataSetChanged();
    }

    private boolean cumpleFiltroTipo(Equipo equipo) {
        return filtroTipo.equals(getString(R.string.todos)) || filtroTipo.equalsIgnoreCase(equipo.getTipo());
    }

    private boolean cumpleFiltroEstado(Equipo equipo) {
        return filtroEstado.equals(getString(R.string.todos)) || filtroEstado.equalsIgnoreCase(equipo.getEstado());
    }
}
