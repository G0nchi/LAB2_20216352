package com.example.lab2_20216352;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lab2_20216352.databinding.ActivityEquipoFormBinding;

public class EquipoFormActivity extends AppCompatActivity {

    public static final String EXTRA_CODIGO = "extra_codigo";

    private ActivityEquipoFormBinding binding;
    private boolean editMode;
    private Equipo equipoOriginal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEquipoFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarForm);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setupTipoDropdown();
        configurarModo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
            return true;
        }
        if (itemId == R.id.action_guardar) {
            guardarConConfirmacion();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupTipoDropdown() {
        String[] tiposEquipo = getResources().getStringArray(R.array.tipos_equipo);
        ArrayAdapter<String> tiposAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tiposEquipo);
        binding.acTipoEquipo.setAdapter(tiposAdapter);
    }

    private void configurarModo() {
        String codigo = getIntent().getStringExtra(EXTRA_CODIGO);
        editMode = codigo != null;

        if (!editMode) {
            binding.toolbarForm.setTitle(R.string.titulo_registrar);
            return;
        }

        equipoOriginal = EquipoRepository.findByCodigo(codigo);
        if (equipoOriginal == null) {
            finish();
            return;
        }

        binding.toolbarForm.setTitle(R.string.titulo_actualizar);
        cargarEquipoEnFormulario(equipoOriginal);

        binding.etCodigo.setEnabled(false);
        binding.acTipoEquipo.setEnabled(false);
    }

    private void cargarEquipoEnFormulario(Equipo equipo) {
        binding.etCodigo.setText(equipo.getCodigo());
        binding.etNombre.setText(equipo.getNombre());
        binding.acTipoEquipo.setText(equipo.getTipo(), false);
        binding.etObservaciones.setText(equipo.getObservaciones());
        seleccionarEstado(equipo.getEstado());
    }

    private void seleccionarEstado(String estado) {
        if (estado.equals(getString(R.string.operativo))) {
            binding.rbOperativo.setChecked(true);
        } else if (estado.equals(getString(R.string.mantenimiento))) {
            binding.rbMantenimiento.setChecked(true);
        } else if (estado.equals(getString(R.string.fuera_servicio))) {
            binding.rbFueraServicio.setChecked(true);
        }
    }

    private void guardarConConfirmacion() {
        if (!validarFormulario()) {
            return;
        }

        int title = editMode ? R.string.confirmar_actualizar_titulo : R.string.confirmar_titulo;
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setNegativeButton(R.string.cancelar, null)
                .setPositiveButton(R.string.aceptar, (dialog, which) -> guardarEquipo())
                .show();
    }

    private boolean validarFormulario() {
        String codigo = getText(binding.etCodigo);
        String nombre = getText(binding.etNombre);
        String tipo = getText(binding.acTipoEquipo);

        if (codigo.isEmpty()) {
            binding.etCodigo.setError(getString(R.string.error_codigo_requerido));
            return false;
        }

        if (nombre.isEmpty()) {
            binding.etNombre.setError(getString(R.string.error_campo_requerido));
            return false;
        }

        if (tipo.isEmpty()) {
            binding.acTipoEquipo.setError(getString(R.string.error_campo_requerido));
            return false;
        }

        if (binding.rgEstado.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, getString(R.string.error_estado_requerido), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void guardarEquipo() {
        String codigo = getText(binding.etCodigo);
        String nombre = getText(binding.etNombre);
        String tipo = getText(binding.acTipoEquipo);
        String estado = getEstadoSeleccionado();
        String observaciones = getText(binding.etObservaciones);

        Equipo equipo = new Equipo(codigo, nombre, tipo, estado, observaciones);

        if (!editMode) {
            boolean creado = EquipoRepository.add(equipo);
            if (!creado) {
                Toast.makeText(this, getString(R.string.msg_codigo_duplicado), Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, getString(R.string.msg_registrado), Toast.LENGTH_SHORT).show();
        } else {
            EquipoRepository.update(equipoOriginal.getCodigo(), equipo);
            Toast.makeText(this, getString(R.string.msg_actualizado), Toast.LENGTH_SHORT).show();
        }

        setResult(RESULT_OK);
        finish();
    }

    private String getEstadoSeleccionado() {
        int checkedId = binding.rgEstado.getCheckedRadioButtonId();
        if (checkedId == R.id.rbOperativo) {
            return getString(R.string.operativo);
        }
        if (checkedId == R.id.rbMantenimiento) {
            return getString(R.string.mantenimiento);
        }
        return getString(R.string.fuera_servicio);
    }

    private String getText(android.widget.TextView textView) {
        return textView.getText() == null ? "" : textView.getText().toString().trim();
    }
}
