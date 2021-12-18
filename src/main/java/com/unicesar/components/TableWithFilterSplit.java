
package com.unicesar.components;

import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.ValoTheme;

public class TableWithFilterSplit extends Table {

    public Panel panel;
    public TextField txtFiltro;
    public VerticalSplitPanel layoutContent;
    private Filterable filterable;
    private Or or;
    private Table table;
    
    public TableWithFilterSplit(String campoFiltro, String caption, boolean habilitado) {
        this.table = this;
        this.setWidth("100%");
        this.setSelectable(true);
        this.setStyleName("tablafilasdelgadascomponente", true);
//        this.setStyleName(ValoTheme.TABLE_NO_HEADER, true);
        
        txtFiltro = new TextField(caption + " (Digite en la 1ra Fila para Filtrar)");
        txtFiltro.setDescription("Digite para filtrar");
        txtFiltro.setReadOnly(!habilitado);
        txtFiltro.setStyleName(ValoTheme.TEXTFIELD_TINY);
        txtFiltro.setStyleName("filtro", true);
        txtFiltro.setSizeFull();
        txtFiltro.addTextChangeListener((FieldEvents.TextChangeEvent event) -> {
            filterable = (Filterable)table.getContainerDataSource();
            
            // Remove old filter
            if (or != null)
                filterable.removeContainerFilter(or);
            
            // Set new filter for the "Name" column
            or = getOr(campoFiltro, event.getText());
            filterable.addContainerFilter(or);
        });
        
        layoutContent = new VerticalSplitPanel(txtFiltro, this);
        layoutContent.setSplitPosition(30, Sizeable.Unit.PIXELS);
        layoutContent.setLocked(true);
        layoutContent.setWidth("100%");
//        layoutContent.setMargin(true);
        
        panel = new Panel(caption + " (Digite en la 1ra Fila para Filtrar)", layoutContent);
        panel.setWidth("100%");
    }
    
    private Or getOr(String camposFiltro, String valor) {
        String[] nombreCampos = camposFiltro.split(",");
        Filter[] filtros = new Filter[nombreCampos.length];
        int contador = 0;
        for ( String filtro : nombreCampos ) {
            filtros[contador] = new SimpleStringFilter(filtro, valor, true, false);
            contador++;
        }
        
        return new Or(filtros);
    }
}
