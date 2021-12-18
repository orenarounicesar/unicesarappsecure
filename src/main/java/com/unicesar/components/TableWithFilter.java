
package com.unicesar.components;

import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class TableWithFilter extends Table {

    public Panel panel;
    public TextField txtFiltro;
    public VerticalLayout layoutContent;
    public SimpleStringFilter filter = null;
    public Filterable f;
    private Or or;
    private Table table;
    
    public TableWithFilter(String campoFiltro, String caption, boolean habilitado) {
        this.table = this;
        this.setWidth("100%");
        this.setSelectable(true);
        this.setStyleName("tablafilasdelgadascomponente", true);
//        this.setStyleName(ValoTheme.TABLE_NO_HEADER, true);
        
        txtFiltro = new TextField(caption + " (Digite en la 1ra Fila para Filtrar)");
        txtFiltro.setDescription("Digite para filtrar");
        txtFiltro.setReadOnly(!habilitado);
        txtFiltro.setWidth("100%");
        txtFiltro.setStyleName(ValoTheme.TEXTFIELD_TINY);
        txtFiltro.setStyleName("filtro", true);
        txtFiltro.addTextChangeListener(new TextChangeListener() {
//            SimpleStringFilter filter = null;
            @Override
            public void textChange(FieldEvents.TextChangeEvent event) {
////                Filterable f = (Filterable)table.getContainerDataSource();
//                f = (Filterable)table.getContainerDataSource();
//
//                // Remove old filter
//                if (filter != null) {
//                    f.removeContainerFilter(filter);
//                    layoutContent.setHeightUndefined();
//                    panel.setHeightUndefined();
//                }
//                // Set new filter for the "Name" column
//                filter = new SimpleStringFilter(campoFiltro, event.getText(), true, false);
//                f.addContainerFilter(filter);
                
                f = (Filterable)table.getContainerDataSource();

                // Remove old filter
                if (or != null)
                    f.removeContainerFilter(or);

                // Set new filter for the "Name" column
                or = getOr(campoFiltro, event.getText());
                f.addContainerFilter(or);
            }
        });
        
        layoutContent = new VerticalLayout(txtFiltro, this);
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
