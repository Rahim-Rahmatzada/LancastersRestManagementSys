package UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.LoginManager;

public class DashboardUI extends BaseUI {
    private LoginManager loginManager;
    private boolean isLoggedIn = true;

    private TextField loginUsernameField;
    private PasswordField loginPasswordField;
    private Button loginButton;
    private Button logoutButton;
    private Button createAccountButton;

    private TextField createUsernameField;
    private PasswordField createPasswordField;
    private PasswordField confirmPasswordField;
    private Button createButton;
    private Button backToLoginButton;

    public DashboardUI(UISwitcher uiSwitcher) {
        super(uiSwitcher);
        highlightButton("Dashboard");
        setTopText("Dashboard Overview");

        loginManager = new LoginManager();

        // Set the main content for the DashboardUI.
        VBox dashboardMainContent = new VBox();
        dashboardMainContent.setPadding(new Insets(20));
        dashboardMainContent.setSpacing(20);
        dashboardMainContent.setStyle("-fx-background-color: #1A1A1A;");

        // Create login form
        loginUsernameField = new TextField();
        loginUsernameField.setPromptText("Username");

        loginPasswordField = new PasswordField();
        loginPasswordField.setPromptText("Password");

        loginButton = new Button("Login");
        loginButton.setOnAction(e -> handleLogin());

        logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> handleLogout());

        createAccountButton = new Button("Create Account");
        createAccountButton.setOnAction(e -> handleCreateAccountButtonClick());

        VBox loginForm = new VBox();
        loginForm.setSpacing(5);
        loginForm.setAlignment(Pos.CENTER);
        loginForm.getChildren().addAll(loginUsernameField, loginPasswordField);

        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(loginButton, createAccountButton);

        VBox loginContainer = new VBox();
        loginContainer.setAlignment(Pos.CENTER);
        loginContainer.setFillWidth(false);
        loginContainer.setSpacing(15);
        loginContainer.getChildren().addAll(loginForm, buttonBox);

        VBox.setMargin(loginContainer, new Insets(100, 0, 0, 0));

        dashboardMainContent.getChildren().add(loginContainer);

        setMainContent(dashboardMainContent);

        preloadUIClasses();
    }

    private void handleLogin() {
        if (isLoggedIn) {
            showAlert("Already Logged In", "You are already logged in.");
        } else {
            String username = loginUsernameField.getText();
            String password = loginPasswordField.getText();
            if (loginManager.authenticate(username, password)) {
                isLoggedIn = true;
                showAlert("Login Successful", "You have successfully logged in.");
                showLoggedInView();
            } else {
                showAlert("Login Failed", "Invalid username or password.");
            }
        }
    }

    private void handleLogout() {
        isLoggedIn = false;
        showAlert("Logout Successful", "You have been logged out.");
        showLoginForm();
    }

    private void handleCreateAccountButtonClick() {
        if (isLoggedIn) {
            showCreateAccountForm();
        } else {
            showAlert("Login Required", "Please log in to create an account.");
        }
    }

    private void showCreateAccountForm() {
        VBox createAccountForm = new VBox();
        createAccountForm.setSpacing(5);
        createAccountForm.setAlignment(Pos.CENTER);

        createUsernameField = new TextField();
        createUsernameField.setPromptText("Username");

        createPasswordField = new PasswordField();
        createPasswordField.setPromptText("Password");

        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");

        createButton = new Button("Create");
        createButton.setOnAction(e -> handleCreateAccount());

        backToLoginButton = new Button("Go Back to Login");
        backToLoginButton.setOnAction(e -> showLoginForm());

        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(createButton, backToLoginButton);

        createAccountForm.getChildren().addAll(createUsernameField, createPasswordField, confirmPasswordField, buttonBox);

        VBox createAccountContainer = new VBox();
        createAccountContainer.setAlignment(Pos.CENTER);
        createAccountContainer.setFillWidth(false);
        createAccountContainer.setSpacing(15);
        createAccountContainer.getChildren().add(createAccountForm);

        VBox.setMargin(createAccountContainer, new Insets(100, 0, 0, 0));

        getMainContent().getChildren().clear();
        getMainContent().getChildren().add(createAccountContainer);
    }

    private void handleCreateAccount() {
        String username = createUsernameField.getText().trim();
        String password = createPasswordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Missing Information", "Both username and password are required.");
            return; // Exit the method early if validation fails
        }

        if (password.equals(confirmPassword)) {
            if (loginManager.createAccount(username, password)) {
                showAlert("Account Created", "Your account has been successfully created.");
                showLoginForm();
            } else {
                showAlert("Account Creation Failed", "Failed to create the account. Please try again.");
            }
        } else {
            showAlert("Password Mismatch", "The passwords do not match. Please try again.");
        }
    }

    private void showLoginForm() {
        loginUsernameField.clear();
        loginPasswordField.clear();

        VBox loginForm = new VBox();
        loginForm.setSpacing(5);
        loginForm.setAlignment(Pos.CENTER);
        loginForm.getChildren().addAll(loginUsernameField, loginPasswordField);

        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(loginButton, createAccountButton);

        VBox loginContainer = new VBox();
        loginContainer.setAlignment(Pos.CENTER);
        loginContainer.setFillWidth(false);
        loginContainer.setSpacing(15);
        loginContainer.getChildren().addAll(loginForm, buttonBox);

        VBox.setMargin(loginContainer, new Insets(100, 0, 0, 0));

        getMainContent().getChildren().clear();
        getMainContent().getChildren().add(loginContainer);
    }

    private void showLoggedInView() {

        loginUsernameField.clear();
        loginPasswordField.clear();

        VBox loginForm = new VBox();
        loginForm.setSpacing(5);
        loginForm.setAlignment(Pos.CENTER);
        loginForm.getChildren().addAll(loginUsernameField, loginPasswordField);

        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(logoutButton, createAccountButton);

        VBox loginContainer = new VBox();
        loginContainer.setAlignment(Pos.CENTER);
        loginContainer.setFillWidth(false);
        loginContainer.setSpacing(15);
        loginContainer.getChildren().addAll(loginForm, buttonBox);

        VBox.setMargin(loginContainer, new Insets(100, 0, 0, 0));

        getMainContent().getChildren().clear();
        getMainContent().getChildren().add(loginContainer);
    }

    @Override
    protected void handleButtonAction(String label) {
        if (isLoggedIn) {
            super.handleButtonAction(label);
        } else {
            showAlert("Login Required", "Please log in to access other pages.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void preloadUIClasses() {
        Thread preloadThread = new Thread(() -> {
            // Preload other UI classes
            uiSwitcher.preloadInventoryUI();
            uiSwitcher.preloadSalesUI();
            uiSwitcher.preloadTableOverviewUI();
            uiSwitcher.preloadMenusUI();
            uiSwitcher.preloadStaffUI();
            uiSwitcher.preloadWineUI();
            uiSwitcher.preloadStockOrdersUI();
        });
        preloadThread.setDaemon(true); // Set the thread as a daemon thread
        preloadThread.start();
    }
}