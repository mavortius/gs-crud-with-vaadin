package demo.ui

import com.vaadin.annotations.Theme
import com.vaadin.data.HasValue
import com.vaadin.server.FontAwesome
import com.vaadin.server.VaadinRequest
import com.vaadin.shared.ui.ValueChangeMode
import com.vaadin.spring.annotation.SpringUI
import com.vaadin.ui.Button
import com.vaadin.ui.Grid
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.TextField
import com.vaadin.ui.UI
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme
import demo.domain.Customer
import demo.repository.CustomerRepository
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired

@SpringUI
@CompileStatic
@Theme(ValoTheme.THEME_NAME)
class MainUI extends UI {

    private final CustomerRepository repository
    private final CustomerEditor editor
    private final Grid<Customer> grid
    private final TextField filter
    private final Button addNewButton

    @Autowired
    MainUI(CustomerRepository repository, CustomerEditor editor) {
        this.repository = repository
        this.grid = new Grid<>(Customer)
        this.editor = editor
        this.filter = new TextField()
        this.addNewButton = new Button('New Customer', FontAwesome.PLUS)
    }

    @Override
    protected void init(VaadinRequest request) {
        HorizontalLayout actions = new HorizontalLayout(filter, addNewButton)
        VerticalLayout mainLayout = new VerticalLayout(actions, grid, editor)
        content = mainLayout

        grid.with {
            setHeight 300, Unit.PIXELS
            setColumns('id', 'firstName', 'lastName')
        }

        // Replace listing with filtered content when user changes filter
        filter.with {
            placeholder = 'Filter by last name'
            valueChangeMode = ValueChangeMode.LAZY
            addValueChangeListener({ HasValue.ValueChangeEvent<String> event ->
                listCustomers(event.value)
            })
        }

        // Connect selected Customer to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener { HasValue.ValueChangeEvent<Customer> event ->
            editor.editCustomer(event.value)
        }

        // Instantiate and edit new Customer the new button is clicked
        addNewButton.addClickListener { editor.editCustomer(new Customer(firstName: '', lastName: '')) }

        // Listen changes made by the editor, refresh data from backend
        editor.setChangeHandler {
            editor.visible = false
            listCustomers(filter.value)
        }

        // Initialize listing
        listCustomers()
    }

    private void listCustomers(String filterText = null) {
        if (!filterText) {
            grid.items = repository.findAll()
        } else {
            grid.items = repository.findByLastNameStartsWithIgnoreCase(filterText)
        }
    }
}
