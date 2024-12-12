package raven.cell;

import java.awt.Component;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;

public class StaffTableActionCellEditor extends DefaultCellEditor {

    private TableActionEvent event;

    public StaffTableActionCellEditor(TableActionEvent event) {
        super(new JCheckBox());
        this.event = event;
    }

    @Override
    public Component getTableCellEditorComponent(JTable jtable, Object o, boolean bln, int row, int column) {
        try {
            StaffActionPanel action = new StaffActionPanel();
            action.initEvent(event, row);
            action.setBackground(jtable.getSelectionBackground());
            return action;
        } catch (IOException ex) {
            Logger.getLogger(StaffTableActionCellEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
