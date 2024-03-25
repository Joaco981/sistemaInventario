package Modelo;

import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

public class AutocompletarComboBoxModel extends AbstractListModel<String> implements ComboBoxModel<String> {

    private List<String> elementos;
    private String textoFiltro;

    public AutocompletarComboBoxModel(List<String> elementos) {
        this.elementos = elementos;
        this.textoFiltro = "";
    }

    public void setFiltro(String texto) {
        textoFiltro = texto;
        fireContentsChanged(this, 0, elementos.size() - 1);
    }

    @Override
    public int getSize() {
        return elementos.size();
    }

    @Override
    public String getElementAt(int index) {
        String elemento = elementos.get(index);
        if (elemento.toLowerCase().startsWith(textoFiltro.toLowerCase())) {
            return elemento;
        }
        return null;
    }

    @Override
    public Object getSelectedItem() {
        return textoFiltro;
    }

    @Override
    public void setSelectedItem(Object anItem) {
        if (anItem != null) {
            textoFiltro = anItem.toString();
            fireContentsChanged(this, 0, elementos.size() - 1);
        }
    }
}
