package demo.ui

import com.vaadin.data.Binder
import com.vaadin.event.ShortcutAction
import com.vaadin.server.FontAwesome
import com.vaadin.spring.annotation.SpringComponent
import com.vaadin.spring.annotation.UIScope
import com.vaadin.ui.Button
import com.vaadin.ui.CssLayout
import com.vaadin.ui.TextField
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme
import demo.domain.Customer
import demo.repository.CustomerRepository
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired

@CompileStatic
@SpringComponent
@UIScope
class CustomerEditor extends VerticalLayout {

    private final CustomerRepository repository
    private Customer customer

    TextField firstName = new TextField('First name')
    TextField lastName = new TextField('Last name')

    Button save = new Button('Save', FontAwesome.SAVE)
    Button cancel = new Button('Cancel')
    Button delete = new Button('Delete', FontAwesome.TRASH_O)
    CssLayout actions = new CssLayout(save, cancel, delete)
    Binder<Customer> binder = new Binder<>(Customer)

    @Autowired
    CustomerEditor(CustomerRepository repository) {
        this.repository = repository

        addComponents firstName, lastName, actions
        // bind using naming convention
        binder.bindInstanceFields this

        // Configure and style components
        spacing = true
        actions.styleName = ValoTheme.LAYOUT_COMPONENT_GROUP
        save.styleName = ValoTheme.BUTTON_PRIMARY
        save.setClickShortcut ShortcutAction.KeyCode.ENTER

        // wire action buttons to save, delete and reset
        save.addClickListener { repository.save(customer) }
        delete.addClickListener { repository.delete(customer) }
        cancel.addClickListener { editCustomer(customer) }

        visible = false
    }

    final void editCustomer(Customer c) {
        if (!c) {
            visible = false
            return
        }

        final boolean persisted = c.id != null

        if (persisted) {
            // Find fresh entity for editing
            customer = repository.findOne c.id
        } else {
            customer = c
        }

        cancel.visible = persisted

        // Bind customer properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        // moving values from fields to entities before saving
        binder.bean = customer
        visible = true

        // A hack to ensure the whole form is visible
        save.focus()
        // Select all text in firstName field automatically
        firstName.selectAll()
    }

    void setChangeHandler(ChangeHandler h) {
        // ChangeHandler is notified when either save or delete
        // is clicked
        save.addClickListener { h.onChange() }
        delete.addClickListener { h.onChange() }
    }

    interface ChangeHandler {
        void onChange()
    }
}
